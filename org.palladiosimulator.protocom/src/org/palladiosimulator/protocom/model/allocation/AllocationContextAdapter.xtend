package org.palladiosimulator.protocom.model.allocation

import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.allocation.AllocationContext
import org.palladiosimulator.protocom.model.resourceenvironment.ResourceContainerAdapter

/**
 * Adapter class for PCM AllocationContext entities.
 * @author Christian Klaussner
 */
class AllocationContextAdapter extends ModelAdapter<AllocationContext> {
	new(AllocationContext entity) {
		super(entity)
	}
	
	/**
	 * Gets the resource container.
	 * @return an adapter for the resource container
	 */
	def getResourceContainer() {
		new ResourceContainerAdapter(entity.resourceContainer_AllocationContext)
	}
	
	/**
	 * Gets the assembly context.
	 * @return an adapter for the assembly context
	 */
	def getAssemblyContext() {
		new AssemblyContextAdapter(entity.assemblyContext_AllocationContext)
	}
}
