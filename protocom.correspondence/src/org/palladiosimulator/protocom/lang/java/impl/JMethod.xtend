package org.palladiosimulator.protocom.lang.java.impl

import org.palladiosimulator.protocom.lang.java.IJMethod
import org.palladiosimulator.protocom.lang.java.IJAnnotation
import java.util.Collection

/**
 * Class representing a Java method.
 * 
 * This is a data class. Since methods are not a compilation unit, they do not
 * inherit GeneratedFile.
 * 
 * A JMethod without name should be handled as a constructor.
 * 
 * TODO: Change class to @Data ?
 * 
 * @author Thomas Zolynski
 */
class JMethod implements IJMethod {
	
	private String returnType
	private String name
	private String parameters
	private String implementation
	private String throwsType
	private String visibility
	private String methodAnnotation // TODO: Obsolete, still here for compatibility!
	private Collection<? extends IJAnnotation> annotations
	private boolean isStatic
	
	new() {
	}
	
	override returnType() {
		'''«IF returnType !== null»«returnType»«ELSE»void«ENDIF»'''
	}
	
	override name() {
		name
	}
	
	override parameters() {
		parameters
	}
	
	override visibilityModifier() {
		'''«IF visibility !== null»«visibility»«ELSE»public«ENDIF»'''
	}
	
	override throwsType() {
		throwsType
	}
	
	override staticModifier() {
		'''«IF isStatic»static«ENDIF»'''		
	}
	
	override isStatic() {
		isStatic
	}
	
	override annotations() {
		if (annotations !== null) annotations else #[]
	}
	
	override body() {
		'''«IF implementation !== null»«implementation»«ELSE»«ENDIF»'''
	}
	
	// TODO: Obsolete, still here for compatibility!
	override methodAnnotation() {
		methodAnnotation
	}
	
	def withReturnType(String returnType) {
		this.returnType = returnType
		this
	}
	
	def withName(String name) {
		this.name = name
		this
	}
	
	def withParameters(String parameters) {
		this.parameters = parameters
		this
	}
	
	def withImplementation(String implementation) {
		this.implementation = implementation
		this
	}
	
	def withVisibilityModifier(String visibility) {
		this.visibility = visibility
		this
	}
	
	def withStaticModifier() {
		this.isStatic = true
		this
	}
	
	def withThrows(String throwsType) {
		this.throwsType = throwsType
		this
	}
	
	// TODO: Obsolete, still here for compatibility!
	def withMethodAnnotation(String annotation) {
		this.methodAnnotation = annotation
		this
	}
	
	def withAnnotations(Collection<? extends IJAnnotation> annotations) {
		this.annotations = annotations
		this
	}
}
