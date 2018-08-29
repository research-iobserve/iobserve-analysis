package org.palladiosimulator.protocom.tech.rmi;

import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.xml.IClasspath;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class PojoClasspathFile<E extends Entity> extends ConceptMapping<E> implements IClasspath {
  public PojoClasspathFile(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String classPathEntries() {
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
