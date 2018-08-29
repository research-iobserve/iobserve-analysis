package org.palladiosimulator.protocom.model.usage

import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.usagemodel.UsageScenario

/**
 * Adapter class for PCM UsageScenario entities.
 * @author Christian Klaussner
 */
class UsageScenarioAdapter extends ModelAdapter<UsageScenario> {
	new(UsageScenario entity) {
		super(entity)
	}
	
	/**
	 * Gets the ID.
	 * @return a string containing the ID
	 */
	def getId() {
		entity.id
	}
	
	/**
	 * Gets the scenario behaviour.
	 * @return an adapter for the scenario behaviour
	 */
	def getScenarioBehaviour() {
		new ScenarioBehaviourAdapter(entity.scenarioBehaviour_UsageScenario)
	}
}
