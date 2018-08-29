package org.palladiosimulator.protocom.lang.manifest;

import org.palladiosimulator.protocom.lang.manifest.IManifest;

@SuppressWarnings("all")
public interface IJseManifest extends IManifest {
  public abstract String bundleManifestVersion();
  
  public abstract String bundleName();
  
  public abstract String bundleSymbolicName();
  
  public abstract String bundleVersion();
  
  public abstract String bundleActivator();
  
  public abstract String requireBundle();
  
  public abstract String eclipseLazyStart();
  
  public abstract String bundleClassPath();
}
