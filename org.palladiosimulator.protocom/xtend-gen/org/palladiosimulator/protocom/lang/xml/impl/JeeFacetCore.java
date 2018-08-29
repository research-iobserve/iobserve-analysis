package org.palladiosimulator.protocom.lang.xml.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.xml.IJeeFacetCoreXml;

@SuppressWarnings("all")
public class JeeFacetCore extends GeneratedFile<IJeeFacetCoreXml> implements IJeeFacetCoreXml {
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
    _builder.append("<faceted-project>");
    _builder.newLine();
    _builder.append("  ");
    _builder.append("<runtime name=\"");
    String _runtimeName = this.runtimeName();
    _builder.append(_runtimeName, "  ");
    _builder.append("\"/>");
    _builder.newLineIfNotEmpty();
    {
      Collection<String> _fixedFacet = this.fixedFacet();
      for(final String f : _fixedFacet) {
        _builder.append("  ");
        _builder.append("<fixed facet=\"");
        _builder.append(f, "  ");
        _builder.append("\"/>");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      HashMap<String, String> _installedFacet = this.installedFacet();
      Set<String> _keySet = _installedFacet.keySet();
      for(final String i : _keySet) {
        _builder.append("   ");
        _builder.append("<installed facet=\"");
        _builder.append(i, "   ");
        _builder.append("\" version=\"");
        HashMap<String, String> _installedFacet_1 = this.installedFacet();
        String _get = _installedFacet_1.get(i);
        _builder.append(_get, "   ");
        _builder.append("\"/>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</faceted-project>");
    return _builder;
  }
  
  @Override
  public String runtimeName() {
    return this.provider.runtimeName();
  }
  
  @Override
  public Collection<String> fixedFacet() {
    return this.provider.fixedFacet();
  }
  
  @Override
  public HashMap<String, String> installedFacet() {
    return this.provider.installedFacet();
  }
}
