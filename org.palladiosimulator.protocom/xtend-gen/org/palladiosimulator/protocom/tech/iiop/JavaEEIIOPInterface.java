package org.palladiosimulator.protocom.tech.iiop;

import java.util.Collection;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.IJeeInterface;
import org.palladiosimulator.protocom.lang.java.util.JavaConstants;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class JavaEEIIOPInterface<E extends Entity> extends ConceptMapping<E> implements IJeeInterface {
  public JavaEEIIOPInterface(final E pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String packageName() {
    return JavaNames.fqnJavaEEInterfacePackage(this.pcmEntity);
  }
  
  @Override
  public String compilationUnitName() {
    return JavaNames.fqnJavaEEInterfaceName(this.pcmEntity);
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
    return JavaNames.fqnJavaEEOperationInterfacePath(this.pcmEntity);
  }
  
  @Override
  public String jeeInterfaceAnnotation() {
    return JavaConstants.JEE_INTERFACE_ANNOTATION_REMOTE;
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEOperationInterfaceProjectName(this.pcmEntity);
  }
}
