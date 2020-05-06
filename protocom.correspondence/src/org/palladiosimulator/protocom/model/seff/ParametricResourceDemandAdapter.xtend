package org.palladiosimulator.protocom.model.seff

import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand

/**
 * @author Christian Klaussner
 */
class ParametricResourceDemandAdapter extends ModelAdapter<ParametricResourceDemand> {
	new(ParametricResourceDemand entity) {
		super(entity)
	}
	
	def getSpecification() {
		entity.specification_ParametericResourceDemand.specification
	}
	
	def getType() {
		entity.requiredResource_ParametricResourceDemand.entityName.toLowerCase
	}
}
