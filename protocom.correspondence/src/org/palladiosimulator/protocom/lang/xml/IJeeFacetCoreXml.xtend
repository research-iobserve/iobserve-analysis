package org.palladiosimulator.protocom.lang.xml

import java.util.Collection
import java.util.HashMap
import org.palladiosimulator.protocom.lang.ICompilationUnit

interface IJeeFacetCoreXml extends ICompilationUnit{
	
	def String runtimeName()
	
	def Collection<String> fixedFacet()
	
	def HashMap<String,String> installedFacet()
	
}