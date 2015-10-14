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
package org.iobserve.analysis.modelprovider;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;

public class SystemModelProvider extends AbstractModelProvider<System> {

	// ********************************************************************
	// * INITIALIZATION
	// ********************************************************************

	public SystemModelProvider(final URI uriModelInstance) {
		super(uriModelInstance);
	}

	// ********************************************************************
	// * GETTER / SETTER
	// ********************************************************************

	@Override
	public EPackage getPackage() {
		return SystemPackage.eINSTANCE;
	}

	public AssemblyContext getAssemblyContext(final String id) {
		final System sys = this.getModel();
		return (AssemblyContext) this.getIdentifiableComponent(id, sys.getAssemblyContexts__ComposedStructure());
	}
}
