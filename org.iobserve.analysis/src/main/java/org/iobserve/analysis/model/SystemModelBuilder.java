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

import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;

public class SystemModelBuilder extends ModelBuilder<SystemModelProvider, org.palladiosimulator.pcm.system.System> {

	public SystemModelBuilder(final SystemModelProvider modelToStartWith) {
		super(modelToStartWith);
	}
	
	// *****************************************************************
	//
	// *****************************************************************

	public SystemModelBuilder save(final ModelSaveStrategy saveStrategy) {
		this.modelProvider.save(saveStrategy);
		return this;
	}
	
	public SystemModelBuilder loadModel() {
		this.modelProvider.loadModel();
		return this;
	}
	
	public SystemModelBuilder resetModel() {
		final org.palladiosimulator.pcm.system.System model = this.modelProvider.getModel();
		model.getAssemblyContexts__ComposedStructure().clear();
		return this;
	}
	
	public SystemModelBuilder createAssemblyContextsIfAbsent(final String name) {
		final boolean absent = this.modelProvider.getAssemblyContextByName(name) == null;
		if (absent) {
			final org.palladiosimulator.pcm.system.System model = this.modelProvider.getModel();
			final AssemblyContext asmContext = CompositionFactory.eINSTANCE.createAssemblyContext();
			asmContext.setEntityName(name);
			model.getAssemblyContexts__ComposedStructure().add(asmContext);
		}
		return this;
	}
	
}
