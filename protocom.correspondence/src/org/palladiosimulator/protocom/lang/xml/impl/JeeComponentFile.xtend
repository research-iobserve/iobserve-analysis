package org.palladiosimulator.protocom.lang.xml.impl

import com.google.inject.Inject
import com.google.inject.name.Named
import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.xml.IJeeComponentFile

class JeeComponentFile extends GeneratedFile<IJeeComponentFile> implements IJeeComponentFile {
	
	@Inject
	@Named("ProjectURI")
	String projectURI
	
	override generate() {
		'''
		«header»
		«body»
		'''
	}
	
	def header() {
		'''<?xml version="1.0" encoding="UTF-8"?>'''
	}
	
	def body() {
		'''<project-modules id="moduleCoreId" project-version="1.5.0">
  <wb-module deploy-name="«projectURI».«wbModuleDeployName»">
	<wb-resource deploy-path="/" source-path="/ejbModule" «wbResource»/>
	«IF property !== null»
	<property name="java-output-path" value="/«projectURI».«property»/build/classes"/>
	<property name="ClientProject" value="«projectURI».«property»Client"/>
	<property name="ClientJARURI" value="«projectURI».«property»Client.jar"/>
	«ENDIF»
  </wb-module>
</project-modules>'''
	}
	
	override wbModuleDeployName() {
		provider.wbModuleDeployName
	}
	
	override wbResource() {
		provider.wbResource
	}
	
	override property() {
		provider.property
	}
	
}