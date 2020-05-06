package org.palladiosimulator.protocom.tech.iiop

import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.lang.java.IJeeClass
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.tech.ConceptMapping

class JavaEEIIOPClass <E extends Entity> extends ConceptMapping<E> implements IJeeClass {
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override superClass() {
	}
	
	override constructors() {
		newLinkedList
	}
	
	override packageName() {
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
		JavaNames::fqnJavaEEBasicComponentClassPath(pcmEntity)
	}
	
	override jeeClassStatelessAnnotation() {
		JavaConstants::JEE_EJB_ANNOTATION_STATELESS
	}
	
	override jeeClassDependencyInjectionAnnotation() {
		JavaConstants::JEE_EJB_ANNOTATION_EJB
	}
	
	override jeeClassDependencyInjection() {
		newLinkedList
	}
	
	override projectName() {
		JavaNames::fqnJavaEEBasicComponentProjectName(pcmEntity)
	}
	
	override annotations() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	
}