package org.palladiosimulator.protocom.model.usage

import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.usagemodel.BranchTransition
import org.palladiosimulator.pcm.usagemodel.Start

/**
 * Adapter class for PCM BranchTransition entities.
 * @author Christian Klaussner
 */
class BranchTransitionAdapter extends ModelAdapter<BranchTransition> {
	new(BranchTransition entity) {
		super(entity)
	}
	
	/**
	 * Gets the start actions of the branch.
	 * @return an adapter for the start action
	 */
	def getStart() {
		val behaviour = entity.branchedBehaviour_BranchTransition
		val start = behaviour.actions_ScenarioBehaviour.findFirst[Start.isInstance(it)]
		
		new StartAdapter(start as Start);
	}
	
	/**
	 * Gets the probability of the branch.
	 * @return the probability of the branch
	 */
	def getProbability() {
		entity.branchProbability
	}
}
