package org.palladiosimulator.protocom.lang.prefs.impl

import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.prefs.IJeePreferences

class JeePreferences extends GeneratedFile<IJeePreferences> implements IJeePreferences {
	
	override generate() {
		'''«eclipsePreferencesVersion»
«codegenInlineJsrBytecode»
«codegenTargetPlatform»
«compliance»
«problemAssertIdentifier»
«problemEnumIdentifier»
«source»
		'''
	}
	
	override eclipsePreferencesVersion() {
		'''eclipse.preferences.version=«provider.eclipsePreferencesVersion»'''
	}
	
	override codegenInlineJsrBytecode() {
		'''org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=«provider.codegenInlineJsrBytecode»'''
	}
	
	override codegenTargetPlatform() {
		'''org.eclipse.jdt.core.compiler.codegen.targetPlatform=«provider.codegenTargetPlatform»'''
	}
	
	override compliance() {
		'''org.eclipse.jdt.core.compiler.compliance=«provider.compliance»'''
	}
	
	override problemAssertIdentifier() {
		'''org.eclipse.jdt.core.compiler.problem.assertIdentifier=«provider.problemAssertIdentifier»'''
	}
	
	override problemEnumIdentifier() {
		'''org.eclipse.jdt.core.compiler.problem.enumIdentifier=«provider.problemEnumIdentifier»'''
	}
	
	override source() {
		'''org.eclipse.jdt.core.compiler.source=«provider.source»'''
	}
	
}