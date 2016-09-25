/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.model;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

public class AllocationModelBuilder extends ModelBuilder<AllocationModelProvider, Allocation> {

    public AllocationModelBuilder(final AllocationModelProvider modelToStartWith) {
        super(modelToStartWith);
    }

    // *****************************************************************
    //
    // *****************************************************************

    public AllocationModelBuilder loadModel() {
        this.modelProvider.loadModel();
        return this;
    }

    public AllocationModelBuilder resetModel() {
        final Allocation model = this.modelProvider.getModel();
        model.getAllocationContexts_Allocation().clear();
        return this;
    }

    /**
     * Create an {@link AllocationContext} for the given {@link ResourceContainer} and
     * {@link AssemblyContext} if they are absent to this model. No check for duplication is done!
     * 
     * @param resContainer
     *            container
     * @param asmCtx
     *            assembly context
     * @return builder
     */
    public AllocationModelBuilder addAllocationContext(final ResourceContainer resContainer,
            final AssemblyContext asmCtx) {

        final Allocation model = this.modelProvider.getModel();

        final AllocationFactory factory = AllocationFactory.eINSTANCE;
        final AllocationContext allocationCtx = factory.createAllocationContext();
        allocationCtx.setEntityName(asmCtx.getEntityName());
        allocationCtx.setAssemblyContext_AllocationContext(asmCtx);
        allocationCtx.setResourceContainer_AllocationContext(resContainer);
        model.getAllocationContexts_Allocation().add(allocationCtx);
        return this;
    }

    /**
     * Create an {@link AllocationContext} for the given {@link ResourceContainer} and
     * {@link AssemblyContext} if they are absent to this model. Check is done via
     * {@link ResourceContainer#getEntityName()} and {@link AssemblyContext#getEntityName()}.
     * 
     * @param resContainer
     *            container
     * @param asmCtx
     *            assembly context.
     * @return build
     */
    public AllocationModelBuilder addAllocationContextIfAbsent(final ResourceContainer resContainer,
            final AssemblyContext asmCtx) {
        final Allocation model = this.modelProvider.getModel();
        if (!model.getAllocationContexts_Allocation().stream().filter(
                context -> context.getAssemblyContext_AllocationContext().getEntityName().equals(asmCtx.getEntityName())
                        && context.getResourceContainer_AllocationContext().getEntityName()
                                .equals(resContainer.getEntityName()))
                .findAny().isPresent()) {
            this.addAllocationContext(resContainer, asmCtx);
        }
        return this;
    }

    // Can not get id of AllocationContext from TUndeployment
    public AllocationModelBuilder removeAllocationContext(final String id) {
        final Allocation model = this.modelProvider.getModel();
        final AllocationContext ctx = (AllocationContext) AbstractModelProvider.getIdentifiableComponent(id,
                model.getAllocationContexts_Allocation());
        model.getAllocationContexts_Allocation().remove(ctx);
        return this;
    }

    /**
     * Remove an {@link AllocationContext} from the given {@link ResourceContainer} and
     * {@link AssemblyContext} if they are contained in the model. Identification is done via
     * {@link ResourceContainer#getEntityName()} and {@link AssemblyContext#getEntityName()}.
     * 
     * @param resContainer
     *            container
     * @param asmCtx
     *            assembly context
     * @return builder
     */
    public AllocationModelBuilder removeAllocationContext(final ResourceContainer resContainer,
            final AssemblyContext asmCtx) {
        // could be inefficient on big models and high recurrences of undeployments
        final Allocation model = this.modelProvider.getModel();
        model.getAllocationContexts_Allocation().stream().filter(
                context -> context.getAssemblyContext_AllocationContext().getEntityName().equals(asmCtx.getEntityName())
                        && context.getResourceContainer_AllocationContext().getEntityName()
                                .equals(resContainer.getEntityName()))
                .findFirst().ifPresent(ctx -> model.getAllocationContexts_Allocation().remove(ctx));

        return this;
    }

    public AllocationModelBuilder addAllocationContext(final Class<?> type) {
        // TODO add an allocation
        return this;
    }

    public AllocationModelBuilder removeAllocationContext(final Class<?> type) {
        // TODO remove allocation context
        return this;
    }

}
