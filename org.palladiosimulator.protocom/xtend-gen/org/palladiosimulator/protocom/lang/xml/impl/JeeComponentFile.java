package org.palladiosimulator.protocom.lang.xml.impl;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.xml.IJeeComponentFile;

@SuppressWarnings("all")
public class JeeComponentFile extends GeneratedFile<IJeeComponentFile> implements IJeeComponentFile {
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
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    return _builder;
  }
  
  public CharSequence body() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<project-modules id=\"moduleCoreId\" project-version=\"1.5.0\">");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<wb-module deploy-name=\"");
    _builder.append(this.projectURI, "  ");
    _builder.append(".");
    String _wbModuleDeployName = this.wbModuleDeployName();
    _builder.append(_wbModuleDeployName, "  ");
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("<wb-resource deploy-path=\"/\" source-path=\"/ejbModule\" ");
    String _wbResource = this.wbResource();
    _builder.append(_wbResource, "\t");
    _builder.append("/>");
    _builder.newLineIfNotEmpty();
    {
      String _property = this.property();
      boolean _notEquals = (!Objects.equal(_property, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("<property name=\"java-output-path\" value=\"/");
        _builder.append(this.projectURI, "\t");
        _builder.append(".");
        String _property_1 = this.property();
        _builder.append(_property_1, "\t");
        _builder.append("/build/classes\"/>");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("<property name=\"ClientProject\" value=\"");
        _builder.append(this.projectURI, "\t");
        _builder.append(".");
        String _property_2 = this.property();
        _builder.append(_property_2, "\t");
        _builder.append("Client\"/>");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("<property name=\"ClientJARURI\" value=\"");
        _builder.append(this.projectURI, "\t");
        _builder.append(".");
        String _property_3 = this.property();
        _builder.append(_property_3, "\t");
        _builder.append("Client.jar\"/>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    _builder.append("</wb-module>");
    _builder.newLine();
    _builder.append("</project-modules>");
    return _builder;
  }
  
  @Override
  public String wbModuleDeployName() {
    return this.provider.wbModuleDeployName();
  }
  
  @Override
  public String wbResource() {
    return this.provider.wbResource();
  }
  
  @Override
  public String property() {
    return this.provider.property();
  }
}
