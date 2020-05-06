package org.palladiosimulator.protocom.lang.java.util

import org.palladiosimulator.pcm.seff.BranchAction
import org.palladiosimulator.pcm.seff.CollectionIteratorAction
import org.palladiosimulator.pcm.seff.ExternalCallAction
import org.palladiosimulator.pcm.seff.ForkAction
import org.palladiosimulator.pcm.seff.GuardedBranchTransition
import org.palladiosimulator.pcm.seff.InternalAction
import org.palladiosimulator.pcm.seff.LoopAction
import org.palladiosimulator.pcm.seff.ProbabilisticBranchTransition
import org.palladiosimulator.pcm.seff.SetVariableAction

/**
 * Defines templates for actions of both kinds: SEFF actions and user actions.
 * 
 * @author Sebastian Lehrig
 */
class PcmStubAction extends PcmAction {

	/**
	 * No idea. We didn't implement this for the last ProtoCom either.
	 * FIXME Implement this as it is a crucial part of the behavior (lehrig)
	 */
	dispatch override String action(CollectionIteratorAction action) {
		'''
			'''
	}

	/**
	 * LoopAction is transformed into a simple FOR statement.
	 */
	dispatch override String action(LoopAction action) {
		'''
			// TODO Configure maxIterationCount 
			int maxIterationCount«JavaNames::javaVariableName(action.id)» = 1;
			
			for (int iterationCount«JavaNames::javaVariableName(action.id)» = 0; iterationCount«JavaNames::
				javaVariableName(action.id)» < maxIterationCount«JavaNames::javaVariableName(action.id)»; iterationCount«JavaNames::
				javaVariableName(action.id)» ++) {
				«actions(action.bodyBehaviour_Loop.steps_Behaviour.findStart)»
			}
		'''
	}

	/**
	 * ExternalCallAction calls a remote service.
	 * 
	 * TODO: Move exception handling to RMI tech.
	 */
	dispatch override String action(ExternalCallAction action) {
		'''
			{
					// TODO Initialize parameters
					«FOR parameter : action.calledService_ExternalService.parameters__OperationSignature»
						«DataTypes::getDataType(parameter.dataType__Parameter)+" param_"+parameter.parameterName+" = null;"»
					«ENDFOR»
					
					«PcmCommons::callStub(action.calledService_ExternalService, action.calledService_ExternalService,
				"myContext.getRole" + JavaNames::javaName(action.role_ExternalService) + "().",
				action.inputVariableUsages__CallAction, action.returnVariableUsage__CallReturnAction)»
			}
		'''
	}

	/**
	 * Currently, InteralActions are removed for stubs.
	 */
	dispatch override String action(InternalAction action) {
		'''
			'''
	}

	/**
	 * BranchAction is implemented as an IF condition. A BranchAction may have two different transition types:
	 * ProbabilisticBranchTransition and GuardedBranchTransition. For code stubs, they are both treated as a
	 * normal if-else-if-else series. 
	 */
	dispatch override String action(BranchAction action) {
		'''
			// TODO Set condition.
			Boolean condition = true;
			«FOR branch : action.branches_Branch SEPARATOR " else "»
				«branchTransition(action, branch)»
			«ENDFOR»
		'''
	}

	/**
	 * Branch transition for ProbabilisticBranchTransition entities. Handled like GuardedBranchTransition for code stubs.
	 */
	dispatch override branchTransition(BranchAction action, ProbabilisticBranchTransition transition) {
		'''
			if (condition) {
				«actions(transition.branchBehaviour_BranchTransition.steps_Behaviour.findStart)»
			}
		'''
	}

	/**
	 * Branch transition for GuardedBranchTransition.
	 */
	dispatch override branchTransition(BranchAction action, GuardedBranchTransition transition) {
		'''
			if (condition) {
				«actions(transition.branchBehaviour_BranchTransition.steps_Behaviour.findStart)»
			}
		'''
	}

	/**
	 * TODO Check whether SetVariableAction is needed.
	 */
	dispatch override String action(SetVariableAction action) {
	}

	/**
	 * A ForkAction spawns a new thread for each defined behavior. These should be processed asynchronously in parallel.
	 * Please note that manually spawning new threads is discouraged on certain middlewares (like JavaEE)!
	 * 
	 * TODO Check whether ForkAction is needed.
	 */
	dispatch override String action(ForkAction action) {
		'''
			'''
	}
}
