package org.palladiosimulator.protocom.tech.iiop.repository;

import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.iiop.repository.JavaEEIIOPManifest;

@SuppressWarnings("all")
public class JavaEEIIOPClientManifest extends JavaEEIIOPManifest {
  public JavaEEIIOPClientManifest(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String classPath() {
    return null;
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEOperationInterfaceProjectName(this.pcmEntity);
  }
}
