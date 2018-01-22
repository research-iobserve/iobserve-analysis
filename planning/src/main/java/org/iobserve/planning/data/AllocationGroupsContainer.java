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

/**
 * Container for all allocation groups contained in an allocation model.
 *
 * @author Tobias Pöppke
 * @see AllocationGroup
 *
 */
public class AllocationGroupsContainer {
    private final Allocation allocation;

    private final Map<String, ComponentGroup> componentNameToComponentGroupMap = new LinkedHashMap<>();
    private final Set<AllocationGroup> allocationGroups = new LinkedHashSet<>();

    /**
     * Constructs an allocation group container for all allocation groups contained in the given
     * allocation model.
     *
     * @param allocation
     *            the allocation model
     */
    public AllocationGroupsContainer(final Allocation allocation) {
        this.allocation = allocation;
        this.initAllocationGroups();
    }

    /**
     * Returns the allocation group to which the given allocation context belongs.
     *
     * @param allocContext
     *            the allocation context for which to get the allocation group
     * @return the corresponding allocation group or null if there is none
     */
    public AllocationGroup getAllocationGroup(final AllocationContext allocContext) {
        final AssemblyContext asmContext = allocContext.getAssemblyContext_AllocationContext();
        final String componentName = asmContext.getEncapsulatedComponent__AssemblyContext().getEntityName();

        final String containerIdentifier = ModelHelper
                .getResourceContainerIdentifier(allocContext.getResourceContainer_AllocationContext());

        return this.getAllocationGroup(componentName, containerIdentifier);
    }

    private AllocationGroup getAllocationGroup(final String componentName, final String containerIdentifier) {
        final Map<String, AllocationGroup> allocationGroupsMap = this.componentNameToComponentGroupMap
                .get(componentName).getAllocationGroups();

        return allocationGroupsMap.get(containerIdentifier);
    }

    /**
     * Returns all allocation groups contained in this allocation groups container.
     *
     * @return all contained allocation groups
     */
    public Set<AllocationGroup> getAllocationGroups() {
        return this.allocationGroups;
    }

    private void initAllocationGroups() {
        for (final AllocationContext context : this.allocation.getAllocationContexts_Allocation()) {
            final String componentName = context.getAssemblyContext_AllocationContext()
                    .getEncapsulatedComponent__AssemblyContext().getEntityName();

            ComponentGroup componentGroup = this.componentNameToComponentGroupMap.get(componentName);

            if (componentGroup == null) {
                componentGroup = new ComponentGroup(componentName);
                this.componentNameToComponentGroupMap.put(componentName, componentGroup);
            }

            final ResourceContainer allocationContainer = context.getResourceContainer_AllocationContext();
            final String containerIdentifier = ModelHelper.getResourceContainerIdentifier(allocationContainer);

            final AllocationGroup addedGroup = componentGroup.addAllocationContext(containerIdentifier, context);
            this.allocationGroups.add(addedGroup);
        }
    }

    /**
     * Internal class component group.
     *
     * @author Tobias Pöppke
     *
     */
    private static class ComponentGroup {
        private final Map<String, AllocationGroup> allocationGroupsMap = new LinkedHashMap<>();

        private final String componentName;

        ComponentGroup(final String componentName) {
            this.componentName = componentName;
        }

        AllocationGroup addAllocationContext(final String containerIdentifier, final AllocationContext context) {
            AllocationGroup group = this.allocationGroupsMap.get(containerIdentifier);

            if (group == null) {
                group = new AllocationGroup(this.componentName, containerIdentifier);
                this.allocationGroupsMap.put(containerIdentifier, group);
            }

            group.addAllocationContext(context);
            return group;
        }

        Map<String, AllocationGroup> getAllocationGroups() {
            return this.allocationGroupsMap;
        }
    }

}
