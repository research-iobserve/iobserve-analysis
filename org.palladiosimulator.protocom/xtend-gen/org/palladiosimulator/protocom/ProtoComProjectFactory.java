package org.palladiosimulator.protocom;

import com.google.common.base.Objects;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import java.io.File;
import java.util.HashMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.palladiosimulator.analyzer.workflow.configurations.AbstractCodeGenerationWorkflowRunConfiguration;
import org.palladiosimulator.protocom.ProjectDeletionChoice;
import org.palladiosimulator.protocom.ProjectDeletionRunner;
import org.palladiosimulator.protocom.ProtoComProject;

@SuppressWarnings("all")
public class ProtoComProjectFactory {
  /**
   * Logger for this class.
   */
  private final static Logger LOGGER = Logger.getLogger(ProtoComProjectFactory.class);
  
  private static HashMap<String, ProtoComProject> createdProjects = CollectionLiterals.<String, ProtoComProject>newHashMap();
  
  private static AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice projectType;
  
  public static ProtoComProject getProject(final String projectURI, final String filePath) {
    final ProtoComProject project = ProtoComProjectFactory.createdProjects.get(projectURI);
    boolean _equals = Objects.equal(project, null);
    if (_equals) {
      NullProgressMonitor _nullProgressMonitor = new NullProgressMonitor();
      ProtoComProjectFactory.checkForExistingProject(_nullProgressMonitor, projectURI);
      return ProtoComProjectFactory.createProject(projectURI, filePath);
    } else {
      return project;
    }
  }
  
  public static HashMap<String, ProtoComProject> getCreatedProjects() {
    return ProtoComProjectFactory.createdProjects;
  }
  
  public static void cleanup() {
    ProtoComProjectFactory.createdProjects.clear();
  }
  
  public static AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice setProjectType(final AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice type) {
    return ProtoComProjectFactory.projectType = type;
  }
  
  public static AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice getProjectType() {
    return ProtoComProjectFactory.projectType;
  }
  
  private static ProtoComProject createProject(final String projectURI, final String filePath) {
    ProtoComProject project = new ProtoComProject(projectURI, filePath, ProtoComProjectFactory.projectType);
    ProtoComProjectFactory.createdProjects.put(projectURI, project);
    return project;
  }
  
  private static void checkForExistingProject(final IProgressMonitor monitor, final String projectURI) {
    try {
      IWorkspace _workspace = ResourcesPlugin.getWorkspace();
      IWorkspaceRoot _root = _workspace.getRoot();
      final IProject iProject = _root.getProject(projectURI);
      boolean _or = false;
      boolean _projectFolderExists = ProtoComProjectFactory.projectFolderExists(projectURI);
      if (_projectFolderExists) {
        _or = true;
      } else {
        boolean _exists = iProject.exists();
        _or = _exists;
      }
      if (_or) {
        ProjectDeletionChoice dialogChoice = ProtoComProjectFactory.userAcceptsProjectDelete(projectURI);
        boolean _equals = Objects.equal(dialogChoice, ProjectDeletionChoice.ABORT);
        if (_equals) {
          throw new JobFailedException("Aborted Prototype generation");
        } else {
          boolean _equals_1 = Objects.equal(dialogChoice, ProjectDeletionChoice.DELETION);
          if (_equals_1) {
            try {
              ProtoComProjectFactory.deleteProject(monitor, iProject, projectURI);
            } catch (final Throwable _t) {
              if (_t instanceof CoreException) {
                final CoreException e = (CoreException)_t;
                throw new JobFailedException("Removing old project failed", e);
              } else {
                throw Exceptions.sneakyThrow(_t);
              }
            }
          } else {
            throw new RuntimeException("Error when asking for user input");
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * @return true, if the folder used for the simulation plugin exists in the
   *         filesystem, false otherwise
   */
  private static boolean projectFolderExists(final String projectURI) {
    IWorkspace _workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot _root = _workspace.getRoot();
    IPath _rawLocation = _root.getRawLocation();
    IPath _append = _rawLocation.append(projectURI);
    final File projectFolder = _append.toFile();
    return projectFolder.exists();
  }
  
  /**
   * ask the user if the plugin folder should be deleted using a message
   * dialog
   * 
   * @return true, if the user selected "delete", false otherwise
   */
  private static ProjectDeletionChoice userAcceptsProjectDelete(final String projectURI) {
    final ProjectDeletionRunner runner = new ProjectDeletionRunner(projectURI);
    IWorkbench _workbench = PlatformUI.getWorkbench();
    Display _display = _workbench.getDisplay();
    _display.syncExec(runner);
    return runner.shouldDelete();
  }
  
  /**
   * @param monitor
   * @param myProject
   * @throws RollbackFailedException
   */
  private static void deleteProject(final IProgressMonitor monitor, final IProject myProject, final String projectURI) throws CoreException {
    boolean _isEnabledFor = ProtoComProjectFactory.LOGGER.isEnabledFor(Level.INFO);
    if (_isEnabledFor) {
      String _name = myProject.getName();
      String _plus = ("Deleting project " + _name);
      ProtoComProjectFactory.LOGGER.info(_plus);
    }
    myProject.close(monitor);
    myProject.delete(IResource.ALWAYS_DELETE_PROJECT_CONTENT, monitor);
    IWorkspace _workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot _root = _workspace.getRoot();
    _root.refreshLocal(1, monitor);
    boolean _projectFolderExists = ProtoComProjectFactory.projectFolderExists(projectURI);
    if (_projectFolderExists) {
      ProtoComProjectFactory.clearProjectFolder(projectURI);
    }
  }
  
  /**
   * clears the simulation plugin folder
   */
  private static void clearProjectFolder(final String projectURI) {
    IWorkspace _workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot _root = _workspace.getRoot();
    IPath _rawLocation = _root.getRawLocation();
    IPath _append = _rawLocation.append(projectURI);
    final File projectFolder = _append.toFile();
    ProtoComProjectFactory.deleteFolder(projectFolder);
  }
  
  /**
   * deletes a folder and all of its contents recursively
   * 
   * @param folder
   *            the folder to be deleted
   * @return true on success, false otherwise
   */
  private static boolean deleteFolder(final File folder) {
    boolean _isDirectory = folder.isDirectory();
    if (_isDirectory) {
      File[] _listFiles = folder.listFiles();
      for (final File child : _listFiles) {
        boolean _deleteFolder = ProtoComProjectFactory.deleteFolder(child);
        boolean _not = (!_deleteFolder);
        if (_not) {
          return false;
        }
      }
    }
    return folder.delete();
  }
}
