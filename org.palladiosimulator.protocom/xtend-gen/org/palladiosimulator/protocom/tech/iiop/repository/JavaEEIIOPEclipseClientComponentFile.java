package org.palladiosimulator.protocom.tech.iiop.repository;

import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPEclipseComponentFile;

@SuppressWarnings("all")
public class JavaEEIIOPEclipseClientComponentFile extends JavaEEIIOPEclipseComponentFile {
  public JavaEEIIOPEclipseClientComponentFile(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String wbModuleDeployName() {
    return JavaNames.fqnJavaEEClientDeployName(this.pcmEntity);
  }
  
  @Override
  public String wbResource() {
    return null;
  }
  
  @Override
  public String property() {
    return null;
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEOperationInterfaceProjectName(this.pcmEntity);
  }
}
