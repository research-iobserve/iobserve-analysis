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
	
	/**
	 * Create an {@link AllocationContext} for the given {@link ResourceContainer} and {@link AssemblyContext}
	 * if they are absent to this model. No check for duplication is done!
	 * @param resContainer container
	 * @param asmCtx assembly context
	 * @return builder
	 */
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
	
	/**
	 * Create an {@link AllocationContext} for the given {@link ResourceContainer} and {@link AssemblyContext}
	 * if they are absent to this model. Check is done via {@link ResourceContainer#getEntityName()} 
	 * and {@link AssemblyContext#getEntityName()}.
	 * @param resContainer container
	 * @param asmCtx assembly context.
	 * @return build
	 */
	public AllocationModelBuilder addAllocationContextIfAbsent(final ResourceContainer resContainer,
			final AssemblyContext asmCtx) {
		final Allocation model = this.modelProvider.getModel();
		if (!model.getAllocationContexts_Allocation()
				.stream()
				.filter(context -> context
						.getAssemblyContext_AllocationContext().getEntityName()
						.equals(asmCtx.getEntityName())
						&& context.getResourceContainer_AllocationContext()
								.getEntityName()
								.equals(resContainer.getEntityName()))
				.findAny().isPresent()) {
			this.addAllocationContext(resContainer, asmCtx);
		}
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
