package org.iobserve.planning.utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.CloudProfileModelProvider;
import org.iobserve.analysis.model.CostModelBuilder;
import org.iobserve.analysis.model.CostModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentCloudBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudProfile;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudProvider;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.CloudResourceType;
import org.palladiosimulator.pcm.cloud.pcmcloud.cloudprofile.VMType;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;
import de.uka.ipd.sdq.pcm.designdecision.DegreeOfFreedomInstance;
import de.uka.ipd.sdq.pcm.designdecision.specific.ResourceContainerReplicationDegree;

/**
 * Helper class for model related tasks, especially resource environment
 * related.
 *
 * @author Tobias Pöppke
 */
public final class ModelHelper {

	private static final String INTERNET_LINKING_RESOURCE_NAME = "Internet";

	private ModelHelper() {
	}

	/**
	 * Returns the identifier for a resource container.
	 *
	 * If the resource container is a cloud container, the returned identifier
	 * contains the provider, location and the type of this container. For other
	 * containers, the entity name is returned.
	 *
	 * @param allocationContainer
	 *            the container for which to retrieve the identifier
	 * @return the identifier
	 */
	public static String getResourceContainerIdentifier(final ResourceContainer allocationContainer) {
		String identifier = "";
		if (allocationContainer instanceof ResourceContainerCloud) {
			ResourceContainerCloud cloudContainer = (ResourceContainerCloud) allocationContainer;
			VMType type = cloudContainer.getInstanceType();

			identifier = String.format("%s_%s_%s", type.getProvider().getName(), type.getLocation(), type.getName());
		} else {
			identifier = allocationContainer.getEntityName();
		}
		return identifier;
	}

	/**
	 * Returns the identifier for a virtual machine type.
	 *
	 * @param type
	 *            the VM type for which to get the identifier
	 * @return the identifier
	 */
	public static String getResourceContainerIdentifier(final VMType type) {
		return String.format("%s_%s_%s", type.getProvider().getName(), type.getLocation(), type.getName());
	}

	/**
	 * Searches the resource environment for a linking resource with name
	 * 'Internet' and returns it, or creates it if it does not exist.
	 *
	 * @param environment
	 *            the resource environment to search
	 * @return the found linking resource or the newly created one
	 */
	public static LinkingResource getInternetLinkingResource(final ResourceEnvironment environment) {
		LinkingResource linkingResource = null;
		List<LinkingResource> linkingResources = environment.getLinkingResources__ResourceEnvironment();

		Optional<LinkingResource> internetLink = linkingResources.stream()
		        .filter(link -> link.getEntityName().contains(INTERNET_LINKING_RESOURCE_NAME)).findFirst();

		linkingResource = internetLink.orElseGet(() -> ResourceEnvironmentCloudBuilder
		        .createLinkingResource(environment, null, INTERNET_LINKING_RESOURCE_NAME));

		return linkingResource;
	}

	/**
	 * Creates a new resource container from a specific {@link VMType},
	 * including an entry in the cost repository.
	 *
	 * @param environment
	 *            the resource environment in which to create the container
	 * @param costRepository
	 *            the cost repository in which to create the costs for the
	 *            container
	 * @param cloudVM
	 *            the type of vm this container is an instance of
	 * @param containerName
	 *            the name of this container
	 * @return the newly created resource container
	 */
	public static ResourceContainerCloud createResourceContainerFromVMType(ResourceEnvironment environment,
			CostRepository costRepository, VMType cloudVM, String containerName) {
		ResourceContainerCloud container = ResourceEnvironmentCloudBuilder.createResourceContainer(environment,
				containerName);

		container.setId(EcoreUtil.generateUUID());
		container.setResourceEnvironment_ResourceContainer(environment);
		container.setInstanceType(cloudVM);

		// Connect container to internet
		LinkingResource linkingResource = getInternetLinkingResource(environment);
		linkingResource.getConnectedResourceContainers_LinkingResource().add(container);

		int nrOfCores = getNrOfCores(cloudVM);
		float processingRate = getProcessingRate(cloudVM);

		// Create processing resource and costs
		ProcessingResourceSpecification processor = ResourceEnvironmentCloudBuilder.createProcessingResource(nrOfCores,
				processingRate, container);
		container.getActiveResourceSpecifications_ResourceContainer().add(processor);
		// TODO HDD is not used by peropteryx?

		CostModelBuilder.createFixedProcessingResourceCost(costRepository, 0.0, cloudVM.getPricePerHour(), processor);
		return container;
	}

	/**
	 * Adds a resource container to the given resource environment, connecting
	 * it to an internet linking resource in the environment. (See
	 * {@link #getInternetLinkingResource(ResourceEnvironment)}).
	 *
	 * @param environment
	 *            the environment in which to add the container
	 * @param container
	 *            the container to add
	 */
	public static void addResourceContainerToEnvironment(ResourceEnvironment environment, ResourceContainer container) {
		environment.getResourceContainer_ResourceEnvironment().add(container);

		LinkingResource linkingResource = getInternetLinkingResource(environment);
		linkingResource.getConnectedResourceContainers_LinkingResource().add(container);
	}

	/**
	 * Adds all available allocation contexts in the allocation model to all
	 * available resource container replication degrees as changeable elements.
	 *
	 * @param allocation
	 *            the allocation model
	 * @param decisionSpace
	 *            the decision space
	 */
	public static void addAllAllocationsToReplicationDegrees(Allocation allocation, DecisionSpace decisionSpace) {
		List<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();

		for (ResourceContainerReplicationDegree dof : ModelHelper.getAllDegreesOf(decisionSpace,
				ResourceContainerReplicationDegree.class)) {
			dof.getChangeableElements().addAll(allocationContexts);
		}
	}

	private static float getProcessingRate(VMType cloudVM) {
		float processingRate = cloudVM.getMinProcessingRate();
		if (processingRate < 0) {
			processingRate = cloudVM.getMaxProcessingRate();
		}
		// Assumption: 1 Instruction per cycle
		return processingRate * 1000000000;
	}

	private static int getNrOfCores(VMType cloudVM) {
		int nrOfCores = cloudVM.getMinCores();
		if (nrOfCores < 0) {
			nrOfCores = cloudVM.getMaxCores();
		}
		return nrOfCores;
	}

	/**
	 * Returns all degrees of freedom of the given degree of freedom type in the
	 * decision space.
	 *
	 * @param decisionSpace
	 *            the decision space in which to look for the degrees
	 * @param degreeClass
	 *            the class of degree to look for
	 * @return a list of degrees of the requested type
	 */
	public static <T extends DegreeOfFreedomInstance> List<T> getAllDegreesOf(DecisionSpace decisionSpace,
			Class<T> degreeClass) {
		List<T> results = decisionSpace.getDegreesOfFreedom().stream().filter(degreeClass::isInstance)
				.map(degreeClass::cast).collect(Collectors.toList());
		return results;
	}

	/**
	 * Creates resource containers and their associated costs from the cloud
	 * profile given in the model provider.
	 *
	 * The created resource containers are of the type
	 * {@link ResourceContainerCloud} and are connected via an Internet linking
	 * resource ({@link #getInternetLinkingResource(ResourceEnvironment)}. For
	 * every vm type in the cloud profile, this method creates one resource
	 * container representing an instance of this specific vm type. The model is
	 * saved after the creation of containers is completed.
	 *
	 * @param modelProviders
	 *            the model providers with an initialized resource environment,
	 *            cost model and cloud profile
	 */
	public static void fillResourceEnvironmentFromCloudProfile(InitializeModelProviders modelProviders) {
		ResourceEnvironmentModelProvider resourceProvider = modelProviders.getResourceEnvironmentModelProvider();
		CloudProfileModelProvider cloudProfileProvider = modelProviders.getCloudProfileModelProvider();
		CostModelProvider costProvider = modelProviders.getCostModelProvider();

		ResourceEnvironment environment = resourceProvider.getModel();
		CostRepository costRepository = costProvider.getModel();

		for (CloudProvider provider : cloudProfileProvider.getModel().getCloudProviders()) {
			for (CloudResourceType cloudResource : provider.getCloudResources()) {
				if (cloudResource instanceof VMType) {
					VMType cloudVM = (VMType) cloudResource;
					createResourceContainerFromVMType(environment, costRepository, cloudVM, cloudVM.getName());
				}
			}
		}
		resourceProvider.save();
		costProvider.save();
	}

	/**
	 * Returns the name of the container group this container is a part of. This
	 * name is used for acquiring and terminating a VM during adaptation
	 * execution.
	 *
	 * @param cloudContainer
	 *            the cloud container
	 * @return the group name of this container
	 */
	public static String getGroupName(ResourceContainerCloud cloudContainer) {
		String groupName = cloudContainer.getGroupName();
		if ((groupName == null) || groupName.trim().isEmpty()) {
			groupName = cloudContainer.getEntityName();
		}
		return groupName;
	}

	/**
	 * Returns the hostname for this resource container.
	 *
	 * The hostname contains information to reconstruct the resource container
	 * from events.
	 *
	 * @param container
	 *            the container for which to create the hostname
	 * @return the hostname
	 */
	public static String getHostname(ResourceContainerCloud container) {
		return String.format("%s_%s", container.getId(), getGroupName(container));
	}

	/**
	 * Creates a cloud container from the given hostname with the information
	 * provided by the model providers in the resource environment provided by
	 * the model providers.
	 *
	 * If the hostname is not a valid cloud container hostname or if no matching
	 * VMType could be found, the method returns null.
	 *
	 * @param modelProviders
	 *            the model providers to use
	 * @param hostname
	 *            the hostname to convert
	 * @return the new cloud container or null in case of a problem
	 */
	public static ResourceContainerCloud getResourceContainerFromHostname(InitializeModelProviders modelProviders,
			String hostname) {
		String[] nameParts = hostname.split("_");

		VMType vmType = null;

		// A cloud container hostname should look like this:
		// ContainerId_AllocationGroupName_ProviderName_Location_InstanceType
		if (nameParts.length == 5) {
			String groupName = nameParts[1];
			String providerName = nameParts[2];
			String location = nameParts[3];
			String instanceType = nameParts[4];

			vmType = getVMType(modelProviders.getCloudProfileModelProvider(), providerName, location, instanceType);

			if (vmType != null) {
				ResourceEnvironment environment = modelProviders.getResourceEnvironmentModelProvider().getModel();
				CostRepository costRepository = modelProviders.getCostModelProvider().getModel();

				ResourceContainerCloud cloudContainer = createResourceContainerFromVMType(
						environment, costRepository, vmType, hostname);
				cloudContainer.setGroupName(groupName);
				return cloudContainer;
			}
		}
		return null;
	}

	private static VMType getVMType(CloudProfileModelProvider cloudProfileProvider, String cloudProviderName,
			String location, String instanceType) {
		CloudProfile profile = cloudProfileProvider.getModel();

		CloudProvider cloudProvider = profile.getCloudProviders().stream()
				.filter(provider -> provider.getName().equals(cloudProviderName)).findFirst().orElse(null);

		if (cloudProvider != null) {
			VMType vmType = cloudProvider.getCloudResources().stream()
					.filter(resource -> ((resource instanceof VMType)
							&& ((VMType) resource).getLocation().equals(location)
							&& ((VMType) resource).getName().equals(instanceType)))
					.map(resource -> (VMType) resource).findFirst().orElse(null);
			return vmType;
		}
		return null;
	}
}
