package org.palladiosimulator.protocom.tech.rmi

import org.palladiosimulator.protocom.tech.ConceptMapping
import org.palladiosimulator.protocom.lang.java.IJClass
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.pcm.core.entity.Entity

/**
 * Common super type for all provider creating Java classes. Defines default values
 * for all templates.
 * 
 * @author Thomas Zolynski
 */
abstract class PojoClass<E extends Entity> extends ConceptMapping<E> implements IJClass {
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override superClass() {
	}
	
	override constructors() {
		newLinkedList
	}
	
	override packageName() {
		JavaNames::implementationPackage(pcmEntity)
	}
	
	override compilationUnitName() {
		JavaNames::javaName(pcmEntity)
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
	
	override filePath() {
		JavaNames::getFileName(pcmEntity)
	}
	
	override projectName(){
		
	}
	
	override annotations() {
	}
	
}