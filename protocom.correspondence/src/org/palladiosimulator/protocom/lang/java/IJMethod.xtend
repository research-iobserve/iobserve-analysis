package org.palladiosimulator.protocom.lang.java

import java.util.Collection
import org.palladiosimulator.protocom.lang.java.IJAnnotation

/**
 * TODO: Common IJMember for Method and Field.
 * 
 * @author Thomas Zolynski
 */
interface IJMethod {
	
	/**
	 * The name of the return type of this method.
	 * 
	 * Default value is VOID.
	 */
	def String returnType()
	
	/**
	 * The name of the method.
	 */
	def String name()
	
	/**
	 * The flattened parameter list as a string.
	 */
	def String parameters()

	/** 
	 * The throw statement.
	 */
	def String throwsType()

	/**
	 * Code of the method as a string.
	 * 
	 * If not set, the method will be treated as abstract.
	 */
	def String body()	
	
	/**
	 * The visibility modifier of this method.
	 * 
	 * Default value is PUBLIC.
	 */
	def String visibilityModifier()
	
	/**
	 * The static modifier.
	 * 
	 * TODO: Move up.
	 */
	def String staticModifier()
	
	def boolean isStatic()
	
	def Collection<? extends IJAnnotation> annotations()
	
	def String methodAnnotation()
}
