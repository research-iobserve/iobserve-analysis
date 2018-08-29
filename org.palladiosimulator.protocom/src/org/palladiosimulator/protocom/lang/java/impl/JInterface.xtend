package org.palladiosimulator.protocom.lang.java.impl

import org.palladiosimulator.protocom.lang.java.IJInterface
import org.palladiosimulator.protocom.lang.java.IJMethod

class JInterface extends JCompilationUnit<IJInterface> implements IJInterface {
	
	override body() {
		'''
		«FOR method : methods»
			«signature(method)»
		«ENDFOR»
		'''
	}
	
	override header() {
		'''
		package «packageName»;
		
		public interface «compilationUnitName»«IF implementedClasses != null» «implementedClasses»«ENDIF»'''
	}
	
	
	override implementedClasses() {
		if (!interfaces.empty) {
			'''«FOR implInterface : interfaces BEFORE ' extends ' SEPARATOR ', '»«implInterface»«ENDFOR»'''
		} else {
			null
		}
		
//		'''
//		«IF interfaces != null»
//			«FOR implInterface : interfaces BEFORE ' extends ' SEPARATOR ', '»«implInterface»«ENDFOR»
//		«ENDIF»
//		'''
	}
	
	def signature (IJMethod method) {
		'''«method.visibilityModifier» «method.returnType» «method.name»(«method.parameters»)«IF method.throwsType != null» throws «method.throwsType»«ENDIF»;'''
	}
}