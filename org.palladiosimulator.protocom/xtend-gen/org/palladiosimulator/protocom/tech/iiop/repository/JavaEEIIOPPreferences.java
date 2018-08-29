package org.palladiosimulator.protocom.tech.iiop.repository;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPPreferencesFile;

@SuppressWarnings("all")
public class JavaEEIIOPPreferences extends JavaEEIIOPPreferencesFile<BasicComponent> {
  public JavaEEIIOPPreferences(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String eclipsePreferencesVersion() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("1");
    return _builder.toString();
  }
  
  @Override
  public String codegenInlineJsrBytecode() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("enabled");
    return _builder.toString();
  }
  
  @Override
  public String codegenTargetPlatform() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("1.7");
    return _builder.toString();
  }
  
  @Override
  public String compliance() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("1.7");
    return _builder.toString();
  }
  
  @Override
  public String problemAssertIdentifier() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("error");
    return _builder.toString();
  }
  
  @Override
  public String problemEnumIdentifier() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("error");
    return _builder.toString();
  }
  
  @Override
  public String source() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("1.7");
    return _builder.toString();
  }
  
  @Override
  public String filePath() {
    String _fqnJavaEEPreferencesPath = JavaNames.fqnJavaEEPreferencesPath(this.pcmEntity);
    return (_fqnJavaEEPreferencesPath + "org.eclipse.jdt.core.prefs");
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEDescriptorProjectName(this.pcmEntity);
  }
}
