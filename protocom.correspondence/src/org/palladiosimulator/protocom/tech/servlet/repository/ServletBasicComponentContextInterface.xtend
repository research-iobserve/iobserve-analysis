package org.palladiosimulator.protocom.tech.servlet.repository

import org.palladiosimulator.protocom.tech.servlet.ServletInterface
import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.pcm.repository.OperationRequiredRole

class ServletBasicComponentContextInterface extends ServletInterface<BasicComponent> {
	new(BasicComponent pcmEntity) {
		super(pcmEntity)
	}
	
	override packageName() {
		JavaNames::fqnContextPackage(pcmEntity)
	}
	
	override compilationUnitName() {
		JavaNames::contextInterfaceName(pcmEntity)
	}
	
	override filePath() {
		"/src/" + JavaNames::fqnToDirectoryPath(JavaNames::fqnContextInterface(pcmEntity)) + ".java"
	}
	
	override methods() {
		val results = newLinkedList
		
		// Role getters.
		results += pcmEntity.requiredRoles_InterfaceRequiringEntity.filter[OperationRequiredRole.isInstance(it)].map[	
			new JMethod()
				.withName("get" + JavaNames::javaName(it))  
				.withReturnType("String")
		]
		
		// Role setters.
		results += pcmEntity.requiredRoles_InterfaceRequiringEntity.filter[OperationRequiredRole.isInstance(it)].map[
			new JMethod()
				.withName("set" + JavaNames::javaName(it))  
				.withParameters("String port")		
		]
		
		// Port getters.
		results += pcmEntity.requiredRoles_InterfaceRequiringEntity.filter[OperationRequiredRole.isInstance(it)].map[	
			new JMethod()
				.withName("getPortFor" + JavaNames::javaName(it))  
				.withReturnType(JavaNames::fqn((it as OperationRequiredRole).requiredInterface__OperationRequiredRole))
		]
		
		results
	}
}
