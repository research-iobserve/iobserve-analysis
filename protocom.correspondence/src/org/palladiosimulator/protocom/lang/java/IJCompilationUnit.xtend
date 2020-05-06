package org.palladiosimulator.protocom.lang.java

import java.util.Collection
import org.palladiosimulator.protocom.lang.ICompilationUnit

/**
 * Common attributes of Java compilation units.
 * 
 * @author Thomas Zolynski
 */
interface IJCompilationUnit extends ICompilationUnit {
	
	/**
	 * Package name of this compilation unit.
	 */
	def String packageName()
	
	/**
	 * Name of the compilation unit.
	 */
	def String compilationUnitName()
	
	/**
	 * Collection of interface names which are either implemented or extended. 
	 */
	def Collection<String> interfaces()
	
	/**
	 * Methods (or signatures) defined by this compilation unit.
	 */
	def Collection<? extends IJMethod> methods()
	
	/**
	 * Fields of this compilation unit.
	 */
	def Collection<? extends IJField> fields()
	
}