package org.iobserve.planning.data;

import java.util.LinkedHashSet;
import java.util.Set;

import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Represents an allocation group.
 *
 * An allocation group is a group of resource containers of the same instance
 * type with the same component deployed on them. Thus an allocation group can
 * be modeled as one allocation context, allocating the assembly context that
 * contains the component to one resource container of this particular instance
 * type.
 *
 * @author Tobias Pöppke
 *
 */
public class AllocationGroup {

	private final Set<AllocationContext> allocationContexts = new LinkedHashSet<>();
	private final Set<ResourceContainer> resourceContainers = new LinkedHashSet<>();

	private final String name;
	private final String componentName;

	/**
	 * Creates a new allocation group with the given component name and resource
	 * container identifier.
	 *
	 * @param componentName
	 *            the component name of the allocated component
	 * @param containerIdentifier
	 *            the container identifier for the container type
	 */
	public AllocationGroup(final String componentName, final String containerIdentifier) {
		this.name = getAllocationGroupName(componentName, containerIdentifier);
		this.componentName = componentName;
	}

	/**
	 * Adds an allocation context to this allocation group. Note that no
	 * checking is done whether or not the context is semantically part of this
	 * group.
	 *
	 * @param context
	 *            the context to add
	 */
	public void addAllocationContext(final AllocationContext context) {
		this.allocationContexts.add(context);
		this.resourceContainers.add(context.getResourceContainer_AllocationContext());
	}

	/**
	 * Returns the name of this group. The name is a combination of the
	 * contained component and the container identifier in the form of
	 * 'componentName_containerIdentifier'.
	 *
	 * @return the group's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns all allocation contexts that are part of this allocation group.
	 *
	 * @return the contained allocation contexts
	 */
	public Set<AllocationContext> getAllocationContexts() {
		return this.allocationContexts;
	}

	/**
	 * Returns all resource containers that are part of this allocation group.
	 *
	 * @return the contained resource containers
	 */
	public Set<ResourceContainer> getResourceContainers() {
		return this.resourceContainers;
	}

	/**
	 * Returns the name of an allocation group given the contained component's
	 * name and the identifier for the container type.
	 *
	 * @param componentName
	 *            the component name
	 * @param containerIdentifier
	 *            the identifier of the container type
	 * @return an allocation group name
	 */
	public static String getAllocationGroupName(final String componentName, final String containerIdentifier) {
		return String.format("%s_%s", componentName, containerIdentifier);
	}

	/**
	 * Returns the first allocation context contained in this allocation group
	 * as a representative context for this allocation group.
	 *
	 * @return the representative allocation context
	 */
	public AllocationContext getRepresentingContext() {
		AllocationContext representingContext = this.allocationContexts.iterator().next();
		return representingContext;
	}

	/**
	 * Returns the first resource container contained in this allocation group
	 * as a representative container for this allocation group.
	 *
	 * @return the representative resource container
	 */
	public ResourceContainerCloud getRepresentingResourceContainer() {
		ResourceContainerCloud representingContainer = this.resourceContainers.stream()
		        .filter(container -> (container instanceof ResourceContainerCloud))
		        .map(container -> ((ResourceContainerCloud) container)).findFirst().orElse(null);
		return representingContainer;
	}

	public String getComponentName() {
		return componentName;
	}
}
