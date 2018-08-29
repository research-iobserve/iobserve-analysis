package org.palladiosimulator.protocom.lang.xml.impl

import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.xml.IClasspath

class Classpath extends GeneratedFile<IClasspath> implements IClasspath{
	
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
</classpath>'''
	}
	
	override classPathEntries() {
		provider.classPathEntries
	}
	
}