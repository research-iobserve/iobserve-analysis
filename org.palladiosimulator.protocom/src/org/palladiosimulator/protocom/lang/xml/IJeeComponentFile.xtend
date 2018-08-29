package org.palladiosimulator.protocom.lang.xml

import org.palladiosimulator.protocom.lang.ICompilationUnit

interface IJeeComponentFile extends ICompilationUnit {
	
	def String wbModuleDeployName()
	
	def String wbResource()
	
	def String property()
	
}