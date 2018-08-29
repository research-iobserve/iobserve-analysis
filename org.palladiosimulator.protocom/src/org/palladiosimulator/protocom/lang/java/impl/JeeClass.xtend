package org.palladiosimulator.protocom.lang.java.impl

import java.util.Collection
import org.palladiosimulator.protocom.lang.java.IJField
import org.palladiosimulator.protocom.lang.java.IJMethod
import org.palladiosimulator.protocom.lang.java.IJeeClass

class JeeClass extends JCompilationUnit<IJeeClass> implements IJeeClass {
	
	
	override superClass() {
		provider.superClass
	}
	
	override constructors() {
		provider.constructors
	}
	
	override annotations() {
		provider.annotations
	}
	
	override Collection<String> interfaces() {
		provider.interfaces
	}
	
	override packageName() {
		provider.packageName
	}
	
	override header() {
			'''
			package «packageName»;
			
			@«jeeClassStatelessAnnotation»
			public class «compilationUnitName» «IF superClass != null»extends «superClass»«ENDIF» «implementedClasses»
		'''
	}
	
	override body() {
			'''		
			«FOR dependencyInjection : jeeClassDependencyInjection»
				@«jeeClassDependencyInjectionAnnotation»(name="«jeeClassDependencyInjectionNameAttribute(dependencyInjection)»")
				«field(dependencyInjection)»
			«ENDFOR»
			
			«FOR field : fields»
				«field(field)»
			«ENDFOR»
			
			«FOR constructor : constructors»
				«constructor(constructor)»
			«ENDFOR»
			
			«FOR method : methods»
				«method(method)»
			«ENDFOR»
			
		'''
	}
	
		def field(IJField field) {
		'''
		«field.visibility» «field.type» «field.name»;
		'''
	}
	
	def constructor(IJMethod method) {
		'''
		«method.visibilityModifier» «compilationUnitName» («method.parameters») «IF method.throwsType != null»throws «method.throwsType»«ENDIF»
		{
			«method.body»
		}
		
		'''
	}
	
	def method(IJMethod method) {
		'''
		«method.methodAnnotation»
		«method.visibilityModifier» «method.staticModifier» «method.returnType» «method.name» («method.parameters») «IF method.throwsType != null»throws «method.throwsType»«ENDIF»
		«IF method.body != null»
		{
			«method.body»
		}
		«ELSE»
		;
		«ENDIF»

		'''
	}
	
	override jeeClassStatelessAnnotation() {
		provider.jeeClassStatelessAnnotation
	}
	
	override jeeClassDependencyInjectionAnnotation() {
		provider.jeeClassDependencyInjectionAnnotation
	}
	
	override jeeClassDependencyInjection() {
		provider.jeeClassDependencyInjection
	}
	
	def jeeClassDependencyInjectionNameAttribute(IJField field) {
		'''«field.type»'''
	}
}
	
