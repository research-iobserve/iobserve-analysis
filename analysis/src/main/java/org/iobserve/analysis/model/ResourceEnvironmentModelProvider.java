/***************************************************************************
 * Copyright (C) 2015 iObserve Project (https://www.iobserve-devops.net)
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
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceenvironmentcloudPackage;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.impl.ResourceenvironmentcloudPackageImpl;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.impl.ResourceenvironmentPrivacyPackageImpl;

/**
 * Model provider to provide {@link ResourceEnvironment} model.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Philipp Weimann
 */
public class ResourceEnvironmentModelProvider extends AbstractModelProvider<ResourceEnvironment> {

	/**
	 * Create model provider to provide {@link ResourceEnvironment} model.
	 *
	 * @param uriUsageModel
	 *            uri to the model
	 */
	public ResourceEnvironmentModelProvider(final URI uriUsageModel) {
		super(uriUsageModel);
		this.loadModel();
	}
	
	public ResourceEnvironmentModelProvider() {
		super();
	}

	@Override
	public void resetModel() {
		final ResourceEnvironment model = this.getModel();
		model.getResourceContainer_ResourceEnvironment().clear();
		model.getLinkingResources__ResourceEnvironment().clear();
	}

	@Override
	protected EPackage getPackage() {
		return ResourceenvironmentcloudPackageImpl.eINSTANCE;
	}

}
