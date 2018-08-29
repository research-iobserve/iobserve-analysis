package org.palladiosimulator.protocom.tech.iiop;

import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.manifest.IJeeManifest;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class JavaEEIIOPMFFile<E extends Entity> extends ConceptMapping<E> implements IJeeManifest {
  public JavaEEIIOPMFFile(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String classPath() {
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
