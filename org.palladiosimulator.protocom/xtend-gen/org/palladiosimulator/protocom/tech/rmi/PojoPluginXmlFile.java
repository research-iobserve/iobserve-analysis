package org.palladiosimulator.protocom.tech.rmi;

import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.xml.IPluginXml;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class PojoPluginXmlFile<E extends Entity> extends ConceptMapping<E> implements IPluginXml {
  public PojoPluginXmlFile(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String extensionPoint() {
    return null;
  }
  
  @Override
  public String actionDelegateClass() {
    return null;
  }
  
  @Override
  public String actionDelegateId() {
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
