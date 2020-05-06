package org.palladiosimulator.protocom.lang.properties

import org.palladiosimulator.protocom.lang.ICompilationUnit

interface IBuildProperties extends ICompilationUnit{
	
	def String output()
	
	def String source()
	
	def String binIncludes()
	
}