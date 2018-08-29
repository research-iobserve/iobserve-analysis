package org.palladiosimulator.protocom.tech.servlet.system;

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
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.core.composition.ProvidedDelegationConnector;
import org.palladiosimulator.pcm.core.entity.ComposedProvidingRequiringEntity;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RequiredRole;
import org.palladiosimulator.protocom.lang.java.IJAnnotation;
import org.palladiosimulator.protocom.lang.java.IJClass;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JAnnotation;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.PcmCalls;
import org.palladiosimulator.protocom.lang.java.util.PcmCommons;
import org.palladiosimulator.protocom.model.allocation.AssemblyContextAdapter;
import org.palladiosimulator.protocom.model.repository.BasicComponentAdapter;
import org.palladiosimulator.protocom.model.repository.OperationProvidedRoleAdapter;
import org.palladiosimulator.protocom.model.system.SystemAdapter;
import org.palladiosimulator.protocom.tech.servlet.ServletClass;

@SuppressWarnings("all")
public class ServletSystemClass<E extends ComposedProvidingRequiringEntity> extends ServletClass<E> implements IJClass {
  private final SystemAdapter entity;
  
  public ServletSystemClass(final SystemAdapter entity, final E pcmEntity) {
    super(pcmEntity);
    this.entity = entity;
  }
  
  @Override
  public Collection<String> interfaces() {
    String _interfaceName = this.entity.getInterfaceName();
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_interfaceName));
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    LinkedList<JField> _xblockexpression = null;
    {
      LinkedList<JField> result = CollectionLiterals.<JField>newLinkedList();
      List<AssemblyContextAdapter> _assemblyContexts = this.entity.getAssemblyContexts();
      final Function1<AssemblyContextAdapter, JField> _function = (AssemblyContextAdapter it) -> {
        JField _jField = new JField();
        String _safeName = it.getSafeName();
        String _plus = ("my" + _safeName);
        JField _withName = _jField.withName(_plus);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(this.frameworkBase, "");
        _builder.append(".prototype.IPort<");
        BasicComponentAdapter _encapsulatedComponent = it.getEncapsulatedComponent();
        String _classFqn = _encapsulatedComponent.getClassFqn();
        _builder.append(_classFqn, "");
        _builder.append(">");
        return _withName.withType(_builder.toString());
      };
      List<JField> _map = ListExtensions.<AssemblyContextAdapter, JField>map(_assemblyContexts, _function);
      Iterables.<JField>addAll(result, _map);
      List<AssemblyContextAdapter> _assemblyContexts_1 = this.entity.getAssemblyContexts();
      for (final AssemblyContextAdapter assemblyContext : _assemblyContexts_1) {
        BasicComponentAdapter _encapsulatedComponent = assemblyContext.getEncapsulatedComponent();
        Iterable<OperationProvidedRoleAdapter> _operationProvidedRoles = _encapsulatedComponent.getOperationProvidedRoles();
        int _size = IterableExtensions.size(_operationProvidedRoles);
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          JField _jField = new JField();
          String _safeName = assemblyContext.getSafeName();
          String _plus = (_safeName + "ID");
          JField _withName = _jField.withName(_plus);
          JField _withType = _withName.withType("String");
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("\"");
          BasicComponentAdapter _encapsulatedComponent_1 = assemblyContext.getEncapsulatedComponent();
          Iterable<OperationProvidedRoleAdapter> _operationProvidedRoles_1 = _encapsulatedComponent_1.getOperationProvidedRoles();
          OperationProvidedRoleAdapter _get = ((OperationProvidedRoleAdapter[])Conversions.unwrapArray(_operationProvidedRoles_1, OperationProvidedRoleAdapter.class))[0];
          String _portClassName = _get.getPortClassName();
          _builder.append(_portClassName, "");
          _builder.append("_");
          String _id = assemblyContext.getId();
          _builder.append(_id, "");
          _builder.append("\"");
          JField _withInitialization = _withType.withInitialization(_builder.toString());
          result.add(_withInitialization);
        }
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    JMethod _jMethod = new JMethod();
    JMethod _withParameters = _jMethod.withParameters("String location, String id");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.frameworkBase, "");
    _builder.append(".protocol.RegistryException");
    JMethod _withThrows = _withParameters.withThrows(_builder.toString());
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append(this.frameworkBase, "");
    _builder_1.append(".prototype.LocalComponentRegistry.getInstance().addComponent(id, this);");
    _builder_1.newLineIfNotEmpty();
    _builder_1.newLine();
    _builder_1.append("initInnerComponents();");
    _builder_1.newLine();
    _builder_1.newLine();
    {
      Iterable<OperationProvidedRoleAdapter> _operationProvidedRoles = this.entity.getOperationProvidedRoles();
      for(final OperationProvidedRoleAdapter role : _operationProvidedRoles) {
        _builder_1.append("startPort(location, \"");
        String _portClassName = role.getPortClassName();
        _builder_1.append(_portClassName, "");
        _builder_1.append("\", id, ");
        OperationProvidedRole _entity = role.getEntity();
        ProvidedDelegationConnector _providedDelegationConnector = PcmCommons.getProvidedDelegationConnector(this.pcmEntity, _entity);
        AssemblyContext _assemblyContext_ProvidedDelegationConnector = _providedDelegationConnector.getAssemblyContext_ProvidedDelegationConnector();
        String _javaName = JavaNames.javaName(_assemblyContext_ProvidedDelegationConnector);
        _builder_1.append(_javaName, "");
        _builder_1.append("ID);");
        _builder_1.newLineIfNotEmpty();
      }
    }
    JMethod _withImplementation = _withThrows.withImplementation(_builder_1.toString());
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation));
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    LinkedList<JMethod> _xblockexpression = null;
    {
      LinkedList<JMethod> result = CollectionLiterals.<JMethod>newLinkedList();
      JMethod _jMethod = new JMethod();
      JMethod _withVisibilityModifier = _jMethod.withVisibilityModifier("private");
      JMethod _withName = _withVisibilityModifier.withName("startPort");
      JMethod _withParameters = _withName.withParameters("String location, String portName, String id, String innerId");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("java.util.ArrayList<");
      _builder.append(this.frameworkBase, "");
      _builder.append(".protocol.Parameter> params = new java.util.ArrayList<");
      _builder.append(this.frameworkBase, "");
      _builder.append(".protocol.Parameter>(4);");
      _builder.newLineIfNotEmpty();
      _builder.append("params.add(new ");
      _builder.append(this.frameworkBase, "");
      _builder.append(".protocol.Parameter(\"action\", \"start\"));");
      _builder.newLineIfNotEmpty();
      _builder.append("params.add(new ");
      _builder.append(this.frameworkBase, "");
      _builder.append(".protocol.Parameter(\"location\", location));");
      _builder.newLineIfNotEmpty();
      _builder.append("params.add(new ");
      _builder.append(this.frameworkBase, "");
      _builder.append(".protocol.Parameter(\"assemblyContext\", id));");
      _builder.newLineIfNotEmpty();
      _builder.append("params.add(new ");
      _builder.append(this.frameworkBase, "");
      _builder.append(".protocol.Parameter(\"componentId\", innerId));");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append(this.frameworkBase, "");
      _builder.append(".protocol.Request.get(location, \"/\" + portName, params);");
      _builder.newLineIfNotEmpty();
      JMethod _withImplementation = _withParameters.withImplementation(_builder.toString());
      result.add(_withImplementation);
      List<AssemblyContextAdapter> _assemblyContexts = this.entity.getAssemblyContexts();
      final Function1<AssemblyContextAdapter, JMethod> _function = (AssemblyContextAdapter it) -> {
        JMethod _xblockexpression_1 = null;
        {
          final AssemblyContext x = it.getEntity();
          JMethod _jMethod_1 = new JMethod();
          String _safeName = it.getSafeName();
          String _plus = ("init" + _safeName);
          JMethod _withName_1 = _jMethod_1.withName(_plus);
          JMethod _withVisibilityModifier_1 = _withName_1.withVisibilityModifier("private");
          StringConcatenation _builder_1 = new StringConcatenation();
          RepositoryComponent _encapsulatedComponent__AssemblyContext = x.getEncapsulatedComponent__AssemblyContext();
          String _fqnContext = JavaNames.fqnContext(_encapsulatedComponent__AssemblyContext);
          _builder_1.append(_fqnContext, "");
          _builder_1.append(" context = new ");
          RepositoryComponent _encapsulatedComponent__AssemblyContext_1 = x.getEncapsulatedComponent__AssemblyContext();
          String _fqnContext_1 = JavaNames.fqnContext(_encapsulatedComponent__AssemblyContext_1);
          _builder_1.append(_fqnContext_1, "");
          _builder_1.append("(");
          _builder_1.newLineIfNotEmpty();
          {
            RepositoryComponent _encapsulatedComponent__AssemblyContext_2 = x.getEncapsulatedComponent__AssemblyContext();
            EList<RequiredRole> _requiredRoles_InterfaceRequiringEntity = _encapsulatedComponent__AssemblyContext_2.getRequiredRoles_InterfaceRequiringEntity();
            final Function1<RequiredRole, Boolean> _function_1 = (RequiredRole it_1) -> {
              return Boolean.valueOf(OperationRequiredRole.class.isInstance(it_1));
            };
            Iterable<RequiredRole> _filter = IterableExtensions.<RequiredRole>filter(_requiredRoles_InterfaceRequiringEntity, _function_1);
            final Function1<RequiredRole, OperationRequiredRole> _function_2 = (RequiredRole it_1) -> {
              return ((OperationRequiredRole) it_1);
            };
            Iterable<OperationRequiredRole> _map = IterableExtensions.<RequiredRole, OperationRequiredRole>map(_filter, _function_2);
            boolean _hasElements = false;
            for(final OperationRequiredRole requiredRole : _map) {
              if (!_hasElements) {
                _hasElements = true;
              } else {
                _builder_1.appendImmediate(", \n", "\t");
              }
              _builder_1.append("\t");
              Connector _connector = PcmCalls.getConnector(this.pcmEntity, x, requiredRole);
              AssemblyContext _providingAssemblyContext_AssemblyConnector = ((AssemblyConnector) _connector).getProvidingAssemblyContext_AssemblyConnector();
              String _javaName = JavaNames.javaName(_providingAssemblyContext_AssemblyConnector);
              _builder_1.append(_javaName, "\t");
              _builder_1.append("ID");
              _builder_1.newLineIfNotEmpty();
            }
          }
          _builder_1.append(");");
          _builder_1.newLine();
          _builder_1.newLine();
          _builder_1.append("my");
          String _safeName_1 = it.getSafeName();
          _builder_1.append(_safeName_1, "");
          _builder_1.append(".setContext(context);");
          _builder_1.newLineIfNotEmpty();
          _xblockexpression_1 = _withVisibilityModifier_1.withImplementation(_builder_1.toString());
        }
        return _xblockexpression_1;
      };
      List<JMethod> _map = ListExtensions.<AssemblyContextAdapter, JMethod>map(_assemblyContexts, _function);
      Iterables.<JMethod>addAll(result, _map);
      JMethod _jMethod_1 = new JMethod();
      JMethod _withName_1 = _jMethod_1.withName("initInnerComponents");
      JMethod _withVisibilityModifier_1 = _withName_1.withVisibilityModifier("private");
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(this.frameworkBase, "");
      _builder_1.append(".protocol.RegistryException");
      JMethod _withThrows = _withVisibilityModifier_1.withThrows(_builder_1.toString());
      JAnnotation _jAnnotation = new JAnnotation();
      JAnnotation _withName_2 = _jAnnotation.withName("SuppressWarnings");
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("\"unchecked\"");
      JAnnotation _withValues = _withName_2.withValues(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_builder_2.toString())));
      JMethod _withAnnotations = _withThrows.withAnnotations(
        Collections.<IJAnnotation>unmodifiableList(CollectionLiterals.<IJAnnotation>newArrayList(_withValues)));
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append("try {");
      _builder_3.newLine();
      {
        List<AssemblyContextAdapter> _assemblyContexts_1 = this.entity.getAssemblyContexts();
        for(final AssemblyContextAdapter assemblyContext : _assemblyContexts_1) {
          {
            BasicComponentAdapter _encapsulatedComponent = assemblyContext.getEncapsulatedComponent();
            Iterable<OperationProvidedRoleAdapter> _operationProvidedRoles = _encapsulatedComponent.getOperationProvidedRoles();
            int _size = IterableExtensions.size(_operationProvidedRoles);
            boolean _greaterThan = (_size > 0);
            if (_greaterThan) {
              _builder_3.append("\t");
              _builder_3.append("my");
              String _safeName = assemblyContext.getSafeName();
              _builder_3.append(_safeName, "\t");
              _builder_3.append(" = (");
              _builder_3.append(this.frameworkBase, "\t");
              _builder_3.append(".prototype.IPort<");
              BasicComponentAdapter _encapsulatedComponent_1 = assemblyContext.getEncapsulatedComponent();
              String _classFqn = _encapsulatedComponent_1.getClassFqn();
              _builder_3.append(_classFqn, "\t");
              _builder_3.append(">) ");
              _builder_3.append(this.frameworkBase, "\t");
              _builder_3.append(".protocol.Registry.getInstance().lookup(\"");
              BasicComponentAdapter _encapsulatedComponent_2 = assemblyContext.getEncapsulatedComponent();
              Iterable<OperationProvidedRoleAdapter> _operationProvidedRoles_1 = _encapsulatedComponent_2.getOperationProvidedRoles();
              OperationProvidedRoleAdapter _get = ((OperationProvidedRoleAdapter[])Conversions.unwrapArray(_operationProvidedRoles_1, OperationProvidedRoleAdapter.class))[0];
              String _portClassName = _get.getPortClassName();
              _builder_3.append(_portClassName, "\t");
              _builder_3.append("_");
              String _id = assemblyContext.getId();
              _builder_3.append(_id, "\t");
              _builder_3.append("\");");
              _builder_3.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder_3.append("\t");
      _builder_3.newLine();
      {
        List<AssemblyContextAdapter> _assemblyContexts_2 = this.entity.getAssemblyContexts();
        for(final AssemblyContextAdapter assemblyContext_1 : _assemblyContexts_2) {
          _builder_3.append("\t");
          _builder_3.append("init");
          String _safeName_1 = assemblyContext_1.getSafeName();
          _builder_3.append(_safeName_1, "\t");
          _builder_3.append("();");
          _builder_3.newLineIfNotEmpty();
        }
      }
      _builder_3.append("} catch (");
      _builder_3.append(this.frameworkBase, "");
      _builder_3.append(".protocol.RegistryException e) {");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("\t");
      _builder_3.append("throw e;");
      _builder_3.newLine();
      _builder_3.append("}");
      _builder_3.newLine();
      JMethod _withImplementation_1 = _withAnnotations.withImplementation(_builder_3.toString());
      result.add(_withImplementation_1);
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
}
