package org.palladiosimulator.protocom.lang.xml.impl

import com.google.inject.Inject
import com.google.inject.name.Named
import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.xml.IJeeClasspath

class JeeClasspath extends GeneratedFile<IJeeClasspath> implements IJeeClasspath{
	
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
		'''<?xml version='1.0'?>'''
	}
	
	def body() {
		'''
<classpath>
	«classPathEntries»
	«IF clientClassPathEntry !== null»
	<classpathentry combineaccessrules="false" kind="src" path="/«projectURI».«clientClassPathEntry»"/>
	«ENDIF»
	«FOR requiredProject : requiredClientProjects»
	<classpathentry combineaccessrules="false" kind="src" path="/«projectURI».«requiredProject»"/>
	«ENDFOR»
	<classpathentry kind="output" path="build/classes"/>
</classpath>'''
	}
	
	override clientClassPathEntry() {
		provider.clientClassPathEntry
	}
	
	override classPathEntries() {
		provider.classPathEntries
	}
	
	override requiredClientProjects() {
		provider.requiredClientProjects
	}
	
}