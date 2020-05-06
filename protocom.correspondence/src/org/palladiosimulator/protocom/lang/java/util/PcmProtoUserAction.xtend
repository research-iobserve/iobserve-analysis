package org.palladiosimulator.protocom.lang.java.util

import org.palladiosimulator.pcm.usagemodel.Branch
import org.palladiosimulator.pcm.usagemodel.Delay
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall
import org.palladiosimulator.pcm.usagemodel.Loop
import org.palladiosimulator.pcm.usagemodel.Start
import org.palladiosimulator.pcm.usagemodel.Stop

/**
 * Defines templates for actions of both kinds: SEFF actions and user actions.
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PcmProtoUserAction extends PcmUserAction {
	
	/**
	 * EntryLevelSystemCall is an user action which calls a system service from an usage scenario.
	 */
	dispatch override String userAction(EntryLevelSystemCall userAction) {
		'''
		{
			ctx.getStack().createAndPushNewStackFrame();
			
			// EntryLevelSystemCall!
			«PcmCommons::call(	userAction.operationSignature__EntryLevelSystemCall,  
								null,  
								JavaNames::portMemberVar(userAction.providedRole_EntryLevelSystemCall)  + ".",
								userAction.inputParameterUsages_EntryLevelSystemCall, 
								null
							)»
			ctx.getStack().removeStackFrame();		
		}
		'''
	}
	
	/**
	 * FIXME Implement and test this action with Thread.sleep
	 */
	dispatch override String userAction(Delay userAction) {
		''''''
	}
	
	dispatch override String userAction(Start userAction) {
		'''
		ctx.getStack().createAndPushNewStackFrame();
		'''
	}
	
	dispatch override String userAction(Stop userAction) {
		'''
		ctx.getStack().removeStackFrame();
		'''
	}
	
	/**
	 * Loop actions are transformed into a simple FOR statement.
	 */
	dispatch override String userAction(Loop userAction) {
		'''
		for (int iterationCount_«JavaNames::javaVariableName(userAction.id)» = 0, maxIterationCount_«JavaNames::javaVariableName(userAction.id)» = ctx.evaluate("«JavaNames::specificationString(userAction.loopIteration_Loop.specification)»", Integer.class); 
			iterationCount_«JavaNames::javaVariableName(userAction.id)» < maxIterationCount_«JavaNames::javaVariableName(userAction.id)»; iterationCount_«JavaNames::javaVariableName(userAction.id)» ++) {
			«userActions(userAction.bodyBehaviour_Loop.actions_ScenarioBehaviour.findUserStart)»
		} 
		'''
	}
	
	/**
	 * UserActions only have probabilistic transitions.
	 */
	dispatch override String userAction(Branch userAction) {
		'''
		double u«JavaNames::javaVariableName(userAction.id)» = ctx.evaluate("DoublePDF[(1.0;1.0)]", Double.class);
		double sum«JavaNames::javaVariableName(userAction.id)» = 0;
		
		«FOR transition : userAction.branchTransitions_Branch»
			if (sum«JavaNames::javaVariableName(userAction.id)» <= u«JavaNames::javaVariableName(userAction.id)» && u«JavaNames::javaVariableName(userAction.id)» < sum«JavaNames::javaVariableName(userAction.id)» + «transition.branchProbability») {
				«userActions(transition.branchedBehaviour_BranchTransition.actions_ScenarioBehaviour.findUserStart)»
			}	
			sum«JavaNames::javaVariableName(userAction.id)» += «transition.branchProbability»;	
		«ENDFOR»
		'''
	}	
}
