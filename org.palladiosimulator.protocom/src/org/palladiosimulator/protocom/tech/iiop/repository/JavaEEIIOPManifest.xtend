package org.palladiosimulator.protocom.tech.iiop.repository

import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPMFFile

class JavaEEIIOPManifest extends JavaEEIIOPMFFile<BasicComponent> {
	
	new(BasicComponent pcmEntity) {
		super(pcmEntity)
	}
	
	override classPath(){
		JavaNames::javaEEEjbClientjar(pcmEntity)
	}
	
	override manifestVersion(){
		'''1.0'''
	}
	
	override filePath(){
		JavaNames::fqnJavaEEDescriptorPath(pcmEntity)+"MANIFEST.MF"
	}
	
	override projectName(){
		JavaNames::fqnJavaEEDescriptorProjectName(pcmEntity)
	}
}