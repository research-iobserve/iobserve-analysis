package org.palladiosimulator.protocom.model.allocation

import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.allocation.Allocation

/**
 * Adapter class for PCM Allocation entities.
 * @author Christian Klaussner
 */
class AllocationAdapter extends ModelAdapter<Allocation> {
	new(Allocation entity) {
		super(entity)
	}
	
	/**
	 * Gets the allocation contexts.
	 * @return a list of adapters for the allocation contexts
	 */
	def getAllocationContexts() {
		entity.allocationContexts_Allocation.filter[
			assemblyContext_AllocationContext !== null
		].map[
			new AllocationContextAdapter(it)
		]
	}
}
