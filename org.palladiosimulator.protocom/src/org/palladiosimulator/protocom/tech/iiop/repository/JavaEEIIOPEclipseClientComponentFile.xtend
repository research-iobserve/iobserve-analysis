package org.palladiosimulator.protocom.tech.iiop.repository

import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.lang.java.util.JavaNames

class JavaEEIIOPEclipseClientComponentFile extends JavaEEIIOPEclipseComponentFile {
	
	new(BasicComponent pcmEntity) {
		super(pcmEntity)
	}
	
	override wbModuleDeployName() {
		JavaNames::fqnJavaEEClientDeployName(pcmEntity)
	}	
	override wbResource() {
	}
	
	override property() {
	}
	
	override projectName() {
		JavaNames::fqnJavaEEOperationInterfaceProjectName(pcmEntity)	
	}
	
}