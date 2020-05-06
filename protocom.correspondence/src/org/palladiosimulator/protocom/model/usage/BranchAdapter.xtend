package org.palladiosimulator.protocom.model.usage

import org.palladiosimulator.pcm.usagemodel.Branch

/**
 * Adapter class for PCM Branch user actions.
 * @author Christian Klaussner
 */
class BranchAdapter extends UserActionAdapter<Branch> {
	new(Branch entity) {
		super(entity)
	}
	
	/**
	 * Gets the branch transitions.
	 * @return a list of adapters for the branch transitions
	 */
	def getBranchTransitions() {
		entity.branchTransitions_Branch.map[
			new BranchTransitionAdapter(it)
		]
	}
}
