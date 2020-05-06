package org.palladiosimulator.protocom.tech.rmi

import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.tech.ConceptMapping
import org.palladiosimulator.protocom.lang.java.IJInterface
import org.palladiosimulator.protocom.lang.java.util.JavaNames

/**
 * Common super type for all provider creating Java interfaces. Defines default values
 * for all templates.
 * 
 * @author Thomas Zolynski
 */
abstract class PojoInterface<E extends Entity> extends ConceptMapping<E> implements IJInterface {
	
	new(E pcmEntity) {
		super(pcmEntity)
	}

	override packageName() {
		JavaNames::implementationPackage(pcmEntity)
	}
	
	override compilationUnitName() {
		JavaNames::javaName(pcmEntity)
	}
	
	override filePath() {
		JavaNames::getFileName(pcmEntity)
	}
	
	override interfaces() {
		newLinkedList
	}
	
	override methods() {
		newLinkedList
	}
	
	override fields() {
		newLinkedList
	}
	
	override projectName(){
		
	}
}