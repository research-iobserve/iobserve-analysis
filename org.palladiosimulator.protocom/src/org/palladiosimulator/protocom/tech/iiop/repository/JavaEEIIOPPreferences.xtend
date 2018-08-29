package org.palladiosimulator.protocom.tech.iiop.repository

import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPPreferencesFile

class JavaEEIIOPPreferences extends JavaEEIIOPPreferencesFile<BasicComponent>{
	
	new(BasicComponent pcmEntity) {
		super(pcmEntity)
	}
	
	override eclipsePreferencesVersion() {
		'''1'''
	}
	
	override codegenInlineJsrBytecode() {
		'''enabled'''
	}
	
	override codegenTargetPlatform() {
		'''1.7'''
	}
	
	override compliance() {
		'''1.7'''
	}
	
	override problemAssertIdentifier() {
		'''error'''
	}
	
	override problemEnumIdentifier() {
		'''error'''
	}
	
	override source() {
		'''1.7'''
	}
	
	override filePath() {
		JavaNames::fqnJavaEEPreferencesPath(pcmEntity) + "org.eclipse.jdt.core.prefs"
	}
	
	override projectName() {
		JavaNames::fqnJavaEEDescriptorProjectName(pcmEntity)
		
	}
	
}
