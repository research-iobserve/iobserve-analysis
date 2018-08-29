package org.palladiosimulator.protocom.tech.rmi.system;

import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.tech.rmi.PojoClasspathFile;

@SuppressWarnings("all")
public class PojoClasspath extends PojoClasspathFile<org.palladiosimulator.pcm.system.System> {
  public PojoClasspath(final org.palladiosimulator.pcm.system.System pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String classPathEntries() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<classpathentry kind=\"con\" path=\"");
    _builder.append(PDECore.JRE_CONTAINER_PATH, "");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("<classpathentry kind=\"con\" path=\"");
    _builder.append(PDECore.REQUIRED_PLUGINS_CONTAINER_PATH, "");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    _builder.append("<classpathentry kind=\"src\" path=\"src\"/>");
    _builder.newLine();
    _builder.append("<classpathentry kind=\"output\" path=\"bin\"/>");
    return _builder.toString();
  }
  
  @Override
  public String filePath() {
    return ".classpath";
  }
}
