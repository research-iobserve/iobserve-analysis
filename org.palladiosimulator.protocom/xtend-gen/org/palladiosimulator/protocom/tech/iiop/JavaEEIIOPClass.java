package org.palladiosimulator.protocom.tech.iiop;

import java.util.Collection;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.java.IJAnnotation;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.IJeeClass;
import org.palladiosimulator.protocom.lang.java.util.JavaConstants;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class JavaEEIIOPClass<E extends Entity> extends ConceptMapping<E> implements IJeeClass {
  public JavaEEIIOPClass(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String superClass() {
    return null;
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    return CollectionLiterals.<IJMethod>newLinkedList();
  }
  
  @Override
  public String packageName() {
    return null;
  }
  
  @Override
  public String compilationUnitName() {
    return JavaNames.javaName(this.pcmEntity);
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
  public String filePath() {
    return JavaNames.fqnJavaEEBasicComponentClassPath(this.pcmEntity);
  }
  
  @Override
  public String jeeClassStatelessAnnotation() {
    return JavaConstants.JEE_EJB_ANNOTATION_STATELESS;
  }
  
  @Override
  public String jeeClassDependencyInjectionAnnotation() {
    return JavaConstants.JEE_EJB_ANNOTATION_EJB;
  }
  
  @Override
  public Collection<? extends IJField> jeeClassDependencyInjection() {
    return CollectionLiterals.<IJField>newLinkedList();
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEBasicComponentProjectName(this.pcmEntity);
  }
  
  @Override
  public Collection<? extends IJAnnotation> annotations() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
}
