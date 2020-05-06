package org.palladiosimulator.protocom.tech.servlet.util

import org.palladiosimulator.pcm.repository.OperationSignature
import org.palladiosimulator.pcm.seff.AbstractAction
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.model.seff.ActionAdapter
import org.palladiosimulator.protocom.model.seff.BranchActionAdapter
import org.palladiosimulator.protocom.model.seff.ExternalCallActionAdapter
import org.palladiosimulator.protocom.model.seff.GuardedBranchTransitionAdapter
import org.palladiosimulator.protocom.model.seff.InternalActionAdapter
import org.palladiosimulator.protocom.model.seff.LoopActionAdapter
import org.palladiosimulator.protocom.model.seff.ProbabilisticBranchTransitionAdapter
import org.palladiosimulator.protocom.model.seff.StartActionAdapter
import org.palladiosimulator.protocom.model.seff.StopActionAdapter
import org.palladiosimulator.protocom.model.seff.ForkActionAdapter
import org.palladiosimulator.protocom.model.seff.AcquireActionAdapter
import org.palladiosimulator.protocom.model.seff.ReleaseActionAdapter
import org.palladiosimulator.protocom.lang.java.util.JavaNames

/**
 * @author Christian Klaussner
 * @author Sebastian Lehrig
 */
class PcmServletProtoAction {
	protected val frameworkBase = "org.palladiosimulator.protocom.framework.java.ee"
	
	def String actions(ActionAdapter<? extends AbstractAction> action) {
		'''
		/*
		 * «action.entity.class.simpleName» («action.entity»)
		 */
		«action(action)»
		«IF !StopActionAdapter.isInstance(action)»
			«action(action.successor)»
		«ENDIF»
		'''
	}
	
	private def dispatch action(BranchActionAdapter action) {
		'''
		«IF ProbabilisticBranchTransitionAdapter.isInstance(action.branchTransitions.get(0))»
			double u«JavaNames::javaVariableName(action.id)» = (Double) ctx.evaluate("DoublePDF[(1.0;1.0)]", Double.class);
			double sum«JavaNames::javaVariableName(action.id)» = 0;
			
			«FOR transition : action.branchTransitions»
				«branchTransition(action, transition)»
			«ENDFOR»
		«ELSE»
			«FOR transition : action.branchTransitions SEPARATOR " else "»
				«branchTransition(action, transition)»
			«ENDFOR»
		«ENDIF»
		'''
	}
	
	private def dispatch branchTransition(BranchActionAdapter action, ProbabilisticBranchTransitionAdapter transition) {
		'''
		if (sum«JavaNames::javaVariableName(action.id)» <= u«JavaNames::javaVariableName(JavaNames::javaVariableName(action.id))» && u«JavaNames::javaVariableName(JavaNames::javaVariableName(action.id))» < sum«JavaNames::javaVariableName(JavaNames::javaVariableName(action.id))» + «transition.probability») {
			«actions(transition.start)»
		}
		'''
	}
	
	private def dispatch branchTransition(BranchActionAdapter action, GuardedBranchTransitionAdapter transition) {
		'''
		if (ctx.evaluate("«action.safeSpecification(transition.condition)»", Boolean.class) == true) {
			«actions(transition.start)»
		}
		'''
	}
	
	private def dispatch action(ForkActionAdapter action) {
		'''
		«FOR forkedBehaviour : action.asynchronousForkedBehaviours»
				FIXME Add fork with ID «forkedBehaviour.id»
		«ENDFOR»
		'''
	}
	
	
	private def dispatch action(ExternalCallActionAdapter action) {
		'''
		{
			try {
				// Start Simulate an external call
				// de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> currentFrame = ctx.getStack().currentStackFrame();
				// prepare stackframe
				// de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> stackframe = ctx.getStack().createAndPushNewStackFrame();
				ctx.getStack().createAndPushNewStackFrame();
				
				«PcmCommons::call(
					action.calledService.entity as OperationSignature,
					action.calledService.entity as OperationSignature, 
					"context.getPortFor" + action.role.safeName + "().",
					action.inputVariableUsages, action.returnVariableUsage)»
			} catch (java.lang.Exception e) {
				// TODO: add logging
				
			} finally {
				ctx.getStack().removeStackFrame();
			}
		}
		'''
	}
	
	private def dispatch action(InternalActionAdapter action) {
		'''
		«FOR resourceDemand : action.resourceDemands»
		{
			double demand = ctx.evaluate("«action.safeSpecification(resourceDemand.specification)»", Double.class);
			
			«IF resourceDemand.type.matches("cpu")»
				«frameworkBase».prototype.StrategiesRegistry.getInstance().getStrategy(org.palladiosimulator.protocom.resourcestrategies.activeresource.ResourceTypeEnum.CPU).consume(demand);
			«ELSEIF resourceDemand.type.matches("hdd")»
				«frameworkBase».prototype.StrategiesRegistry.getInstance().getStrategy(org.palladiosimulator.protocom.resourcestrategies.activeresource.ResourceTypeEnum.HDD).consume(demand);
			«ELSEIF resourceDemand.type.matches("delay")»
			«ELSE»
				throw new java.lang.UnsupportedOperationException("Resource type not yet supported");
			«ENDIF»
		}
		«ENDFOR»
		'''
	}
	
	private def dispatch action(LoopActionAdapter action) {
		'''
		int maxIterationCount«JavaNames::javaVariableName(action.id)» = ctx.evaluate("«action.safeSpecification(action.iterationCount)»", Integer.class);
		
		for (int iterationCount«JavaNames::javaVariableName(action.id)» = 0; iterationCount«JavaNames::javaVariableName(action.id)» < maxIterationCount«JavaNames::javaVariableName(action.id)»; iterationCount«JavaNames::javaVariableName(action.id)»++) {
			«actions(action.start)»
		}
		'''
	}
	
	private def dispatch action(AcquireActionAdapter action) {
		"FIXME AcquireActions not supported yet"
	}
	
	private def dispatch action(ReleaseActionAdapter action) {
		"FIXME ReleaseActions not supported yet"
	}
	
	private def dispatch action(StartActionAdapter action) {
		""
	}
	
	private def dispatch action(StopActionAdapter action) {
		""
	}	
}
