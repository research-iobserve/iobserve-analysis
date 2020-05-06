package org.palladiosimulator.protocom.tech.servlet.repository

import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity
import org.palladiosimulator.protocom.lang.java.IJInterface
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.tech.ConceptMapping

class ServletComposedStructureInterface extends ConceptMapping<InterfaceProvidingEntity> implements IJInterface {
	new(InterfaceProvidingEntity pcmEntity) {
		super(pcmEntity)
	}
		
	override packageName() {
		JavaNames::implementationPackage(pcmEntity)
	}
	
	override compilationUnitName() {
		JavaNames::interfaceName(pcmEntity)
	}
	
	override interfaces() {
	}
	
	override methods() {
		var result = newLinkedList
		
		result
	}
	
	override fields() {
	}
	
	override projectName() {
	}
	
	override filePath() {
		"/src/" + JavaNames::fqnToDirectoryPath(JavaNames::fqnInterface(pcmEntity)) + ".java"
	}
}
