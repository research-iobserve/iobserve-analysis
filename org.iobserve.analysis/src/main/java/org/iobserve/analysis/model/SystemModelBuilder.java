package org.iobserve.analysis.model;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;

public class SystemModelBuilder extends ModelBuilder<SystemModelProvider, org.palladiosimulator.pcm.system.System> {

	public SystemModelBuilder(final SystemModelProvider modelToStartWith) {
		super(modelToStartWith);
	}

	@Override
	public org.palladiosimulator.pcm.system.System build() {
		return this.modelProvider.getModel();
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
