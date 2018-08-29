package org.palladiosimulator.protocom.lang.java.impl

import org.palladiosimulator.protocom.lang.java.IJAnnotation
import java.util.List

class JAnnotation implements IJAnnotation {
	var String name
	var List<String> values
	
	override name() {
		name
	}
	
	override values() {
		values
	}
	
	override generate() {
		if (values != null && !values.empty) {
			'''@«name»(«FOR v : values SEPARATOR ', '»«v»«ENDFOR»)'''
		} else {
			'''@«name»'''
		}
	}
	
	def withName(String name) {
		this.name = name
		this
	}
	
	def withValues(List<String> values) {
		this.values = values
		this
	}
}
