package org.palladiosimulator.protocom.lang.xml.impl;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.xml.IJeeDeploymentDescriptor;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public abstract class JeeDeploymentDescriptor extends GeneratedFile<IJeeDeploymentDescriptor> implements IJeeDeploymentDescriptor {
  @Override
  public String projectName() {
    return null;
  }
  
  public abstract String body();
  
  @Override
  public String generate() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("<web-app xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://java.sun.com/xml/ns/javaee\" xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd\" version=\"3.0\">");
    _builder.newLine();
    _builder.append("\t");
    String _body = this.body();
    _builder.append(_body, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("</web-app>");
    _builder.newLine();
    return _builder.toString();
  }
}
