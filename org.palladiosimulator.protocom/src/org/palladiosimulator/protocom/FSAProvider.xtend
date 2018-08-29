package org.palladiosimulator.protocom

import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Provider
import java.util.HashMap
import java.util.Map
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.xtext.generator.AbstractFileSystemAccess2
import org.eclipse.xtext.generator.OutputConfiguration

/**
 * Google Guice provider for FileSystemAccess. It configures and
 * injects into FSA objects.
 * 
 * TODO: Class is unfinished. Read configuration from wizard pages.
 * 
 * @author Thomas Zolynski
 */
class FSAProvider implements Provider<AbstractFileSystemAccess2> {
	
	@Inject
	Injector injector
	
	
	
	override AbstractFileSystemAccess2 get() {

		val PCMEclipseResourceFileSystemAccess2 fsa = injector.getInstance(typeof(PCMEclipseResourceFileSystemAccess2)) 
		
		// Inject into FSA...
		fsa.setOutputConfigurations(defaultConfig())
		fsa.setMonitor(new NullProgressMonitor)	
		fsa
	}
	
	def Map<String, OutputConfiguration> defaultConfig() {

		val OutputConfiguration defaultOutput = new OutputConfiguration("PCM")
		defaultOutput.setDescription("Output Folder")
		//defaultOutput.setOutputDirectory("./src")
		defaultOutput.setOutputDirectory(".")
		defaultOutput.setOverrideExistingResources(true)
		defaultOutput.setCreateOutputDirectory(true)
		defaultOutput.setCleanUpDerivedResources(true)
		defaultOutput.setSetDerivedProperty(true)

		val Map<String, OutputConfiguration> map = new HashMap<String, OutputConfiguration>()
		map.put("PCM", defaultOutput)
		map
	}
	
	
}