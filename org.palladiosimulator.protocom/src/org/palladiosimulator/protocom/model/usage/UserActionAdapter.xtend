package org.palladiosimulator.protocom.model.usage

import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction
import org.palladiosimulator.pcm.usagemodel.Branch
import org.palladiosimulator.pcm.usagemodel.Delay
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall
import org.palladiosimulator.pcm.usagemodel.Loop
import org.palladiosimulator.pcm.usagemodel.Start
import org.palladiosimulator.pcm.usagemodel.Stop

/**
 * Abstract base class for PCM user actions.
 * @author Christian Klaussner
 */
class UserActionAdapter<E extends AbstractUserAction> extends ModelAdapter<E> {
	new(E entity) {
		super(entity)
	}
	
	/**
	 * Gets the successor action.
	 * @return an adapter for the successor action
	 */
	def getSuccessor() {
		val successor = entity.successor
		
		switch successor {
			Branch: new BranchAdapter(successor)
			Delay: new DelayAdapter(successor)
			EntryLevelSystemCall: new EntryLevelSystemCallAdapter(successor)
			Loop: new LoopAdapter(successor)
			Start: new StartAdapter(successor)
			Stop: new StopAdapter(successor)
			
			default: throw new RuntimeException("unknown action type")
		}
	}
}
