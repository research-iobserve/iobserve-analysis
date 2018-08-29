package org.palladiosimulator.protocom.lang;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import java.util.Map;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.xtext.generator.AbstractFileSystemAccess2;
import org.eclipse.xtext.generator.OutputConfiguration;
import org.palladiosimulator.protocom.FSAProvider;
import org.palladiosimulator.protocom.PCMEclipseResourceFileSystemAccess2;
import org.palladiosimulator.protocom.ProtoComProject;
import org.palladiosimulator.protocom.ProtoComProjectFactory;
import org.palladiosimulator.protocom.lang.ICompilationUnit;

/**
 * Abstract class representing generated files.
 * 
 * Generic type defines the common interface for language and provider classes.
 * 
 * @author Thomas Zolynski
 */
@SuppressWarnings("all")
public abstract class GeneratedFile<L extends ICompilationUnit> implements ICompilationUnit {
  private String myProjectURI;
  
  @Inject
  protected Injector injector;
  
  @Inject
  @Named("ProjectURI")
  private String projectURI;
  
  /**
   * File System Access used for storing this file.
   */
  @Inject
  protected AbstractFileSystemAccess2 fsa;
  
  /**
   * Provider for this compilation unit. Providers need to implement the language interface,
   * since these are used for delegation.
   * 
   * TODO: Add (Xtend) annotations which generate the boilerplate code for provider delegation.
   *       e.g. @Provided
   */
  protected L provider;
  
  @Override
  public String filePath() {
    return this.provider.filePath();
  }
  
  @Override
  public String projectName() {
    return this.provider.projectName();
  }
  
  /**
   * Inject the provider for this generated file.
   */
  public GeneratedFile<L> createFor(final L concept) {
    GeneratedFile<L> _xblockexpression = null;
    {
      this.provider = concept;
      _xblockexpression = this;
    }
    return _xblockexpression;
  }
  
  /**
   * Generate the source code for this compilation unit.
   */
  public abstract String generate();
  
  /**
   * Store the generated file using Xtext/Xtend file system access.
   */
  public void store() {
    NullProgressMonitor mon = new NullProgressMonitor();
    FSAProvider fsa = new FSAProvider();
    String path = this.filePath();
    String _projectName = this.projectName();
    boolean _notEquals = (!Objects.equal(_projectName, null));
    if (_notEquals) {
      String _projectName_1 = this.projectName();
      String _plus = (this.projectURI + _projectName_1);
      this.myProjectURI = _plus;
    } else {
      this.myProjectURI = this.projectURI;
    }
    ProtoComProject protoComProject = ProtoComProjectFactory.getProject(this.myProjectURI, path);
    PCMEclipseResourceFileSystemAccess2 fsa2 = this.injector.<PCMEclipseResourceFileSystemAccess2>getInstance(PCMEclipseResourceFileSystemAccess2.class);
    fsa2.setMonitor(mon);
    IProject _iProject = protoComProject.getIProject();
    fsa2.setProject(_iProject);
    Map<String, OutputConfiguration> _defaultConfig = fsa.defaultConfig();
    fsa2.setOutputConfigurations(_defaultConfig);
    String _filePath = this.filePath();
    String _generate = this.generate();
    fsa2.generateFile(_filePath, "PCM", _generate);
  }
}
