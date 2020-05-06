package org.palladiosimulator.protocom.lang.java.util

import org.palladiosimulator.pcm.usagemodel.Branch
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall
import org.palladiosimulator.pcm.usagemodel.Loop

/**
 * Defines templates for actions of both kinds: SEFF actions and user actions.
 *
 * 
 * @author Sebastian Lehrig
 */
class PcmStubUserAction extends PcmUserAction {
	/**
	 * EntryLevelSystemCall is an user action which calls a system service from an usage scenario.
	 */
	dispatch override String userAction(EntryLevelSystemCall userAction) {
		'''
			try {
				// EntryLevelSystemCall!
				«PcmCommons::call(
				userAction.operationSignature__EntryLevelSystemCall,
				null,
				JavaNames::portMemberVar(userAction.providedRole_EntryLevelSystemCall) + ".",
				userAction.inputParameterUsages_EntryLevelSystemCall,
				null
			)»
											
			} catch (java.rmi.RemoteException e) {
				// TODO: Logger!
				
			}
		'''
	}

	/**
	 * Loop actions are transformed into a simple FOR statement.
	 */
	dispatch override String userAction(Loop userAction) {
		'''
			// TODO Configure maxIterationCount 
			int maxIterationCount«JavaNames::javaVariableName(userAction.id)» = 1;
			
			for (int iterationCount_«JavaNames::javaVariableName(userAction.id)» = 0; 
				iterationCount_«JavaNames::javaVariableName(userAction.id)» < maxIterationCount_«JavaNames::
				javaVariableName(userAction.id)»; iterationCount_«JavaNames::javaVariableName(userAction.id)» ++) {
				«userActions(userAction.bodyBehaviour_Loop.actions_ScenarioBehaviour.findUserStart)»
			} 
		'''
	}

	/**
	 * UserActions only have probabilistic transitions. That might even make sense for test generation 
	 * for code stubs. Therefore, This might be added here.
	 * 
	 * TODO Branch user actions are currently not generated with code stubs. AS probabilities make sense here the ctx object might be useful. In any case, for test generation, user actions should be included in future.
	 */
	dispatch override String userAction(Branch userAction) {
		'''
			double u«JavaNames::javaVariableName(userAction.id)» = ctx.evaluate("DoublePDF[(1.0;1.0)]", Double.class);
			double sum«JavaNames::javaVariableName(userAction.id)» = 0;
			
			«FOR transition : userAction.branchTransitions_Branch»
				if (sum«JavaNames::javaVariableName(userAction.id)» <= u«JavaNames::javaVariableName(userAction.id)» && u«JavaNames::
				javaVariableName(userAction.id)» < sum«JavaNames::javaVariableName(userAction.id)» + «transition.
				branchProbability») {
					«userActions(transition.branchedBehaviour_BranchTransition.actions_ScenarioBehaviour.findUserStart)»
				}	
				sum«JavaNames::javaVariableName(userAction.id)» += «transition.branchProbability»;	
			«ENDFOR»
		'''
	}
}
