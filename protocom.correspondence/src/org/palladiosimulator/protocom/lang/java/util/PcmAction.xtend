package org.palladiosimulator.protocom.lang.java.util

import org.palladiosimulator.pcm.seff.AbstractAction
import org.palladiosimulator.pcm.seff.AcquireAction
import org.palladiosimulator.pcm.seff.BranchAction
import org.palladiosimulator.pcm.seff.CollectionIteratorAction
import org.palladiosimulator.pcm.seff.EmitEventAction
import org.palladiosimulator.pcm.seff.ExternalCallAction
import org.palladiosimulator.pcm.seff.ForkAction
import org.palladiosimulator.pcm.seff.GuardedBranchTransition
import org.palladiosimulator.pcm.seff.InternalAction
import org.palladiosimulator.pcm.seff.LoopAction
import org.palladiosimulator.pcm.seff.ProbabilisticBranchTransition
import org.palladiosimulator.pcm.seff.ReleaseAction
import org.palladiosimulator.pcm.seff.SetVariableAction
import org.palladiosimulator.pcm.seff.StartAction
import org.palladiosimulator.pcm.seff.StopAction

/**
 * Abstract class for implementing PCM actions, i.e., the behavior of components as specified
 * via SEFFs. Refinements of this class could, for instance, add simulate stack frames for performance
 * prototypes or provide code stub generation.
 * 
 * @author Sebastian Lehrig
 */
abstract class PcmAction {
	/**
	 * Follows the action path and calls "action" for each action in it.
	 * Note that actions do not branch! Branching is solved by a BranchAction, therefore 
	 * at most one successor is given at any time.
	 */
	def String actions(AbstractAction action) {
		'''		
		/*
		 * «action.class.simpleName» («action»)
		 */
		«action(action)»

		«IF (!StopAction.isInstance(action))»
			«actions(action.successor_AbstractAction)»
		«ENDIF»
		'''
	}

	/**
	 * StartAction. Should be empty, I guess.
	 */
	dispatch def String action(StartAction action) {
		'''
		'''
	}

	/**
	 * StopAction. Nothing to see here either.
	 */
	dispatch def String action(StopAction action) {
		'''
		'''
	}

	/**
	 * No idea. We didn't implement this for the last ProtoCom either.
	 * FIXME Implement this as it is a crucial part of the bahavior (lehrig)
	 */
	dispatch def String action(CollectionIteratorAction action) {
		'''
		'''
	}

	/**
	 * LoopAction is transformed into a simple FOR statement.
	 */
	dispatch def String action(LoopAction action) {
		'''
		'''
	}

	/**
	 * ExternalCallAction calls a remote service.
	 *
	 */
	dispatch def String action(ExternalCallAction action) {
		'''
		'''
	}

	/**
	 * InteralAction uses a load generator to simulate CPU/HDD usage.
	 * Note that ProtoCom does NOT use InfrastructureCalls from the PCM model, because these 
	 * should be reflected by the underlying middleware and OS!
	 */
	dispatch def String action(InternalAction action) {
		'''
		'''
	}

	/**
	 * BranchAction is implemented as an IF condition. A BranchAction may have two different transition types:
	 * ProbabilisticBranchTransition and GuardedBranchTransition.
	 */
	dispatch def String action(BranchAction action) {
		'''
		'''
	}
	
	
	
	/**
	 * Branch transition for ProbabilisticBranchTransition entities.
	 */
	dispatch def branchTransition(BranchAction action, ProbabilisticBranchTransition transition) {
		'''
		'''
	}
	
	/**
	 * Branch transition for GuardedBranchTransition.
	 */
	dispatch def branchTransition(BranchAction action, GuardedBranchTransition transition) {
		'''
		'''
	}

	/**
	 * AcquireAction is mapped to the acquire method of Java Collection's semaphore implementation.
	 */
	dispatch def String action(AcquireAction action) {
		'''
		try {
			//logger.debug("Acquiring passive resource «JavaNames::javaString(action.passiveresource_AcquireAction.entityName)»");
			passive_resource_«JavaNames::javaVariableName(action.passiveresource_AcquireAction.entityName)».acquire();
		} catch (InterruptedException e) {
			//logger.error("Should never happen: Acquire of semaphore «JavaNames::javaString(action.passiveresource_AcquireAction.entityName)» interrupted");
			java.lang.System.exit(-1);
		}
		'''
	}

	/**
	 * ReleaseAction is mapped to the release method of Java Collection's semaphore implementation.
	 */
	dispatch def String action(ReleaseAction action) {
		'''
		// Release «action.passiveResource_ReleaseAction»
		//logger.debug("Releasing passive resource «JavaNames::javaString(action.passiveResource_ReleaseAction.entityName)»");
		passive_resource_«JavaNames::javaVariableName(action.passiveResource_ReleaseAction.entityName)».release();
		'''
	}

	dispatch def String action(SetVariableAction action) {
	}

	/**
	 * A ForkAction spawns a new thread for each defined behavior. These should be processed asynchronously in parallel.
	 * Please note that manually spawning new threads is discouraged on certain middlewares (like JavaEE)!
	 */
	dispatch def String action(ForkAction action) {
		'''
		'''		
	}
	
	/**
	 * TODO Think about EmitEventAction. JMS?
	 */
	dispatch def String action(EmitEventAction action) {
		'''
		// FIXME: Add EmitEventAction.
		'''
	}
	
	/**
	 * Helper method to find the first StartAction in a list of actions.
	 */
	static def StartAction findStart(Iterable<AbstractAction> actions) {
		actions.findFirst[StartAction.isInstance(it)] as StartAction
	}
	

}