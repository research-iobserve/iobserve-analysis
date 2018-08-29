package org.palladiosimulator.protocom.model.seff

import org.palladiosimulator.pcm.seff.ProbabilisticBranchTransition

/**
 * @author Christian Klaussner
 */
class ProbabilisticBranchTransitionAdapter extends BranchTransitionAdapter<ProbabilisticBranchTransition> {
	new(ProbabilisticBranchTransition entity) {
		super(entity)
	}
	
	def getProbability() {
		entity.branchProbability
	}
}
