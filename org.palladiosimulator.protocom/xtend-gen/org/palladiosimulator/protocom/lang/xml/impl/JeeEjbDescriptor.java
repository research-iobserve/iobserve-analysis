package org.palladiosimulator.protocom.lang.xml.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.Collection;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.xml.IJeeEjbDescriptor;

@SuppressWarnings("all")
public class JeeEjbDescriptor extends GeneratedFile<IJeeEjbDescriptor> implements IJeeEjbDescriptor {
  @Inject
  @Named("ProjectURI")
  private String projectURI;
  
  @Override
  public String displayName() {
    return this.provider.displayName();
  }
  
  @Override
  public String ejbClientJar() {
    return this.provider.ejbClientJar();
  }
  
  public CharSequence header() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    return _builder;
  }
  
  public CharSequence body() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<ejb-jar xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://java.sun.com/xml/ns/javaee\" xmlns:ejb=\"http://java.sun.com/xml/ns/javaee/ejb-jar_3_0.xsd\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/ejb-jar_3_1.xsd\" version=\"3.1\">");
    _builder.newLine();
    _builder.append("  \t\t\t");
    _builder.append("<display-name>");
    String _displayName = this.displayName();
    _builder.append(_displayName, "  \t\t\t");
    _builder.append("</display-name>");
    _builder.newLineIfNotEmpty();
    _builder.append("  \t\t\t");
    _builder.append("<ejb-client-jar>");
    _builder.append(this.projectURI, "  \t\t\t");
    _builder.append(".");
    String _ejbClientJar = this.ejbClientJar();
    _builder.append(_ejbClientJar, "  \t\t\t");
    _builder.append("</ejb-client-jar>");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("</ejb-jar>");
    return _builder;
  }
  
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
  
  @Override
  public String ejbName() {
    return this.provider.ejbName();
  }
  
  @Override
  public Collection<String> ejbRefName() {
    return this.provider.ejbRefName();
  }
}
