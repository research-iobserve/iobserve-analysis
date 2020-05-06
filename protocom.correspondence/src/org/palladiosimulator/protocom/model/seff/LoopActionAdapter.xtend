package org.palladiosimulator.protocom.model.seff

import org.palladiosimulator.pcm.seff.LoopAction
import org.palladiosimulator.pcm.seff.StartAction

/**
 * @author Christian Klaussner
 */
class LoopActionAdapter extends ActionAdapter<LoopAction> {
	new(LoopAction entity) {
		super(entity)
	}
	
	def getIterationCount() {
		entity.iterationCount_LoopAction.specification
	}
	
	def getStart() {
		val actions = entity.bodyBehaviour_Loop.steps_Behaviour
		val start = actions.findFirst[StartAction.isInstance(it)]
		
		new StartActionAdapter(start as StartAction)
	}
}
