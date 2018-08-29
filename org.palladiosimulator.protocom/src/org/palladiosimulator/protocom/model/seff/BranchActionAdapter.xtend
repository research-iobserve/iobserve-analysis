package org.palladiosimulator.protocom.model.seff

import org.palladiosimulator.pcm.seff.BranchAction
import org.palladiosimulator.pcm.seff.ProbabilisticBranchTransition
import org.palladiosimulator.pcm.seff.GuardedBranchTransition
import java.util.List
import org.palladiosimulator.pcm.seff.AbstractBranchTransition

/**
 * @author Christian Klaussner
 */
class BranchActionAdapter extends ActionAdapter<BranchAction> {
	new(BranchAction entity) {
		super(entity)
	}
	
	def List<BranchTransitionAdapter<? extends AbstractBranchTransition>> getBranchTransitions() {
		entity.branches_Branch.map[
			switch it {
				ProbabilisticBranchTransition:
					new ProbabilisticBranchTransitionAdapter(it)
					
				GuardedBranchTransition:
					new GuardedBranchTransitionAdapter(it)
			}
		]
	}
}
