package org.palladiosimulator.protocom.lang.properties.impl;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.properties.IBuildProperties;

@SuppressWarnings("all")
public class BuildProperties extends GeneratedFile<IBuildProperties> implements IBuildProperties {
  @Override
  public String generate() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(" ");
    _builder.append("output.. = ");
    String _output = this.output();
    _builder.append(_output, " ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("source.. = ");
    String _source = this.source();
    _builder.append(_source, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("bin.includes = ");
    String _binIncludes = this.binIncludes();
    _builder.append(_binIncludes, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t\t\t\t\t");
    _builder.newLine();
    return _builder.toString();
  }
  
  @Override
  public String output() {
    return this.provider.output();
  }
  
  @Override
  public String source() {
    return this.provider.source();
  }
  
  @Override
  public String binIncludes() {
    return this.provider.binIncludes();
  }
}
