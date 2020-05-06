package org.palladiosimulator.protocom.lang.java.impl

import java.util.Collection
import org.palladiosimulator.protocom.lang.java.IJClass
import org.palladiosimulator.protocom.lang.java.IJMethod
import org.palladiosimulator.protocom.lang.java.IJField

class JClass extends JCompilationUnit<IJClass> implements IJClass {

	override Collection<String> interfaces() {
		provider.interfaces
	}

	override String superClass() {
		provider.superClass
	}
	
	override constructors() {
		provider.constructors
	}
	
	override annotations() {
		provider.annotations
	}

	override def String header() {
		'''
		package «packageName»;
		
		«IF annotations !== null && !annotations.empty»
			«FOR a : annotations SEPARATOR '\n'»«a.generate»«ENDFOR»
		«ENDIF»
		public class «compilationUnitName»«IF superClass !== null» extends «superClass»«ENDIF»«implementedClasses»'''
	}

	override body() {
		'''
		«FOR field : fields AFTER '\n'»
			«field(field)»
		«ENDFOR»
		«IF !constructors.empty»	
			«FOR constructor : constructors SEPARATOR '\n'»
				«constructor(constructor)»
			«ENDFOR»
			
		«ENDIF»
		«FOR method : methods SEPARATOR '\n'»
			«method(method)»
		«ENDFOR»
		'''
	}
	

	def field(IJField field) {
		'''«field.visibility»«IF field.staticModifier» static«ENDIF»«IF field.finalModifier» final«ENDIF» «field.type» «field.name»«IF field.initialization !== null» = «field.initialization»«ENDIF»;'''
	}
	
	def constructor(IJMethod method) {
		'''
		«method.visibilityModifier» «compilationUnitName»(«method.parameters») «IF method.throwsType !== null»throws «method.throwsType» «ENDIF»{
			«method.body»
		}
		'''
	}
	
	def method(IJMethod method) {
		'''
		«FOR a : method.annotations SEPARATOR '\n'»«a.generate»«ENDFOR»
		«method.visibilityModifier»«IF method.isStatic» «method.staticModifier»«ENDIF» «method.returnType» «method.name»(«method.parameters»)«IF method.throwsType !== null» throws «method.throwsType»«ENDIF»«IF method.body !== null» {
			«method.body»
		}«ELSE»;«ENDIF»
		'''
	}
}
