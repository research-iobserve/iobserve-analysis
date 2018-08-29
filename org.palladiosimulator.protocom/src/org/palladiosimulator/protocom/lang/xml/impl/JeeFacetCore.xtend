package org.palladiosimulator.protocom.lang.xml.impl

import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.xml.IJeeFacetCoreXml

class JeeFacetCore extends GeneratedFile<IJeeFacetCoreXml> implements IJeeFacetCoreXml{
	
	override generate() {
	'''
	«header»
	«body»
		'''
	}
	
	def header() {
		'''<?xml version="1.0" encoding="UTF-8"?>'''
	}
	
	def body() {
		'''<faceted-project>
  <runtime name="«runtimeName»"/>
  «FOR f : fixedFacet»
  <fixed facet="«f»"/>
   «ENDFOR»
   «FOR i : installedFacet.keySet()»
   <installed facet="«i»" version="«installedFacet.get(i)»"/>
  «ENDFOR»
</faceted-project>'''
	}
	
	override runtimeName() {
		provider.runtimeName
	}
	
	override fixedFacet() {
		provider.fixedFacet
	}
	
	override installedFacet() {
		provider.installedFacet
	}
	
}