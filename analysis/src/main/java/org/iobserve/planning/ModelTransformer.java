package org.iobserve.planning;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.CloudProfileModelProvider;
import org.iobserve.analysis.model.CostModelProvider;
import org.iobserve.analysis.model.DesignDecisionModelBuilder;
import org.iobserve.analysis.model.DesignDecisionModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
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

import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;

/**
 * Class for performing the transformation of the original PCM model into the
 * simplified model with allocation groups.
 *
 * The processed model is stored in a new 'processedModel' subfolder of the
 * original model folder, so PerOpteryx should use this model during the
 * optimization.
 *
 * @author Tobias Pöppke
 *
 */
public class ModelTransformer {

	private final PlanningData planningData;
	private final InitializeModelProviders originalModelProviders;
	private InitializeModelProviders processedModelProviders;

	private AllocationGroupsContainer originalAllocationGroups;

	private AllocationModelProvider allocationProvider;
	private CostModelProvider costProvider;
	private ResourceEnvironmentModelProvider resourceProvider;
	private CloudProfileModelProvider cloudProfileProvider;
	private DesignDecisionModelProvider decisionProvider;

	/**
	 * Constructs a new model transformer with the given planning data and
	 * updates the planning data with the original model directory.
	 *
	 * @param planningData
	 */
	public ModelTransformer(final PlanningData planningData) {
		this.planningData = planningData;
		String originalModelDir = planningData.getOriginalModelDir().toFileString();
		this.originalModelProviders = new InitializeModelProviders(new File(originalModelDir));
	}

	/**
	 * Performs the actual model transformation.
	 *
	 * First the original model is copied into the processed model folder and a
	 * new decision space is created. The original allocation groups are
	 * extracted and saved for further processing. The next step is to clear all
	 * cloud containers from the resource environment and clear all allocation
	 * contexts.
	 *
	 * Now the new resource environment is built with only one set of resource
	 * containers for each allocation group, derived from the available VMTypes
	 * in the cloud profile. For each of these resource containers a replication
	 * degree is created. A new allocation context is then created for each
	 * allocation group, allocating the referenced assembly context to the
	 * correct type of resource container. A new allocation degree is also
	 * created for each allocation group, with all available resource containers
	 * as viable choices.
	 *
	 * @throws IOException
	 *             if an error occured while saving the models
	 * @throws InitializationException
	 *             if an error occurred during snapshot generation
	 */
	public void transformModel() throws IOException, InitializationException {
		this.initModelTransformation();
		this.clearUnneededElements();
		this.rebuildEnvironment();
	}

	private void initModelTransformation() throws IOException, InitializationException {
		URI processedModelDir = this.planningData.getOriginalModelDir()
		        .appendSegment(ModelProcessing.PROCESSED_MODEL_DIR);

		SnapshotBuilder.setBaseSnapshotURI(this.planningData.getOriginalModelDir());

		SnapshotBuilder snapshotBuilder = new SnapshotBuilder(ModelProcessing.PROCESSED_MODEL_DIR,
		        this.originalModelProviders);
		snapshotBuilder.createSnapshot();

		this.planningData.setProcessedModelDir(processedModelDir);
		this.processedModelProviders = new InitializeModelProviders(new File(processedModelDir.toFileString()));

		this.allocationProvider = this.processedModelProviders.getAllocationModelProvider();
		this.cloudProfileProvider = this.processedModelProviders.getCloudProfileModelProvider();
		this.costProvider = this.processedModelProviders.getCostModelProvider();
		this.resourceProvider = this.processedModelProviders.getResourceEnvironmentModelProvider();
		this.decisionProvider = this.processedModelProviders.getDesignDecisionModelProvider();

		DecisionSpace decisionSpace = this.processedModelProviders.getDesignDecisionModelProvider().getModel();

		if (decisionSpace == null) {
			decisionSpace = DesignDecisionModelBuilder.createDecisionSpace("processedDecision");
			this.processedModelProviders.getDesignDecisionModelProvider().setModel(decisionSpace);
		}

		Allocation originalAllocation = this.originalModelProviders.getAllocationModelProvider().getModel();

		this.originalAllocationGroups = new AllocationGroupsContainer(originalAllocation);
	}

	private void clearUnneededElements() {
		this.clearCloudResourcesFromResourceEnv();
		this.clearAllocationContexts();
	}

	private void rebuildEnvironment() {
		for (AllocationGroup allocationGroup : this.originalAllocationGroups.getAllocationGroups()) {
			AllocationContext representingContext = allocationGroup.getRepresentingContext();

			this.createResourcesAndReplicationDegrees(this.decisionProvider.getModel(), allocationGroup);

			this.allocationProvider.getModel().getAllocationContexts_Allocation().add(representingContext);
		}

		for (AllocationGroup allocationGroup : this.originalAllocationGroups.getAllocationGroups()) {
			String allocationDegreeName = String.format("%s_AllocationDegree", allocationGroup.getName());

			DesignDecisionModelBuilder.createAllocationDegree(this.decisionProvider.getModel(), allocationDegreeName,
			        allocationGroup.getRepresentingContext(),
			        this.resourceProvider.getModel().getResourceContainer_ResourceEnvironment());
		}

		ModelHelper.addAllAllocationsToReplicationDegrees(this.allocationProvider.getModel(),
		        this.decisionProvider.getModel());

		this.saveModels();
	}

	private void saveModels() {
		this.decisionProvider.save();
		this.allocationProvider.save();
		this.costProvider.save();
		this.resourceProvider.save();
	}

	private void createResourcesAndReplicationDegrees(final DecisionSpace decisionSpace,
	        final AllocationGroup allocationGroup) {
		CloudProfile profile = this.cloudProfileProvider.getModel();
		ResourceEnvironment environment = this.resourceProvider.getModel();
		CostRepository costs = this.costProvider.getModel();

		// Get resource container that represents the instances this allocation
		// group is currently deployed on
		ResourceContainerCloud representingContainer = allocationGroup.getRepresentingResourceContainer();

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

		int nrOfCurrentReplicas = allocationGroup.getAllocationContexts().size();

		// Upper bound for number of replicas for one resource
		// container type should be sufficiently high
		int toNrOfReplicas = (nrOfCurrentReplicas + PlanningData.POSSIBLE_REPLICAS_OFFSET)
		        * PlanningData.POSSIBLE_REPLICAS_FACTOR;

		// Create a new resource container with replication degree for each
		// VMType in the cloud profile, except for the one already added as the
		// representing container create only replication degree
		for (CloudProvider provider : profile.getCloudProviders()) {
			for (CloudResourceType cloudResource : provider.getCloudResources()) {
				if (cloudResource instanceof VMType) {
					VMType cloudVM = (VMType) cloudResource;

					String degreeName;
					ResourceContainerCloud createdContainer;
					if (this.isSameVMType(cloudVM, representingContainer)) {
						createdContainer = representingContainer;
						degreeName = String.format("%s_ReplicationDegree", allocationGroup.getName());
					} else {
						String containerName = AllocationGroup.getAllocationGroupName(
						        allocationGroup.getComponentName(),
						        ModelHelper.getResourceContainerIdentifier(cloudVM));
						createdContainer = ModelHelper.createResourceContainerFromVMType(environment, costs, cloudVM,
						        containerName);
						degreeName = String.format("%s_ReplicationDegree", containerName);
					}

					DesignDecisionModelBuilder.createReplicationDegree(decisionSpace, degreeName, createdContainer, 1,
					        toNrOfReplicas);

				}
			}
		}
	}

	private boolean isSameVMType(final VMType cloudVM, final ResourceContainerCloud representingContainer) {
		return EcoreUtil.equals(cloudVM, representingContainer.getInstanceType());
	}

	private void clearAllocationContexts() {
		this.allocationProvider.resetModel();
	}

	private void clearCloudResourcesFromResourceEnv() {
		ResourceEnvironment environment = this.resourceProvider.getModel();

		List<ResourceContainer> resourceContainers = environment.getResourceContainer_ResourceEnvironment();
		for (Iterator<ResourceContainer> iter = resourceContainers.iterator(); iter.hasNext();) {
			ResourceContainer container = iter.next();
			if (container instanceof ResourceContainerCloud) {
				iter.remove();
			}
		}

		LinkingResource linkingResource = ModelHelper.getInternetLinkingResource(environment);
		linkingResource.getConnectedResourceContainers_LinkingResource().clear();
	}
}
