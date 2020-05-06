package org.palladiosimulator.protocom.lang.xml

import org.palladiosimulator.protocom.lang.ICompilationUnit

interface IPluginXml extends ICompilationUnit {
	
	def String extensionPoint()
	
	def String actionDelegateClass()
	
	def String actionDelegateId()
}