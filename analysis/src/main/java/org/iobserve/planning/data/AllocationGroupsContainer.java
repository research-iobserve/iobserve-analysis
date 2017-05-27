package org.iobserve.planning.data;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.iobserve.planning.utils.ModelHelper;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

public class AllocationGroupsContainer {
	private final Allocation allocation;

	private Map<String, ComponentGroup> componentNameToComponentGroupMap = new LinkedHashMap<>();
	private Set<AllocationGroup> allocationGroups = new LinkedHashSet<>();

	public AllocationGroupsContainer(Allocation allocation) {
		this.allocation = allocation;
		this.initAllocationGroups();
	}

	public AllocationGroup getAllocationGroup(AllocationContext allocContext) {
		AssemblyContext asmContext = allocContext.getAssemblyContext_AllocationContext();
		String componentName = asmContext.getEncapsulatedComponent__AssemblyContext().getEntityName();

		String containerIdentifier = ModelHelper
				.getResourceContainerIdentifier(allocContext.getResourceContainer_AllocationContext());

		return this.getAllocationGroup(componentName, containerIdentifier);
	}

	private AllocationGroup getAllocationGroup(String componentName, String containerIdentifier) {
		Map<String, AllocationGroup> allocationGroupsMap = this.componentNameToComponentGroupMap.get(componentName)
				.getAllocationGroups();

		return allocationGroupsMap.get(containerIdentifier);
	}

	public Set<AllocationGroup> getAllocationGroups() {
		return this.allocationGroups;
	}

	private void initAllocationGroups() {
		for (AllocationContext context : this.allocation.getAllocationContexts_Allocation()) {
			String componentName = context.getAssemblyContext_AllocationContext()
					.getEncapsulatedComponent__AssemblyContext().getEntityName();

			ComponentGroup componentGroup = this.componentNameToComponentGroupMap.get(componentName);

			if (componentGroup == null) {
				componentGroup = new ComponentGroup(componentName);
				this.componentNameToComponentGroupMap.put(componentName, componentGroup);
			}

			ResourceContainer allocationContainer = context.getResourceContainer_AllocationContext();
			String containerIdentifier = ModelHelper.getResourceContainerIdentifier(allocationContainer);

			AllocationGroup addedGroup = componentGroup.addAllocationContext(containerIdentifier, context);
			this.allocationGroups.add(addedGroup);
		}
	}

	public static class ComponentGroup {
		private Map<String, AllocationGroup> allocationGroupsMap = new LinkedHashMap<>();

		private final String componentName;

		public ComponentGroup(String componentName) {
			this.componentName = componentName;
		}

		public AllocationGroup addAllocationContext(String containerIdentifier, AllocationContext context) {
			AllocationGroup group = this.allocationGroupsMap.get(containerIdentifier);

			if (group == null) {
				group = new AllocationGroup(this.componentName, containerIdentifier);
				this.allocationGroupsMap.put(containerIdentifier, group);
			}

			group.addAllocationContext(context);
			return group;
		}

		public Map<String, AllocationGroup> getAllocationGroups() {
			return this.allocationGroupsMap;
		}

		public String getComponentName() {
			return this.componentName;
		}
	}

}
