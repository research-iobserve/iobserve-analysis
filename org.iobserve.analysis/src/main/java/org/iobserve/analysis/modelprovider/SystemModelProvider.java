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
