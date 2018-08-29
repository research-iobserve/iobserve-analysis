package org.palladiosimulator.protocom.tech.rmi.util

import org.palladiosimulator.pcm.seff.ExternalCallAction
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.lang.java.util.PcmProtoAction

/**
 * Defines templates for actions of both kinds: SEFF actions and user actions.
 * 
 * TODO: Remove programming language and technology-depend source from this class
 * and use template methods instead. Also move it to .lang then.
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PcmRMIProtoAction extends PcmProtoAction {


	/**
	 * ExternalCallAction calls a remote service.
	 * 
	 * TODO: Move exception handling to RMI tech.
	 */
	dispatch override String action(ExternalCallAction action) {
		'''
		{
			try {
				// Start Simulate an external call
				de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> currentFrame = ctx.getStack().currentStackFrame();
				// prepare stackframe
				de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> stackframe = ctx.getStack().createAndPushNewStackFrame();
						
				«PcmCommons::call(action.calledService_ExternalService, action.calledService_ExternalService, 
					"myContext.getRole"+JavaNames::javaName(action.role_ExternalService)+"().",
					action.inputVariableUsages__CallAction, action.returnVariableUsage__CallReturnAction)»
			} catch (java.rmi.RemoteException e) {
				// TODO: add logging
				
			} finally {
				ctx.getStack().removeStackFrame();
			}
					
		}
		'''
	}	
		
}
