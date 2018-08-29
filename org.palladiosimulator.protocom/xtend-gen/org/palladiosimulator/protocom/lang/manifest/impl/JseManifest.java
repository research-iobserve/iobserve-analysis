package org.palladiosimulator.protocom.lang.manifest.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.protocom.lang.GeneratedFile;
import org.palladiosimulator.protocom.lang.manifest.IJseManifest;

@SuppressWarnings("all")
public class JseManifest extends GeneratedFile<IJseManifest> implements IJseManifest {
  @Inject
  @Named("ProjectURI")
  private String projectURI;
  
  @Override
  public String generate() {
    StringConcatenation _builder = new StringConcatenation();
    String _manifestVersion = this.manifestVersion();
    _builder.append(_manifestVersion, "");
    _builder.newLineIfNotEmpty();
    String _bundleManifestVersion = this.bundleManifestVersion();
    _builder.append(_bundleManifestVersion, "");
    _builder.newLineIfNotEmpty();
    String _bundleName = this.bundleName();
    _builder.append(_bundleName, "");
    _builder.newLineIfNotEmpty();
    String _bundleSymbolicName = this.bundleSymbolicName();
    _builder.append(_bundleSymbolicName, "");
    _builder.newLineIfNotEmpty();
    String _bundleVersion = this.bundleVersion();
    _builder.append(_bundleVersion, "");
    _builder.newLineIfNotEmpty();
    String _bundleActivator = this.bundleActivator();
    _builder.append(_bundleActivator, "");
    _builder.newLineIfNotEmpty();
    String _requireBundle = this.requireBundle();
    _builder.append(_requireBundle, "");
    _builder.newLineIfNotEmpty();
    String _eclipseLazyStart = this.eclipseLazyStart();
    _builder.append(_eclipseLazyStart, "");
    _builder.newLineIfNotEmpty();
    String _bundleClassPath = this.bundleClassPath();
    _builder.append(_bundleClassPath, "");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append(".");
    _builder.newLine();
    return _builder.toString();
  }
  
  @Override
  public String bundleManifestVersion() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Bundle-ManifestVersion: ");
    String _bundleManifestVersion = this.provider.bundleManifestVersion();
    _builder.append(_bundleManifestVersion, "");
    return _builder.toString();
  }
  
  @Override
  public String bundleName() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Bundle-Name: ");
    String _bundleName = this.provider.bundleName();
    _builder.append(_bundleName, "");
    return _builder.toString();
  }
  
  @Override
  public String bundleSymbolicName() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Bundle-SymbolicName: ");
    _builder.append(this.projectURI, "");
    _builder.append(";");
    String _bundleSymbolicName = this.provider.bundleSymbolicName();
    _builder.append(_bundleSymbolicName, "");
    return _builder.toString();
  }
  
  @Override
  public String bundleVersion() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Bundle-Version: ");
    String _bundleVersion = this.provider.bundleVersion();
    _builder.append(_bundleVersion, "");
    return _builder.toString();
  }
  
  @Override
  public String bundleActivator() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Bundle-Activator: ");
    String _bundleActivator = this.provider.bundleActivator();
    _builder.append(_bundleActivator, "");
    return _builder.toString();
  }
  
  @Override
  public String requireBundle() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Require-Bundle: ");
    String _requireBundle = this.provider.requireBundle();
    _builder.append(_requireBundle, "");
    return _builder.toString();
  }
  
  @Override
  public String eclipseLazyStart() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Eclipse-LazyStart: ");
    String _eclipseLazyStart = this.provider.eclipseLazyStart();
    _builder.append(_eclipseLazyStart, "");
    return _builder.toString();
  }
  
  @Override
  public String bundleClassPath() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Bundle-ClassPath: ");
    String _bundleClassPath = this.provider.bundleClassPath();
    _builder.append(_bundleClassPath, "");
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
}
