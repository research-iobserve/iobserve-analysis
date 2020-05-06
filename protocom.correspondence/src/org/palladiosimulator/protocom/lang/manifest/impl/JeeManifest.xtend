package org.palladiosimulator.protocom.lang.manifest.impl

import com.google.inject.Inject
import com.google.inject.name.Named
import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.manifest.IJeeManifest

class JeeManifest extends GeneratedFile<IJeeManifest> implements IJeeManifest {
	
	@Inject
	@Named("ProjectURI")
	String projectURI
	
	override classPath() {
		'''Class-Path: «IF provider.classPath !== null»«projectURI».«provider.classPath»«ENDIF»'''
	}
	
	override manifestVersion() {
		'''Manifest-Version: «provider.manifestVersion»'''
	}
	
	override generate() {
		'''
		«manifestVersion»
		«classPath»'''	}
	
	
}