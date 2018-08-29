package org.palladiosimulator.protocom.tech.pojo.repository;

import com.google.common.collect.Iterables;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.InfrastructureInterface;
import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole;
import org.palladiosimulator.pcm.repository.InfrastructureSignature;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.DataTypes;
import org.palladiosimulator.protocom.lang.java.util.JavaConstants;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.Parameters;
import org.palladiosimulator.protocom.tech.rmi.PojoClass;

/**
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PojoComposedStructurePortClass extends PojoClass<ProvidedRole> {
  public PojoComposedStructurePortClass(final ProvidedRole pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String superClass() {
    return JavaConstants.RMI_REMOTE_OBJECT_CLASS;
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    JField _jField = new JField();
    JField _withName = _jField.withName("myCompositeComponent");
    InterfaceProvidingEntity _providingEntity_ProvidedRole = this.pcmEntity.getProvidingEntity_ProvidedRole();
    String _fqnInterface = JavaNames.fqnInterface(_providingEntity_ProvidedRole);
    JField _withType = _withName.withType(_fqnInterface);
    JField _jField_1 = new JField();
    JField _withName_1 = _jField_1.withName("myInnerPort");
    OperationInterface _providedInterface__OperationProvidedRole = ((OperationProvidedRole) this.pcmEntity).getProvidedInterface__OperationProvidedRole();
    String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
    JField _withType_1 = _withName_1.withType(_fqn);
    return Collections.<IJField>unmodifiableList(CollectionLiterals.<IJField>newArrayList(_withType, _withType_1));
  }
  
  @Override
  public String packageName() {
    return JavaNames.fqnPortPackage(this.pcmEntity);
  }
  
  @Override
  public String compilationUnitName() {
    return JavaNames.portClassName(this.pcmEntity);
  }
  
  @Override
  public Collection<String> interfaces() {
    InterfaceProvidingEntity _providingEntity_ProvidedRole = this.pcmEntity.getProvidingEntity_ProvidedRole();
    String _fqnInterface = JavaNames.fqnInterface(_providingEntity_ProvidedRole);
    String _plus = ("org.palladiosimulator.protocom.framework.java.se.port.IPort<" + _fqnInterface);
    String _plus_1 = (_plus + ">");
    OperationInterface _providedInterface__OperationProvidedRole = ((OperationProvidedRole) this.pcmEntity).getProvidedInterface__OperationProvidedRole();
    String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_plus_1, _fqn, JavaConstants.RMI_REMOTE_INTERFACE, JavaConstants.SERIALIZABLE_INTERFACE));
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    JMethod _jMethod = new JMethod();
    OperationInterface _providedInterface__OperationProvidedRole = ((OperationProvidedRole) this.pcmEntity).getProvidedInterface__OperationProvidedRole();
    String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
    String _plus = (_fqn + " myInnerPort, ");
    InterfaceProvidingEntity _providingEntity_ProvidedRole = this.pcmEntity.getProvidingEntity_ProvidedRole();
    String _fqnInterface = JavaNames.fqnInterface(_providingEntity_ProvidedRole);
    String _plus_1 = (_plus + _fqnInterface);
    String _plus_2 = (_plus_1 + " myComponent, String assemblyContext");
    JMethod _withParameters = _jMethod.withParameters(_plus_2);
    JMethod _withThrows = _withParameters.withThrows(JavaConstants.RMI_REMOTE_EXCEPTION);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("this.myInnerPort = myInnerPort;");
    _builder.newLine();
    _builder.append("this.myCompositeComponent = myComponent;");
    _builder.newLine();
    _builder.append("org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.registerPort(org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.getRemoteAddress(),");
    _builder.newLine();
    _builder.append("org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.getRegistryPort(), this, \"");
    String _portClassName = JavaNames.portClassName(this.pcmEntity);
    _builder.append(_portClassName, "");
    _builder.append("_\" + assemblyContext);");
    _builder.newLineIfNotEmpty();
    JMethod _withImplementation = _withThrows.withImplementation(_builder.toString());
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation));
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    LinkedList<JMethod> _xblockexpression = null;
    {
      final LinkedList<JMethod> results = CollectionLiterals.<JMethod>newLinkedList();
      List<JMethod> _providedRoleMethods = this.providedRoleMethods(this.pcmEntity);
      Iterables.<JMethod>addAll(results, _providedRoleMethods);
      JMethod _jMethod = new JMethod();
      JMethod _withName = _jMethod.withName("setContext");
      JMethod _withParameters = _withName.withParameters("Object myContext");
      JMethod _withThrows = _withParameters.withThrows(JavaConstants.RMI_REMOTE_EXCEPTION);
      JMethod _withImplementation = _withThrows.withImplementation("myCompositeComponent.setContext(myContext);");
      JMethod _jMethod_1 = new JMethod();
      JMethod _withName_1 = _jMethod_1.withName("getComponent");
      InterfaceProvidingEntity _providingEntity_ProvidedRole = this.pcmEntity.getProvidingEntity_ProvidedRole();
      String _fqnInterface = JavaNames.fqnInterface(_providingEntity_ProvidedRole);
      JMethod _withReturnType = _withName_1.withReturnType(_fqnInterface);
      JMethod _withThrows_1 = _withReturnType.withThrows(JavaConstants.RMI_REMOTE_EXCEPTION);
      JMethod _withImplementation_1 = _withThrows_1.withImplementation("return myCompositeComponent;");
      Iterables.<JMethod>addAll(results, Collections.<JMethod>unmodifiableList(CollectionLiterals.<JMethod>newArrayList(_withImplementation, _withImplementation_1)));
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
  
  @Override
  public String filePath() {
    String _fqnPortPackage = JavaNames.fqnPortPackage(this.pcmEntity);
    String _fqnToDirectoryPath = JavaNames.fqnToDirectoryPath(_fqnPortPackage);
    String _plus = ("/src/" + _fqnToDirectoryPath);
    String _plus_1 = (_plus + "/");
    String _portClassName = JavaNames.portClassName(this.pcmEntity);
    String _plus_2 = (_plus_1 + _portClassName);
    return (_plus_2 + ".java");
  }
  
  protected List<JMethod> _providedRoleMethods(final OperationProvidedRole role) {
    OperationInterface _providedInterface__OperationProvidedRole = role.getProvidedInterface__OperationProvidedRole();
    EList<OperationSignature> _signatures__OperationInterface = _providedInterface__OperationProvidedRole.getSignatures__OperationInterface();
    final Function1<OperationSignature, JMethod> _function = (OperationSignature it) -> {
      JMethod _jMethod = new JMethod();
      String _javaName = JavaNames.javaName(it);
      JMethod _withName = _jMethod.withName(_javaName);
      DataType _returnType__OperationSignature = it.getReturnType__OperationSignature();
      String _dataType = DataTypes.getDataType(_returnType__OperationSignature);
      JMethod _withReturnType = _withName.withReturnType(_dataType);
      String _parameterList = Parameters.getParameterList(it);
      JMethod _withParameters = _withReturnType.withParameters(_parameterList);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("// TODO Initialize parameters");
      _builder.newLine();
      {
        EList<Parameter> _parameters__OperationSignature = it.getParameters__OperationSignature();
        for(final Parameter parameter : _parameters__OperationSignature) {
          DataType _dataType__Parameter = parameter.getDataType__Parameter();
          String _dataType_1 = DataTypes.getDataType(_dataType__Parameter);
          String _plus = (_dataType_1 + " param_");
          String _parameterName = parameter.getParameterName();
          String _plus_1 = (_plus + _parameterName);
          String _plus_2 = (_plus_1 + " = ");
          String _parameterName_1 = parameter.getParameterName();
          String _plus_3 = (_plus_2 + _parameterName_1);
          String _plus_4 = (_plus_3 + ";");
          _builder.append(_plus_4, "");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.newLine();
      {
        String _returnDataType = DataTypes.getReturnDataType(it);
        boolean _equals = _returnDataType.equals("void");
        boolean _not = (!_equals);
        if (_not) {
          _builder.append("return ");
          _builder.newLine();
        }
      }
      _builder.append("\t");
      _builder.append("myInnerPort.");
      String _javaName_1 = JavaNames.javaName(it);
      _builder.append(_javaName_1, "\t");
      _builder.append("(");
      String _parameterUsageList = Parameters.getParameterUsageList(it);
      _builder.append(_parameterUsageList, "\t");
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      JMethod _withImplementation = _withParameters.withImplementation(_builder.toString());
      return _withImplementation.withThrows(JavaConstants.RMI_REMOTE_EXCEPTION);
    };
    return ListExtensions.<OperationSignature, JMethod>map(_signatures__OperationInterface, _function);
  }
  
  protected List<JMethod> _providedRoleMethods(final InfrastructureProvidedRole role) {
    InfrastructureInterface _providedInterface__InfrastructureProvidedRole = role.getProvidedInterface__InfrastructureProvidedRole();
    EList<InfrastructureSignature> _infrastructureSignatures__InfrastructureInterface = _providedInterface__InfrastructureProvidedRole.getInfrastructureSignatures__InfrastructureInterface();
    final Function1<InfrastructureSignature, JMethod> _function = (InfrastructureSignature it) -> {
      JMethod _jMethod = new JMethod();
      String _javaName = JavaNames.javaName(it);
      JMethod _withName = _jMethod.withName(_javaName);
      JMethod _withReturnType = _withName.withReturnType("void");
      String _parameterList = Parameters.getParameterList(it);
      JMethod _withParameters = _withReturnType.withParameters(_parameterList);
      JMethod _withImplementation = _withParameters.withImplementation("");
      return _withImplementation.withThrows(JavaConstants.RMI_REMOTE_EXCEPTION);
    };
    return ListExtensions.<InfrastructureSignature, JMethod>map(_infrastructureSignatures__InfrastructureInterface, _function);
  }
  
  public List<JMethod> providedRoleMethods(final ProvidedRole role) {
    if (role instanceof InfrastructureProvidedRole) {
      return _providedRoleMethods((InfrastructureProvidedRole)role);
    } else if (role instanceof OperationProvidedRole) {
      return _providedRoleMethods((OperationProvidedRole)role);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(role).toString());
    }
  }
}
