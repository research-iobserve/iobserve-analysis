package org.palladiosimulator.protocom.tech.rmi.repository

import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.protocom.tech.ConceptMapping
import org.palladiosimulator.protocom.lang.java.IJInterface
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity

/**
 * Defining the content of component implementation interfaces (the interfaces for 
 * the classes implementing the component behavior).
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PojoComposedStructureInterface extends ConceptMapping<InterfaceProvidingEntity> implements IJInterface {
	
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
		#[ JavaConstants::RMI_REMOTE_INTERFACE, JavaConstants::SERIALIZABLE_INTERFACE ]
	}
	
	override methods() {
		val results = newLinkedList
				
		// Context
		results += #[
			new JMethod()
				.withName("setContext")
				.withParameters("Object myContext")
			]
		
		// Provided ports getter for OperationProvidedRoles
		results += pcmEntity.providedRoles_InterfaceProvidingEntity.filter[OperationProvidedRole.isInstance(it)].map[
			new JMethod()
				.withName(JavaNames::portGetter(it))
				.withReturnType(JavaNames::fqn((it as OperationProvidedRole).providedInterface__OperationProvidedRole))
		]
				
		results
	}
	
	override fields() {
	}
	
	override filePath() {
		"/src/" + JavaNames::fqnToDirectoryPath(JavaNames::fqnInterface(pcmEntity)) + ".java"
	}

	override projectName() {
	}
	
}