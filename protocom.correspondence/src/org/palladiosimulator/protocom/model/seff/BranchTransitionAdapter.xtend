package org.palladiosimulator.protocom.model.seff

import org.palladiosimulator.pcm.seff.AbstractBranchTransition
import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.seff.StartAction

/**
 * @author Christian Klaussner
 */
class BranchTransitionAdapter<E extends AbstractBranchTransition> extends ModelAdapter<E> {
	new(E entity) {
		super(entity)
	}
	
	def getStart() {
		val behaviour = entity.branchBehaviour_BranchTransition
		val start = behaviour.steps_Behaviour.findFirst[StartAction.isInstance(it)]
		
		new StartActionAdapter(start as StartAction)
	}
}
