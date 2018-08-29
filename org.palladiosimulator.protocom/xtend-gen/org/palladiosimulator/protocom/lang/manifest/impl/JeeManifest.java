package org.palladiosimulator.protocom.lang.manifest.impl;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.manifest.IJeeManifest;

@SuppressWarnings("all")
public class JeeManifest extends GeneratedFile<IJeeManifest> implements IJeeManifest {
  @Inject
  @Named("ProjectURI")
  private String projectURI;
  
  @Override
  public String classPath() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Class-Path: ");
    {
      String _classPath = this.provider.classPath();
      boolean _notEquals = (!Objects.equal(_classPath, null));
      if (_notEquals) {
        _builder.append(this.projectURI, "");
        _builder.append(".");
        String _classPath_1 = this.provider.classPath();
        _builder.append(_classPath_1, "");
      }
    }
    return _builder.toString();
  }
  
  @Override
  public String manifestVersion() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Manifest-Version: ");
    String _manifestVersion = this.provider.manifestVersion();
    _builder.append(_manifestVersion, "");
    return _builder.toString();
  }
  
  @Override
  public String generate() {
    StringConcatenation _builder = new StringConcatenation();
    String _manifestVersion = this.manifestVersion();
    _builder.append(_manifestVersion, "");
    _builder.newLineIfNotEmpty();
    String _classPath = this.classPath();
    _builder.append(_classPath, "");
    return _builder.toString();
  }
}
