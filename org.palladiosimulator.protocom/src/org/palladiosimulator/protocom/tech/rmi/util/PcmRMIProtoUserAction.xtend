package org.palladiosimulator.protocom.tech.rmi.util

import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.lang.java.util.PcmProtoUserAction

/**
 * Defines templates for actions of both kinds: SEFF actions and user actions.
 * 
 * TODO: Remove programming language and technology-depend source from this class
 * and use template methods instead. Also move it to .lang then.
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PcmRMIProtoUserAction extends PcmProtoUserAction {
	
	/**
	 * EntryLevelSystemCall is an user action which calls a system service from an usage scenario.
	 */
	dispatch override String userAction(EntryLevelSystemCall userAction) {
		'''
		try {
			ctx.getStack().createAndPushNewStackFrame();
			
			// EntryLevelSystemCall!
			«PcmCommons::call(	userAction.operationSignature__EntryLevelSystemCall,  
								null,  
								JavaNames::portMemberVar(userAction.providedRole_EntryLevelSystemCall)  + ".",
								userAction.inputParameterUsages_EntryLevelSystemCall, 
								null
							)»
										
		} catch (java.rmi.RemoteException e) {
			// TODO: Logger!
			
		} finally {
			ctx.getStack().removeStackFrame();		
		}
		'''
	}
	


}
