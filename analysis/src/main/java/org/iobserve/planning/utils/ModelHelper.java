package org.iobserve.planning.utils;

import java.io.IOException;
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
import org.iobserve.planning.cloudprofile.CloudProvider;
import org.iobserve.planning.cloudprofile.CloudResourceType;
import org.iobserve.planning.cloudprofile.VMType;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import de.uka.ipd.sdq.pcm.cost.CostRepository;
import de.uka.ipd.sdq.pcm.designdecision.DecisionSpace;
import de.uka.ipd.sdq.pcm.designdecision.DegreeOfFreedomInstance;
import de.uka.ipd.sdq.pcm.designdecision.specific.ResourceContainerReplicationDegree;

public final class ModelHelper {

	private static final String INTERNET_LINKING_RESOURCE_NAME = "Internet";

	private ModelHelper() {
	}

	public static String getResourceContainerIdentifier(ResourceContainer allocationContainer) {
		String identifier = "";
		if (allocationContainer instanceof ResourceContainerCloud) {
			ResourceContainerCloud cloudContainer = (ResourceContainerCloud) allocationContainer;
			identifier = String.format("%s_%s_%s", cloudContainer.getCloudProviderName(), cloudContainer.getLocation(),
					cloudContainer.getInstanceType());
		} else {
			identifier = allocationContainer.getEntityName();
		}
		return identifier;
	}

	public static LinkingResource getInternetLinkingResource(ResourceEnvironment environment) {
		LinkingResource linkingResource = null;
		List<LinkingResource> linkingResources = environment.getLinkingResources__ResourceEnvironment();

		Optional<LinkingResource> internetLink = linkingResources.stream()
				.filter(link -> link.getEntityName().contains(INTERNET_LINKING_RESOURCE_NAME)).findFirst();

		if (internetLink.isPresent()) {
			linkingResource = internetLink.get();
		} else {
			linkingResource = ResourceEnvironmentCloudBuilder.createLinkingResource(environment, null,
					INTERNET_LINKING_RESOURCE_NAME);
		}
		return linkingResource;
	}

	public static ResourceContainerCloud createResourceContainerFromVMType(ResourceEnvironment environment,
			CostRepository costRepository, VMType cloudVM, String containerName) {
		ResourceContainerCloud container = ResourceEnvironmentCloudBuilder.createResourceContainer(environment,
				containerName);

		container.setId(EcoreUtil.generateUUID());
		container.setResourceEnvironment_ResourceContainer(environment);
		container.setCloudProviderName(cloudVM.getProvider().getName());
		container.setInstanceType(cloudVM.getName());
		container.setLocation(cloudVM.getLocation());

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

	public static void addResourceContainerToEnvironment(ResourceEnvironment environment, ResourceContainer container) {
		environment.getResourceContainer_ResourceEnvironment().add(container);

		LinkingResource linkingResource = getInternetLinkingResource(environment);
		linkingResource.getConnectedResourceContainers_LinkingResource().add(container);
	}

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

	public static <T extends DegreeOfFreedomInstance> List<T> getAllDegreesOf(DecisionSpace decisionSpace,
			Class<T> degreeClass) {
		List<T> results = decisionSpace.getDegreesOfFreedom().stream().filter(degreeClass::isInstance)
				.map(degreeClass::cast).collect(Collectors.toList());
		return results;
	}

	public static void fillResourceEnvironmentFromCloudProfile(InitializeModelProviders modelProviders)
			throws IOException {
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

	public static String getGroupName(ResourceContainerCloud cloudContainer) {
		String groupName = cloudContainer.getGroupName();
		if ((groupName == null) || groupName.trim().isEmpty()) {
			groupName = cloudContainer.getEntityName();
		}
		return groupName;
	}
}
