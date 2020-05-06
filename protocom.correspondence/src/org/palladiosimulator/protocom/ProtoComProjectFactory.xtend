package org.palladiosimulator.protocom

import de.uka.ipd.sdq.workflow.jobs.JobFailedException
import org.apache.log4j.Level
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.ui.PlatformUI
import java.io.File
import org.apache.log4j.Logger
import java.util.HashMap
import org.palladiosimulator.analyzer.workflow.configurations.AbstractCodeGenerationWorkflowRunConfiguration

class ProtoComProjectFactory {
	/** Logger for this class. */
	private static final Logger LOGGER = Logger.getLogger(ProtoComProjectFactory);
	
	private static HashMap<String, ProtoComProject> createdProjects = newHashMap;
	private static AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice projectType;
	
	public static def ProtoComProject getProject(String projectURI, String filePath) {
		val ProtoComProject project = createdProjects.get(projectURI);
		
		if(project === null) {
			checkForExistingProject(new NullProgressMonitor,projectURI)
			return createProject(projectURI, filePath);
		}
		else {
			return project;
		}
	}
	
	public static def getCreatedProjects(){
		createdProjects
	}
	
	public static def cleanup() {
		createdProjects.clear();
	}
	
	public static def setProjectType(AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice type) {
		projectType = type;
	}
	
	public static def AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice getProjectType() {
		return projectType;
	}
	
	private static def ProtoComProject createProject(String projectURI, String filePath) {
		var project = new ProtoComProject(projectURI, filePath, projectType);
		createdProjects.put(projectURI, project);
		return project;
	}
	
	private static def checkForExistingProject(IProgressMonitor monitor,String projectURI){
		val iProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectURI);	
		if(projectFolderExists(projectURI) || iProject.exists()) {
			var dialogChoice = userAcceptsProjectDelete(projectURI);
			
			if (dialogChoice == ProjectDeletionChoice.ABORT) {
				throw new JobFailedException("Aborted Prototype generation");
			}
			else if (dialogChoice == ProjectDeletionChoice.DELETION) {
				try {
					deleteProject(monitor, iProject, projectURI);
				} catch (CoreException e) {
					throw new JobFailedException("Removing old project failed",
							e);
				}
			} else {
				throw new RuntimeException("Error when asking for user input")
			}
		}
	}
	
	/**
	 * @return true, if the folder used for the simulation plugin exists in the
	 *         filesystem, false otherwise
	 */
	private static def boolean projectFolderExists(String projectURI) {
		val File projectFolder = ResourcesPlugin.getWorkspace().getRoot()
				.getRawLocation().append(projectURI).toFile();

		return projectFolder.exists();
	}

	/**
	 * ask the user if the plugin folder should be deleted using a message
	 * dialog
	 *
	 * @return true, if the user selected "delete", false otherwise
	 */
	private static def ProjectDeletionChoice userAcceptsProjectDelete(String projectURI) {
		val ProjectDeletionRunner runner = new ProjectDeletionRunner(projectURI);
		PlatformUI.getWorkbench().getDisplay().syncExec(runner);
		return runner.shouldDelete();
	}
	
	
	/**
	 * @param monitor
	 * @param myProject
	 * @throws RollbackFailedException
	 */
	private static def void deleteProject(IProgressMonitor monitor, IProject myProject, String projectURI)
			throws CoreException {
		if(org.palladiosimulator.protocom.ProtoComProjectFactory.LOGGER.isEnabledFor(Level.INFO))
			org.palladiosimulator.protocom.ProtoComProjectFactory.LOGGER.info("Deleting project " + myProject.getName());

		myProject.close(monitor);
		myProject.delete(IResource.ALWAYS_DELETE_PROJECT_CONTENT, monitor);
		ResourcesPlugin.getWorkspace().getRoot().refreshLocal(1, monitor);

		if (projectFolderExists(projectURI)) {
			// Eclipse failed in fully cleaning the directory
			clearProjectFolder(projectURI);
		}
	}
	
	/**
	 * clears the simulation plugin folder
	 */
	private static def void clearProjectFolder(String projectURI) {
		val File projectFolder = ResourcesPlugin.getWorkspace().getRoot()
				.getRawLocation().append(projectURI).toFile();

		deleteFolder(projectFolder);
	}
	
	/**
	 * deletes a folder and all of its contents recursively
	 *
	 * @param folder
	 *            the folder to be deleted
	 * @return true on success, false otherwise
	 */
	private static def boolean deleteFolder(File folder) {
		if (folder.isDirectory()) {
			for (File child : folder.listFiles()) {
				if (!deleteFolder(child)) {
					return false;
				}
			}
		}

		// empty folders can be deleted directly
		return folder.delete();
	}
}

	
enum ProjectDeletionChoice {
	NONSELECTED,
	DELETION,
	ABORT
}
		
/**
 * Helper class that allows a message box to appear from a non user
 * interface thread because the workbench shell is otherwise not accessible.
 *
 * @author Philipp Meier
 */
 class ProjectDeletionRunner implements Runnable {
	private ProjectDeletionChoice projectDeletionChoice = ProjectDeletionChoice.NONSELECTED;
	private String myProjectId
	private int usersChoice
	
	public new(String myProjectId) {
		super();
		this.myProjectId = myProjectId
	}

	 def public ProjectDeletionChoice shouldDelete() {
		return projectDeletionChoice;
	}
	
	def override public void run() {
		val String[] options = #["Delete Project and Regenerate all Files", "Abort"] 
		val MessageDialog dlg = new MessageDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(),
				"Temporary analysis project folder already exists", null,
				"The project used for the analysis already exists. Should "
						+ myProjectId
						+ " and all of its contents be deleted?",
				MessageDialog.QUESTION, options, 1);
				
		// check if the user selected Delete
		
		usersChoice = dlg.open()
		
		if (usersChoice == 0) {
			projectDeletionChoice = ProjectDeletionChoice.DELETION
		}
		 else if (usersChoice == 1){
			projectDeletionChoice = ProjectDeletionChoice.ABORT
		}

	}

}
