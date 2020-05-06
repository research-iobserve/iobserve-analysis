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
 * Defines templates for SEFF actions.
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PcmProtoAction extends PcmAction {
	/**
	 * No idea. We didn't implement this for the last ProtoCom either.
	 * FIXME Implement this as it is a crucial part of the bahavior (lehrig)
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
		int maxIterationCount«JavaNames::javaVariableName(action.id)» = ctx.evaluate("«JavaNames::specificationString(action.iterationCount_LoopAction.specification)»", Integer.class);
		
		for (int iterationCount«JavaNames::javaVariableName(action.id)» = 0; iterationCount«JavaNames::javaVariableName(action.id)» < maxIterationCount«JavaNames::javaVariableName(action.id)»; iterationCount«JavaNames::javaVariableName(action.id)» ++) {
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
				// Start Simulate an external call
				de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> currentFrame = ctx.getStack().currentStackFrame();
				// prepare stackframe
				de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> stackframe = ctx.getStack().createAndPushNewStackFrame();
						
				«PcmCommons::call(action.calledService_ExternalService, action.calledService_ExternalService, 
					"myContext.getRole"+JavaNames::javaName(action.role_ExternalService)+"().",
					action.inputVariableUsages__CallAction, action.returnVariableUsage__CallReturnAction)»
				ctx.getStack().removeStackFrame();			
		}
		'''
	}

	/**
	 * InteralAction uses a load generator to simulate CPU/HDD usage.
	 * Note that ProtoCom does NOT use InfrastructureCalls from the PCM model, because these 
	 * should be reflected by the underlying middleware and OS!
	 */
	dispatch override String action(InternalAction action) {
		'''
		«FOR resourceDemand : action.resourceDemand_Action»
			{
				double demand = ctx.evaluate("«JavaNames::specificationString(resourceDemand.specification_ParametericResourceDemand.specification)»", Double.class);
				
				«IF resourceDemand.requiredResource_ParametricResourceDemand.entityName.toLowerCase.matches("cpu")»
					org.palladiosimulator.protocom.framework.java.se.strategies.DemandConsumerStrategiesRegistry.singleton().getStrategyFor(org.palladiosimulator.protocom.resourcestrategies.activeresource.ResourceTypeEnum.CPU).consume(demand);
				«ELSEIF resourceDemand.requiredResource_ParametricResourceDemand.entityName.toLowerCase.matches("hdd")»
					org.palladiosimulator.protocom.framework.java.se.strategies.DemandConsumerStrategiesRegistry.singleton().getStrategyFor(org.palladiosimulator.protocom.resourcestrategies.activeresource.ResourceTypeEnum.HDD).consume(demand);
				«ELSEIF resourceDemand.requiredResource_ParametricResourceDemand.entityName.toLowerCase().matches("delay")»
					org.palladiosimulator.protocom.framework.AbstractResourceEnvironment.performDelay(demand);
				«ELSE»
					throw new java.lang.UnsupportedOperationException("Resourcetype not yet supported in prototype");
				«ENDIF»
			}
		«ENDFOR»
		'''
	}

	/**
	 * BranchAction is implemented as an IF condition. A BranchAction may have two different transition types:
	 * ProbabilisticBranchTransition and GuardedBranchTransition.
	 */
	dispatch override String action(BranchAction action) {
		'''
		«IF ProbabilisticBranchTransition.isInstance(action.branches_Branch.get(0))»
			double u«JavaNames::javaVariableName(JavaNames::javaVariableName(action.id))» = (Double) ctx.evaluate("DoublePDF[(1.0;1.0)]", Double.class);
			double sum«JavaNames::javaVariableName(JavaNames::javaVariableName(action.id))» = 0;
			
			«FOR branch : action.branches_Branch»
				«branchTransition(action, branch)»
			«ENDFOR»
		«ELSE»
			«FOR branch : action.branches_Branch SEPARATOR " else "»
				«branchTransition(action, branch)»
			«ENDFOR»
		«ENDIF»
		'''
	}
	
	/**
	 * Branch transition for ProbabilisticBranchTransition entities.
	 */
	dispatch override branchTransition(BranchAction action, ProbabilisticBranchTransition transition) {
		'''
		if (sum«JavaNames::javaVariableName(action.id)» <= u«JavaNames::javaVariableName(action.id)» && u«JavaNames::javaVariableName(action.id)» < sum«JavaNames::javaVariableName(action.id)» + «transition.branchProbability») {
			«actions(transition.branchBehaviour_BranchTransition.steps_Behaviour.findStart)»
		}
		'''
	}
	
	/**
	 * Branch transition for GuardedBranchTransition.
	 */
	dispatch override branchTransition(BranchAction action, GuardedBranchTransition transition) {
		'''
		if (ctx.evaluate("«JavaNames::specificationString(transition.branchCondition_GuardedBranchTransition.specification)»", Boolean.class) == true) {
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
