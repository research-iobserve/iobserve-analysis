package org.palladiosimulator.protocom.model.repository

import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.protocom.model.ModelAdapter

/**
 * Adapter class for PCM OperationProvidedRole entities.
 * @author Christian Klaussner
 */
class OperationProvidedRoleAdapter extends ModelAdapter<OperationProvidedRole> {
	new(OperationProvidedRole entity) {
		super(entity)
	}
	
	def getProvidedInterface() {
		new OperationInterfaceAdapter(entity.providedInterface__OperationProvidedRole)
	}
	
	// Translation methods
	
	def getPortClassName() {
		entity.providedInterface__OperationProvidedRole.safeName
		+ "_"
		+ entity.providingEntity_ProvidedRole.safeName
	}
}
