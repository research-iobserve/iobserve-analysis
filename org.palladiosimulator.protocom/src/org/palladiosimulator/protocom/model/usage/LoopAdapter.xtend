package org.palladiosimulator.protocom.model.usage

import org.palladiosimulator.pcm.usagemodel.Loop
import org.palladiosimulator.pcm.usagemodel.Start

/**
 * Adapter class for PCM Loop user actions.
 * @author Christian Klaussner
 */
class LoopAdapter extends UserActionAdapter<Loop> {
	new(Loop entity) {
		super(entity)
	}
	
	def getIterationCount() {
		entity.loopIteration_Loop.specification
	}
	
	def getStart() {
		val actions = entity.bodyBehaviour_Loop.actions_ScenarioBehaviour
		val start = actions.findFirst[Start.isInstance(it)]
		
		new StartAdapter(start as Start)
	}
	
	def getScenarioBehaviour() {
		new ScenarioBehaviourAdapter(entity.bodyBehaviour_Loop)
	}
}
