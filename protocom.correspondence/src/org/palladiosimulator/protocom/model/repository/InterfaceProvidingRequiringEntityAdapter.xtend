package org.palladiosimulator.protocom.model.repository

import org.palladiosimulator.protocom.model.ModelAdapter
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingRequiringEntity
import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole
import org.palladiosimulator.pcm.repository.OperationRequiredRole

/**
 * Abstract base class for PCM InterfaceProvidingRequiring entity adapters.
 * @author Christian Klaussner
 */
abstract class InterfaceProvidingRequiringEntityAdapter
	<E extends InterfaceProvidingRequiringEntity> extends ModelAdapter<E> {
	
	new(E entity) {
		super(entity)
	}

	/**
	 * Gets the operation provided roles.
	 * @return a list of adapters for the operation provided roles
	 */
	def getOperationProvidedRoles() {
		entity.providedRoles_InterfaceProvidingEntity.filter[
			OperationProvidedRole.isInstance(it)
		].map[
			new OperationProvidedRoleAdapter(it as OperationProvidedRole)
		]
	}
	
	/**
	 * Gets the infrastructure provided roles.
	 * @return a list of adapters for the infrastructure provided roles
	 */
	def getInfrastructureProvidedRoles() {
		entity.providedRoles_InterfaceProvidingEntity.filter[
			InfrastructureProvidedRole.isInstance(it)
		].map[
			new InfrastructureProvidedRoleAdapter(it as InfrastructureProvidedRole)
		]
	}
	
	/**
	 * Gets the operation required roles.
	 * @return a list of adapters for the operation required roles
	 */
	def getOperationRequiredRoles() {
		entity.requiredRoles_InterfaceRequiringEntity.filter[
			OperationRequiredRole.isInstance(it)
		].map[
			new OperationRequiredRoleAdapter(it as OperationRequiredRole)
		]
	}
}
