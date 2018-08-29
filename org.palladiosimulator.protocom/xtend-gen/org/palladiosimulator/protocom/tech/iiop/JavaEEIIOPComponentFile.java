package org.palladiosimulator.protocom.tech.iiop;

import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.xml.IJeeComponentFile;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class JavaEEIIOPComponentFile<E extends Entity> extends ConceptMapping<E> implements IJeeComponentFile {
  public JavaEEIIOPComponentFile(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String wbModuleDeployName() {
    return null;
  }
  
  @Override
  public String wbResource() {
    return null;
  }
  
  @Override
  public String property() {
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
