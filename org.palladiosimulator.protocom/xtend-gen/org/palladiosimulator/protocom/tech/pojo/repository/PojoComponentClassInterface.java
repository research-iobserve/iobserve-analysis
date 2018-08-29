package org.palladiosimulator.protocom.tech.pojo.repository;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Signature;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.DataTypes;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.Parameters;
import org.palladiosimulator.protocom.tech.rmi.PojoInterface;

/**
 * Defining the content of component implementation interfaces (the interfaces for the
 * classes implementing the component behavior).
 * 
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PojoComponentClassInterface extends PojoInterface<BasicComponent> {
  public PojoComponentClassInterface(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String compilationUnitName() {
    return JavaNames.interfaceName(this.pcmEntity);
  }
  
  @Override
  public Collection<String> interfaces() {
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("org.palladiosimulator.protocom.framework.java.se.IComponent"));
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
      EList<ServiceEffectSpecification> _serviceEffectSpecifications__BasicComponent = this.pcmEntity.getServiceEffectSpecifications__BasicComponent();
      final Function1<ServiceEffectSpecification, JMethod> _function = (ServiceEffectSpecification it) -> {
        JMethod _jMethod_1 = new JMethod();
        Signature _describedService__SEFF = it.getDescribedService__SEFF();
        String _serviceNameStub = JavaNames.serviceNameStub(_describedService__SEFF);
        JMethod _withName_1 = _jMethod_1.withName(_serviceNameStub);
        Signature _describedService__SEFF_1 = it.getDescribedService__SEFF();
        String _returnDataType = DataTypes.getReturnDataType(_describedService__SEFF_1);
        JMethod _withReturnType = _withName_1.withReturnType(_returnDataType);
        Signature _describedService__SEFF_2 = it.getDescribedService__SEFF();
        String _parameterList = Parameters.getParameterList(_describedService__SEFF_2);
        return _withReturnType.withParameters(_parameterList);
      };
      List<JMethod> _map = ListExtensions.<ServiceEffectSpecification, JMethod>map(_serviceEffectSpecifications__BasicComponent, _function);
      Iterables.<JMethod>addAll(results, _map);
      EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = this.pcmEntity.getProvidedRoles_InterfaceProvidingEntity();
      final Function1<ProvidedRole, Boolean> _function_1 = (ProvidedRole it) -> {
        return Boolean.valueOf(OperationProvidedRole.class.isInstance(it));
      };
      Iterable<ProvidedRole> _filter = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity, _function_1);
      final Function1<ProvidedRole, OperationProvidedRole> _function_2 = (ProvidedRole it) -> {
        return ((OperationProvidedRole) it);
      };
      Iterable<OperationProvidedRole> _map_1 = IterableExtensions.<ProvidedRole, OperationProvidedRole>map(_filter, _function_2);
      final Function1<OperationProvidedRole, JMethod> _function_3 = (OperationProvidedRole it) -> {
        JMethod _jMethod_1 = new JMethod();
        String _portGetter = JavaNames.portGetter(it);
        JMethod _withName_1 = _jMethod_1.withName(_portGetter);
        OperationInterface _providedInterface__OperationProvidedRole = it.getProvidedInterface__OperationProvidedRole();
        String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
        return _withName_1.withReturnType(_fqn);
      };
      Iterable<JMethod> _map_2 = IterableExtensions.<OperationProvidedRole, JMethod>map(_map_1, _function_3);
      Iterables.<JMethod>addAll(results, _map_2);
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
  
  @Override
  public String filePath() {
    String _fqnInterface = JavaNames.fqnInterface(this.pcmEntity);
    String _fqnToDirectoryPath = JavaNames.fqnToDirectoryPath(_fqnInterface);
    String _plus = ("/src/" + _fqnToDirectoryPath);
    return (_plus + ".java");
  }
}
