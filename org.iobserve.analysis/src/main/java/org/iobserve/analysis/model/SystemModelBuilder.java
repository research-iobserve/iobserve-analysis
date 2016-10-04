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

import java.util.Optional;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.system.System;

/**
 * SystemModelBuilder is used to provide functionality in order to build {@link System} model.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public class SystemModelBuilder
        extends AbstractModelBuilder<SystemModelProvider, org.palladiosimulator.pcm.system.System> {

    /**
     * Create a system model builder
     * 
     * @param modelToStartWith
     *            model proivder
     */
    public SystemModelBuilder(final SystemModelProvider modelToStartWith) {
        super(modelToStartWith);
    }

    /**
     * Save the model with the given strategy.
     * 
     * @param saveStrategy
     *            strategy
     * @return builder to chain commands
     */
    public SystemModelBuilder save(final ModelSaveStrategy saveStrategy) {
        this.modelProvider.save(saveStrategy);
        return this;
    }

    /**
     * Load the given model. Use this also to re-load.
     * 
     * @return builder to chain commands
     */
    public SystemModelBuilder loadModel() {
        this.modelProvider.loadModel();
        return this;
    }

    /**
     * Remove all elements from the model. End up with an empty model.
     * 
     * @return builder to chain commands
     */
    public SystemModelBuilder resetModel() {
        final org.palladiosimulator.pcm.system.System model = this.modelProvider.getModel();
        model.getAssemblyContexts__ComposedStructure().clear();
        return this;
    }

    /**
     * Create an {@link AssemblyContext} with the given name.
     * 
     * @param name
     *            entity name
     * @return created assembly context
     */
    public AssemblyContext createAssemblyContext(final String name) {
        final Optional<AssemblyContext> optCtx = this.modelProvider.getAssemblyContextByName(name);
        if (!optCtx.isPresent()) {
            final org.palladiosimulator.pcm.system.System model = this.modelProvider.getModel();
            final AssemblyContext ctx = CompositionFactory.eINSTANCE.createAssemblyContext();
            ctx.setEntityName(name);
            model.getAssemblyContexts__ComposedStructure().add(ctx);
            return ctx;
        }
        return optCtx.get();
    }
}
