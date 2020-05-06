package org.palladiosimulator.protocom.lang.xml

import java.util.Collection
import org.palladiosimulator.protocom.lang.ICompilationUnit

interface IJeeDescriptor extends ICompilationUnit{
	
	def String ejbName()
	
	def Collection<String> ejbRefName()
	
	def String displayName()
	
}