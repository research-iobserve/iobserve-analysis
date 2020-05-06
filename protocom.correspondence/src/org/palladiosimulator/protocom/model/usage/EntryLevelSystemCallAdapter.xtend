package org.palladiosimulator.protocom.model.usage

import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall
import org.palladiosimulator.protocom.model.repository.OperationProvidedRoleAdapter
import org.palladiosimulator.protocom.model.repository.SignatureAdapter

/**
 * Adapter class for PCM EntryLevelSystemCall user actions.
 * @author Christian Klaussner
 */
class EntryLevelSystemCallAdapter extends UserActionAdapter<EntryLevelSystemCall> {
	new(EntryLevelSystemCall entity) {
		super(entity)
	}
	
	def getProvidedRole() {
		new OperationProvidedRoleAdapter(entity.providedRole_EntryLevelSystemCall)
	}
	
	def getOperationSignature() {
		new SignatureAdapter(entity.operationSignature__EntryLevelSystemCall)
	}
}
