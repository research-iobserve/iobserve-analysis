package org.palladiosimulator.protocom

import de.uka.ipd.sdq.workflow.jobs.JobFailedException
import org.palladiosimulator.analyzer.workflow.configurations.AbstractCodeGenerationWorkflowRunConfiguration
import java.util.List
import org.apache.log4j.Logger
import org.eclipse.core.resources.ICommand
import org.eclipse.core.resources.IMarker
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IProjectDescription
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.IncrementalProjectBuilder
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.NullProgressMonitor
import org.palladiosimulator.protocom.constants.ProtoComConstants

/**
 * Encapsulates all information needed to generated a single project by ProtoCom.
 * The constructor establishes all basic information. Afterwards, source files can
 * be generated into the project. Finally, the <code>compile</code> method runs
 * all registered builders.
 * 
 * TODO Modify JavaDoc
 * TODO Split up class over framework & lang packages?
 * TODO Remove redundant <code>this.projectType</code> if-checks. Use inheritance? 
 * 
 * @author Sebastian Lehrig, Daria Giacinto
 */
class ProtoComProject {
	/** Logger for this class. */
	private static final Logger LOGGER = Logger.getLogger(ProtoComProject)
	
	private val IProgressMonitor monitor = new NullProgressMonitor()
	
	private val String projectURI
	private val String filePath
	private val IProject iProject
		
	private val AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice projectType
	private val String[] natures
	private val String[] builders
	
	private val IProjectDescription description
	
	
	new(String projectURI, String filePath, AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice projectType) {		
		this.projectURI = projectURI
		this.filePath = filePath
		this.iProject = createProject(projectURI, this.monitor)		
		this.projectType = projectType
		this.natures = createNatures()
		this.builders = createBuilders()
		
		this.description = createDescription()		
		try {
			iProject.setDescription(this.description, this.monitor);
		} catch (CoreException e) {
			throw new JobFailedException("Failed setting Java and PDE nature and builders",e);
		}
	}		
	
	def getIProject() {
		iProject
	}	
	
	def public void compile(){
		refreshPluginInWorkspace();		
		buildProject();
		checkForErrors();
	}
	
	def private IProject createProject(String projectURI, IProgressMonitor monitor)
			throws JobFailedException {
		val iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectURI);		
		
		if (iProject.exists())
			throw new JobFailedException(
					"Tried to create an existing project. Preceeding cleanup failed");

		if(org.palladiosimulator.protocom.ProtoComProject.LOGGER.isDebugEnabled())
			org.palladiosimulator.protocom.ProtoComProject.LOGGER.debug("Creating Eclipse workspace project " + iProject.getName());
		iProject.create(monitor);
		iProject.open(monitor);
		
		return iProject;
	}
	
	private def String[] createNatures() {
		if (this.projectType == AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.PROTO || 
			this.projectType == AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.POJO) {
			return ProtoComConstants.JAVA_SE_NATURE;			
		} else if (this.projectType == AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.EJB3 ||
			this.projectType == AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.SERVLET) {
			return ProtoComConstants.JAVA_EE_NATURE;	
		} else {
			throw new RuntimeException("No suitable project natures found (project type is \""+this.projectType+"\")");
		}
	}
	
	private def String[] createBuilders() {
		if (this.projectType == AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.PROTO || 
			this.projectType == AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.POJO) {
			return ProtoComConstants.JAVA_SE_BUILDERS;			
		} else if (this.projectType == AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.EJB3 ||
			this.projectType == AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.SERVLET) {
			return ProtoComConstants.JAVA_EE_BUILDERS;	
		} else {
			throw new RuntimeException("No suitable project builders found (project type is \""+this.projectType+"\")");
		}
	}
	
	/**
	 * Create a project description and set the JavaCore.NATURE_ID and
	 * PDE.PLUGIN_NATURE
	 */
	private def IProjectDescription createDescription()
			throws JobFailedException {
		val description = ResourcesPlugin.getWorkspace().newProjectDescription(iProject.getName());
		description.setNatureIds(natures);
		description.setLocation(null);
		
		val List<ICommand> buildCommands = newArrayList
		for(String builder : this.builders) {			
			val command = description.newCommand()
			command.setBuilderName(builder)
			buildCommands.add(command)
		}
		description.setBuildSpec(buildCommands)
		
		return description
	}
	
	/**
	 * @throws JobFailedException
	 */
	def private void refreshPluginInWorkspace() {
		try {
			this.iProject.refreshLocal(IResource.DEPTH_INFINITE, this.monitor);
		} catch (Exception e) {
			throw new JobFailedException("Refreshing plugin project failed", e);
		}
	}
	
	/**
	 * @throws JobFailedException
	 */
	def private void buildProject() {
		try {
			this.iProject.build(IncrementalProjectBuilder.FULL_BUILD, this.monitor);
		} catch (Exception e) {
			throw new JobFailedException("Building plugin project failed", e);
		}
	}
	
	/**
	 * @throws JobFailedException
	 */
	def private void checkForErrors() {
		try {
			if (this.iProject.findMarkers(IMarker.PROBLEM, true,
					IResource.DEPTH_INFINITE).length > 0) {
				var failed = false;
				var IMarker[] markers = this.iProject.findMarkers(IMarker.PROBLEM, true,
						IResource.DEPTH_INFINITE);
				var errorList = "";
				for (marker : markers) {
					if ((marker.getAttribute(IMarker.SEVERITY)) == IMarker.SEVERITY_ERROR) {
						errorList = errorList + marker.getAttribute(IMarker.MESSAGE)
								+ "\n";
						failed = true;
					}
				}
				if (failed) {
					LOGGER.error("Unable to build a simulation plug-in; trying to continue. Failure Messages: "
									+ errorList)
					/*throw new JobFailedException(
							"Unable to build the simulation plug-in. Failure Messages: "
									+ errorList);*/
				}
			}
		} catch (CoreException e) {
			throw new JobFailedException(
					"Compile Plugin failed. Error finding project markers.", e);
		}
	}
}
