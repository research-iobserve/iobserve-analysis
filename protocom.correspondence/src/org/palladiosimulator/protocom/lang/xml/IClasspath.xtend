package org.palladiosimulator.protocom.lang.xml

import org.palladiosimulator.protocom.lang.ICompilationUnit

interface IClasspath extends ICompilationUnit{
	
	def String classPathEntries()
}