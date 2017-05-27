package org.iobserve.analysis.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceenvironmentcloudPackage;

public class ResourceEnvironmentCloudModelProvider extends ResourceEnvironmentModelProvider {

	public ResourceEnvironmentCloudModelProvider(URI uriUsageModel) {
		super(uriUsageModel);
	}

	@Override
	protected EPackage getPackage() {
		return ResourceenvironmentcloudPackage.eINSTANCE;
	}
}
