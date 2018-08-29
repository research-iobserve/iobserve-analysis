package org.palladiosimulator.protocom.tech.rmi.util

import org.palladiosimulator.pcm.seff.ExternalCallAction
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.lang.java.util.PcmStubAction

/**
 * Defines templates for actions of both kinds: SEFF actions and user actions.
 * 
 * TODO: Remove programming language and technology-depend source from this class
 * and use template methods instead. Also move it to .lang then.
 * 
 * @author Sebastian Lehrig
 */
class PcmRMIStubAction extends PcmStubAction {

	
	/**
	 * ExternalCallAction calls a remote service.
	 * 
	 * TODO: Move exception handling to RMI tech.
	 */
	dispatch override String action(ExternalCallAction action) {
		'''
			{
				try {
					// TODO Initialize parameters
					«PcmCommons::callStub(action.calledService_ExternalService, action.calledService_ExternalService,
				"myContext.getRole" + JavaNames::javaName(action.role_ExternalService) + "().",
				action.inputVariableUsages__CallAction, action.returnVariableUsage__CallReturnAction)»
				} catch (java.rmi.RemoteException e) {
					// TODO: add logging
					
				}
			}
		'''
	}

	
}
