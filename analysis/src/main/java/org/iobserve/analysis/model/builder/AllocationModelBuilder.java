/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.model.builder;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class AllocationModelBuilder {

    /**
     *
     * @param modelProvider
     */
    private AllocationModelBuilder() {
    }

    /**
     * Add an {@link AllocationContext} for the given {@link ResourceContainer} and
     * {@link AssemblyContext} if they are absent to this model. No check for duplication is done!
     *
     * @param model
     *            allocation model
     * @param resContainer
     *            container
     * @param asmCtx
     *            assembly context
     */
    public static void addAllocationContext(final Allocation model, final ResourceContainer resContainer,
            final AssemblyContext asmCtx) {
        final AllocationFactory factory = AllocationFactory.eINSTANCE;
        final AllocationContext allocationCtx = factory.createAllocationContext();
        allocationCtx.setEntityName(asmCtx.getEntityName());
        allocationCtx.setAssemblyContext_AllocationContext(asmCtx);
        allocationCtx.setResourceContainer_AllocationContext(resContainer);
        model.getAllocationContexts_Allocation().add(allocationCtx);
    }

    /**
     * Test whether an {@link AllocationContext} for the given {@link ResourceContainer} and
     * {@link AssemblyContext} is present this model. Check is done via
     * {@link ResourceContainer#getEntityName()} and {@link AssemblyContext#getEntityName()}.
     *
     * @param allocationModel
     *            the allocation model
     * @param resourceContainer
     *            container
     * @param assemblyContext
     *            assembly context.
     */
    public static boolean isAllocationPresent(final Allocation allocationModel, final ResourceContainer resourceContainer,
            final AssemblyContext assemblyContext) {
        return allocationModel.getAllocationContexts_Allocation().stream().filter(
                context -> context.getAssemblyContext_AllocationContext().getEntityName().equals(assemblyContext.getEntityName())
                        && context.getResourceContainer_AllocationContext().getEntityName()
                                .equals(resourceContainer.getEntityName()))
                .findAny().isPresent();
    }

    /**
     * Remove an {@link AllocationContext} from the given {@link ResourceContainer} and
     * {@link AssemblyContext} if they are contained in the model. Identification is done via
     * {@link ResourceContainer#getEntityName()} and {@link AssemblyContext#getEntityName()}.
     *
     * @param model
     *            the allocation model
     * @param resContainer
     *            container
     * @param asmCtx
     *            assembly context
     */
    public static void removeAllocationContext(final Allocation model, final ResourceContainer resContainer,
            final AssemblyContext asmCtx) {
        // could be inefficient on big models and high recurrences of undeployments
        model.getAllocationContexts_Allocation().stream().filter(
                context -> context.getAssemblyContext_AllocationContext().getEntityName().equals(asmCtx.getEntityName())
                        && context.getResourceContainer_AllocationContext().getEntityName()
                                .equals(resContainer.getEntityName()))
                .findFirst().ifPresent(ctx -> model.getAllocationContexts_Allocation().remove(ctx));
    }

}
