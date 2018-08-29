package org.palladiosimulator.protocom.tech.pojo.repository;

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
import org.palladiosimulator.protocom.lang.java.util.DataTypes;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.Parameters;
import org.palladiosimulator.protocom.tech.rmi.PojoClass;
import org.palladiosimulator.protocom.tech.rmi.util.PcmRMIStubAction;

/**
 * Defining the content of component implementations (classes implementing the component behavior).
 * 
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
      Iterables.<JField>addAll(results, Collections.<JField>unmodifiableList(CollectionLiterals.<JField>newArrayList(_withType)));
      JField _jField_1 = new JField();
      JField _withName_1 = _jField_1.withName("assemblyContextID");
      JField _withType_1 = _withName_1.withType("String");
      Iterables.<JField>addAll(results, Collections.<JField>unmodifiableList(CollectionLiterals.<JField>newArrayList(_withType_1)));
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
        JField _jField_2 = new JField();
        String _portMemberVar = JavaNames.portMemberVar(it);
        JField _withName_2 = _jField_2.withName(_portMemberVar);
        OperationInterface _providedInterface__OperationProvidedRole = it.getProvidedInterface__OperationProvidedRole();
        String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
        return _withName_2.withType(_fqn);
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
        JField _jField_2 = new JField();
        String _portMemberVar = JavaNames.portMemberVar(it);
        JField _withName_2 = _jField_2.withName(_portMemberVar);
        InfrastructureInterface _providedInterface__InfrastructureProvidedRole = it.getProvidedInterface__InfrastructureProvidedRole();
        String _fqn = JavaNames.fqn(_providedInterface__InfrastructureProvidedRole);
        return _withName_2.withType(_fqn);
      };
      Iterable<JField> _map_3 = IterableExtensions.<InfrastructureProvidedRole, JField>map(_map_2, _function_5);
      Iterables.<JField>addAll(results, _map_3);
      EList<PassiveResource> _passiveResource_BasicComponent = this.pcmEntity.getPassiveResource_BasicComponent();
      final Function1<PassiveResource, JField> _function_6 = (PassiveResource it) -> {
        JField _jField_2 = new JField();
        String _entityName = it.getEntityName();
        String _javaVariableName = JavaNames.javaVariableName(_entityName);
        String _plus = ("passive_resource_" + _javaVariableName);
        JField _withName_2 = _jField_2.withName(_plus);
        return _withName_2.withType("java.util.concurrent.Semaphore");
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
      Iterables.<JMethod>addAll(results, Collections.<JMethod>unmodifiableList(CollectionLiterals.<JMethod>newArrayList(_withImplementation)));
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
        JMethod _withParameters_1 = _withReturnType.withParameters(_parameterList);
        StringConcatenation _builder = new StringConcatenation();
        PcmRMIStubAction _pcmRMIStubAction = new PcmRMIStubAction();
        EList<AbstractAction> _steps_Behaviour = ((ResourceDemandingBehaviour) it).getSteps_Behaviour();
        AbstractAction _get = _steps_Behaviour.get(0);
        String _actions = _pcmRMIStubAction.actions(_get);
        _builder.append(_actions, "");
        _builder.newLineIfNotEmpty();
        {
          boolean _or = false;
          boolean _or_1 = false;
          boolean _or_2 = false;
          Signature _describedService__SEFF_3 = it.getDescribedService__SEFF();
          String _returnDataType_1 = DataTypes.getReturnDataType(_describedService__SEFF_3);
          boolean _equals = _returnDataType_1.equals("byte");
          if (_equals) {
            _or_2 = true;
          } else {
            Signature _describedService__SEFF_4 = it.getDescribedService__SEFF();
            String _returnDataType_2 = DataTypes.getReturnDataType(_describedService__SEFF_4);
            boolean _equals_1 = _returnDataType_2.equals("double");
            _or_2 = _equals_1;
          }
          if (_or_2) {
            _or_1 = true;
          } else {
            Signature _describedService__SEFF_5 = it.getDescribedService__SEFF();
            String _returnDataType_3 = DataTypes.getReturnDataType(_describedService__SEFF_5);
            boolean _equals_2 = _returnDataType_3.equals("int");
            _or_1 = _equals_2;
          }
          if (_or_1) {
            _or = true;
          } else {
            Signature _describedService__SEFF_6 = it.getDescribedService__SEFF();
            String _returnDataType_4 = DataTypes.getReturnDataType(_describedService__SEFF_6);
            boolean _equals_3 = _returnDataType_4.equals("long");
            _or = _equals_3;
          }
          if (_or) {
            _builder.append("return 0;");
            _builder.newLine();
          } else {
            Signature _describedService__SEFF_7 = it.getDescribedService__SEFF();
            String _returnDataType_5 = DataTypes.getReturnDataType(_describedService__SEFF_7);
            boolean _equals_4 = _returnDataType_5.equals("char");
            if (_equals_4) {
              _builder.append("return \'A\';");
              _builder.newLine();
            } else {
              Signature _describedService__SEFF_8 = it.getDescribedService__SEFF();
              String _returnDataType_6 = DataTypes.getReturnDataType(_describedService__SEFF_8);
              boolean _equals_5 = _returnDataType_6.equals("boolean");
              if (_equals_5) {
                _builder.append("return false;");
                _builder.newLine();
              } else {
                Signature _describedService__SEFF_9 = it.getDescribedService__SEFF();
                String _returnDataType_7 = DataTypes.getReturnDataType(_describedService__SEFF_9);
                boolean _equals_6 = _returnDataType_7.equals("void");
                boolean _not = (!_equals_6);
                if (_not) {
                  _builder.append("return null;");
                  _builder.newLine();
                }
              }
            }
          }
        }
        return _withParameters_1.withImplementation(_builder.toString());
      };
      List<JMethod> _map = ListExtensions.<ServiceEffectSpecification, JMethod>map(_serviceEffectSpecifications__BasicComponent, _function);
      Iterables.<JMethod>addAll(results, _map);
      EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = this.pcmEntity.getProvidedRoles_InterfaceProvidingEntity();
      final Function1<ProvidedRole, Boolean> _function_1 = (ProvidedRole it) -> {
        return Boolean.valueOf(OperationProvidedRole.class.isInstance(it));
      };
      Iterable<ProvidedRole> _filter = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity, _function_1);
      final Function1<ProvidedRole, JMethod> _function_2 = (ProvidedRole it) -> {
        JMethod _jMethod_1 = new JMethod();
        String _portGetter = JavaNames.portGetter(it);
        JMethod _withName_1 = _jMethod_1.withName(_portGetter);
        OperationInterface _providedInterface__OperationProvidedRole = ((OperationProvidedRole) it).getProvidedInterface__OperationProvidedRole();
        String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
        JMethod _withReturnType = _withName_1.withReturnType(_fqn);
        String _portMemberVar = JavaNames.portMemberVar(((OperationProvidedRole) it));
        String _plus_2 = ("return " + _portMemberVar);
        String _plus_3 = (_plus_2 + ";");
        return _withReturnType.withImplementation(_plus_3);
      };
      Iterable<JMethod> _map_1 = IterableExtensions.<ProvidedRole, JMethod>map(_filter, _function_2);
      Iterables.<JMethod>addAll(results, _map_1);
      JMethod _jMethod_1 = new JMethod();
      JMethod _withName_1 = _jMethod_1.withName("main");
      JMethod _withParameters_1 = _withName_1.withParameters("String[] args");
      JMethod _withStaticModifier = _withParameters_1.withStaticModifier();
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
      JMethod _withImplementation_1 = _withStaticModifier.withImplementation(_builder.toString());
      results.add(_withImplementation_1);
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
}
