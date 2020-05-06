package org.palladiosimulator.protocom.model.usage

import org.palladiosimulator.pcm.usagemodel.Delay

/**
 * Adapter class for PCM Delay user actions.
 * @author Christian Klaussner
 */
class DelayAdapter extends UserActionAdapter<Delay> {
	new(Delay entity) {
		super(entity)
	}
	
	def getDelay() {
		entity.timeSpecification_Delay.specification
	}
}
