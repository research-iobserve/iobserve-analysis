package org.palladiosimulator.protocom.lang.prefs

import org.palladiosimulator.protocom.lang.ICompilationUnit

interface IJeePreferences extends ICompilationUnit {
	
	def String eclipsePreferencesVersion()
	
	def String codegenInlineJsrBytecode()
	
	def String codegenTargetPlatform()
	
	def String compliance()
	
	def String problemAssertIdentifier()
	
	def String problemEnumIdentifier()
	
	def String source()
	
}