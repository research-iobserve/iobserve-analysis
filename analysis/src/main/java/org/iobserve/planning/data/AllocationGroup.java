package org.iobserve.planning.data;

import java.util.LinkedHashSet;
import java.util.Set;

import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

public class AllocationGroup {

	private final Set<AllocationContext> allocationContexts = new LinkedHashSet<>();
	private final Set<ResourceContainer> resourceContainers = new LinkedHashSet<>();

	private final String name;

	public AllocationGroup(String componentName, String containerIdentifier) {
		this.name = getAllocationGroupName(componentName, containerIdentifier);
	}

	public void addAllocationContext(AllocationContext context) {
		this.allocationContexts.add(context);
		this.resourceContainers.add(context.getResourceContainer_AllocationContext());
	}

	public String getName() {
		return this.name;
	}

	public Set<AllocationContext> getAllocationContexts() {
		return this.allocationContexts;
	}

	public Set<ResourceContainer> getResourceContainers() {
		return this.resourceContainers;
	}

	public static String getAllocationGroupName(String componentName, String containerIdentifier) {
		return String.format("%s_%s", componentName, containerIdentifier);
	}

	public AllocationContext getRepresentingContext() {
		AllocationContext representingContext = this.allocationContexts.iterator().next();
		return representingContext;
	}

	public ResourceContainerCloud getRepresentingResourceContainer() {
		ResourceContainerCloud representingContainer = this.resourceContainers.stream()
				.filter(container -> (container instanceof ResourceContainerCloud))
				.map(container -> ((ResourceContainerCloud) container)).findFirst().orElse(null);
		return representingContainer;
	}
}
