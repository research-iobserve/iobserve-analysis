package org.palladiosimulator.protocom.lang.xml.impl

import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.xml.IPluginXml

class PluginXml extends GeneratedFile<IPluginXml> implements IPluginXml {
	
	override generate() {
		'''
		«header»
		«body»
		'''
	}
	
	def header() {
		'''<?xml version='1.0'?>'''
	}
	
	def body() {
		'''<plugin>
 <extension
   point="«extensionPoint»">
     <actionDelegate
        class="«actionDelegateClass»"
        id="«actionDelegateId»">
     </actionDelegate>
 </extension>
</plugin>'''
	}
	
	override extensionPoint() {
		provider.extensionPoint
	}
	
	override actionDelegateClass() {
		provider.actionDelegateClass
	}
	
	override actionDelegateId() {
		provider.actionDelegateId
	}
	
}