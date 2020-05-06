package org.palladiosimulator.protocom.lang

/**
 * This interface is the smallest common subset of attributes for generated 
 * compilation units of all languages, e.g., Java classes or interfaces.
 * 
 * TODO: File or something else.
 * 
 * @author Thomas Zolynski
 */
interface ICompilationUnit {
	
	/**
	 * Path and name of the generated file.
	 */
	def String filePath()
	
	def String projectName()
}