package org.palladiosimulator.protocom.tech.rmi.repository

import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.PcmCommons
import org.palladiosimulator.protocom.tech.rmi.PojoInterface

/**
 * Defining the content of component implementation interfaces (the interfaces for the 
 * classes implementing the component behavior).
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PojoComponentClassInterface extends PojoInterface<BasicComponent> {
	
	new(BasicComponent pcmEntity) {
		super(pcmEntity)
	}
	
	override compilationUnitName() {
		JavaNames::interfaceName(pcmEntity)
	}
	
	override interfaces() {
		#["org.palladiosimulator.protocom.framework.java.se.IPerformancePrototypeComponent"]
	}
	
	override methods() {
		val results = newLinkedList
		
		// Context & ComponentFrame
		results += #[
			new JMethod()
				.withName("setContext")
				.withParameters("Object myContext")
			,
			new JMethod()
				.withName("setComponentFrame")
				.withParameters(PcmCommons::stackframeParameterList)
			]
			
		// From operation interfaces	
		results += pcmEntity.serviceEffectSpecifications__BasicComponent.map[
			new JMethod()
				.withName(JavaNames::serviceName(it.describedService__SEFF))
				.withReturnType(PcmCommons::stackframeType)
				.withParameters(PcmCommons::stackContextParameterList)
		]
		
		// Provided ports getter for OperationProvidedRoles
		results += pcmEntity.providedRoles_InterfaceProvidingEntity.filter[OperationProvidedRole.isInstance(it)].map[it as OperationProvidedRole].map[
			new JMethod()
				.withName(JavaNames::portGetter(it))
				.withReturnType(JavaNames::fqn(it.providedInterface__OperationProvidedRole))
		]
		
		// Provided ports getter for InfrastructureProvidedRoles
//		results += pcmEntity.providedRoles_InterfaceProvidingEntity.filter[InfrastructureProvidedRole.isInstance(it)].map[it as InfrastructureProvidedRole].map[
//			new JMethod()
//				.withName(JavaNames::portGetter(it))
//				.withReturnType(JavaNames::fqn(it.providedInterface__InfrastructureProvidedRole))
//		]
				
		results
	}
	
	override filePath() {
		"/src/" + JavaNames::fqnToDirectoryPath(JavaNames::fqnInterface(pcmEntity)) + ".java"
	}

	
}