package org.palladiosimulator.protocom.lang.manifest.impl

import com.google.inject.Inject
import com.google.inject.name.Named
import org.palladiosimulator.protocom.lang.GeneratedFile
import org.palladiosimulator.protocom.lang.manifest.IJseManifest

class JseManifest extends GeneratedFile<IJseManifest> implements IJseManifest{
	
	@Inject
	@Named("ProjectURI")
	String projectURI
	
	override generate() {
		'''
		«manifestVersion»
		«bundleManifestVersion»
		«bundleName»
		«bundleSymbolicName»
		«bundleVersion»
		«bundleActivator»
		«requireBundle»
		«eclipseLazyStart»
		«bundleClassPath»
		 .
		   '''
	}
	
	override bundleManifestVersion() {
		'''Bundle-ManifestVersion: «provider.bundleManifestVersion»'''
	}
	
	override bundleName() {
		'''Bundle-Name: «provider.bundleName»'''
	}
	
	override bundleSymbolicName() {
		'''Bundle-SymbolicName: «projectURI»;«provider.bundleSymbolicName»'''
	}
	
	override bundleVersion() {
		'''Bundle-Version: «provider.bundleVersion»'''
	}
	
	override bundleActivator() {
		'''Bundle-Activator: «provider.bundleActivator»'''
	}
	
	override requireBundle() {
		'''Require-Bundle: «provider.requireBundle»'''
	}
	
	override eclipseLazyStart() {
		'''Eclipse-LazyStart: «provider.eclipseLazyStart»'''
	}
	
	override bundleClassPath() {
		'''Bundle-ClassPath: «provider.bundleClassPath»'''
	}
	
	override manifestVersion() {
		'''Manifest-Version: «provider.manifestVersion»'''
	}
	
}