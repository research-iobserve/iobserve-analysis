package org.palladiosimulator.protocom.lang.prefs.impl;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.prefs.IJeePreferences;

@SuppressWarnings("all")
public class JeePreferences extends GeneratedFile<IJeePreferences> implements IJeePreferences {
  @Override
  public String generate() {
    StringConcatenation _builder = new StringConcatenation();
    String _eclipsePreferencesVersion = this.eclipsePreferencesVersion();
    _builder.append(_eclipsePreferencesVersion, "");
    _builder.newLineIfNotEmpty();
    String _codegenInlineJsrBytecode = this.codegenInlineJsrBytecode();
    _builder.append(_codegenInlineJsrBytecode, "");
    _builder.newLineIfNotEmpty();
    String _codegenTargetPlatform = this.codegenTargetPlatform();
    _builder.append(_codegenTargetPlatform, "");
    _builder.newLineIfNotEmpty();
    String _compliance = this.compliance();
    _builder.append(_compliance, "");
    _builder.newLineIfNotEmpty();
    String _problemAssertIdentifier = this.problemAssertIdentifier();
    _builder.append(_problemAssertIdentifier, "");
    _builder.newLineIfNotEmpty();
    String _problemEnumIdentifier = this.problemEnumIdentifier();
    _builder.append(_problemEnumIdentifier, "");
    _builder.newLineIfNotEmpty();
    String _source = this.source();
    _builder.append(_source, "");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  @Override
  public String eclipsePreferencesVersion() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("eclipse.preferences.version=");
    String _eclipsePreferencesVersion = this.provider.eclipsePreferencesVersion();
    _builder.append(_eclipsePreferencesVersion, "");
    return _builder.toString();
  }
  
  @Override
  public String codegenInlineJsrBytecode() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=");
    String _codegenInlineJsrBytecode = this.provider.codegenInlineJsrBytecode();
    _builder.append(_codegenInlineJsrBytecode, "");
    return _builder.toString();
  }
  
  @Override
  public String codegenTargetPlatform() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("org.eclipse.jdt.core.compiler.codegen.targetPlatform=");
    String _codegenTargetPlatform = this.provider.codegenTargetPlatform();
    _builder.append(_codegenTargetPlatform, "");
    return _builder.toString();
  }
  
  @Override
  public String compliance() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("org.eclipse.jdt.core.compiler.compliance=");
    String _compliance = this.provider.compliance();
    _builder.append(_compliance, "");
    return _builder.toString();
  }
  
  @Override
  public String problemAssertIdentifier() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("org.eclipse.jdt.core.compiler.problem.assertIdentifier=");
    String _problemAssertIdentifier = this.provider.problemAssertIdentifier();
    _builder.append(_problemAssertIdentifier, "");
    return _builder.toString();
  }
  
  @Override
  public String problemEnumIdentifier() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("org.eclipse.jdt.core.compiler.problem.enumIdentifier=");
    String _problemEnumIdentifier = this.provider.problemEnumIdentifier();
    _builder.append(_problemEnumIdentifier, "");
    return _builder.toString();
  }
  
  @Override
  public String source() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("org.eclipse.jdt.core.compiler.source=");
    String _source = this.provider.source();
    _builder.append(_source, "");
    return _builder.toString();
  }
}
