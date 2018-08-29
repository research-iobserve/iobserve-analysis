package org.palladiosimulator.protocom.tech.iiop.repository;

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
import org.palladiosimulator.pcm.repository.InfrastructureInterface;
import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole;
import org.palladiosimulator.pcm.repository.InfrastructureSignature;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.protocom.correspondencemodel.CorrespondenceModelGeneratorFacade;
import org.palladiosimulator.protocom.correspondencemodel.HighLevelModelElemDescr;
import org.palladiosimulator.protocom.correspondencemodel.LowLevelModelElemDescr;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.PcmCommons;
import org.palladiosimulator.protocom.tech.iiop.JavaEEIIOPClass;

@SuppressWarnings("all")
public class JavaEEIIOPBasicComponentPortClass extends JavaEEIIOPClass<ProvidedRole> {
  public JavaEEIIOPBasicComponentPortClass(final ProvidedRole pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String superClass() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("org.palladiosimulator.protocom.framework.java.se.port.AbstractPerformancePrototypeBasicPort<");
    InterfaceProvidingEntity _providingEntity_ProvidedRole = this.pcmEntity.getProvidingEntity_ProvidedRole();
    String _fqnJavaEEComponentPortSuperClass = JavaNames.fqnJavaEEComponentPortSuperClass(_providingEntity_ProvidedRole);
    _builder.append(_fqnJavaEEComponentPortSuperClass, "");
    _builder.append(">");
    return _builder.toString();
  }
  
  @Override
  public String packageName() {
    return JavaNames.fqnJavaEEPortPackage(this.pcmEntity);
  }
  
  @Override
  public String compilationUnitName() {
    return JavaNames.portClassName(this.pcmEntity);
  }
  
  @Override
  public Collection<String> interfaces() {
    String _providedRoleInterface = this.providedRoleInterface(this.pcmEntity);
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_providedRoleInterface));
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    JMethod _jMethod = new JMethod();
    JMethod _withThrows = _jMethod.withThrows("java.rmi.RemoteException");
    JMethod _jMethod_1 = new JMethod();
    JMethod _withParameters = _jMethod_1.withParameters("String assemblyContext");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("addVisitor(org.palladiosimulator.protocom.framework.java.se.visitor.SensorFrameworkVisitor.getInstance());");
    _builder.newLine();
    JMethod _withImplementation = _withParameters.withImplementation(_builder.toString());
    JMethod _withThrows_1 = _withImplementation.withThrows("java.rmi.RemoteException");
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withThrows, _withThrows_1));
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    return this.providedRoleMethods(this.pcmEntity);
  }
  
  @Override
  public String filePath() {
    return JavaNames.fqnJavaEEBasicComponentPortClassPath(this.pcmEntity);
  }
  
  @Override
  public String projectName() {
    return JavaNames.fqnJavaEEBasicComponentPortProjectName(this.pcmEntity);
  }
  
  protected List<JMethod> _providedRoleMethods(final OperationProvidedRole role) {
    OperationInterface _providedInterface__OperationProvidedRole = role.getProvidedInterface__OperationProvidedRole();
    EList<OperationSignature> _signatures__OperationInterface = _providedInterface__OperationProvidedRole.getSignatures__OperationInterface();
    final Function1<OperationSignature, JMethod> _function = (OperationSignature it) -> {
      JMethod _jMethod = new JMethod();
      String _javaSignature = JavaNames.javaSignature(it);
      JMethod _withName = _jMethod.withName(_javaSignature);
      String _stackframeType = PcmCommons.stackframeType();
      JMethod _withReturnType = _withName.withReturnType(_stackframeType);
      String _stackContextParameterList = PcmCommons.stackContextParameterList();
      JMethod _withParameters = _withReturnType.withParameters(_stackContextParameterList);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("preCallVisitor(ctx, \"");
      String _serviceName = JavaNames.serviceName(it);
      _builder.append(_serviceName, "");
      _builder.append("\");");
      _builder.newLineIfNotEmpty();
      _builder.append("de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> result = myComponent.");
      String _serviceName_1 = JavaNames.serviceName(it);
      _builder.append(_serviceName_1, "");
      _builder.append("(ctx);");
      _builder.newLineIfNotEmpty();
      _builder.append("postCallVisitor(ctx, \"");
      String _serviceName_2 = JavaNames.serviceName(it);
      _builder.append(_serviceName_2, "");
      _builder.append("\");");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append("return result;");
      _builder.newLine();
      final JMethod m = _withParameters.withImplementation(_builder.toString());
      final HighLevelModelElemDescr highLevelModelElemDescr = new HighLevelModelElemDescr(it);
      String _visibilityModifier = m.visibilityModifier();
      String _returnType = m.returnType();
      String _packageName = this.packageName();
      String _compilationUnitName = this.compilationUnitName();
      String _name = m.name();
      String _parameters = m.parameters();
      String _throwExceptionSignature = m.throwExceptionSignature();
      String _format = String.format("%s %s %s.%s.%s(%s)%s", _visibilityModifier, _returnType, _packageName, _compilationUnitName, _name, _parameters, _throwExceptionSignature);
      String _name_1 = m.name();
      String _packageName_1 = this.packageName();
      String _compilationUnitName_1 = this.compilationUnitName();
      String _format_1 = String.format("%s.%s", _packageName_1, _compilationUnitName_1);
      final LowLevelModelElemDescr lowLevelModelElemDescr = new LowLevelModelElemDescr(_format, _name_1, _format_1);
      CorrespondenceModelGeneratorFacade.INSTANCE.createCorrespondence(highLevelModelElemDescr, lowLevelModelElemDescr);
      return m;
    };
    return ListExtensions.<OperationSignature, JMethod>map(_signatures__OperationInterface, _function);
  }
  
  protected List<JMethod> _providedRoleMethods(final InfrastructureProvidedRole role) {
    InfrastructureInterface _providedInterface__InfrastructureProvidedRole = role.getProvidedInterface__InfrastructureProvidedRole();
    EList<InfrastructureSignature> _infrastructureSignatures__InfrastructureInterface = _providedInterface__InfrastructureProvidedRole.getInfrastructureSignatures__InfrastructureInterface();
    final Function1<InfrastructureSignature, JMethod> _function = (InfrastructureSignature it) -> {
      JMethod _jMethod = new JMethod();
      String _javaSignature = JavaNames.javaSignature(it);
      JMethod _withName = _jMethod.withName(_javaSignature);
      String _stackframeType = PcmCommons.stackframeType();
      JMethod _withReturnType = _withName.withReturnType(_stackframeType);
      String _stackContextParameterList = PcmCommons.stackContextParameterList();
      JMethod _withParameters = _withReturnType.withParameters(_stackContextParameterList);
      final JMethod m = _withParameters.withImplementation("return null;");
      final HighLevelModelElemDescr highLevelModelElemDescr = new HighLevelModelElemDescr(it);
      String _visibilityModifier = m.visibilityModifier();
      String _returnType = m.returnType();
      String _packageName = this.packageName();
      String _compilationUnitName = this.compilationUnitName();
      String _name = m.name();
      String _parameters = m.parameters();
      String _throwExceptionSignature = m.throwExceptionSignature();
      String _format = String.format("%s %s %s.%s.%s(%s)%s", _visibilityModifier, _returnType, _packageName, _compilationUnitName, _name, _parameters, _throwExceptionSignature);
      String _name_1 = m.name();
      String _packageName_1 = this.packageName();
      String _compilationUnitName_1 = this.compilationUnitName();
      String _format_1 = String.format("%s.%s", _packageName_1, _compilationUnitName_1);
      final LowLevelModelElemDescr lowLevelModelElemDescr = new LowLevelModelElemDescr(_format, _name_1, _format_1);
      CorrespondenceModelGeneratorFacade.INSTANCE.createCorrespondence(highLevelModelElemDescr, lowLevelModelElemDescr);
      return m;
    };
    return ListExtensions.<InfrastructureSignature, JMethod>map(_infrastructureSignatures__InfrastructureInterface, _function);
  }
  
  protected String _providedRoleInterface(final OperationProvidedRole role) {
    return JavaNames.fqnJavaEEComponentPortInterface(role);
  }
  
  protected String _providedRoleInterface(final InfrastructureProvidedRole role) {
    return JavaNames.fqnJavaEEComponentPortInterface(role);
  }
  
  @Override
  public Collection<? extends IJField> jeeClassDependencyInjection() {
    LinkedList<JField> _xblockexpression = null;
    {
      final LinkedList<JField> results = CollectionLiterals.<JField>newLinkedList();
      JField _jField = new JField();
      JField _withName = _jField.withName("myComponent");
      InterfaceProvidingEntity _providingEntity_ProvidedRole = this.pcmEntity.getProvidingEntity_ProvidedRole();
      String _javaName = JavaNames.javaName(_providingEntity_ProvidedRole);
      JField _withType = _withName.withType(_javaName);
      Iterables.<JField>addAll(results, Collections.<JField>unmodifiableList(CollectionLiterals.<JField>newArrayList(_withType)));
      _xblockexpression = results;
    }
    return _xblockexpression;
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
  
  public String providedRoleInterface(final ProvidedRole role) {
    if (role instanceof InfrastructureProvidedRole) {
      return _providedRoleInterface((InfrastructureProvidedRole)role);
    } else if (role instanceof OperationProvidedRole) {
      return _providedRoleInterface((OperationProvidedRole)role);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(role).toString());
    }
  }
}
