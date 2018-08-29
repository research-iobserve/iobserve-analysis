package org.palladiosimulator.protocom.tech.pojo.system;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.tech.rmi.PojoBuildPropertiesFile;

@SuppressWarnings("all")
public class PojoBuildProperties extends PojoBuildPropertiesFile<org.palladiosimulator.pcm.system.System> {
  public PojoBuildProperties(final org.palladiosimulator.pcm.system.System pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String output() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("bin/");
    return _builder.toString();
  }
  
  @Override
  public String source() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("src/");
    return _builder.toString();
  }
  
  @Override
  public String binIncludes() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(" ");
    _builder.append("plugin.xml,\\");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("META-INF/,\\");
    _builder.newLine();
    _builder.append(" ");
    _builder.append(".");
    return _builder.toString();
  }
  
  @Override
  public String filePath() {
    return "build.properties";
  }
}
