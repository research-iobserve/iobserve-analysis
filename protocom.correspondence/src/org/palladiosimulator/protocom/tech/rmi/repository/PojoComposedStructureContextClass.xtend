package org.palladiosimulator.protocom.tech.rmi.repository

import org.palladiosimulator.pcm.repository.OperationRequiredRole
import org.palladiosimulator.protocom.lang.java.impl.JField
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaConstants
import org.palladiosimulator.pcm.core.entity.InterfaceRequiringEntity
import org.palladiosimulator.protocom.tech.rmi.PojoClass

/**
 * @author Thomas Zolynski, Sebastian Lehrig
 */
class PojoComposedStructureContextClass extends PojoClass<InterfaceRequiringEntity> {
	
	new(InterfaceRequiringEntity pcmEntity) {
		super(pcmEntity)
	}
	
	override fields() {
		val results = newLinkedList
			
		results += pcmEntity.requiredRoles_InterfaceRequiringEntity.filter[OperationRequiredRole.isInstance(it)].map[
	
			new JField()
				.withType(JavaNames::fqn((it as OperationRequiredRole).requiredInterface__OperationRequiredRole))
				.withName(JavaNames::javaName(it).toFirstLower)
		]
		
		results
	}
	
	override constructors() {
		#[ 
			new JMethod()
				.withParameters('''«FOR role : pcmEntity.requiredRoles_InterfaceRequiringEntity SEPARATOR ", "»«JavaNames::fqn((role as OperationRequiredRole).requiredInterface__OperationRequiredRole)» «JavaNames::javaName(role).toFirstLower» «ENDFOR»''')
				.withImplementation('''«FOR role : pcmEntity.requiredRoles_InterfaceRequiringEntity SEPARATOR ", "»this.«JavaNames::javaName(role).toFirstLower» = «JavaNames::javaName(role).toFirstLower»;«ENDFOR»''')
		]
	}
	
	override packageName() {
		JavaNames::fqnContextPackage(pcmEntity)
	}
	
	override compilationUnitName() {
		JavaNames::contextClassName(pcmEntity)	
	}
	
	override interfaces() {
		#[ JavaNames::contextInterfaceName(pcmEntity), JavaConstants::SERIALIZABLE_INTERFACE ]
	}
	
	override methods() {
		val results = newLinkedList
		
		results += pcmEntity.requiredRoles_InterfaceRequiringEntity.filter[OperationRequiredRole.isInstance(it)].map[
			
			new JMethod()
				.withName("getRole" + JavaNames::javaName(it))  
				.withReturnType(JavaNames::fqn((it as OperationRequiredRole).requiredInterface__OperationRequiredRole))
				.withImplementation(
					'''
					if («JavaNames::javaName(it).toFirstLower» == null) {
						throw new RuntimeException("Attempt to retrieve unbounded port. Check your architecture! Role «it.entityName» <«it.id»> RequiringEntity «it.requiringEntity_RequiredRole.entityName»");
					}
					return «JavaNames::javaName(it).toFirstLower»;
					''')
					
		]
		
		results += pcmEntity.requiredRoles_InterfaceRequiringEntity.filter[OperationRequiredRole.isInstance(it)].map[
			
			new JMethod()
				.withName("setRole" + JavaNames::javaName(it))  
				.withParameters(JavaNames::fqn((it as OperationRequiredRole).requiredInterface__OperationRequiredRole) + " newValue")
				.withImplementation('''this.«JavaNames::javaName(it).toFirstLower» = newValue;''')
					
		]
		
		results
		
	}
	
	override filePath() {
		"/src/" + JavaNames::fqnToDirectoryPath(JavaNames::fqnContext(pcmEntity)) + ".java"
	}
	
	
}