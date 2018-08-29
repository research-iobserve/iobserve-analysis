package org.palladiosimulator.protocom.tech.iiop.system

import com.google.common.collect.ArrayListMultimap
import org.palladiosimulator.pcm.system.System
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPReadMeFile

class JavaEEIIOPReadMe extends JavaEEIIOPReadMeFile<System>{
	
	new(System pcmEntity) {
		super(pcmEntity)
	}
	
	override filePath() {
		"ReadMe.txt"
	}
	
	override projectName() {
		".ReadMe"
	}
	
	override basicComponentClassName() {
		val basicComponentClasses = newHashMap
		for(assemblyContext: pcmEntity.assemblyContexts__ComposedStructure){
			basicComponentClasses.put(
				JavaNames::fqnJavaEEBasicComponentProjectName(assemblyContext.encapsulatedComponent__AssemblyContext),
				JavaNames::fqnJavaEEBasicComponentClassName(assemblyContext.encapsulatedComponent__AssemblyContext)
				)
		}
		return basicComponentClasses
	}
	
	override basicComponentPortClassName() {
		val basicComponentPortClasses = ArrayListMultimap.create
		for(assemblyContext: pcmEntity.assemblyContexts__ComposedStructure){
			var providedRoles = assemblyContext.encapsulatedComponent__AssemblyContext.providedRoles_InterfaceProvidingEntity
			var portNames = newLinkedList
			for(providedRole : providedRoles){
				portNames.add(JavaNames::portClassName(providedRole))
			}
				basicComponentPortClasses.putAll(
				JavaNames::fqnJavaEEBasicComponentProjectName(assemblyContext.encapsulatedComponent__AssemblyContext),portNames)
		}
		return basicComponentPortClasses
	}
	
	
}