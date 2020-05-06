package org.palladiosimulator.protocom.lang.java.impl

import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.java.IJCompilationUnit

abstract class JCompilationUnit<E extends IJCompilationUnit> extends GeneratedFile<E> implements IJCompilationUnit {
	
	override String packageName() {
		provider.packageName
	}
	
	override compilationUnitName() {
		provider.compilationUnitName
	}
	
	override interfaces() {
		val i = provider.interfaces
		if (i !== null) i else #[]
	}	
	
	override methods() {
		val m = provider.methods
		if (m !== null) m else #[]
	}
	
	override fields() {
		val f = provider.fields
		if (f !== null) f else #[]
	}
	
	override generate() {
		'''
		«header» {
			«body»
		}
		'''
	}
	

	def implementedClasses() {
		'''«FOR implInterface : interfaces BEFORE ' implements ' SEPARATOR ', '»«implInterface»«ENDFOR»'''
	}
	
	
	/**
	 * Template for the header part of this compilation unit: package, imports, type definition.
	 */
	abstract def String header()
	
	/**
	 * Template for the body part of this compilation unit: members, methods/signatures.
	 */
	abstract def String body()

}

