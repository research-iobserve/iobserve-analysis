package org.palladiosimulator.protocom.tech.pojo.repository

import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.DataTypes
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.util.Parameters
import org.palladiosimulator.protocom.tech.rmi.PojoInterface

/**
 * Defining the content of component implementation interfaces (the interfaces for the 
 * classes implementing the component behavior).
 * 
 * @author Sebastian Lehrig
 */
class PojoComponentClassInterface extends PojoInterface<BasicComponent> {
	
	new(BasicComponent pcmEntity) {
		super(pcmEntity)
	}
	
	override compilationUnitName() {
		JavaNames::interfaceName(pcmEntity)
	}
	
	override interfaces() {
		#["org.palladiosimulator.protocom.framework.java.se.IComponent"]
	}
	
	override methods() {
		val results = newLinkedList
		
		// Context
		results += #[
			new JMethod()
				.withName("setContext")
				.withParameters("Object myContext")
			]
			
		// From operation interfaces	
		results += pcmEntity.serviceEffectSpecifications__BasicComponent.map[
			new JMethod()
				.withName(JavaNames::serviceNameStub(it.describedService__SEFF))
				.withReturnType(DataTypes::getReturnDataType(it.describedService__SEFF))
				.withParameters(Parameters::getParameterList(it.describedService__SEFF))
		]
		
		// Provided ports getter for OperationProvidedRoles
		results += pcmEntity.providedRoles_InterfaceProvidingEntity.filter[OperationProvidedRole.isInstance(it)].map[it as OperationProvidedRole].map[
			new JMethod()
				.withName(JavaNames::portGetter(it))
				.withReturnType(JavaNames::fqn(it.providedInterface__OperationProvidedRole))
		]
			
		results
	}
	
	override filePath() {
		"/src/" + JavaNames::fqnToDirectoryPath(JavaNames::fqnInterface(pcmEntity)) + ".java"
	}

	
}