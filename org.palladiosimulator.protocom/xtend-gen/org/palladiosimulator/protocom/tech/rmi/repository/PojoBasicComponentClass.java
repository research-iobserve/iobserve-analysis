package org.palladiosimulator.protocom.tech.rmi.repository;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.InfrastructureInterface;
import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.PassiveResource;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Signature;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.PcmCommons;
import org.palladiosimulator.protocom.tech.rmi.PojoClass;
import org.palladiosimulator.protocom.tech.rmi.util.PcmRMIProtoAction;

/**
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PojoBasicComponentClass extends PojoClass<BasicComponent> {
  public PojoBasicComponentClass(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public Collection<String> interfaces() {
    String _interfaceName = JavaNames.interfaceName(this.pcmEntity);
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_interfaceName));
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    JMethod _jMethod = new JMethod();
    JMethod _withParameters = _jMethod.withParameters("String assemblyContextID");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("this.assemblyContextID = assemblyContextID;");
    _builder.newLine();
    {
      EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = this.pcmEntity.getProvidedRoles_InterfaceProvidingEntity();
      final Function1<ProvidedRole, Boolean> _function = (ProvidedRole it) -> {
        return Boolean.valueOf(OperationProvidedRole.class.isInstance(it));
      };
      Iterable<ProvidedRole> _filter = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity, _function);
      final Function1<ProvidedRole, OperationProvidedRole> _function_1 = (ProvidedRole it) -> {
        return ((OperationProvidedRole) it);
      };
      Iterable<OperationProvidedRole> _map = IterableExtensions.<ProvidedRole, OperationProvidedRole>map(_filter, _function_1);
      boolean _hasElements = false;
      for(final OperationProvidedRole role : _map) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append("try {", "");
        }
        String _portMemberVar = JavaNames.portMemberVar(role);
        _builder.append(_portMemberVar, "");
        _builder.append(" = new ");
        String _fqnPort = JavaNames.fqnPort(role);
        _builder.append(_fqnPort, "");
        _builder.append("(this, assemblyContextID);");
        _builder.newLineIfNotEmpty();
      }
      if (_hasElements) {
        _builder.append("} catch (java.rmi.RemoteException e) {  }", "");
      }
    }
    _builder.newLine();
    {
      EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity_1 = this.pcmEntity.getProvidedRoles_InterfaceProvidingEntity();
      final Function1<ProvidedRole, Boolean> _function_2 = (ProvidedRole it) -> {
        return Boolean.valueOf(InfrastructureProvidedRole.class.isInstance(it));
      };
      Iterable<ProvidedRole> _filter_1 = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity_1, _function_2);
      final Function1<ProvidedRole, InfrastructureProvidedRole> _function_3 = (ProvidedRole it) -> {
        return ((InfrastructureProvidedRole) it);
      };
      Iterable<InfrastructureProvidedRole> _map_1 = IterableExtensions.<ProvidedRole, InfrastructureProvidedRole>map(_filter_1, _function_3);
      boolean _hasElements_1 = false;
      for(final InfrastructureProvidedRole role_1 : _map_1) {
        if (!_hasElements_1) {
          _hasElements_1 = true;
          _builder.append("try {", "");
        }
        String _portMemberVar_1 = JavaNames.portMemberVar(role_1);
        _builder.append(_portMemberVar_1, "");
        _builder.append(" = new ");
        String _fqnPort_1 = JavaNames.fqnPort(role_1);
        _builder.append(_fqnPort_1, "");
        _builder.append("(this, assemblyContextID);");
        _builder.newLineIfNotEmpty();
      }
      if (_hasElements_1) {
        _builder.append("} catch (java.rmi.RemoteException e) {  }", "");
      }
    }
    _builder.newLine();
    {
      EList<PassiveResource> _passiveResource_BasicComponent = this.pcmEntity.getPassiveResource_BasicComponent();
      for(final PassiveResource resource : _passiveResource_BasicComponent) {
        _builder.append("passive_resource_");
        String _entityName = resource.getEntityName();
        String _javaVariableName = JavaNames.javaVariableName(_entityName);
        _builder.append(_javaVariableName, "");
        _builder.append(" = new java.util.concurrent.Semaphore(de.uka.ipd.sdq.simucomframework.variables.StackContext.evaluateStatic(\"");
        PCMRandomVariable _capacity_PassiveResource = resource.getCapacity_PassiveResource();
        String _specification = _capacity_PassiveResource.getSpecification();
        String _specificationString = JavaNames.specificationString(_specification);
        _builder.append(_specificationString, "");
        _builder.append("\", Integer.class), true);");
        _builder.newLineIfNotEmpty();
      }
    }
    JMethod _withImplementation = _withParameters.withImplementation(_builder.toString());
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation));
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    LinkedList<JField> _xblockexpression = null;
    {
      final LinkedList<JField> results = CollectionLiterals.<JField>newLinkedList();
      JField _jField = new JField();
      JField _withName = _jField.withName("myContext");
      String _fqnContextInterface = JavaNames.fqnContextInterface(this.pcmEntity);
      JField _withType = _withName.withType(_fqnContextInterface);
      JField _jField_1 = new JField();
      JField _withName_1 = _jField_1.withName("myComponentStackFrame");
      String _stackframeType = PcmCommons.stackframeType();
      JField _withType_1 = _withName_1.withType(_stackframeType);
      JField _jField_2 = new JField();
      JField _withName_2 = _jField_2.withName("myDefaultComponentStackFrame");
      String _stackframeType_1 = PcmCommons.stackframeType();
      JField _withType_2 = _withName_2.withType(_stackframeType_1);
      Iterables.<JField>addAll(results, Collections.<JField>unmodifiableList(CollectionLiterals.<JField>newArrayList(_withType, _withType_1, _withType_2)));
      JField _jField_3 = new JField();
      JField _withName_3 = _jField_3.withName("assemblyContextID");
      JField _withType_3 = _withName_3.withType("String");
      Iterables.<JField>addAll(results, Collections.<JField>unmodifiableList(CollectionLiterals.<JField>newArrayList(_withType_3)));
      EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = this.pcmEntity.getProvidedRoles_InterfaceProvidingEntity();
      final Function1<ProvidedRole, Boolean> _function = (ProvidedRole it) -> {
        return Boolean.valueOf(OperationProvidedRole.class.isInstance(it));
      };
      Iterable<ProvidedRole> _filter = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity, _function);
      final Function1<ProvidedRole, OperationProvidedRole> _function_1 = (ProvidedRole it) -> {
        return ((OperationProvidedRole) it);
      };
      Iterable<OperationProvidedRole> _map = IterableExtensions.<ProvidedRole, OperationProvidedRole>map(_filter, _function_1);
      final Function1<OperationProvidedRole, JField> _function_2 = (OperationProvidedRole it) -> {
        JField _jField_4 = new JField();
        String _portMemberVar = JavaNames.portMemberVar(it);
        JField _withName_4 = _jField_4.withName(_portMemberVar);
        OperationInterface _providedInterface__OperationProvidedRole = it.getProvidedInterface__OperationProvidedRole();
        String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
        return _withName_4.withType(_fqn);
      };
      Iterable<JField> _map_1 = IterableExtensions.<OperationProvidedRole, JField>map(_map, _function_2);
      Iterables.<JField>addAll(results, _map_1);
      EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity_1 = this.pcmEntity.getProvidedRoles_InterfaceProvidingEntity();
      final Function1<ProvidedRole, Boolean> _function_3 = (ProvidedRole it) -> {
        return Boolean.valueOf(InfrastructureProvidedRole.class.isInstance(it));
      };
      Iterable<ProvidedRole> _filter_1 = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity_1, _function_3);
      final Function1<ProvidedRole, InfrastructureProvidedRole> _function_4 = (ProvidedRole it) -> {
        return ((InfrastructureProvidedRole) it);
      };
      Iterable<InfrastructureProvidedRole> _map_2 = IterableExtensions.<ProvidedRole, InfrastructureProvidedRole>map(_filter_1, _function_4);
      final Function1<InfrastructureProvidedRole, JField> _function_5 = (InfrastructureProvidedRole it) -> {
        JField _jField_4 = new JField();
        String _portMemberVar = JavaNames.portMemberVar(it);
        JField _withName_4 = _jField_4.withName(_portMemberVar);
        InfrastructureInterface _providedInterface__InfrastructureProvidedRole = it.getProvidedInterface__InfrastructureProvidedRole();
        String _fqn = JavaNames.fqn(_providedInterface__InfrastructureProvidedRole);
        return _withName_4.withType(_fqn);
      };
      Iterable<JField> _map_3 = IterableExtensions.<InfrastructureProvidedRole, JField>map(_map_2, _function_5);
      Iterables.<JField>addAll(results, _map_3);
      EList<PassiveResource> _passiveResource_BasicComponent = this.pcmEntity.getPassiveResource_BasicComponent();
      final Function1<PassiveResource, JField> _function_6 = (PassiveResource it) -> {
        JField _jField_4 = new JField();
        String _entityName = it.getEntityName();
        String _javaVariableName = JavaNames.javaVariableName(_entityName);
        String _plus = ("passive_resource_" + _javaVariableName);
        JField _withName_4 = _jField_4.withName(_plus);
        return _withName_4.withType("java.util.concurrent.Semaphore");
      };
      List<JField> _map_4 = ListExtensions.<PassiveResource, JField>map(_passiveResource_BasicComponent, _function_6);
      Iterables.<JField>addAll(results, _map_4);
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    LinkedList<JMethod> _xblockexpression = null;
    {
      final LinkedList<JMethod> results = CollectionLiterals.<JMethod>newLinkedList();
      JMethod _jMethod = new JMethod();
      JMethod _withName = _jMethod.withName("setContext");
      JMethod _withParameters = _withName.withParameters("Object myContext");
      String _fqnContextInterface = JavaNames.fqnContextInterface(this.pcmEntity);
      String _plus = ("this.myContext = (" + _fqnContextInterface);
      String _plus_1 = (_plus + ") myContext;");
      JMethod _withImplementation = _withParameters.withImplementation(_plus_1);
      JMethod _jMethod_1 = new JMethod();
      JMethod _withName_1 = _jMethod_1.withName("setComponentFrame");
      String _stackframeParameterList = PcmCommons.stackframeParameterList();
      JMethod _withParameters_1 = _withName_1.withParameters(_stackframeParameterList);
      JMethod _withImplementation_1 = _withParameters_1.withImplementation("this.myComponentStackFrame = myComponentStackFrame; this.myDefaultComponentStackFrame = new de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object>();");
      Iterables.<JMethod>addAll(results, Collections.<JMethod>unmodifiableList(CollectionLiterals.<JMethod>newArrayList(_withImplementation, _withImplementation_1)));
      EList<ServiceEffectSpecification> _serviceEffectSpecifications__BasicComponent = this.pcmEntity.getServiceEffectSpecifications__BasicComponent();
      final Function1<ServiceEffectSpecification, JMethod> _function = (ServiceEffectSpecification it) -> {
        JMethod _jMethod_2 = new JMethod();
        Signature _describedService__SEFF = it.getDescribedService__SEFF();
        String _serviceName = JavaNames.serviceName(_describedService__SEFF);
        JMethod _withName_2 = _jMethod_2.withName(_serviceName);
        String _stackframeType = PcmCommons.stackframeType();
        JMethod _withReturnType = _withName_2.withReturnType(_stackframeType);
        String _stackContextParameterList = PcmCommons.stackContextParameterList();
        JMethod _withParameters_2 = _withReturnType.withParameters(_stackContextParameterList);
        StringConcatenation _builder = new StringConcatenation();
        PcmRMIProtoAction _pcmRMIProtoAction = new PcmRMIProtoAction();
        EList<AbstractAction> _steps_Behaviour = ((ResourceDemandingBehaviour) it).getSteps_Behaviour();
        AbstractAction _get = _steps_Behaviour.get(0);
        String _actions = _pcmRMIProtoAction.actions(_get);
        _builder.append(_actions, "");
        _builder.newLineIfNotEmpty();
        _builder.append("return null;");
        _builder.newLine();
        return _withParameters_2.withImplementation(_builder.toString());
      };
      List<JMethod> _map = ListExtensions.<ServiceEffectSpecification, JMethod>map(_serviceEffectSpecifications__BasicComponent, _function);
      Iterables.<JMethod>addAll(results, _map);
      EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = this.pcmEntity.getProvidedRoles_InterfaceProvidingEntity();
      final Function1<ProvidedRole, Boolean> _function_1 = (ProvidedRole it) -> {
        return Boolean.valueOf(OperationProvidedRole.class.isInstance(it));
      };
      Iterable<ProvidedRole> _filter = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity, _function_1);
      final Function1<ProvidedRole, JMethod> _function_2 = (ProvidedRole it) -> {
        JMethod _jMethod_2 = new JMethod();
        String _portGetter = JavaNames.portGetter(it);
        JMethod _withName_2 = _jMethod_2.withName(_portGetter);
        OperationInterface _providedInterface__OperationProvidedRole = ((OperationProvidedRole) it).getProvidedInterface__OperationProvidedRole();
        String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
        JMethod _withReturnType = _withName_2.withReturnType(_fqn);
        String _portMemberVar = JavaNames.portMemberVar(((OperationProvidedRole) it));
        String _plus_2 = ("return " + _portMemberVar);
        String _plus_3 = (_plus_2 + ";");
        return _withReturnType.withImplementation(_plus_3);
      };
      Iterable<JMethod> _map_1 = IterableExtensions.<ProvidedRole, JMethod>map(_filter, _function_2);
      Iterables.<JMethod>addAll(results, _map_1);
      JMethod _jMethod_2 = new JMethod();
      JMethod _withName_2 = _jMethod_2.withName("main");
      JMethod _withParameters_2 = _withName_2.withParameters("String[] args");
      JMethod _withStaticModifier = _withParameters_2.withStaticModifier();
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("String ip = org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.getIpFromArguments(args);");
      _builder.newLine();
      _builder.append("int port = org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.getPortFromArguments(args);");
      _builder.newLine();
      _builder.newLine();
      _builder.append("String assemblyContext = org.palladiosimulator.protocom.framework.java.se.AbstractMain.getAssemblyContextFromArguments(args);");
      _builder.newLine();
      _builder.newLine();
      _builder.append("org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.setRemoteAddress(ip);");
      _builder.newLine();
      _builder.append("org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.setRegistryPort(port);");
      _builder.newLine();
      _builder.newLine();
      _builder.append("new ");
      String _fqn = JavaNames.fqn(this.pcmEntity);
      _builder.append(_fqn, "");
      _builder.append("(assemblyContext);");
      _builder.newLineIfNotEmpty();
      JMethod _withImplementation_2 = _withStaticModifier.withImplementation(_builder.toString());
      results.add(_withImplementation_2);
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
}
