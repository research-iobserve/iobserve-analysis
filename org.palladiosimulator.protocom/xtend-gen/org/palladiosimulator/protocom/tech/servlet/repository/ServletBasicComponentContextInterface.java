package org.palladiosimulator.protocom.tech.servlet.repository;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.LinkedList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.RequiredRole;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.servlet.ServletInterface;

@SuppressWarnings("all")
public class ServletBasicComponentContextInterface extends ServletInterface<BasicComponent> {
  public ServletBasicComponentContextInterface(final BasicComponent pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String packageName() {
    return JavaNames.fqnContextPackage(this.pcmEntity);
  }
  
  @Override
  public String compilationUnitName() {
    return JavaNames.contextInterfaceName(this.pcmEntity);
  }
  
  @Override
  public String filePath() {
    String _fqnContextInterface = JavaNames.fqnContextInterface(this.pcmEntity);
    String _fqnToDirectoryPath = JavaNames.fqnToDirectoryPath(_fqnContextInterface);
    String _plus = ("/src/" + _fqnToDirectoryPath);
    return (_plus + ".java");
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
        String _plus = ("get" + _javaName);
        JMethod _withName = _jMethod.withName(_plus);
        return _withName.withReturnType("String");
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
        String _plus = ("set" + _javaName);
        JMethod _withName = _jMethod.withName(_plus);
        return _withName.withParameters("String port");
      };
      Iterable<JMethod> _map_1 = IterableExtensions.<RequiredRole, JMethod>map(_filter_1, _function_3);
      Iterables.<JMethod>addAll(results, _map_1);
      EList<RequiredRole> _requiredRoles_InterfaceRequiringEntity_2 = this.pcmEntity.getRequiredRoles_InterfaceRequiringEntity();
      final Function1<RequiredRole, Boolean> _function_4 = (RequiredRole it) -> {
        return Boolean.valueOf(OperationRequiredRole.class.isInstance(it));
      };
      Iterable<RequiredRole> _filter_2 = IterableExtensions.<RequiredRole>filter(_requiredRoles_InterfaceRequiringEntity_2, _function_4);
      final Function1<RequiredRole, JMethod> _function_5 = (RequiredRole it) -> {
        JMethod _jMethod = new JMethod();
        String _javaName = JavaNames.javaName(it);
        String _plus = ("getPortFor" + _javaName);
        JMethod _withName = _jMethod.withName(_plus);
        OperationInterface _requiredInterface__OperationRequiredRole = ((OperationRequiredRole) it).getRequiredInterface__OperationRequiredRole();
        String _fqn = JavaNames.fqn(_requiredInterface__OperationRequiredRole);
        return _withName.withReturnType(_fqn);
      };
      Iterable<JMethod> _map_2 = IterableExtensions.<RequiredRole, JMethod>map(_filter_2, _function_5);
      Iterables.<JMethod>addAll(results, _map_2);
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
}
