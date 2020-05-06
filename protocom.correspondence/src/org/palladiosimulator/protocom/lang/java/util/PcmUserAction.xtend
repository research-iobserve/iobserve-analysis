package org.palladiosimulator.protocom.lang.java.util

import org.palladiosimulator.pcm.usagemodel.AbstractUserAction
import org.palladiosimulator.pcm.usagemodel.Branch
import org.palladiosimulator.pcm.usagemodel.Delay
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall
import org.palladiosimulator.pcm.usagemodel.Loop
import org.palladiosimulator.pcm.usagemodel.Start
import org.palladiosimulator.pcm.usagemodel.Stop

/**
 * Abstract class for implementing PCM actions, i.e., the behavior of components as specified
 * via SEFFs. Refinements of this class could, for instance, add simulate stack frames for performance
 * prototypes or provide code stub generation.
 * 
 * @author Sebastian Lehrig, Daria Giacinto
 */
abstract class PcmUserAction {
	
	
	/**
	 * Follows the user action path and calls "userAction" for each action in it.
	 * Note that actions do not branch! Branching is solved by a Branch action, therefore 
	 * at most one successor is given at any time.
	 */
	def String userActions(AbstractUserAction userAction) {
		'''
		/*
		 * «userAction.class.simpleName» («userAction»)
		 */
		«userAction(userAction)»
		
		«IF !Stop.isInstance(userAction)»
			«userActions(userAction.successor)»
		«ENDIF»
		'''
	}
	
	/**
	 * EntryLevelSystemCall is an user action which calls a system service from an usage scenario.
	 */
	dispatch def String userAction(EntryLevelSystemCall userAction) {
		'''
		'''
	}
	
	/**
	 * FIXME Implement and test this action with Thread.sleep
	 */
	dispatch def String userAction(Delay userAction) {
		''''''
	}
	
	dispatch def String userAction(Start userAction) {
		'''
		'''
	}
	
	dispatch def String userAction(Stop userAction) {
		'''
		'''
	}
	
	/**
	 * Loop actions are transformed into a simple FOR statement.
	 */
	dispatch def String userAction(Loop userAction) {
		'''
		'''
	}
	
	/**
	 * UserActions only have probabilistic transitions.
	 */
	dispatch def String userAction(Branch userAction) {
		'''
		'''
	}	
	
	/**
	 * And another helper method, since Actions and UserActions are *obviously* so
	 * different that they cannot have a common supertype...
	 */
	static def Start findUserStart(Iterable<AbstractUserAction> actions) {
		actions.findFirst[Start.isInstance(it)] as Start
	}
}