package org.palladiosimulator.protocom.tech.servlet.repository;

import java.util.Collection;
import java.util.LinkedList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJInterface;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.ConceptMapping;

@SuppressWarnings("all")
public class ServletComposedStructureInterface extends ConceptMapping<InterfaceProvidingEntity> implements IJInterface {
  public ServletComposedStructureInterface(final InterfaceProvidingEntity pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String packageName() {
    return JavaNames.implementationPackage(this.pcmEntity);
  }
  
  @Override
  public String compilationUnitName() {
    return JavaNames.interfaceName(this.pcmEntity);
  }
  
  @Override
  public Collection<String> interfaces() {
    return null;
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    LinkedList<IJMethod> _xblockexpression = null;
    {
      LinkedList<IJMethod> result = CollectionLiterals.<IJMethod>newLinkedList();
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    return null;
  }
  
  @Override
  public String projectName() {
    return null;
  }
  
  @Override
  public String filePath() {
    String _fqnInterface = JavaNames.fqnInterface(this.pcmEntity);
    String _fqnToDirectoryPath = JavaNames.fqnToDirectoryPath(_fqnInterface);
    String _plus = ("/src/" + _fqnToDirectoryPath);
    return (_plus + ".java");
  }
}
