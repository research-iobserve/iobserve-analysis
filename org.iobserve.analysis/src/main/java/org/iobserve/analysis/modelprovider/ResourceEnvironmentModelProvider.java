package org.iobserve.analysis.modelprovider;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

public class ResourceEnvironmentModelProvider extends AbstractModelProvider<ResourceEnvironment> {

	// ********************************************************************
	// * INITIALIZATION
	// ********************************************************************

	public ResourceEnvironmentModelProvider(final URI uriModelInstance) {
		super(uriModelInstance);
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

}
