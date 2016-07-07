/***************************************************************************
 * Copyright 2015 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;

/**
 * Model provider to provide {@link System} model.
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class SystemModelProvider extends AbstractModelProvider<System> {

	/**
	 * Create model provider to provide {@link System} model.
	 * @param thePlatform platform
	 * @param uriUsageModel uri to model
	 */
	SystemModelProvider(final ModelProviderPlatform thePlatform,
			final URI uriUsageModel) {
		super(thePlatform, uriUsageModel);
	}

	@Override
	public EPackage getPackage() {
		return SystemPackage.eINSTANCE;
	}

	/**
	 * Get the assembly context with the given id.
	 * 
	 * @param id
	 *            id
	 * @return assembly context instance, null if no assembly context with the
	 *         given id could be found.
	 */
	public AssemblyContext getAssemblyContext(final String id) {
		final System sys = this.getModel();
		return (AssemblyContext) AbstractModelProvider
				.getIdentifiableComponent(id,
						sys.getAssemblyContexts__ComposedStructure());
	}
	
	/**
	 * Get the assembly context by the name.
	 * 
	 * @param name
	 *            name of assembly context
	 * @return assembly context instance, null if no assembly context with the
	 *         given name could be found.
	 */
	public AssemblyContext getAssemblyContextByName(final String name) {
		final System sys = this.getModel();
		for (final AssemblyContext nextAssemblyContext:
				sys.getAssemblyContexts__ComposedStructure()) {
			if (nextAssemblyContext.getEntityName().equals(name)) {
				return nextAssemblyContext;
			}
		}
		return null;
	}
}
