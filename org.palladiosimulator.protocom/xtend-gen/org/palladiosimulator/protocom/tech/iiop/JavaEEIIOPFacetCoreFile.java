package org.palladiosimulator.protocom.tech.iiop;

import java.util.Collection;
import java.util.HashMap;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.xml.IJeeFacetCoreXml;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class JavaEEIIOPFacetCoreFile<E extends Entity> extends ConceptMapping<E> implements IJeeFacetCoreXml {
  public JavaEEIIOPFacetCoreFile(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String runtimeName() {
    return null;
  }
  
  @Override
  public Collection<String> fixedFacet() {
    return CollectionLiterals.<String>newLinkedList();
  }
  
  @Override
  public HashMap<String, String> installedFacet() {
    return CollectionLiterals.<String, String>newHashMap();
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
