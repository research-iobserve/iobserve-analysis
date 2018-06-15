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
package org.iobserve.planning.stages;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;

import teetime.stage.basic.AbstractFilter;

import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.iobserve.model.ModelHandlingErrorException;
import org.iobserve.model.PCMModelHandler;
import org.iobserve.model.factory.CostModelFactory;
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
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
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

    private PCMModelHandler processedModelHandler;

    private AllocationGroupsContainer originalAllocationGroups;
    private Allocation allocationModel;
    private CloudProfile cloudProfileModel;
    private CostRepository costModel;
    private DecisionSpace decisionModel;
    private ResourceEnvironment resourceEnvironmentModel;
    private org.palladiosimulator.pcm.system.System systemModel;

    private String allocationModelName;
    private String costModelName;
    private String decisionSpaceName;
    private String resourceEnvironmentName;

    private File processedModelDirectory;

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
     *             if an error occurred while saving the models
     * @throws InitializationException
     *             if an error occurred during snapshot generation
     * @throws ModelHandlingErrorException
     *             if an err occurred during resource container creation
     */
    @Override
    protected void execute(final File originalModelDirectory)
            throws IOException, InitializationException, ModelHandlingErrorException {
        this.saveModelNames(originalModelDirectory);
        this.initModelTransformation(originalModelDirectory);
        this.clearUnneededElements();
        this.rebuildEnvironment();

        this.outputPort.send(this.processedModelDirectory);
    }

    private void saveModelNames(final File pcmModelsDirectory) {
        final File[] files = pcmModelsDirectory.listFiles();
        for (final File nextFile : files) {
            final String filename = nextFile.getName();
            final String extension = filename.substring(filename.lastIndexOf('.') + 1, filename.length());
            if (PCMModelHandler.ALLOCATION_SUFFIX.equalsIgnoreCase(extension)) {
                this.allocationModelName = filename;
            } else if (PCMModelHandler.RESOURCE_ENVIRONMENT_SUFFIX.equalsIgnoreCase(extension)) {
                this.resourceEnvironmentName = filename;
            } else if (PCMModelHandler.COST_SUFFIX.equalsIgnoreCase(extension)) {
                this.costModelName = filename;
            } else if (PCMModelHandler.DESIGN_DECISION_SUFFIX.equalsIgnoreCase(extension)) {
                this.decisionSpaceName = filename;
            }

        }
    }

    private void initModelTransformation(final File originalModelDirectory)
            throws IOException, InitializationException {
        final PCMModelHandler originalModelHandler = new PCMModelHandler(originalModelDirectory);

        this.processedModelDirectory = new File(originalModelDirectory, ModelProcessing.PROCESSED_MODEL_FOLDER);

        FileUtils.copyDirectory(originalModelDirectory, this.processedModelDirectory);

        this.processedModelHandler = new PCMModelHandler(this.processedModelDirectory);

        this.allocationModel = this.processedModelHandler.getAllocationModel();
        this.cloudProfileModel = this.processedModelHandler.getCloudProfileModel();
        this.costModel = this.processedModelHandler.getCostModel();
        this.decisionModel = this.processedModelHandler.getDesignDecisionModel();
        this.resourceEnvironmentModel = this.processedModelHandler.getResourceEnvironmentModel();
        this.systemModel = this.processedModelHandler.getSystemModel();

        if (this.decisionModel == null) {
            this.decisionModel = DesignDecisionModelFactory.createDecisionSpace("processedDecision");
        }

        this.originalAllocationGroups = new AllocationGroupsContainer(originalModelHandler.getAllocationModel());
    }

    private void clearUnneededElements() {
        this.clearCosts();
        this.clearCloudResourcesFromResourceEnv();
        this.clearAllocationContexts();
    }

    private void rebuildEnvironment() throws ModelHandlingErrorException {
        for (final AllocationGroup allocationGroup : this.originalAllocationGroups.getAllocationGroups()) {
            final AllocationContext representingContext = allocationGroup.getRepresentingContext();

            // Set assembly context from allocation group to the context from the processed model
            final AssemblyContext representedAsmCtx = this.systemModel
                    .getAssemblyContexts__ComposedStructure().stream().filter(ctx -> representingContext
                            .getAssemblyContext_AllocationContext().getId().equals(ctx.getId()))
                    .findFirst().orElse(null);

            if (representedAsmCtx == null) {
                throw new RuntimeException(
                        "There was something wrong with the system model. Please check your models.");
            }

            representingContext.setAssemblyContext_AllocationContext(representedAsmCtx);

            this.createResourcesAndReplicationDegrees(this.decisionModel, allocationGroup);

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
        final URI decisionModelURI = URI
                .createFileURI(new File(this.processedModelDirectory, this.decisionSpaceName).getAbsolutePath());
        final URI allocationModelURI = URI
                .createFileURI(new File(this.processedModelDirectory, this.allocationModelName).getAbsolutePath());
        final URI costModelURI = URI
                .createFileURI(new File(this.processedModelDirectory, this.costModelName).getAbsolutePath());
        final URI resourceenvironmentModelURI = URI
                .createFileURI(new File(this.processedModelDirectory, this.resourceEnvironmentName).getAbsolutePath());
        this.processedModelHandler.save(decisionModelURI);
        this.processedModelHandler.save(allocationModelURI);
        this.processedModelHandler.save(costModelURI);
        this.processedModelHandler.save(resourceenvironmentModelURI);

    }

    private void createResourcesAndReplicationDegrees(final DecisionSpace decisionSpace,
            final AllocationGroup allocationGroup) throws ModelHandlingErrorException {
        // final CloudProfile profile = this.cloudProfileModel;
        // final ResourceEnvironment environment = this.resourceEnvironmentModel;
        // final CostRepository costs = this.costModel;

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
        ModelHelper.addResourceContainerToEnvironment(this.resourceEnvironmentModel, representingContainer);

        final int nrOfCurrentReplicas = allocationGroup.getAllocationContexts().size();

        // Upper bound for number of replicas for one resource
        // container type should be sufficiently high
        final int toNrOfReplicas = (nrOfCurrentReplicas + ModelProcessing.POSSIBLE_REPLICAS_OFFSET)
                * ModelProcessing.POSSIBLE_REPLICAS_FACTOR;

        // Create a new resource container with replication degree for each
        // VMType in the cloud profile, except for the one already added as the
        // representing container create only replication degree
        for (final CloudProvider provider : this.cloudProfileModel.getCloudProviders()) {
            for (final CloudResourceType cloudResource : provider.getCloudResources()) {
                if (cloudResource instanceof VMType) {
                    final VMType cloudVM = (VMType) cloudResource;
                    final String degreeName;
                    final ResourceContainerCloud createdContainer;

                    if (this.isSameVMType(cloudVM, representingContainer)) {
                        createdContainer = representingContainer;
                        // If we just use the container, the references in the
                        // resulting environment would point to the original cloudprofile
                        createdContainer.setInstanceType(cloudVM);

                        CostModelFactory.createFixedProcessingResourceCost(this.costModel, 0.0,
                                cloudVM.getPricePerHour(),
                                createdContainer.getActiveResourceSpecifications_ResourceContainer().get(0));

                        degreeName = String.format("%s_ReplicationDegree", allocationGroup.getName());
                    } else {
                        final String containerName = AllocationGroup.getAllocationGroupName(
                                allocationGroup.getComponentName(),
                                ModelHelper.getResourceContainerIdentifier(cloudVM));
                        createdContainer = ModelHelper.createResourceContainerFromVMType(this.resourceEnvironmentModel,
                                this.costModel, cloudVM, containerName);
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

    private void clearAllocationContexts() {
        this.allocationModel.getAllocationContexts_Allocation().clear();
    }

    private void clearCosts() {
        this.costModel.getCost().clear();
    }

    private void clearCloudResourcesFromResourceEnv() {
        final List<ResourceContainer> resourceContainers = this.resourceEnvironmentModel
                .getResourceContainer_ResourceEnvironment();
        for (final Iterator<ResourceContainer> iter = resourceContainers.iterator(); iter.hasNext();) {
            final ResourceContainer container = iter.next();
            if (container instanceof ResourceContainerCloud) {
                iter.remove();
            }
        }

        final LinkingResource linkingResource = ModelHelper.getInternetLinkingResource(this.resourceEnvironmentModel);
        linkingResource.getConnectedResourceContainers_LinkingResource().clear();
    }
}
