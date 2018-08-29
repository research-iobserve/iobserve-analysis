package org.palladiosimulator.protocom.tech.iiop.repository

import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPDescriptor

class JavaEEIIOPEjbDescriptor extends JavaEEIIOPDescriptor<BasicComponent>{
	
	new(BasicComponent pcmEntity) {
		super(pcmEntity)
	}
	
	override displayName() {
		JavaNames::javaEEDisplayName(pcmEntity)
	}
	
	override ejbClientJar() {
		JavaNames::javaEEEjbClientjar(pcmEntity)
		
	}
	
	override filePath() {
		JavaNames::fqnJavaEEDescriptorPath(pcmEntity)+"ejb-jar.xml"
	}
	
	override projectName(){
		JavaNames::fqnJavaEEDescriptorProjectName(pcmEntity)
	}
	
}