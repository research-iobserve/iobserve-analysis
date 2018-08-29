package org.palladiosimulator.protocom.tech.iiop.repository;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPComponentFile;

@SuppressWarnings("all")
public class JavaEEIIOPEclipseComponentFile extends JavaEEIIOPComponentFile<BasicComponent> {
  public JavaEEIIOPEclipseComponentFile(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String wbModuleDeployName() {
    return JavaNames.fqnJavaEEBasicComponentClassPackage(this.pcmEntity);
  }
  
  @Override
  public String wbResource() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("tag=\"defaultRootSource\"");
    return _builder.toString();
  }
  
  @Override
  public String property() {
    return JavaNames.fqnJavaEEBasicComponentClassPackage(this.pcmEntity);
  }
  
  @Override
  public String filePath() {
    String _fqnJavaEEPreferencesPath = JavaNames.fqnJavaEEPreferencesPath(this.pcmEntity);
    return (_fqnJavaEEPreferencesPath + "org.eclipse.wst.prototype.component");
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEDescriptorProjectName(this.pcmEntity);
  }
}
