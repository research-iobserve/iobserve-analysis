package org.palladiosimulator.protocom.model.usage

import org.palladiosimulator.pcm.usagemodel.ClosedWorkload

/**
 * Adapter class for PCM ClosedWorkload entities.
 * @author Christian Klaussner
 */
class ClosedWorkloadAdapter extends WorkloadAdapter<ClosedWorkload> {
	new(ClosedWorkload entity) {
		super(entity)
	}
	
	def getThinkTime() {
		entity.thinkTime_ClosedWorkload.specification
	}
}
