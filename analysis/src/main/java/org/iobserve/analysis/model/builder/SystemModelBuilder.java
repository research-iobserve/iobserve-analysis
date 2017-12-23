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

import java.util.Optional;

import org.iobserve.analysis.modelneo4j.legacyprovider.AbstractModelProvider;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.system.System;

/**
 * SystemModelBuilder is used to provide functionality in order to build {@link System} model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public final class SystemModelBuilder {

    /**
     * Create a system model builder.
     *
     * @param modelToStartWith
     *            model proivder
     */
    private SystemModelBuilder() {
    }

    /**
     * Checks whether an assembly context with the provided name exists, and, in case it is missing,
     * a new assembly context is created and added to the system model.
     *
     * @param system
     *            system model
     * @param name
     *            name of the assembly context
     *
     * @return returns the new assembly context
     */
    public static AssemblyContext createAssemblyContextsIfAbsent(final System system, final String name) {
        final Optional<AssemblyContext> context = SystemModelBuilder.getAssemblyContextByName(system, name);
        if (!context.isPresent()) {
            final AssemblyContext asmContext = CompositionFactory.eINSTANCE.createAssemblyContext();
            asmContext.setEntityName(name);
            system.getAssemblyContexts__ComposedStructure().add(asmContext);
            return asmContext;
        } else {
            return context.get();
        }
    }

    /**
     * Get the assembly context with the given id.
     *
     * @param system
     *            system model
     * @param id
     *            id
     * @return assembly context instance, null if no assembly context with the given id could be
     *         found.
     */
    public static AssemblyContext getAssemblyContext(final System system, final String id) {
        return (AssemblyContext) AbstractModelProvider.getIdentifiableComponent(id,
                system.getAssemblyContexts__ComposedStructure());
    }

    /**
     * Get the assembly context by the name.
     *
     * @param system
     *            the system model
     * @param name
     *            name of assembly context
     * @return assembly context instance, null if no assembly context with the given name could be
     *         found.
     */
    public static Optional<AssemblyContext> getAssemblyContextByName(final System system, final String name) {
        for (final AssemblyContext nextAssemblyContext : system.getAssemblyContexts__ComposedStructure()) {
            if (nextAssemblyContext.getEntityName().equals(name)) {
                return Optional.of(nextAssemblyContext);
            }
        }
        return Optional.empty();
    }

}
