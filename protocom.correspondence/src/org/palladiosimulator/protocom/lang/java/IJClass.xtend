package org.palladiosimulator.protocom.lang.java

import java.util.Collection
import org.palladiosimulator.protocom.lang.java.IJAnnotation

interface IJClass extends IJCompilationUnit {
	
	/**
	 * Inherited class name.
	 */
	def String superClass()
	
	/**
	 * Constructors of this class.
	 * 
	 * FIXME: JMethod is ok'ish, but not entirely correct.
	 */
	def Collection<? extends IJMethod> constructors()
	
	def Collection<? extends IJAnnotation> annotations()
}
