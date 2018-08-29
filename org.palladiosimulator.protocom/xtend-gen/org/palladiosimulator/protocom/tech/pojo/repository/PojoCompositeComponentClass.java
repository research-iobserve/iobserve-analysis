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
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.ProvidedDelegationConnector;
import org.palladiosimulator.pcm.repository.CompositeComponent;
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
 * Provider for CompositeComponents.
 * 
 * In contrast to other ComposedStructures, the encapsulated components are created by this one.
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PojoCompositeComponentClass extends PojoComposedStructureClass<CompositeComponent> {
  public PojoCompositeComponentClass(final CompositeComponent pcmEntity) {
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
    _builder.newLine();
    {
      EList<AssemblyContext> _assemblyContexts__ComposedStructure = this.pcmEntity.getAssemblyContexts__ComposedStructure();
      for(final AssemblyContext assemblyContext : _assemblyContexts__ComposedStructure) {
        _builder.append("my");
        String _javaName = JavaNames.javaName(assemblyContext);
        _builder.append(_javaName, "");
        _builder.append(" = new ");
        RepositoryComponent _encapsulatedComponent__AssemblyContext = assemblyContext.getEncapsulatedComponent__AssemblyContext();
        String _fqn = JavaNames.fqn(_encapsulatedComponent__AssemblyContext);
        _builder.append(_fqn, "");
        _builder.append("(\"");
        String _id = assemblyContext.getId();
        _builder.append(_id, "");
        _builder.append("\");");
        _builder.newLineIfNotEmpty();
      }
    }
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
        String _fqn_1 = JavaNames.fqn(_providedInterface__OperationProvidedRole);
        _builder.append(_fqn_1, "");
        _builder.append(") my");
        ProvidedDelegationConnector _providedDelegationConnector = PcmCommons.getProvidedDelegationConnector(this.pcmEntity, role);
        AssemblyContext _assemblyContext_ProvidedDelegationConnector = _providedDelegationConnector.getAssemblyContext_ProvidedDelegationConnector();
        String _javaName_1 = JavaNames.javaName(_assemblyContext_ProvidedDelegationConnector);
        _builder.append(_javaName_1, "");
        _builder.append(",\t\t\t\t\t");
        _builder.newLineIfNotEmpty();
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
        return _withName.withType(_fqn);
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
          _builder.append("\t");
          _builder.append("init");
          String _javaName = JavaNames.javaName(assemblyContext);
          _builder.append(_javaName, "\t");
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
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
}
