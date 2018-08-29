package org.palladiosimulator.protocom.lang

import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.name.Named
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.xtext.generator.AbstractFileSystemAccess2
import org.palladiosimulator.protocom.FSAProvider
import org.palladiosimulator.protocom.ProtoComProjectFactory
import org.palladiosimulator.protocom.PCMEclipseResourceFileSystemAccess2

/**
 * Abstract class representing generated files.
 * 
 * Generic type defines the common interface for language and provider classes. 
 * 
 * @author Thomas Zolynski
 */
abstract class GeneratedFile<L extends ICompilationUnit> implements ICompilationUnit {
	 
	private String myProjectURI
	@Inject
	protected Injector injector 
	
	@Inject
	@Named("ProjectURI")
	String projectURI
	
	/**
	 * File System Access used for storing this file.
	 */
	@Inject
	protected AbstractFileSystemAccess2 fsa
	
	/**
	 * Provider for this compilation unit. Providers need to implement the language interface, 
	 * since these are used for delegation.
	 * 
	 * TODO: Add (Xtend) annotations which generate the boilerplate code for provider delegation.
	 *       e.g. @Provided
	 */
	protected L provider
		
	override String filePath() {
		provider.filePath
	}

	override String projectName(){
		provider.projectName
	}
	
	/**
	 * Inject the provider for this generated file.
	 */
	def createFor(L concept) {	
		provider = concept
		this
	}
	
	/**
	 * Generate the source code for this compilation unit.
	 */
	def String generate()
	
	/**
	 * Store the generated file using Xtext/Xtend file system access.
	 */
	 def void store() {
	 	var NullProgressMonitor mon = new NullProgressMonitor
	 	var FSAProvider fsa = new FSAProvider
	 	var path = filePath()
	 	
	 	if(projectName != null){
	 		myProjectURI = projectURI+projectName
	 	}
	 	else{
	 		myProjectURI = projectURI
	 	}
	
	 	var protoComProject = ProtoComProjectFactory.getProject(myProjectURI, path);
		var PCMEclipseResourceFileSystemAccess2 fsa2 = injector.getInstance(typeof(PCMEclipseResourceFileSystemAccess2))
		
		fsa2.setMonitor(mon) 
		fsa2.setProject(protoComProject.getIProject)
		fsa2.setOutputConfigurations(fsa.defaultConfig)
		
		fsa2.generateFile(filePath, "PCM", generate)
	}
}
