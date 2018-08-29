package org.palladiosimulator.protocom.model.usage

import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour
import org.palladiosimulator.pcm.usagemodel.Start

/**
 * @author Christian Klaussner
 */
class ScenarioBehaviourAdapter extends ModelAdapter<ScenarioBehaviour> {
	new(ScenarioBehaviour entity) {
		super(entity)
	}
	
	/**
	 * Gets the start actions of the branch.
	 * @return an adapter for the start action
	 */
	def getStart() {
		val start = entity.actions_ScenarioBehaviour.findFirst[Start.isInstance(it)]
		new StartAdapter(start as Start);
	}
}
