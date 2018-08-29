package org.palladiosimulator.protocom.tech.rmi;

import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.manifest.IJseManifest;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class PojoMFFile<E extends Entity> extends ConceptMapping<E> implements IJseManifest {
  public PojoMFFile(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String bundleManifestVersion() {
    return null;
  }
  
  @Override
  public String bundleName() {
    return null;
  }
  
  @Override
  public String bundleSymbolicName() {
    return null;
  }
  
  @Override
  public String bundleVersion() {
    return null;
  }
  
  @Override
  public String bundleActivator() {
    return null;
  }
  
  @Override
  public String requireBundle() {
    return null;
  }
  
  @Override
  public String eclipseLazyStart() {
    return null;
  }
  
  @Override
  public String bundleClassPath() {
    return null;
  }
  
  @Override
  public String manifestVersion() {
    return null;
  }
  
  @Override
  public String filePath() {
    return null;
  }
  
  @Override
  public String projectName() {
    return null;
  }
}
