package org.palladiosimulator.protocom.tech.iiop.repository;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPMFFile;

@SuppressWarnings("all")
public class JavaEEIIOPManifest extends JavaEEIIOPMFFile<BasicComponent> {
  public JavaEEIIOPManifest(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String classPath() {
    return JavaNames.javaEEEjbClientjar(this.pcmEntity);
  }
  
  @Override
  public String manifestVersion() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("1.0");
    return _builder.toString();
  }
  
  @Override
  public String filePath() {
    String _fqnJavaEEDescriptorPath = JavaNames.fqnJavaEEDescriptorPath(this.pcmEntity);
    return (_fqnJavaEEDescriptorPath + "MANIFEST.MF");
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEDescriptorProjectName(this.pcmEntity);
  }
}
