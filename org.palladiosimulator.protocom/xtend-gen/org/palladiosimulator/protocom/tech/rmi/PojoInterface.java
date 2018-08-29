package org.palladiosimulator.protocom.tech.rmi;

import java.util.Collection;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJInterface;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.ConceptMapping;

/**
 * Common super type for all provider creating Java interfaces. Defines default values
 * for all templates.
 * 
 * @author Thomas Zolynski
 */
@SuppressWarnings("all")
public abstract class PojoInterface<E extends Entity> extends ConceptMapping<E> implements IJInterface {
  public PojoInterface(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String packageName() {
    return JavaNames.implementationPackage(this.pcmEntity);
  }
  
  @Override
  public String compilationUnitName() {
    return JavaNames.javaName(this.pcmEntity);
  }
  
  @Override
  public String filePath() {
    return JavaNames.getFileName(this.pcmEntity);
  }
  
  @Override
  public Collection<String> interfaces() {
    return CollectionLiterals.<String>newLinkedList();
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    return CollectionLiterals.<IJMethod>newLinkedList();
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    return CollectionLiterals.<IJField>newLinkedList();
  }
  
  @Override
  public String projectName() {
    return null;
  }
}
