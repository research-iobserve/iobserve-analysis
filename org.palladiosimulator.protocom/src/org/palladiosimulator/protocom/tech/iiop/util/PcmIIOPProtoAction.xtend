package org.palladiosimulator.protocom.tech.iiop.util

import org.palladiosimulator.pcm.seff.ExternalCallAction
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.lang.java.util.PcmProtoAction

/**
 * Defines templates for actions of both kinds: SEFF actions.
 * 
 * @author Thomas Zolynski, Sebastian Lehrig, Daria Giacinto
 */
class PcmIIOPProtoAction extends PcmProtoAction {


	/**
	 * ExternalCallAction calls a remote service.
	 * 
	 * TODO: Implement method body regarding java ee
	 */
	dispatch override String action(ExternalCallAction action) {
		'''
		{
				// Start Simulate an external call
				de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> currentFrame = ctx.getStack().currentStackFrame();
				// prepare stackframe
				de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> stackframe = ctx.getStack().createAndPushNewStackFrame();
						
				«PcmCommons::call(action.calledService_ExternalService, action.calledService_ExternalService, 
					JavaNames::javaName(action.role_ExternalService).toFirstLower+".",
					action.inputVariableUsages__CallAction, action.returnVariableUsage__CallReturnAction)»
				ctx.getStack().removeStackFrame();
					
		}
		'''
	}	
		
}
