package org.palladiosimulator.protocom.lang.java.impl

import org.palladiosimulator.protocom.lang.java.IJMethod
import org.palladiosimulator.protocom.lang.java.IJeeInterface

class JeeInterface extends JCompilationUnit<IJeeInterface> implements IJeeInterface{
	
	override header() {
		'''
		package «packageName»;
		
		@«jeeInterfaceAnnotation»
		public interface «compilationUnitName» «implementedClasses»
		'''
	}
	
	override body() {
		'''
		«FOR method : methods»
			«signature(method)»
		«ENDFOR»
		'''
	}
	
	def signature (IJMethod method) {
		'''«method.visibilityModifier» «method.returnType» «method.name» («method.parameters»)«IF method.throwsType !== null» throws «method.throwsType»«ENDIF»;'''
	}
	
	override implementedClasses() {
		'''
		«IF interfaces !== null»
			«FOR implInterface : interfaces BEFORE ' extends ' SEPARATOR ', '»«implInterface»«ENDFOR»
		«ENDIF»
		'''
	}
	
	override jeeInterfaceAnnotation() {
		provider.jeeInterfaceAnnotation
	}
	
}