package org.palladiosimulator.protocom.tech.servlet.repository;

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
import org.palladiosimulator.pcm.repository.SinkRole;
import org.palladiosimulator.protocom.lang.java.IJAnnotation;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.servlet.ServletClass;

/**
 * @author Christian Klaussner
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class ServletBasicComponentPortClass extends ServletClass<ProvidedRole> {
  public ServletBasicComponentPortClass(final ProvidedRole pcmEntity) {
    super(pcmEntity);
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
  public String superClass() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.frameworkBase, "");
    _builder.append(".prototype.PortServlet<");
    InterfaceProvidingEntity _providingEntity_ProvidedRole = this.pcmEntity.getProvidingEntity_ProvidedRole();
    String _fqnInterface = JavaNames.fqnInterface(_providingEntity_ProvidedRole);
    _builder.append(_fqnInterface, "");
    _builder.append(">");
    return _builder.toString();
  }
  
  @Override
  public Collection<String> interfaces() {
    String _providedRoleInterface = this.providedRoleInterface(this.pcmEntity);
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_providedRoleInterface));
  }
  
  @Override
  public Collection<? extends IJAnnotation> annotations() {
    return Collections.<IJAnnotation>unmodifiableList(CollectionLiterals.<IJAnnotation>newArrayList());
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    JField _jField = new JField();
    JField _asDefaultSerialVersionUID = _jField.asDefaultSerialVersionUID();
    return Collections.<IJField>unmodifiableList(CollectionLiterals.<IJField>newArrayList(_asDefaultSerialVersionUID));
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    LinkedList<JMethod> _xblockexpression = null;
    {
      String iface = null;
      if ((this.pcmEntity instanceof OperationProvidedRole)) {
        OperationInterface _providedInterface__OperationProvidedRole = ((OperationProvidedRole)this.pcmEntity).getProvidedInterface__OperationProvidedRole();
        String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
        iface = _fqn;
      }
      LinkedList<JMethod> result = CollectionLiterals.<JMethod>newLinkedList();
      JMethod _jMethod = new JMethod();
      JMethod _withVisibilityModifier = _jMethod.withVisibilityModifier("public");
      JMethod _withParameters = _withVisibilityModifier.withParameters("String componentId, String assemblyContext");
      JMethod _withName = _withParameters.withName("start");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(this.frameworkBase, "");
      _builder.append(".modules.ModuleStartException");
      JMethod _withThrows = _withName.withThrows(_builder.toString());
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("this.component = (");
      InterfaceProvidingEntity _providingEntity_ProvidedRole = this.pcmEntity.getProvidingEntity_ProvidedRole();
      String _fqnInterface = JavaNames.fqnInterface(_providingEntity_ProvidedRole);
      _builder_1.append(_fqnInterface, "");
      _builder_1.append(") ");
      _builder_1.append(this.frameworkBase, "");
      _builder_1.append(".prototype.LocalComponentRegistry.getInstance().getComponent(assemblyContext);");
      _builder_1.newLineIfNotEmpty();
      _builder_1.newLine();
      _builder_1.append("try {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("Class<?>[] interfaces = new Class<?>[] {");
      _builder_1.append(iface, "\t");
      _builder_1.append(".class, ");
      _builder_1.append(this.frameworkBase, "\t");
      _builder_1.append(".prototype.IPort.class};");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append(this.frameworkBase, "\t");
      _builder_1.append(".protocol.Registry.getInstance().register(\"");
      String _portClassName = JavaNames.portClassName(this.pcmEntity);
      _builder_1.append(_portClassName, "\t");
      _builder_1.append("\" + \"_\" + assemblyContext, interfaces, location, \"/");
      String _portClassName_1 = JavaNames.portClassName(this.pcmEntity);
      _builder_1.append(_portClassName_1, "\t");
      _builder_1.append("\");");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("} catch (");
      _builder_1.append(this.frameworkBase, "");
      _builder_1.append(".protocol.RegistryException e) {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("throw new ");
      _builder_1.append(this.frameworkBase, "\t");
      _builder_1.append(".modules.ModuleStartException();");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      JMethod _withImplementation = _withThrows.withImplementation(_builder_1.toString());
      result.add(_withImplementation);
      JMethod _jMethod_1 = new JMethod();
      JMethod _withVisibilityModifier_1 = _jMethod_1.withVisibilityModifier("public");
      JMethod _withParameters_1 = _withVisibilityModifier_1.withParameters("Object context");
      JMethod _withName_1 = _withParameters_1.withName("setContext");
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("this.component.setContext(context);");
      _builder_2.newLine();
      JMethod _withImplementation_1 = _withName_1.withImplementation(_builder_2.toString());
      result.add(_withImplementation_1);
      if ((this.pcmEntity instanceof OperationProvidedRole)) {
        List<JMethod> _providedRoleMethods = this.providedRoleMethods(this.pcmEntity);
        Iterables.<JMethod>addAll(result, _providedRoleMethods);
      }
      _xblockexpression = result;
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
      String _javaSignature = JavaNames.javaSignature(it);
      JMethod _withName = _jMethod.withName(_javaSignature);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(this.stackFrame, "");
      _builder.append("<Object>");
      JMethod _withReturnType = _withName.withReturnType(_builder.toString());
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(this.stackContext, "");
      _builder_1.append(" ctx");
      JMethod _withParameters = _withReturnType.withParameters(_builder_1.toString());
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("preCall(\"");
      String _serviceName = JavaNames.serviceName(it);
      _builder_2.append(_serviceName, "");
      _builder_2.append("\");");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append("// de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> result = component.");
      String _serviceName_1 = JavaNames.serviceName(it);
      _builder_2.append(_serviceName_1, "");
      _builder_2.append("(ctx);");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append(this.stackFrame, "");
      _builder_2.append("<Object> result = component.");
      String _serviceName_2 = JavaNames.serviceName(it);
      _builder_2.append(_serviceName_2, "");
      _builder_2.append("(ctx);");
      _builder_2.newLineIfNotEmpty();
      _builder_2.append("postCall(\"");
      String _serviceName_3 = JavaNames.serviceName(it);
      _builder_2.append(_serviceName_3, "");
      _builder_2.append("\");");
      _builder_2.newLineIfNotEmpty();
      _builder_2.newLine();
      _builder_2.append("return result;");
      _builder_2.newLine();
      return _withParameters.withImplementation(_builder_2.toString());
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
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(this.stackFrame, "");
      _builder.append("<Object>");
      JMethod _withReturnType = _withName.withReturnType(_builder.toString());
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(this.stackContext, "");
      _builder_1.append(" ctx");
      JMethod _withParameters = _withReturnType.withParameters(_builder_1.toString());
      return _withParameters.withImplementation("return null;");
    };
    return ListExtensions.<InfrastructureSignature, JMethod>map(_infrastructureSignatures__InfrastructureInterface, _function);
  }
  
  /**
   * TODO Implement SinkRoles?
   */
  protected List<JMethod> _providedRoleMethods(final SinkRole role) {
    return null;
  }
  
  protected String _providedRoleInterface(final OperationProvidedRole role) {
    OperationInterface _providedInterface__OperationProvidedRole = role.getProvidedInterface__OperationProvidedRole();
    return JavaNames.fqn(_providedInterface__OperationProvidedRole);
  }
  
  protected String _providedRoleInterface(final InfrastructureProvidedRole role) {
    InfrastructureInterface _providedInterface__InfrastructureProvidedRole = role.getProvidedInterface__InfrastructureProvidedRole();
    return JavaNames.fqn(_providedInterface__InfrastructureProvidedRole);
  }
  
  /**
   * TODO Implement SinkRoles?
   */
  protected String _providedRoleInterface(final SinkRole role) {
    return "";
  }
  
  public List<JMethod> providedRoleMethods(final ProvidedRole role) {
    if (role instanceof InfrastructureProvidedRole) {
      return _providedRoleMethods((InfrastructureProvidedRole)role);
    } else if (role instanceof OperationProvidedRole) {
      return _providedRoleMethods((OperationProvidedRole)role);
    } else if (role instanceof SinkRole) {
      return _providedRoleMethods((SinkRole)role);
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
    } else if (role instanceof SinkRole) {
      return _providedRoleInterface((SinkRole)role);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(role).toString());
    }
  }
}
