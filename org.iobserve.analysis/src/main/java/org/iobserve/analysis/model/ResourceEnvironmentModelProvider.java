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
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

public class ResourceEnvironmentModelProvider extends AbstractModelProvider<ResourceEnvironment> {

	// ********************************************************************
	// * INITIALIZATION
	// ********************************************************************

	public ResourceEnvironmentModelProvider(final URI uriModelInstance, final ModelProviderPlatform thePlatform) {
		super(uriModelInstance, thePlatform);
	}
	
	@Override
	public void resetModel() {
		// TODO Auto-generated method stub
		
	}

	// ********************************************************************
	// * GETTER / SETTER
	// ********************************************************************

	@Override
	public EPackage getPackage() {
		return ResourceenvironmentPackage.eINSTANCE;
	}

	public ResourceContainer getResourceContainer(final String id) {
		final ResourceEnvironment env = this.getModel();
		return (ResourceContainer) this.getIdentifiableComponent(id, env.getResourceContainer_ResourceEnvironment());
	}
	
	public ResourceContainer getResourceContainerByName(final String name) {
		final ResourceEnvironment env = this.getModel();
		for(final ResourceContainer nextResContainer : env.getResourceContainer_ResourceEnvironment()) {
			if (nextResContainer.getEntityName().equals(name)) {
				return nextResContainer;
			}
		}
		return null;
	}
	
	public ResourceContainer createResourceContainer(final String name) {
		final ResourceContainer resContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
		resContainer.setEntityName(name);
		return resContainer;
	}

}
