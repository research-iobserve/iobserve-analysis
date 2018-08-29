package org.palladiosimulator.protocom.tech.pojo.system;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.ProvidedDelegationConnector;
import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaConstants;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.PcmCommons;
import org.palladiosimulator.protocom.tech.pojo.PojoComposedStructureClass;

/**
 * @author Thomas Zolynski, Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PojoSystemClass extends PojoComposedStructureClass<org.palladiosimulator.pcm.system.System> {
  public PojoSystemClass(final org.palladiosimulator.pcm.system.System pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public Collection<String> interfaces() {
    String _interfaceName = JavaNames.interfaceName(this.pcmEntity);
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_interfaceName, JavaConstants.SERIALIZABLE_INTERFACE));
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    JMethod _jMethod = new JMethod();
    JMethod _withParameters = _jMethod.withParameters("String assemblyContextID");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("this.assemblyContextID = assemblyContextID;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("initInnerComponents();");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
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
        _builder.append("(");
        _builder.newLineIfNotEmpty();
        _builder.append("(");
        OperationInterface _providedInterface__OperationProvidedRole = role.getProvidedInterface__OperationProvidedRole();
        String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
        _builder.append(_fqn, "");
        _builder.append(") my");
        ProvidedDelegationConnector _providedDelegationConnector = PcmCommons.getProvidedDelegationConnector(this.pcmEntity, role);
        AssemblyContext _assemblyContext_ProvidedDelegationConnector = _providedDelegationConnector.getAssemblyContext_ProvidedDelegationConnector();
        String _javaName = JavaNames.javaName(_assemblyContext_ProvidedDelegationConnector);
        _builder.append(_javaName, "");
        _builder.append(",");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t\t\t\t\t\t\t\t\t");
        _builder.newLine();
        _builder.append("this, assemblyContextID);");
        _builder.newLine();
      }
      if (_hasElements) {
        _builder.append("} catch (java.rmi.RemoteException e) {  }", "");
      }
    }
    JMethod _withImplementation = _withParameters.withImplementation(_builder.toString());
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation));
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    LinkedList<IJField> _xblockexpression = null;
    {
      final LinkedList<IJField> results = CollectionLiterals.<IJField>newLinkedList();
      Collection<? extends IJField> _fields = super.fields();
      Iterables.<IJField>addAll(results, _fields);
      EList<AssemblyContext> _assemblyContexts__ComposedStructure = this.pcmEntity.getAssemblyContexts__ComposedStructure();
      final Function1<AssemblyContext, JField> _function = (AssemblyContext it) -> {
        JField _jField = new JField();
        String _javaName = JavaNames.javaName(it);
        String _plus = ("my" + _javaName);
        JField _withName = _jField.withName(_plus);
        RepositoryComponent _encapsulatedComponent__AssemblyContext = it.getEncapsulatedComponent__AssemblyContext();
        String _fqn = JavaNames.fqn(_encapsulatedComponent__AssemblyContext);
        String _plus_1 = ("org.palladiosimulator.protocom.framework.java.se.port.IPerformancePrototypePort<" + _fqn);
        String _plus_2 = (_plus_1 + ">");
        return _withName.withType(_plus_2);
      };
      List<JField> _map = ListExtensions.<AssemblyContext, JField>map(_assemblyContexts__ComposedStructure, _function);
      Iterables.<IJField>addAll(results, _map);
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    LinkedList<IJMethod> _xblockexpression = null;
    {
      final LinkedList<IJMethod> results = CollectionLiterals.<IJMethod>newLinkedList();
      Collection<? extends IJMethod> _methods = super.methods();
      Iterables.<IJMethod>addAll(results, _methods);
      JMethod _jMethod = new JMethod();
      JMethod _withName = _jMethod.withName("initInnerComponents");
      JMethod _withVisibilityModifier = _withName.withVisibilityModifier(JavaConstants.VISIBILITY_PRIVATE);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("try {");
      _builder.newLine();
      {
        EList<AssemblyContext> _assemblyContexts__ComposedStructure = this.pcmEntity.getAssemblyContexts__ComposedStructure();
        for(final AssemblyContext assemblyContext : _assemblyContexts__ComposedStructure) {
          {
            RepositoryComponent _encapsulatedComponent__AssemblyContext = assemblyContext.getEncapsulatedComponent__AssemblyContext();
            EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = _encapsulatedComponent__AssemblyContext.getProvidedRoles_InterfaceProvidingEntity();
            final Function1<ProvidedRole, Boolean> _function = (ProvidedRole it) -> {
              return Boolean.valueOf(OperationProvidedRole.class.isInstance(it));
            };
            Iterable<ProvidedRole> _filter = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity, _function);
            int _size = IterableExtensions.size(_filter);
            boolean _greaterThan = (_size > 0);
            if (_greaterThan) {
              _builder.append("\t");
              _builder.append("my");
              String _javaName = JavaNames.javaName(assemblyContext);
              _builder.append(_javaName, "\t");
              _builder.append(" = (org.palladiosimulator.protocom.framework.java.se.port.IPerformancePrototypePort<");
              RepositoryComponent _encapsulatedComponent__AssemblyContext_1 = assemblyContext.getEncapsulatedComponent__AssemblyContext();
              String _fqn = JavaNames.fqn(_encapsulatedComponent__AssemblyContext_1);
              _builder.append(_fqn, "\t");
              _builder.append(">) org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.lookup(\"");
              RepositoryComponent _encapsulatedComponent__AssemblyContext_2 = assemblyContext.getEncapsulatedComponent__AssemblyContext();
              EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity_1 = _encapsulatedComponent__AssemblyContext_2.getProvidedRoles_InterfaceProvidingEntity();
              final Function1<ProvidedRole, Boolean> _function_1 = (ProvidedRole it) -> {
                return Boolean.valueOf(OperationProvidedRole.class.isInstance(it));
              };
              Iterable<ProvidedRole> _filter_1 = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity_1, _function_1);
              ProvidedRole _get = ((ProvidedRole[])Conversions.unwrapArray(_filter_1, ProvidedRole.class))[0];
              String _portClassName = JavaNames.portClassName(((OperationProvidedRole) _get));
              _builder.append(_portClassName, "\t");
              _builder.append("_");
              String _id = assemblyContext.getId();
              _builder.append(_id, "\t");
              _builder.append("\");");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.append("\t");
      _builder.newLine();
      {
        EList<AssemblyContext> _assemblyContexts__ComposedStructure_1 = this.pcmEntity.getAssemblyContexts__ComposedStructure();
        for(final AssemblyContext assemblyContext_1 : _assemblyContexts__ComposedStructure_1) {
          {
            RepositoryComponent _encapsulatedComponent__AssemblyContext_3 = assemblyContext_1.getEncapsulatedComponent__AssemblyContext();
            EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity_2 = _encapsulatedComponent__AssemblyContext_3.getProvidedRoles_InterfaceProvidingEntity();
            final Function1<ProvidedRole, Boolean> _function_2 = (ProvidedRole it) -> {
              return Boolean.valueOf(InfrastructureProvidedRole.class.isInstance(it));
            };
            Iterable<ProvidedRole> _filter_2 = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity_2, _function_2);
            int _size_1 = IterableExtensions.size(_filter_2);
            boolean _greaterThan_1 = (_size_1 > 0);
            if (_greaterThan_1) {
              _builder.append("\t");
              _builder.append("my");
              String _javaName_1 = JavaNames.javaName(assemblyContext_1);
              _builder.append(_javaName_1, "\t");
              _builder.append(" = (org.palladiosimulator.protocom.framework.java.se.port.IPerformancePrototypePort<");
              RepositoryComponent _encapsulatedComponent__AssemblyContext_4 = assemblyContext_1.getEncapsulatedComponent__AssemblyContext();
              String _fqn_1 = JavaNames.fqn(_encapsulatedComponent__AssemblyContext_4);
              _builder.append(_fqn_1, "\t");
              _builder.append(">) org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.lookup(\"");
              RepositoryComponent _encapsulatedComponent__AssemblyContext_5 = assemblyContext_1.getEncapsulatedComponent__AssemblyContext();
              EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity_3 = _encapsulatedComponent__AssemblyContext_5.getProvidedRoles_InterfaceProvidingEntity();
              final Function1<ProvidedRole, Boolean> _function_3 = (ProvidedRole it) -> {
                return Boolean.valueOf(InfrastructureProvidedRole.class.isInstance(it));
              };
              Iterable<ProvidedRole> _filter_3 = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity_3, _function_3);
              ProvidedRole _get_1 = ((ProvidedRole[])Conversions.unwrapArray(_filter_3, ProvidedRole.class))[0];
              String _portClassName_1 = JavaNames.portClassName(((InfrastructureProvidedRole) _get_1));
              _builder.append(_portClassName_1, "\t");
              _builder.append("_");
              String _id_1 = assemblyContext_1.getId();
              _builder.append(_id_1, "\t");
              _builder.append("\");");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.append("\t");
      _builder.newLine();
      {
        EList<AssemblyContext> _assemblyContexts__ComposedStructure_2 = this.pcmEntity.getAssemblyContexts__ComposedStructure();
        for(final AssemblyContext assemblyContext_2 : _assemblyContexts__ComposedStructure_2) {
          _builder.append("\t");
          _builder.append("init");
          String _javaName_2 = JavaNames.javaName(assemblyContext_2);
          _builder.append(_javaName_2, "\t");
          _builder.append("();");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      _builder.append("} catch (java.rmi.RemoteException e) {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("e.printStackTrace();");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      JMethod _withImplementation = _withVisibilityModifier.withImplementation(_builder.toString());
      Iterables.<IJMethod>addAll(results, Collections.<JMethod>unmodifiableList(CollectionLiterals.<JMethod>newArrayList(_withImplementation)));
      JMethod _jMethod_1 = new JMethod();
      JMethod _withName_1 = _jMethod_1.withName("main");
      JMethod _withParameters = _withName_1.withParameters("String... args");
      JMethod _withStaticModifier = _withParameters.withStaticModifier();
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("String ip = org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.getIpFromArguments(args);");
      _builder_1.newLine();
      _builder_1.append("int port = org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.getPortFromArguments(args);");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("String assemblyContext = org.palladiosimulator.protocom.framework.java.se.AbstractMain.getAssemblyContextFromArguments(args);");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.setRemoteAddress(ip);");
      _builder_1.newLine();
      _builder_1.append("org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.setRegistryPort(port);");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("new ");
      String _fqn_2 = JavaNames.fqn(this.pcmEntity);
      _builder_1.append(_fqn_2, "");
      _builder_1.append("(assemblyContext);");
      _builder_1.newLineIfNotEmpty();
      JMethod _withImplementation_1 = _withStaticModifier.withImplementation(_builder_1.toString());
      Iterables.<IJMethod>addAll(results, Collections.<JMethod>unmodifiableList(CollectionLiterals.<JMethod>newArrayList(_withImplementation_1)));
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
}
