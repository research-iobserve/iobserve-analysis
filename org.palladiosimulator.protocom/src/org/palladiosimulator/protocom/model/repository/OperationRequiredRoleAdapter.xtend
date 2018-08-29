package org.palladiosimulator.protocom.model.repository

import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.repository.OperationRequiredRole

/**
 * Adapter class for PCM OperationRequiredRole entities.
 * @author Christian Klaussner
 */
class OperationRequiredRoleAdapter extends ModelAdapter<OperationRequiredRole> {
	new(OperationRequiredRole entity) {
		super(entity)
	}
	
	/**
	 * Gets the ID.
	 * @return a string containing the ID
	 */
	def getId() {
		entity.id
	}
	
	/**
	 * Gets the requiring entity.
	 * @return an adapter for the requiring entity
	 */
	def getRequiringEntity() {
		new InterfaceRequiringEntityAdapter(entity.requiringEntity_RequiredRole)
	}
	
	/**
	 * Get the required interface.
	 * @return an adapter for the required interface
	 */
	def getRequiredInterface() {
		new OperationInterfaceAdapter(entity.requiredInterface__OperationRequiredRole)
	}
}
