package org.palladiosimulator.protocom.tech.servlet.repository;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.protocom.lang.java.IJAnnotation;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JAnnotation;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.model.repository.BasicComponentAdapter;
import org.palladiosimulator.protocom.model.repository.OperationInterfaceAdapter;
import org.palladiosimulator.protocom.model.repository.OperationRequiredRoleAdapter;
import org.palladiosimulator.protocom.tech.servlet.ServletClass;

@SuppressWarnings("all")
public class ServletBasicComponentContextClass extends ServletClass<BasicComponent> {
  private final BasicComponentAdapter entity;
  
  public ServletBasicComponentContextClass(final BasicComponentAdapter entity, final BasicComponent pcmEntity) {
    super(pcmEntity);
    this.entity = entity;
  }
  
  @Override
  public String packageName() {
    return this.entity.getContextPackageFqn();
  }
  
  @Override
  public String compilationUnitName() {
    return this.entity.getContextClassName();
  }
  
  @Override
  public Collection<String> interfaces() {
    String _contextInterfaceName = this.entity.getContextInterfaceName();
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_contextInterfaceName));
  }
  
  @Override
  public Collection<? extends IJAnnotation> annotations() {
    JAnnotation _jAnnotation = new JAnnotation();
    JAnnotation _withName = _jAnnotation.withName("com.fasterxml.jackson.annotation.JsonAutoDetect");
    JAnnotation _withValues = _withName.withValues(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY")));
    return Collections.<IJAnnotation>unmodifiableList(CollectionLiterals.<IJAnnotation>newArrayList(_withValues));
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    LinkedList<JField> _xblockexpression = null;
    {
      final LinkedList<JField> result = CollectionLiterals.<JField>newLinkedList();
      final Iterable<OperationRequiredRoleAdapter> requiredRoles = this.entity.getOperationRequiredRoles();
      final Function1<OperationRequiredRoleAdapter, JField> _function = (OperationRequiredRoleAdapter it) -> {
        JField _jField = new JField();
        JField _withType = _jField.withType("String");
        String _safeName = it.getSafeName();
        String _firstLower = StringExtensions.toFirstLower(_safeName);
        return _withType.withName(_firstLower);
      };
      Iterable<JField> _map = IterableExtensions.<OperationRequiredRoleAdapter, JField>map(requiredRoles, _function);
      Iterables.<JField>addAll(result, _map);
      final Function1<OperationRequiredRoleAdapter, JField> _function_1 = (OperationRequiredRoleAdapter it) -> {
        JField _jField = new JField();
        OperationInterfaceAdapter _requiredInterface = it.getRequiredInterface();
        String _interfaceFqn = _requiredInterface.getInterfaceFqn();
        JField _withType = _jField.withType(_interfaceFqn);
        String _safeName = it.getSafeName();
        String _firstLower = StringExtensions.toFirstLower(_safeName);
        String _plus = ("portFor_" + _firstLower);
        return _withType.withName(_plus);
      };
      Iterable<JField> _map_1 = IterableExtensions.<OperationRequiredRoleAdapter, JField>map(requiredRoles, _function_1);
      Iterables.<JField>addAll(result, _map_1);
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    LinkedList<JMethod> _xblockexpression = null;
    {
      LinkedList<JMethod> result = CollectionLiterals.<JMethod>newLinkedList();
      JMethod _jMethod = new JMethod();
      StringConcatenation _builder = new StringConcatenation();
      JMethod _withImplementation = _jMethod.withImplementation(_builder.toString());
      result.add(_withImplementation);
      Iterable<OperationRequiredRoleAdapter> _operationRequiredRoles = this.entity.getOperationRequiredRoles();
      int _length = ((Object[])Conversions.unwrapArray(_operationRequiredRoles, Object.class)).length;
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        JMethod _jMethod_1 = new JMethod();
        StringConcatenation _builder_1 = new StringConcatenation();
        {
          Iterable<OperationRequiredRoleAdapter> _operationRequiredRoles_1 = this.entity.getOperationRequiredRoles();
          boolean _hasElements = false;
          for(final OperationRequiredRoleAdapter role : _operationRequiredRoles_1) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder_1.appendImmediate(", ", "");
            }
            _builder_1.append("String ");
            String _safeName = role.getSafeName();
            String _firstLower = StringExtensions.toFirstLower(_safeName);
            _builder_1.append(_firstLower, "");
            _builder_1.newLineIfNotEmpty();
          }
        }
        JMethod _withParameters = _jMethod_1.withParameters(_builder_1.toString());
        StringConcatenation _builder_2 = new StringConcatenation();
        {
          Iterable<OperationRequiredRoleAdapter> _operationRequiredRoles_2 = this.entity.getOperationRequiredRoles();
          for(final OperationRequiredRoleAdapter role_1 : _operationRequiredRoles_2) {
            _builder_2.append("this.");
            String _safeName_1 = role_1.getSafeName();
            String _firstLower_1 = StringExtensions.toFirstLower(_safeName_1);
            _builder_2.append(_firstLower_1, "");
            _builder_2.append(" = ");
            String _safeName_2 = role_1.getSafeName();
            String _firstLower_2 = StringExtensions.toFirstLower(_safeName_2);
            _builder_2.append(_firstLower_2, "");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
          }
        }
        JMethod _withImplementation_1 = _withParameters.withImplementation(_builder_2.toString());
        result.add(_withImplementation_1);
      }
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    LinkedList<JMethod> _xblockexpression = null;
    {
      LinkedList<JMethod> result = CollectionLiterals.<JMethod>newLinkedList();
      Iterable<OperationRequiredRoleAdapter> _operationRequiredRoles = this.entity.getOperationRequiredRoles();
      final Function1<OperationRequiredRoleAdapter, JMethod> _function = (OperationRequiredRoleAdapter it) -> {
        JMethod _jMethod = new JMethod();
        String _safeName = it.getSafeName();
        String _plus = ("get" + _safeName);
        JMethod _withName = _jMethod.withName(_plus);
        JMethod _withReturnType = _withName.withReturnType("String");
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("return ");
        String _safeName_1 = it.getSafeName();
        String _firstLower = StringExtensions.toFirstLower(_safeName_1);
        _builder.append(_firstLower, "");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        return _withReturnType.withImplementation(_builder.toString());
      };
      Iterable<JMethod> _map = IterableExtensions.<OperationRequiredRoleAdapter, JMethod>map(_operationRequiredRoles, _function);
      Iterables.<JMethod>addAll(result, _map);
      Iterable<OperationRequiredRoleAdapter> _operationRequiredRoles_1 = this.entity.getOperationRequiredRoles();
      final Function1<OperationRequiredRoleAdapter, JMethod> _function_1 = (OperationRequiredRoleAdapter it) -> {
        JMethod _jMethod = new JMethod();
        String _safeName = it.getSafeName();
        String _plus = ("set" + _safeName);
        JMethod _withName = _jMethod.withName(_plus);
        JMethod _withParameters = _withName.withParameters("String port");
        StringConcatenation _builder = new StringConcatenation();
        String _safeName_1 = it.getSafeName();
        String _firstLower = StringExtensions.toFirstLower(_safeName_1);
        _builder.append(_firstLower, "");
        _builder.append(" = port;");
        _builder.newLineIfNotEmpty();
        return _withParameters.withImplementation(_builder.toString());
      };
      Iterable<JMethod> _map_1 = IterableExtensions.<OperationRequiredRoleAdapter, JMethod>map(_operationRequiredRoles_1, _function_1);
      Iterables.<JMethod>addAll(result, _map_1);
      Iterable<OperationRequiredRoleAdapter> _operationRequiredRoles_2 = this.entity.getOperationRequiredRoles();
      final Function1<OperationRequiredRoleAdapter, JMethod> _function_2 = (OperationRequiredRoleAdapter it) -> {
        JMethod _jMethod = new JMethod();
        String _safeName = it.getSafeName();
        String _plus = ("getPortFor" + _safeName);
        JMethod _withName = _jMethod.withName(_plus);
        OperationInterfaceAdapter _requiredInterface = it.getRequiredInterface();
        String _interfaceFqn = _requiredInterface.getInterfaceFqn();
        JMethod _withReturnType = _withName.withReturnType(_interfaceFqn);
        JAnnotation _jAnnotation = new JAnnotation();
        JAnnotation _withName_1 = _jAnnotation.withName("com.fasterxml.jackson.annotation.JsonIgnore");
        JMethod _withAnnotations = _withReturnType.withAnnotations(
          Collections.<IJAnnotation>unmodifiableList(CollectionLiterals.<IJAnnotation>newArrayList(_withName_1)));
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("try {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("portFor_");
        String _safeName_1 = it.getSafeName();
        String _firstLower = StringExtensions.toFirstLower(_safeName_1);
        _builder.append(_firstLower, "\t");
        _builder.append(" = (");
        OperationInterfaceAdapter _requiredInterface_1 = it.getRequiredInterface();
        String _interfaceFqn_1 = _requiredInterface_1.getInterfaceFqn();
        _builder.append(_interfaceFqn_1, "\t");
        _builder.append(") ");
        _builder.append(this.frameworkBase, "\t");
        _builder.append(".protocol.Registry.getInstance().lookup(");
        String _safeName_2 = it.getSafeName();
        String _firstLower_1 = StringExtensions.toFirstLower(_safeName_2);
        _builder.append(_firstLower_1, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("} catch (");
        _builder.append(this.frameworkBase, "");
        _builder.append(".protocol.RegistryException e) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("e.printStackTrace();");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("return portFor_");
        String _safeName_3 = it.getSafeName();
        String _firstLower_2 = StringExtensions.toFirstLower(_safeName_3);
        _builder.append(_firstLower_2, "");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        return _withAnnotations.withImplementation(_builder.toString());
      };
      Iterable<JMethod> _map_2 = IterableExtensions.<OperationRequiredRoleAdapter, JMethod>map(_operationRequiredRoles_2, _function_2);
      Iterables.<JMethod>addAll(result, _map_2);
      _xblockexpression = result;
    }
    return _xblockexpression;
  }
  
  @Override
  public String filePath() {
    String _fqnContext = JavaNames.fqnContext(this.pcmEntity);
    String _fqnToDirectoryPath = JavaNames.fqnToDirectoryPath(_fqnContext);
    String _plus = ("/src/" + _fqnToDirectoryPath);
    return (_plus + ".java");
  }
}
