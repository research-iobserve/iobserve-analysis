package org.palladiosimulator.protocom.tech.rmi.repository;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJInterface;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaConstants;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.ConceptMapping;

/**
 * Defining the content of component implementation interfaces (the interfaces for
 * the classes implementing the component behavior).
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PojoComposedStructureInterface extends ConceptMapping<InterfaceProvidingEntity> implements IJInterface {
  public PojoComposedStructureInterface(final InterfaceProvidingEntity pcmEntity) {
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
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(JavaConstants.RMI_REMOTE_INTERFACE, JavaConstants.SERIALIZABLE_INTERFACE));
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    LinkedList<JMethod> _xblockexpression = null;
    {
      final LinkedList<JMethod> results = CollectionLiterals.<JMethod>newLinkedList();
      JMethod _jMethod = new JMethod();
      JMethod _withName = _jMethod.withName("setContext");
      JMethod _withParameters = _withName.withParameters("Object myContext");
      Iterables.<JMethod>addAll(results, Collections.<JMethod>unmodifiableList(CollectionLiterals.<JMethod>newArrayList(_withParameters)));
      EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = this.pcmEntity.getProvidedRoles_InterfaceProvidingEntity();
      final Function1<ProvidedRole, Boolean> _function = (ProvidedRole it) -> {
        return Boolean.valueOf(OperationProvidedRole.class.isInstance(it));
      };
      Iterable<ProvidedRole> _filter = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity, _function);
      final Function1<ProvidedRole, JMethod> _function_1 = (ProvidedRole it) -> {
        JMethod _jMethod_1 = new JMethod();
        String _portGetter = JavaNames.portGetter(it);
        JMethod _withName_1 = _jMethod_1.withName(_portGetter);
        OperationInterface _providedInterface__OperationProvidedRole = ((OperationProvidedRole) it).getProvidedInterface__OperationProvidedRole();
        String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
        return _withName_1.withReturnType(_fqn);
      };
      Iterable<JMethod> _map = IterableExtensions.<ProvidedRole, JMethod>map(_filter, _function_1);
      Iterables.<JMethod>addAll(results, _map);
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    return null;
  }
  
  @Override
  public String filePath() {
    String _fqnInterface = JavaNames.fqnInterface(this.pcmEntity);
    String _fqnToDirectoryPath = JavaNames.fqnToDirectoryPath(_fqnInterface);
    String _plus = ("/src/" + _fqnToDirectoryPath);
    return (_plus + ".java");
  }
  
  @Override
  public String projectName() {
    return null;
  }
}
