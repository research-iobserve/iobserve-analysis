package org.palladiosimulator.protocom.tech.rmi.repository;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.palladiosimulator.pcm.core.entity.InterfaceRequiringEntity;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.RequiredRole;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaConstants;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.rmi.PojoClass;

/**
 * @author Thomas Zolynski, Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PojoComposedStructureContextClass extends PojoClass<InterfaceRequiringEntity> {
  public PojoComposedStructureContextClass(final InterfaceRequiringEntity pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    LinkedList<JField> _xblockexpression = null;
    {
      final LinkedList<JField> results = CollectionLiterals.<JField>newLinkedList();
      EList<RequiredRole> _requiredRoles_InterfaceRequiringEntity = this.pcmEntity.getRequiredRoles_InterfaceRequiringEntity();
      final Function1<RequiredRole, Boolean> _function = (RequiredRole it) -> {
        return Boolean.valueOf(OperationRequiredRole.class.isInstance(it));
      };
      Iterable<RequiredRole> _filter = IterableExtensions.<RequiredRole>filter(_requiredRoles_InterfaceRequiringEntity, _function);
      final Function1<RequiredRole, JField> _function_1 = (RequiredRole it) -> {
        JField _jField = new JField();
        OperationInterface _requiredInterface__OperationRequiredRole = ((OperationRequiredRole) it).getRequiredInterface__OperationRequiredRole();
        String _fqn = JavaNames.fqn(_requiredInterface__OperationRequiredRole);
        JField _withType = _jField.withType(_fqn);
        String _javaName = JavaNames.javaName(it);
        String _firstLower = StringExtensions.toFirstLower(_javaName);
        return _withType.withName(_firstLower);
      };
      Iterable<JField> _map = IterableExtensions.<RequiredRole, JField>map(_filter, _function_1);
      Iterables.<JField>addAll(results, _map);
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    JMethod _jMethod = new JMethod();
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<RequiredRole> _requiredRoles_InterfaceRequiringEntity = this.pcmEntity.getRequiredRoles_InterfaceRequiringEntity();
      boolean _hasElements = false;
      for(final RequiredRole role : _requiredRoles_InterfaceRequiringEntity) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        OperationInterface _requiredInterface__OperationRequiredRole = ((OperationRequiredRole) role).getRequiredInterface__OperationRequiredRole();
        String _fqn = JavaNames.fqn(_requiredInterface__OperationRequiredRole);
        _builder.append(_fqn, "");
        _builder.append(" ");
        String _javaName = JavaNames.javaName(role);
        String _firstLower = StringExtensions.toFirstLower(_javaName);
        _builder.append(_firstLower, "");
        _builder.append(" ");
      }
    }
    JMethod _withParameters = _jMethod.withParameters(_builder.toString());
    StringConcatenation _builder_1 = new StringConcatenation();
    {
      EList<RequiredRole> _requiredRoles_InterfaceRequiringEntity_1 = this.pcmEntity.getRequiredRoles_InterfaceRequiringEntity();
      boolean _hasElements_1 = false;
      for(final RequiredRole role_1 : _requiredRoles_InterfaceRequiringEntity_1) {
        if (!_hasElements_1) {
          _hasElements_1 = true;
        } else {
          _builder_1.appendImmediate(", ", "");
        }
        _builder_1.append("this.");
        String _javaName_1 = JavaNames.javaName(role_1);
        String _firstLower_1 = StringExtensions.toFirstLower(_javaName_1);
        _builder_1.append(_firstLower_1, "");
        _builder_1.append(" = ");
        String _javaName_2 = JavaNames.javaName(role_1);
        String _firstLower_2 = StringExtensions.toFirstLower(_javaName_2);
        _builder_1.append(_firstLower_2, "");
        _builder_1.append(";");
      }
    }
    JMethod _withImplementation = _withParameters.withImplementation(_builder_1.toString());
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation));
  }
  
  @Override
  public String packageName() {
    return JavaNames.fqnContextPackage(this.pcmEntity);
  }
  
  @Override
  public String compilationUnitName() {
    return JavaNames.contextClassName(this.pcmEntity);
  }
  
  @Override
  public Collection<String> interfaces() {
    String _contextInterfaceName = JavaNames.contextInterfaceName(this.pcmEntity);
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_contextInterfaceName, JavaConstants.SERIALIZABLE_INTERFACE));
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    LinkedList<JMethod> _xblockexpression = null;
    {
      final LinkedList<JMethod> results = CollectionLiterals.<JMethod>newLinkedList();
      EList<RequiredRole> _requiredRoles_InterfaceRequiringEntity = this.pcmEntity.getRequiredRoles_InterfaceRequiringEntity();
      final Function1<RequiredRole, Boolean> _function = (RequiredRole it) -> {
        return Boolean.valueOf(OperationRequiredRole.class.isInstance(it));
      };
      Iterable<RequiredRole> _filter = IterableExtensions.<RequiredRole>filter(_requiredRoles_InterfaceRequiringEntity, _function);
      final Function1<RequiredRole, JMethod> _function_1 = (RequiredRole it) -> {
        JMethod _jMethod = new JMethod();
        String _javaName = JavaNames.javaName(it);
        String _plus = ("getRole" + _javaName);
        JMethod _withName = _jMethod.withName(_plus);
        OperationInterface _requiredInterface__OperationRequiredRole = ((OperationRequiredRole) it).getRequiredInterface__OperationRequiredRole();
        String _fqn = JavaNames.fqn(_requiredInterface__OperationRequiredRole);
        JMethod _withReturnType = _withName.withReturnType(_fqn);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("if (");
        String _javaName_1 = JavaNames.javaName(it);
        String _firstLower = StringExtensions.toFirstLower(_javaName_1);
        _builder.append(_firstLower, "");
        _builder.append(" == null) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("throw new RuntimeException(\"Attempt to retrieve unbounded port. Check your architecture! Role ");
        String _entityName = it.getEntityName();
        _builder.append(_entityName, "\t");
        _builder.append(" <");
        String _id = it.getId();
        _builder.append(_id, "\t");
        _builder.append("> RequiringEntity ");
        InterfaceRequiringEntity _requiringEntity_RequiredRole = it.getRequiringEntity_RequiredRole();
        String _entityName_1 = _requiringEntity_RequiredRole.getEntityName();
        _builder.append(_entityName_1, "\t");
        _builder.append("\");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("return ");
        String _javaName_2 = JavaNames.javaName(it);
        String _firstLower_1 = StringExtensions.toFirstLower(_javaName_2);
        _builder.append(_firstLower_1, "");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        return _withReturnType.withImplementation(_builder.toString());
      };
      Iterable<JMethod> _map = IterableExtensions.<RequiredRole, JMethod>map(_filter, _function_1);
      Iterables.<JMethod>addAll(results, _map);
      EList<RequiredRole> _requiredRoles_InterfaceRequiringEntity_1 = this.pcmEntity.getRequiredRoles_InterfaceRequiringEntity();
      final Function1<RequiredRole, Boolean> _function_2 = (RequiredRole it) -> {
        return Boolean.valueOf(OperationRequiredRole.class.isInstance(it));
      };
      Iterable<RequiredRole> _filter_1 = IterableExtensions.<RequiredRole>filter(_requiredRoles_InterfaceRequiringEntity_1, _function_2);
      final Function1<RequiredRole, JMethod> _function_3 = (RequiredRole it) -> {
        JMethod _jMethod = new JMethod();
        String _javaName = JavaNames.javaName(it);
        String _plus = ("setRole" + _javaName);
        JMethod _withName = _jMethod.withName(_plus);
        OperationInterface _requiredInterface__OperationRequiredRole = ((OperationRequiredRole) it).getRequiredInterface__OperationRequiredRole();
        String _fqn = JavaNames.fqn(_requiredInterface__OperationRequiredRole);
        String _plus_1 = (_fqn + " newValue");
        JMethod _withParameters = _withName.withParameters(_plus_1);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("this.");
        String _javaName_1 = JavaNames.javaName(it);
        String _firstLower = StringExtensions.toFirstLower(_javaName_1);
        _builder.append(_firstLower, "");
        _builder.append(" = newValue;");
        return _withParameters.withImplementation(_builder.toString());
      };
      Iterable<JMethod> _map_1 = IterableExtensions.<RequiredRole, JMethod>map(_filter_1, _function_3);
      Iterables.<JMethod>addAll(results, _map_1);
      _xblockexpression = results;
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
