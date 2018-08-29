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
import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.protocom.lang.java.IJAnnotation;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JAnnotation;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.servlet.ServletClass;

@SuppressWarnings("all")
public class ServletComposedStructurePortClass extends ServletClass<ProvidedRole> {
  public ServletComposedStructurePortClass(final ProvidedRole pcmEntity) {
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
  public Collection<String> interfaces() {
    OperationInterface _providedInterface__OperationProvidedRole = ((OperationProvidedRole) this.pcmEntity).getProvidedInterface__OperationProvidedRole();
    String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_fqn));
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
  public Collection<? extends IJAnnotation> annotations() {
    JAnnotation _jAnnotation = new JAnnotation();
    JAnnotation _withName = _jAnnotation.withName("javax.servlet.annotation.WebServlet");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("urlPatterns = \"/");
    String _compilationUnitName = this.compilationUnitName();
    _builder.append(_compilationUnitName, "");
    _builder.append("\"");
    JAnnotation _withValues = _withName.withValues(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_builder.toString(), "loadOnStartup = 0")));
    return Collections.<IJAnnotation>unmodifiableList(CollectionLiterals.<IJAnnotation>newArrayList(_withValues));
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    JField _jField = new JField();
    JField _asDefaultSerialVersionUID = _jField.asDefaultSerialVersionUID();
    JField _jField_1 = new JField();
    JField _withName = _jField_1.withName("compositeComponentOrSystem");
    InterfaceProvidingEntity _providingEntity_ProvidedRole = this.pcmEntity.getProvidingEntity_ProvidedRole();
    String _fqnInterface = JavaNames.fqnInterface(_providingEntity_ProvidedRole);
    JField _withType = _withName.withType(_fqnInterface);
    JField _jField_2 = new JField();
    JField _withName_1 = _jField_2.withName("innerPort");
    OperationInterface _providedInterface__OperationProvidedRole = ((OperationProvidedRole) this.pcmEntity).getProvidedInterface__OperationProvidedRole();
    String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
    JField _withType_1 = _withName_1.withType(_fqn);
    return Collections.<IJField>unmodifiableList(CollectionLiterals.<IJField>newArrayList(_asDefaultSerialVersionUID, _withType, _withType_1));
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    JMethod _jMethod = new JMethod();
    StringConcatenation _builder = new StringConcatenation();
    JMethod _withImplementation = _jMethod.withImplementation(_builder.toString());
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation));
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    LinkedList<JMethod> _xblockexpression = null;
    {
      String iface = null;
      LinkedList<JMethod> result = CollectionLiterals.<JMethod>newLinkedList();
      if ((this.pcmEntity instanceof OperationProvidedRole)) {
        OperationInterface _providedInterface__OperationProvidedRole = ((OperationProvidedRole)this.pcmEntity).getProvidedInterface__OperationProvidedRole();
        String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
        iface = _fqn;
      }
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
      _builder_1.append("innerPort = (");
      OperationInterface _providedInterface__OperationProvidedRole_1 = ((OperationProvidedRole) this.pcmEntity).getProvidedInterface__OperationProvidedRole();
      String _fqn_1 = JavaNames.fqn(_providedInterface__OperationProvidedRole_1);
      _builder_1.append(_fqn_1, "\t");
      _builder_1.append(") ");
      _builder_1.append(this.frameworkBase, "\t");
      _builder_1.append(".protocol.Registry.getInstance().lookup(componentId);");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("} catch (");
      _builder_1.append(this.frameworkBase, "");
      _builder_1.append(".protocol.RegistryException e) {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("e.printStackTrace();");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
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
      _builder_1.append("\", interfaces, location, \"/");
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
      JMethod _withImplementation_1 = _withName_1.withImplementation(_builder_2.toString());
      result.add(_withImplementation_1);
      List<JMethod> _providedRoleMethods = this.providedRoleMethods(this.pcmEntity);
      Iterables.<JMethod>addAll(result, _providedRoleMethods);
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
      String _javaSignature_1 = JavaNames.javaSignature(it);
      String _plus = ("return innerPort." + _javaSignature_1);
      String _plus_1 = (_plus + "(ctx);");
      return _withParameters.withImplementation(_plus_1);
    };
    return ListExtensions.<OperationSignature, JMethod>map(_signatures__OperationInterface, _function);
  }
  
  protected List<JMethod> _providedRoleMethods(final InfrastructureProvidedRole role) {
    return Collections.<JMethod>unmodifiableList(CollectionLiterals.<JMethod>newArrayList());
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
