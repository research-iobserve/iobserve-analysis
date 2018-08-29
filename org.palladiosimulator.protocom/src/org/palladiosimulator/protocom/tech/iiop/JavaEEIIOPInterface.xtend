package org.palladiosimulator.protocom.tech.iiop

import org.palladiosimulator.pcm.core.entity.Entity
import org.palladiosimulator.protocom.lang.java.IJeeInterface
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.tech.ConceptMapping

class JavaEEIIOPInterface <E extends Entity> extends ConceptMapping<E> implements IJeeInterface{
	
	new(E pcmEntity) {
		super(pcmEntity)
	}
	
	override packageName() {
		JavaNames::fqnJavaEEInterfacePackage(pcmEntity)
	}
	
	override compilationUnitName() {
		JavaNames::fqnJavaEEInterfaceName(pcmEntity)
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
		JavaNames::fqnJavaEEOperationInterfacePath(pcmEntity)
	}
	
	override jeeInterfaceAnnotation() {
		JavaConstants::JEE_INTERFACE_ANNOTATION_REMOTE
	}
	
	override projectName() {
		JavaNames::fqnJavaEEOperationInterfaceProjectName(pcmEntity)
	}
	
}