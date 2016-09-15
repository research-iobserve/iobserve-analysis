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

import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

/**
 * Model provider to provide {@link ResourceEnvironment} model.
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 */
public final class ResourceEnvironmentModelProvider extends AbstractModelProvider<ResourceEnvironment> {

	/**
	 * Create model provider to provide {@link ResourceEnvironment} model.
	 * @param thePlatform platform
	 * @param uriUsageModel uri to the model
	 */
	ResourceEnvironmentModelProvider(final ModelProviderPlatform thePlatform, final URI uriUsageModel) {
		super(thePlatform, uriUsageModel);
	}

	@Override
	protected EPackage getPackage() {
		return ResourceenvironmentPackage.eINSTANCE;
	}
	
	/**
	 * Get the {@link ResourceContainer} by its
	 * {@link ResourceContainer#getEntityName()}.
	 * 
	 * @param name name
	 * @return resource container instance or null if no resource container
	 *         available with the given name.
	 */
	public Optional<ResourceContainer> getResourceContainerByName(final String name) {
		final ResourceEnvironment env = this.getModel();
		return env.getResourceContainer_ResourceEnvironment().stream()
					.filter(container -> container.getEntityName().equals(name))
					.findFirst();
	}
}
