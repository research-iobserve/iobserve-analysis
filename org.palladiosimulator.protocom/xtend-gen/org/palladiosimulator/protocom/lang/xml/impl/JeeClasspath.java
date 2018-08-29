package org.palladiosimulator.protocom.lang.xml.impl;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.xml.IJeeClasspath;

@SuppressWarnings("all")
public class JeeClasspath extends GeneratedFile<IJeeClasspath> implements IJeeClasspath {
  @Inject
  @Named("ProjectURI")
  private String projectURI;
  
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
    {
      String _clientClassPathEntry = this.clientClassPathEntry();
      boolean _notEquals = (!Objects.equal(_clientClassPathEntry, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/");
        _builder.append(this.projectURI, "\t");
        _builder.append(".");
        String _clientClassPathEntry_1 = this.clientClassPathEntry();
        _builder.append(_clientClassPathEntry_1, "\t");
        _builder.append("\"/>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      Set<String> _requiredClientProjects = this.requiredClientProjects();
      for(final String requiredProject : _requiredClientProjects) {
        _builder.append("\t");
        _builder.append("<classpathentry combineaccessrules=\"false\" kind=\"src\" path=\"/");
        _builder.append(this.projectURI, "\t");
        _builder.append(".");
        _builder.append(requiredProject, "\t");
        _builder.append("\"/>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("<classpathentry kind=\"output\" path=\"build/classes\"/>");
    _builder.newLine();
    _builder.append("</classpath>");
    return _builder;
  }
  
  @Override
  public String clientClassPathEntry() {
    return this.provider.clientClassPathEntry();
  }
  
  @Override
  public String classPathEntries() {
    return this.provider.classPathEntries();
  }
  
  @Override
  public Set<String> requiredClientProjects() {
    return this.provider.requiredClientProjects();
  }
}
