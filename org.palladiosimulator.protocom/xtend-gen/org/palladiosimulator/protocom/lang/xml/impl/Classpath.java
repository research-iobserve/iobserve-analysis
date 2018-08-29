package org.palladiosimulator.protocom.lang.xml.impl;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.xml.IClasspath;

@SuppressWarnings("all")
public class Classpath extends GeneratedFile<IClasspath> implements IClasspath {
  @Override
  public String generate() {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _header = this.header();
    _builder.append(_header, "");
    _builder.newLineIfNotEmpty();
    CharSequence _body = this.body();
    _builder.append(_body, "");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public CharSequence header() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\'1.0\'?>");
    return _builder;
  }
  
  public CharSequence body() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<classpath>");
    _builder.newLine();
    _builder.append("\t");
    String _classPathEntries = this.classPathEntries();
    _builder.append(_classPathEntries, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("</classpath>");
    return _builder;
  }
  
  @Override
  public String classPathEntries() {
    return this.provider.classPathEntries();
  }
}
