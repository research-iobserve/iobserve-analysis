package org.palladiosimulator.protocom.tech.servlet;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.xml.impl.JeeDeploymentDescriptor;

@SuppressWarnings("all")
public class ServletDeploymentDescriptor extends JeeDeploymentDescriptor {
  @Override
  public String filePath() {
    return "WebContent/WEB-INF/web.xml";
  }
  
  @Override
  public String body() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<display-name>ProtoCom Performance Prototype</display-name>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("<filter>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<filter-name>guiceFilter</filter-name>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<filter-class>com.google.inject.servlet.GuiceFilter</filter-class>");
    _builder.newLine();
    _builder.append("</filter>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("<filter-mapping>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<filter-name>guiceFilter</filter-name>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<url-pattern>/*</url-pattern>");
    _builder.newLine();
    _builder.append("</filter-mapping>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("<jsp-config>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<jsp-property-group>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<url-pattern>*.jsp</url-pattern>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<trim-directive-whitespaces>true</trim-directive-whitespaces>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</jsp-property-group>");
    _builder.newLine();
    _builder.append("</jsp-config>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("<resource-ref>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<res-ref-name>EcmService</res-ref-name>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<res-type>com.sap.ecm.api.EcmService</res-type>");
    _builder.newLine();
    _builder.append("</resource-ref>");
    _builder.newLine();
    return _builder.toString();
  }
}
