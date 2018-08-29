package org.palladiosimulator.protocom.model.seff

import org.palladiosimulator.pcm.seff.ExternalCallAction
import org.palladiosimulator.protocom.model.repository.SignatureAdapter
import org.palladiosimulator.protocom.model.repository.OperationRequiredRoleAdapter

/**
 * @author Christian Klaussner
 */
class ExternalCallActionAdapter extends ActionAdapter<ExternalCallAction> {
	new(ExternalCallAction entity) {
		super(entity)
	}
	
	def getCalledService() {
		new SignatureAdapter(entity.calledService_ExternalService)
	}
	
	def getRole() {
		new OperationRequiredRoleAdapter(entity.role_ExternalService)
	}
	
	def getInputVariableUsages() {
		// TODO: This method should return a list of VariableUsageAdapter objects.
		// However, PcmCommons and the RMI transformations still use the EMF
		// objects directly and, therefore, need to support adapters first.
		
		entity.inputVariableUsages__CallAction
	}
	
	def getReturnVariableUsage() {
		// TODO: This method should return a list of VariableUsageAdapter objects.
		// See getInputVariableUsages
		
		entity.returnVariableUsage__CallReturnAction
	}
}
