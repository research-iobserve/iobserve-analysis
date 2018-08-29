package org.palladiosimulator.protocom;

import com.google.common.base.Objects;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.palladiosimulator.analyzer.workflow.configurations.AbstractCodeGenerationWorkflowRunConfiguration;
import org.palladiosimulator.protocom.constants.ProtoComConstants;

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
@SuppressWarnings("all")
public class ProtoComProject {
  /**
   * Logger for this class.
   */
  private final static Logger LOGGER = Logger.getLogger(ProtoComProject.class);
  
  private final IProgressMonitor monitor = new NullProgressMonitor();
  
  private final String projectURI;
  
  private final String filePath;
  
  private final IProject iProject;
  
  private final AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice projectType;
  
  private final String[] natures;
  
  private final String[] builders;
  
  private final IProjectDescription description;
  
  public ProtoComProject(final String projectURI, final String filePath, final AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice projectType) {
    try {
      this.projectURI = projectURI;
      this.filePath = filePath;
      IProject _createProject = this.createProject(projectURI, this.monitor);
      this.iProject = _createProject;
      this.projectType = projectType;
      String[] _createNatures = this.createNatures();
      this.natures = _createNatures;
      String[] _createBuilders = this.createBuilders();
      this.builders = _createBuilders;
      IProjectDescription _createDescription = this.createDescription();
      this.description = _createDescription;
      try {
        this.iProject.setDescription(this.description, this.monitor);
      } catch (final Throwable _t) {
        if (_t instanceof CoreException) {
          final CoreException e = (CoreException)_t;
          throw new JobFailedException("Failed setting Java and PDE nature and builders", e);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public IProject getIProject() {
    return this.iProject;
  }
  
  public void compile() {
    this.refreshPluginInWorkspace();
    this.buildProject();
    this.checkForErrors();
  }
  
  private IProject createProject(final String projectURI, final IProgressMonitor monitor) throws JobFailedException {
    try {
      IWorkspace _workspace = ResourcesPlugin.getWorkspace();
      IWorkspaceRoot _root = _workspace.getRoot();
      final IProject iProject = _root.getProject(projectURI);
      boolean _exists = iProject.exists();
      if (_exists) {
        throw new JobFailedException(
          "Tried to create an existing project. Preceeding cleanup failed");
      }
      boolean _isDebugEnabled = ProtoComProject.LOGGER.isDebugEnabled();
      if (_isDebugEnabled) {
        String _name = iProject.getName();
        String _plus = ("Creating Eclipse workspace project " + _name);
        ProtoComProject.LOGGER.debug(_plus);
      }
      iProject.create(monitor);
      iProject.open(monitor);
      return iProject;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private String[] createNatures() {
    boolean _or = false;
    boolean _equals = Objects.equal(this.projectType, AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.PROTO);
    if (_equals) {
      _or = true;
    } else {
      boolean _equals_1 = Objects.equal(this.projectType, AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.POJO);
      _or = _equals_1;
    }
    if (_or) {
      return ProtoComConstants.JAVA_SE_NATURE;
    } else {
      boolean _or_1 = false;
      boolean _equals_2 = Objects.equal(this.projectType, AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.EJB3);
      if (_equals_2) {
        _or_1 = true;
      } else {
        boolean _equals_3 = Objects.equal(this.projectType, AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.SERVLET);
        _or_1 = _equals_3;
      }
      if (_or_1) {
        return ProtoComConstants.JAVA_EE_NATURE;
      } else {
        throw new RuntimeException((("No suitable project natures found (project type is \"" + this.projectType) + "\")"));
      }
    }
  }
  
  private String[] createBuilders() {
    boolean _or = false;
    boolean _equals = Objects.equal(this.projectType, AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.PROTO);
    if (_equals) {
      _or = true;
    } else {
      boolean _equals_1 = Objects.equal(this.projectType, AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.POJO);
      _or = _equals_1;
    }
    if (_or) {
      return ProtoComConstants.JAVA_SE_BUILDERS;
    } else {
      boolean _or_1 = false;
      boolean _equals_2 = Objects.equal(this.projectType, AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.EJB3);
      if (_equals_2) {
        _or_1 = true;
      } else {
        boolean _equals_3 = Objects.equal(this.projectType, AbstractCodeGenerationWorkflowRunConfiguration.CodeGenerationAdvice.SERVLET);
        _or_1 = _equals_3;
      }
      if (_or_1) {
        return ProtoComConstants.JAVA_EE_BUILDERS;
      } else {
        throw new RuntimeException((("No suitable project builders found (project type is \"" + this.projectType) + "\")"));
      }
    }
  }
  
  /**
   * Create a project description and set the JavaCore.NATURE_ID and
   * PDE.PLUGIN_NATURE
   */
  private IProjectDescription createDescription() throws JobFailedException {
    IWorkspace _workspace = ResourcesPlugin.getWorkspace();
    String _name = this.iProject.getName();
    final IProjectDescription description = _workspace.newProjectDescription(_name);
    description.setNatureIds(this.natures);
    description.setLocation(null);
    final List<ICommand> buildCommands = CollectionLiterals.<ICommand>newArrayList();
    for (final String builder : this.builders) {
      {
        final ICommand command = description.newCommand();
        command.setBuilderName(builder);
        buildCommands.add(command);
      }
    }
    description.setBuildSpec(((ICommand[])Conversions.unwrapArray(buildCommands, ICommand.class)));
    return description;
  }
  
  /**
   * @throws JobFailedException
   */
  private void refreshPluginInWorkspace() {
    try {
      try {
        this.iProject.refreshLocal(IResource.DEPTH_INFINITE, this.monitor);
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception e = (Exception)_t;
          throw new JobFailedException("Refreshing plugin project failed", e);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * @throws JobFailedException
   */
  private void buildProject() {
    try {
      try {
        this.iProject.build(IncrementalProjectBuilder.FULL_BUILD, this.monitor);
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception e = (Exception)_t;
          throw new JobFailedException("Building plugin project failed", e);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * @throws JobFailedException
   */
  private void checkForErrors() {
    try {
      try {
        IMarker[] _findMarkers = this.iProject.findMarkers(IMarker.PROBLEM, true, 
          IResource.DEPTH_INFINITE);
        int _length = _findMarkers.length;
        boolean _greaterThan = (_length > 0);
        if (_greaterThan) {
          boolean failed = false;
          IMarker[] markers = this.iProject.findMarkers(IMarker.PROBLEM, true, 
            IResource.DEPTH_INFINITE);
          String errorList = "";
          for (final IMarker marker : markers) {
            Object _attribute = marker.getAttribute(IMarker.SEVERITY);
            boolean _equals = Objects.equal(_attribute, Integer.valueOf(IMarker.SEVERITY_ERROR));
            if (_equals) {
              Object _attribute_1 = marker.getAttribute(IMarker.MESSAGE);
              String _plus = (errorList + _attribute_1);
              String _plus_1 = (_plus + "\n");
              errorList = _plus_1;
              failed = true;
            }
          }
          if (failed) {
            ProtoComProject.LOGGER.error(
              ("Unable to build a simulation plug-in; trying to continue. Failure Messages: " + errorList));
          }
        }
      } catch (final Throwable _t) {
        if (_t instanceof CoreException) {
          final CoreException e = (CoreException)_t;
          throw new JobFailedException(
            "Compile Plugin failed. Error finding project markers.", e);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
