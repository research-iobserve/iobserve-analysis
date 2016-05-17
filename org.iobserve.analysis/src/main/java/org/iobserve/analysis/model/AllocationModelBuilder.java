package org.iobserve.analysis.model;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

public class AllocationModelBuilder extends ModelBuilder<AllocationModelProvider, Allocation> {

	public AllocationModelBuilder(final AllocationModelProvider modelToStartWith) {
		super(modelToStartWith);
	}

	@Override
	public Allocation build() {
		return this.modelProvider.getModel();
	}
	
	// *****************************************************************
	//
	// *****************************************************************

	public AllocationModelBuilder loadModel() {
		this.modelProvider.loadModel();
		return this;
	}
	
	public AllocationModelBuilder resetModel() {
		final Allocation model = this.modelProvider.getModel();
		model.getAllocationContexts_Allocation().clear();
		return this;
	}
	
	
	public AllocationModelBuilder addAllocationContext(final ResourceContainer resContainer,
			final AssemblyContext asmCtx) {
		
		final Allocation model = this.modelProvider.getModel();
		
		final AllocationFactory factory = AllocationFactory.eINSTANCE;
		final AllocationContext allocationCtx = factory.createAllocationContext();
		allocationCtx.setEntityName(asmCtx.getEntityName());
		allocationCtx.setAssemblyContext_AllocationContext(asmCtx);
		allocationCtx.setResourceContainer_AllocationContext(resContainer);
		model.getAllocationContexts_Allocation().add(allocationCtx);
		return this;
	}

	// TODO testing
	public AllocationModelBuilder removeAllocationContext(final String id) {
		final Allocation model = this.modelProvider.getModel();
		final AllocationContext ctx = (AllocationContext) 
				AbstractModelProvider.getIdentifiableComponent(id, model.getAllocationContexts_Allocation());
		model.getAllocationContexts_Allocation().remove(ctx);
		return this;
	}
	
	public AllocationModelBuilder addAllocationContext(final Class<?> type) {
		// TODO add an allocation
		return this;
	}

	public AllocationModelBuilder removeAllocationContext(final Class<?> type) {
		// TODO remove allocation context
		return this;
	}
	

}
