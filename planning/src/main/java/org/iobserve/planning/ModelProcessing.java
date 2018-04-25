/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.planning;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;

import teetime.stage.basic.AbstractFilter;

import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.iobserve.model.ModelHandlingErrorException;
import org.iobserve.model.PCMModelHandler;
import org.iobserve.model.factory.DesignDecisionModelFactory;
import org.iobserve.planning.data.AllocationGroup;
import org.iobserve.planning.data.AllocationGroupsContainer;
import org.iobserve.planning.utils.ModelHelper;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudProfile;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudProvider;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudResourceType;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.VMType;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stage for processing the PCM model before the model is used in PerOpteryx for generating
 * adaptation candidates. This stage performs the grouping of allocation contexts into allocation
 * groups to reduce the available degrees of freedom for the design space exploration.
 *
 * The processed model is stored in a new 'processedModel' subfolder of the original model folder,
 * so PerOpteryx should use this model during the optimization.
 *
 * @author Tobias Pöppke
 * @author Lars Bluemke (implementation as a TeeTime filter)
 *
 */
public class ModelProcessing extends AbstractFilter<File> {
    public static final Logger LOGGER = LoggerFactory.getLogger(ModelProcessing.class);
    public static final String PROCESSED_MODEL_FOLDER = "processedModel";

    public static final int POSSIBLE_REPLICAS_OFFSET = 10;
    public static final int POSSIBLE_REPLICAS_FACTOR = 1;

    private AllocationGroupsContainer originalAllocationGroups;
    private Allocation allocationModel;
    private CloudProfile cloudProfileModel;
    private CostRepository costModel;
    private ResourceEnvironment resourceEnvironmentModel;
    private DecisionSpace decisionModel;

    private File processedModelDir;

    /**
     * Performs the actual model transformation.
     *
     * First the original model is copied into the processed model folder and a new decision space
     * is created. The original allocation groups are extracted and saved for further processing.
     * The next step is to clear all cloud containers from the resource environment and clear all
     * allocation contexts.
     *
     * Now the new resource environment is built with only one set of resource containers for each
     * allocation group, derived from the available VMTypes in the cloud profile. For each of these
     * resource containers a replication degree is created. A new allocation context is then created
     * for each allocation group, allocating the referenced assembly context to the correct type of
     * resource container. A new allocation degree is also created for each allocation group, with
     * all available resource containers as viable choices.
     *
     * @throws IOException
     *             if an error occured while saving the models
     * @throws InitializationException
     *             if an error occurred during snapshot generation
     */
    @Override
    protected void execute(final File modelDirectory) throws Exception {
        this.initModelTransformation(modelDirectory);
        this.clearUnneededElements();
        this.rebuildEnvironment();

        this.outputPort.send(this.processedModelDir);
    }

    private void initModelTransformation(final File originalModelDirectory)
            throws IOException, InitializationException {
        PCMModelHandler originalModelHandler;
        PCMModelHandler processedModelHandler;
        DecisionSpace decisionSpace;

        originalModelHandler = new PCMModelHandler(originalModelDirectory);
        this.processedModelDir = new File(originalModelDirectory, ModelProcessing.PROCESSED_MODEL_FOLDER);

        FileUtils.copyDirectory(originalModelDirectory, this.processedModelDir);

        processedModelHandler = new PCMModelHandler(this.processedModelDir);

        this.allocationModel = processedModelHandler.getAllocationModel();
        this.cloudProfileModel = processedModelHandler.getCloudProfileModel();
        this.costModel = processedModelHandler.getCostModel();
        this.resourceEnvironmentModel = processedModelHandler.getResourceEnvironmentModel();
        this.decisionModel = processedModelHandler.getDesignDecisionModel();
        decisionSpace = processedModelHandler.getDesignDecisionModel();

        if (decisionSpace == null) {
            decisionSpace = DesignDecisionModelFactory.createDecisionSpace("processedDecision");
        }

        final Allocation originalAllocation = originalModelHandler.getAllocationModel();

        this.originalAllocationGroups = new AllocationGroupsContainer(originalAllocation);
    }

    private void clearUnneededElements() {
        this.clearCloudResourcesFromResourceEnv();
    }

    private void rebuildEnvironment() {
        for (final AllocationGroup allocationGroup : this.originalAllocationGroups.getAllocationGroups()) {
            final AllocationContext representingContext = allocationGroup.getRepresentingContext();

            try {
                this.createResourcesAndReplicationDegrees(this.decisionModel, allocationGroup);
            } catch (final ModelHandlingErrorException e) {
                ModelProcessing.LOGGER.error("Couldn't create ResourceContainer from VMType.");
            }

            this.allocationModel.getAllocationContexts_Allocation().add(representingContext);
        }

        for (final AllocationGroup allocationGroup : this.originalAllocationGroups.getAllocationGroups()) {
            final String allocationDegreeName = String.format("%s_AllocationDegree", allocationGroup.getName());

            DesignDecisionModelFactory.createAllocationDegree(this.decisionModel, allocationDegreeName,
                    allocationGroup.getRepresentingContext(),
                    this.resourceEnvironmentModel.getResourceContainer_ResourceEnvironment());
        }

        ModelHelper.addAllAllocationsToReplicationDegrees(this.allocationModel, this.decisionModel);

        this.saveModels();
    }

    private void saveModels() {
        // new DesignDecisionModelHandler().save(
        // this.processedModelDir.appendFileExtension(IPCMModelHandler.DESIGN_DECISION_SUFFIX),
        // this.decisionModel);
        // new AllocationModelHandler().save(
        // this.processedModelDir.appendFileExtension(IPCMModelHandler.ALLOCATION_SUFFIX),
        // this.allocationModel);
        // new
        // CostModelHandler().save(this.processedModelDir.appendFileExtension(IPCMModelHandler.COST_SUFFIX),
        // this.costModel);
        // new ResourceEnvironmentModelHandler().save(
        // this.processedModelDir.appendFileExtension(IPCMModelHandler.RESOURCE_ENVIRONMENT_SUFFIX),
        // this.resourceEnvironmentModel);
    }

    private void createResourcesAndReplicationDegrees(final DecisionSpace decisionSpace,
            final AllocationGroup allocationGroup) throws ModelHandlingErrorException {
        final CloudProfile profile = this.cloudProfileModel;
        final ResourceEnvironment environment = this.resourceEnvironmentModel;
        final CostRepository costs = this.costModel;

        // Get resource container that represents the instances this allocation
        // group is currently deployed on
        final ResourceContainerCloud representingContainer = allocationGroup.getRepresentingResourceContainer();

        if (representingContainer == null) {
            throw new IllegalArgumentException(
                    String.format("Could not find a cloud container for allocation group '%s'. Check your model.",
                            allocationGroup.getName()));
        }

        // Set group name of the representing container and add it to the
        // resource environment
        representingContainer.setGroupName(allocationGroup.getName());
        representingContainer.setEntityName(allocationGroup.getName());
        ModelHelper.addResourceContainerToEnvironment(environment, representingContainer);

        final int nrOfCurrentReplicas = allocationGroup.getAllocationContexts().size();

        // Upper bound for number of replicas for one resource
        // container type should be sufficiently high
        final int toNrOfReplicas = (nrOfCurrentReplicas + ModelProcessing.POSSIBLE_REPLICAS_OFFSET)
                * ModelProcessing.POSSIBLE_REPLICAS_FACTOR;

        // Create a new resource container with replication degree for each
        // VMType in the cloud profile, except for the one already added as the
        // representing container create only replication degree
        for (final CloudProvider provider : profile.getCloudProviders()) {
            for (final CloudResourceType cloudResource : provider.getCloudResources()) {
                if (cloudResource instanceof VMType) {
                    final VMType cloudVM = (VMType) cloudResource;

                    String degreeName; // NOCS
                    ResourceContainerCloud createdContainer; // NOCS
                    if (this.isSameVMType(cloudVM, representingContainer)) {
                        createdContainer = representingContainer;
                        degreeName = String.format("%s_ReplicationDegree", allocationGroup.getName());
                    } else {
                        final String containerName = AllocationGroup.getAllocationGroupName(
                                allocationGroup.getComponentName(),
                                ModelHelper.getResourceContainerIdentifier(cloudVM));
                        createdContainer = ModelHelper.createResourceContainerFromVMType(environment, costs, cloudVM,
                                containerName);
                        degreeName = String.format("%s_ReplicationDegree", containerName);
                    }

                    DesignDecisionModelFactory.createReplicationDegree(decisionSpace, degreeName, createdContainer, 1,
                            toNrOfReplicas);

                }
            }
        }
    }

    private boolean isSameVMType(final VMType cloudVM, final ResourceContainerCloud representingContainer) {
        return EcoreUtil.equals(cloudVM, representingContainer.getInstanceType());
    }

    private void clearCloudResourcesFromResourceEnv() {
        final ResourceEnvironment environment = this.resourceEnvironmentModel;

        final List<ResourceContainer> resourceContainers = environment.getResourceContainer_ResourceEnvironment();
        for (final Iterator<ResourceContainer> iter = resourceContainers.iterator(); iter.hasNext();) {
            final ResourceContainer container = iter.next();
            if (container instanceof ResourceContainerCloud) {
                iter.remove();
            }
        }

        final LinkingResource linkingResource = ModelHelper.getInternetLinkingResource(environment);
        linkingResource.getConnectedResourceContainers_LinkingResource().clear();
    }
}
