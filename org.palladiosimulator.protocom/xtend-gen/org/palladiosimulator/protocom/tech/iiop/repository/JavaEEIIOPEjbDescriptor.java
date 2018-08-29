package org.palladiosimulator.protocom.tech.iiop.repository;

import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPDescriptor;

@SuppressWarnings("all")
public class JavaEEIIOPEjbDescriptor extends JavaEEIIOPDescriptor<BasicComponent> {
  public JavaEEIIOPEjbDescriptor(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String displayName() {
    return JavaNames.javaEEDisplayName(this.pcmEntity);
  }
  
  @Override
  public String ejbClientJar() {
    return JavaNames.javaEEEjbClientjar(this.pcmEntity);
  }
  
  @Override
  public String filePath() {
    String _fqnJavaEEDescriptorPath = JavaNames.fqnJavaEEDescriptorPath(this.pcmEntity);
    return (_fqnJavaEEDescriptorPath + "ejb-jar.xml");
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEDescriptorProjectName(this.pcmEntity);
  }
}
