package org.palladiosimulator.protocom.lang.java

import java.util.Collection

interface IJeeClass extends IJClass{
	
	def String jeeClassStatelessAnnotation()
	
	def String jeeClassDependencyInjectionAnnotation()
	
	def Collection<? extends IJField> jeeClassDependencyInjection()
}