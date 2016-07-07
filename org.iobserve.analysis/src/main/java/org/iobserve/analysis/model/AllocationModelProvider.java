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
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;

/**
 * Model provider to provide {@link Allocation} model.
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class AllocationModelProvider 
	extends AbstractModelProvider<Allocation> {

	/**
	 * Create model provider to provide {@link Allocation} model.
	 * @param thePlatform platform
	 * @param uriModelInstance uri to the model
	 */
	AllocationModelProvider(final ModelProviderPlatform thePlatform, 
			final URI uriModelInstance) {
		super(thePlatform, uriModelInstance);
	}

	@Override
	public EPackage getPackage() {
		return AllocationPackage.eINSTANCE;
	}
}
