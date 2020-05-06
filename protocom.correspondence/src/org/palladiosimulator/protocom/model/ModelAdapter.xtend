package org.palladiosimulator.protocom.model

import org.palladiosimulator.pcm.core.entity.NamedElement
import org.eclipse.emf.ecore.EObject

/**
 * Base class for all PCM model adapters.
 * @author Christian Klaussner
 */
abstract class ModelAdapter<T extends EObject> {
	protected val T entity
	
	new(T entity) {
		this.entity = entity
	}
	
	/**
	 * Gets the PCM entity that the adapter represents.
	 * @return the PCM entity
	 */
	def getEntity() {
		entity
	}
	
	/**
	 * Gets the name of the entity.
	 * @return a string containing the name of the entity
	 */
	def getName() {
		switch entity {
			NamedElement : entity.entityName
			default : ""
		}
	}
	
	// Translation methods
	
	def getSafeName() {
		(entity as NamedElement).safeName
	}
	
	def safeSpecification(String specification) {
		specification.replaceAll('"', "\\\\").replaceAll("\\s", " ");
	}
	
	protected def getSafeName(NamedElement entity) {
		entity.entityName.replaceAll("[(\")(\\s)(<)(>)(:)(\\.)(\\\\)(\\+)(\\-)(\\()(\\))]", "_")
	}
	
	protected def getBasePackageName(NamedElement entity) {
		entity.safeName.toLowerCase
	}
}
