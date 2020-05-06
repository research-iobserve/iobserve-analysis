package org.palladiosimulator.protocom.tech.servlet.repository

import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.lang.java.impl.JAnnotation
import org.palladiosimulator.protocom.lang.java.impl.JField
import org.palladiosimulator.protocom.lang.java.impl.JMethod
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.model.repository.BasicComponentAdapter
import org.palladiosimulator.protocom.tech.servlet.ServletClass

class ServletBasicComponentContextClass extends ServletClass<BasicComponent> {
	private val BasicComponentAdapter entity
	
	new(BasicComponentAdapter entity, BasicComponent pcmEntity) {
		super(pcmEntity)
		this.entity = entity
	}
	
	override packageName() {
		entity.contextPackageFqn
	}
	
	override compilationUnitName() {
		entity.contextClassName
	}
	
	override interfaces() {
		#[entity.contextInterfaceName]
	}
	
	override annotations() {
		#[
			new JAnnotation()
				.withName("com.fasterxml.jackson.annotation.JsonAutoDetect")
				.withValues(#["fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY"])
		]
	}
	
	override fields() {
		val result = newLinkedList
		
		val requiredRoles = entity.operationRequiredRoles
		
		// Port ID.
		result += requiredRoles.map[
			new JField()
				.withType("String")
				.withName(it.safeName.toFirstLower)
		]
		
		// Port class.
		result += requiredRoles.map[
			new JField()
				.withType(it.requiredInterface.interfaceFqn)
				.withName("portFor_" + it.safeName.toFirstLower)
		]
		
		result
	}
	
	override constructors() {
		var result = newLinkedList
		
		result +=
			new JMethod()
				.withImplementation('''
				''')
		
		if (entity.operationRequiredRoles.length > 0) {
			result +=
				new JMethod()
					.withParameters('''
						«FOR role : entity.operationRequiredRoles SEPARATOR ", "»
							String «role.safeName.toFirstLower»
						«ENDFOR»
					''')
					.withImplementation('''
						«FOR role : entity.operationRequiredRoles»
							this.«role.safeName.toFirstLower» = «role.safeName.toFirstLower»;
						«ENDFOR»
					''')
		}
		
		result
	}
	
	override methods() {
		var result = newLinkedList
		
		result += entity.operationRequiredRoles.map[
			new JMethod()
				.withName("get" + it.safeName)
				.withReturnType("String")
				.withImplementation('''
					return «it.safeName.toFirstLower»;
				''')
		]
		
		result += entity.operationRequiredRoles.map[
			new JMethod()
			  	.withName("set" + it.safeName)
			  	.withParameters("String port")
			  	.withImplementation('''
			  		«it.safeName.toFirstLower» = port;
			  	''')
		]
		
		result += entity.operationRequiredRoles.map[
			new JMethod()
				.withName("getPortFor" + it.safeName)
				.withReturnType(it.requiredInterface.interfaceFqn)
				.withAnnotations(#[
					new JAnnotation()
						.withName("com.fasterxml.jackson.annotation.JsonIgnore") 
				])
				.withImplementation('''
					try {
						portFor_«it.safeName.toFirstLower» = («it.requiredInterface.interfaceFqn») «frameworkBase».protocol.Registry.getInstance().lookup(«it.safeName.toFirstLower»);
					} catch («frameworkBase».protocol.RegistryException e) {
						e.printStackTrace();
					}
					
					return portFor_«it.safeName.toFirstLower»;
				''')
		]
		
		result
	}
	
	override filePath() {
		"/src/" + JavaNames::fqnToDirectoryPath(JavaNames::fqnContext(pcmEntity)) + ".java"
	}
}
