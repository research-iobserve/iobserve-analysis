/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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

import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.iobserve.model.ModelHandlingErrorException;
import org.iobserve.model.ModelImporter;
import org.iobserve.model.factory.DesignDecisionModelFactory;
import org.iobserve.model.provider.file.AllocationModelHandler;
import org.iobserve.model.provider.file.CostModelHandler;
import org.iobserve.model.provider.file.DesignDecisionModelHandler;
import org.iobserve.model.provider.file.ResourceEnvironmentModelHandler;
import org.iobserve.model.snapshot.SnapshotBuilder;
import org.iobserve.planning.data.AllocationGroup;
import org.iobserve.planning.data.AllocationGroupsContainer;
import org.iobserve.planning.data.PlanningData;
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

/**
 * Class for performing the transformation of the original PCM model into the simplified model with
 * allocation groups.
 *
 * The processed model is stored in a new 'processedModel' subfolder of the original model folder,
 * so PerOpteryx should use this model during the optimization.
 *
 * @author Tobias Pöppke
 *
 */
public class ModelTransformer {

    private final PlanningData planningData;
    private final ModelImporter originalModelHandler;
    private ModelImporter processedModelHandler;

    private AllocationGroupsContainer originalAllocationGroups;
    private Allocation allocationModel;
    private CloudProfile cloudProfileModel;
    private CostRepository costModel;
    private ResourceEnvironment resourceEnvironmentModel;
    private DecisionSpace decisionModel;
    private DecisionSpace decisionSpace;

    private URI processedModelDir;

    /**
     * Constructs a new model transformer with the given planning data and updates the planning data
     * with the original model directory.
     *
     * @param planningData
     *            input data
     */
    public ModelTransformer(final PlanningData planningData) {
        this.planningData = planningData;
        final String originalModelDir = planningData.getOriginalModelDir().toFileString();
        final File directory = new File(originalModelDir);

        this.originalModelHandler = new ModelImporter(directory);
    }

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
     * @throws ModelHandlingErrorException
     *             model handling error
     */
    public void transformModel() throws IOException, InitializationException, ModelHandlingErrorException {
        this.initModelTransformation();
        this.clearUnneededElements();
        this.rebuildEnvironment();
    }

    private void initModelTransformation() throws IOException, InitializationException {
        this.processedModelDir = this.planningData.getOriginalModelDir()
                .appendSegment(ModelProcessing.PROCESSED_MODEL_FOLDER);

        SnapshotBuilder.setBaseSnapshotURI(this.planningData.getOriginalModelDir());

        final SnapshotBuilder snapshotBuilder = new SnapshotBuilder(ModelProcessing.PROCESSED_MODEL_FOLDER,
                this.originalModelHandler);
        snapshotBuilder.createSnapshot();

        this.planningData.setProcessedModelDir(this.processedModelDir);

        this.processedModelHandler = new ModelImporter(new File(this.processedModelDir.toFileString()));

        this.allocationModel = this.processedModelHandler.getAllocationModel();
        this.cloudProfileModel = this.processedModelHandler.getCloudProfileModel();
        this.costModel = this.processedModelHandler.getCostModel();
        this.resourceEnvironmentModel = this.processedModelHandler.getResourceEnvironmentModel();
        this.decisionModel = this.processedModelHandler.getDesignDecisionModel();
        this.decisionSpace = this.processedModelHandler.getDesignDecisionModel();

        if (this.decisionSpace == null) {
            this.decisionSpace = DesignDecisionModelFactory.createDecisionSpace("processedDecision");
        }

        final Allocation originalAllocation = this.originalModelHandler.getAllocationModel();

        this.originalAllocationGroups = new AllocationGroupsContainer(originalAllocation);
    }

    private void clearUnneededElements() {
        this.clearCloudResourcesFromResourceEnv();
    }

    private void rebuildEnvironment() throws ModelHandlingErrorException {
        for (final AllocationGroup allocationGroup : this.originalAllocationGroups.getAllocationGroups()) {
            final AllocationContext representingContext = allocationGroup.getRepresentingContext();

            this.createResourcesAndReplicationDegrees(allocationGroup);

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
        new DesignDecisionModelHandler().save(this.processedModelDir, this.decisionModel);
        new AllocationModelHandler().save(this.processedModelDir, this.allocationModel);
        new CostModelHandler().save(this.processedModelDir, this.costModel);
        new ResourceEnvironmentModelHandler().save(this.processedModelDir, this.resourceEnvironmentModel);
    }

    private void createResourcesAndReplicationDegrees(final AllocationGroup allocationGroup)
            throws ModelHandlingErrorException {
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
        final int toNrOfReplicas = (nrOfCurrentReplicas + PlanningData.POSSIBLE_REPLICAS_OFFSET)
                * PlanningData.POSSIBLE_REPLICAS_FACTOR;

        // Create a new resource container with replication degree for each
        // VMType in the cloud profile, except for the one already added as the
        // representing container create only replication degree
        for (final CloudProvider provider : profile.getCloudProviders()) {
            for (final CloudResourceType cloudResource : provider.getCloudResources()) {
                if (cloudResource instanceof VMType) {
                    final VMType cloudVM = (VMType) cloudResource;

                    final String degreeName;
                    final ResourceContainerCloud createdContainer;
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

                    DesignDecisionModelFactory.createReplicationDegree(this.decisionSpace, degreeName, createdContainer,
                            1, toNrOfReplicas);

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
