package org.palladiosimulator.protocom.tech.rmi;

import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.properties.IBuildProperties;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class PojoBuildPropertiesFile<E extends Entity> extends ConceptMapping<E> implements IBuildProperties {
  public PojoBuildPropertiesFile(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String output() {
    return null;
  }
  
  @Override
  public String source() {
    return null;
  }
  
  @Override
  public String binIncludes() {
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
