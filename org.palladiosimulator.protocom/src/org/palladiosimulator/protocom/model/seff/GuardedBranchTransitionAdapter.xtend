package org.palladiosimulator.protocom.model.seff

import org.palladiosimulator.pcm.seff.GuardedBranchTransition

/**
 * @author Christian Klaussner
 */
class GuardedBranchTransitionAdapter extends BranchTransitionAdapter<GuardedBranchTransition> {
	new(GuardedBranchTransition entity) {
		super(entity)
	}
	
	def getCondition() {
		entity.branchCondition_GuardedBranchTransition.specification
	}
}
