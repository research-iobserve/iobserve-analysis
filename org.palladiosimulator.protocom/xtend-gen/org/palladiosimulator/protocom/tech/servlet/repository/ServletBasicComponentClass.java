package org.palladiosimulator.protocom.tech.servlet.repository;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.model.repository.BasicComponentAdapter;
import org.palladiosimulator.protocom.model.repository.OperationProvidedRoleAdapter;
import org.palladiosimulator.protocom.model.repository.SignatureAdapter;
import org.palladiosimulator.protocom.model.seff.ServiceEffectSpecificationAdapter;
import org.palladiosimulator.protocom.model.seff.StartActionAdapter;
import org.palladiosimulator.protocom.tech.servlet.ServletClass;
import org.palladiosimulator.protocom.tech.servlet.util.PcmServletProtoAction;

@SuppressWarnings("all")
public class ServletBasicComponentClass extends ServletClass<BasicComponent> {
  private final BasicComponentAdapter entity;
  
  public ServletBasicComponentClass(final BasicComponentAdapter entity, final BasicComponent pcmEntity) {
    super(pcmEntity);
    this.entity = entity;
  }
  
  @Override
  public Collection<String> interfaces() {
    String _interfaceName = this.entity.getInterfaceName();
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_interfaceName));
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    JMethod _jMethod = new JMethod();
    JMethod _withParameters = _jMethod.withParameters("String location, String assemblyContext");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.frameworkBase, "");
    _builder.append(".prototype.LocalComponentRegistry.getInstance().addComponent(assemblyContext, this);");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("java.util.ArrayList<");
    _builder.append(this.frameworkBase, "");
    _builder.append(".protocol.Parameter> params = new java.util.ArrayList<");
    _builder.append(this.frameworkBase, "");
    _builder.append(".protocol.Parameter>(3);");
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
    _builder.append(".protocol.Parameter(\"assemblyContext\", assemblyContext));");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      Iterable<OperationProvidedRoleAdapter> _operationProvidedRoles = this.entity.getOperationProvidedRoles();
      for(final OperationProvidedRoleAdapter role : _operationProvidedRoles) {
        _builder.append(this.frameworkBase, "");
        _builder.append(".protocol.Request.get(location, \"/");
        String _portClassName = role.getPortClassName();
        _builder.append(_portClassName, "");
        _builder.append("\", params);");
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
      LinkedList<JField> result = CollectionLiterals.<JField>newLinkedList();
      JField _jField = new JField();
      JField _withName = _jField.withName("context");
      String _contextInterfaceFqn = this.entity.getContextInterfaceFqn();
      JField _withType = _withName.withType(_contextInterfaceFqn);
      Iterables.<JField>addAll(result, Collections.<JField>unmodifiableList(CollectionLiterals.<JField>newArrayList(_withType)));
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    LinkedList<JMethod> _xblockexpression = null;
    {
      LinkedList<JMethod> result = CollectionLiterals.<JMethod>newLinkedList();
      JMethod _jMethod = new JMethod();
      JMethod _withName = _jMethod.withName("setContext");
      JMethod _withParameters = _withName.withParameters("Object context");
      String _contextInterfaceFqn = this.entity.getContextInterfaceFqn();
      String _plus = ("this.context = (" + _contextInterfaceFqn);
      String _plus_1 = (_plus + ") context;");
      JMethod _withImplementation = _withParameters.withImplementation(_plus_1);
      result.add(_withImplementation);
      List<ServiceEffectSpecificationAdapter> _serviceEffectSpecifications = this.entity.getServiceEffectSpecifications();
      final Function1<ServiceEffectSpecificationAdapter, JMethod> _function = (ServiceEffectSpecificationAdapter it) -> {
        JMethod _xblockexpression_1 = null;
        {
          final SignatureAdapter signature = it.getSignature();
          JMethod _jMethod_1 = new JMethod();
          String _serviceName = signature.getServiceName();
          JMethod _withName_1 = _jMethod_1.withName(_serviceName);
          StringConcatenation _builder = new StringConcatenation();
          _builder.append(this.stackFrame, "");
          _builder.append("<Object>");
          JMethod _withReturnType = _withName_1.withReturnType(_builder.toString());
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append(this.stackContext, "");
          _builder_1.append(" ctx");
          JMethod _withParameters_1 = _withReturnType.withParameters(_builder_1.toString());
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append("org.apache.log4j.Logger.getRootLogger().info(\"Invoking \'");
          String _serviceName_1 = signature.getServiceName();
          _builder_2.append(_serviceName_1, "");
          _builder_2.append("\'\");");
          _builder_2.newLineIfNotEmpty();
          _builder_2.append("ctx.getStack().createAndPushNewStackFrame();");
          _builder_2.newLine();
          PcmServletProtoAction _pcmServletProtoAction = new PcmServletProtoAction();
          StartActionAdapter _start = it.getStart();
          String _actions = _pcmServletProtoAction.actions(_start);
          _builder_2.append(_actions, "");
          _builder_2.newLineIfNotEmpty();
          _builder_2.append("return null;");
          _builder_2.newLine();
          _xblockexpression_1 = _withParameters_1.withImplementation(_builder_2.toString());
        }
        return _xblockexpression_1;
      };
      List<JMethod> _map = ListExtensions.<ServiceEffectSpecificationAdapter, JMethod>map(_serviceEffectSpecifications, _function);
      Iterables.<JMethod>addAll(result, _map);
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
}
