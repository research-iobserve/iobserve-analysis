package org.palladiosimulator.protocom.tech.iiop.repository

import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.protocom.lang.java.util.JavaNames
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPComponentFile

class JavaEEIIOPEclipseComponentFile extends JavaEEIIOPComponentFile<BasicComponent>{
	
	new(BasicComponent pcmEntity) {
		super(pcmEntity)
	}
	
	override wbModuleDeployName() {
		JavaNames::fqnJavaEEBasicComponentClassPackage(pcmEntity)
	}
	
	override wbResource() {
		'''tag="defaultRootSource"'''
	}
	
	override property() {
		JavaNames::fqnJavaEEBasicComponentClassPackage(pcmEntity)
		
	}
	
	override filePath() {
		JavaNames::fqnJavaEEPreferencesPath(pcmEntity) + "org.eclipse.wst.prototype.component"
	}
	
	override projectName() {
		JavaNames::fqnJavaEEDescriptorProjectName(pcmEntity)
	}
	
}