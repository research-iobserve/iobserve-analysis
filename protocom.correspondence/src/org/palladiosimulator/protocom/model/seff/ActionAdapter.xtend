package org.palladiosimulator.protocom.model.seff

import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.seff.AbstractAction
import org.palladiosimulator.pcm.seff.StartAction
import org.palladiosimulator.pcm.seff.StopAction
import org.palladiosimulator.pcm.seff.BranchAction
import org.palladiosimulator.pcm.seff.ExternalCallAction
import org.palladiosimulator.pcm.seff.InternalAction
import org.palladiosimulator.pcm.seff.ForkAction
import org.palladiosimulator.pcm.seff.AcquireAction
import org.palladiosimulator.pcm.seff.ReleaseAction

/**
 * @author Christian Klaussner
 */
class ActionAdapter<E extends AbstractAction> extends ModelAdapter<E> {
	new(E entity) {
		super(entity)
	}
	
	def getId() {
		entity.id
	}
	
	def getSuccessor() {
		val successor = entity.successor_AbstractAction
		
		switch successor {
			BranchAction: new BranchActionAdapter(successor)
			ExternalCallAction: new ExternalCallActionAdapter(successor)
			InternalAction: new InternalActionAdapter(successor)
			StartAction: new StartActionAdapter(successor)
			StopAction: new StopActionAdapter(successor)
			ForkAction: new ForkActionAdapter(successor)
			AcquireAction: new AcquireActionAdapter(successor)
			ReleaseAction: new ReleaseActionAdapter(successor)
			
			default: throw new RuntimeException("unknown action type ("+successor.toString+")")
		}
	}
	
	// Translation methods
	
	def getSafeId() {
		// TODO: Really necessary? IDs seem to be alphanumeric (+ underscore)...
	}
}
