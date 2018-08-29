package org.palladiosimulator.protocom.tech.servlet.system;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.protocom.lang.java.IJAnnotation;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JAnnotation;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.servlet.ServletClass;

@SuppressWarnings("all")
public class ServletSystemMain extends ServletClass<org.palladiosimulator.pcm.system.System> {
  public ServletSystemMain(final org.palladiosimulator.pcm.system.System pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String superClass() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.frameworkBase, "");
    _builder.append(".main.MainServlet");
    return _builder.toString();
  }
  
  @Override
  public Collection<? extends IJAnnotation> annotations() {
    JAnnotation _jAnnotation = new JAnnotation();
    JAnnotation _withName = _jAnnotation.withName("javax.servlet.annotation.WebServlet");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("urlPatterns = \"\", loadOnStartup = 0");
    JAnnotation _withValues = _withName.withValues(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_builder.toString())));
    return Collections.<IJAnnotation>unmodifiableList(CollectionLiterals.<IJAnnotation>newArrayList(_withValues));
  }
  
  @Override
  public String compilationUnitName() {
    return "Main";
  }
  
  @Override
  public String packageName() {
    return "main";
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    JField _jField = new JField();
    JField _asDefaultSerialVersionUID = _jField.asDefaultSerialVersionUID();
    return Collections.<IJField>unmodifiableList(CollectionLiterals.<IJField>newArrayList(_asDefaultSerialVersionUID));
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    List<IJMethod> _xblockexpression = null;
    {
      final String system = JavaNames.javaName(this.pcmEntity);
      final String systemClass = JavaNames.fqn(this.pcmEntity);
      JMethod _jMethod = new JMethod();
      JMethod _withVisibilityModifier = _jMethod.withVisibilityModifier("protected");
      JMethod _withReturnType = _withVisibilityModifier.withReturnType("void");
      JMethod _withName = _withReturnType.withName("initPrototype");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(this.frameworkBase, "");
      _builder.append(".prototype.PrototypeBridge bridge");
      JMethod _withParameters = _withName.withParameters(_builder.toString());
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("bridge.setSystem(bridge.new System(\"");
      _builder_1.append(system, "");
      _builder_1.append("\", \"");
      _builder_1.append(systemClass, "");
      _builder_1.append("\"));");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("bridge.setUsageScenarios(getUsageScenarios());");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("ResourceEnvironment.init(bridge);");
      _builder_1.newLine();
      _builder_1.append("ComponentAllocation.init(bridge);");
      _builder_1.newLine();
      JMethod _withImplementation = _withParameters.withImplementation(_builder_1.toString());
      JMethod _jMethod_1 = new JMethod();
      JMethod _withVisibilityModifier_1 = _jMethod_1.withVisibilityModifier("private");
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append(this.frameworkBase, "");
      _builder_2.append(".prototype.IUsageScenario[]");
      JMethod _withReturnType_1 = _withVisibilityModifier_1.withReturnType(_builder_2.toString());
      JMethod _withName_1 = _withReturnType_1.withName("getUsageScenarios");
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append("Class<?>[] classes = ");
      _builder_3.append(this.frameworkBase, "");
      _builder_3.append(".prototype.ClassHelper.getSubclasses(\"usagescenarios\", ");
      _builder_3.append(this.frameworkBase, "");
      _builder_3.append(".prototype.IUsageScenario.class);");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append(this.frameworkBase, "");
      _builder_3.append(".prototype.IUsageScenario[] scenarios = new ");
      _builder_3.append(this.frameworkBase, "");
      _builder_3.append(".prototype.IUsageScenario[classes.length];");
      _builder_3.newLineIfNotEmpty();
      _builder_3.newLine();
      _builder_3.append("try {");
      _builder_3.newLine();
      _builder_3.append("\t");
      _builder_3.append("for (int i = 0; i < classes.length; i++) {");
      _builder_3.newLine();
      _builder_3.append("\t\t");
      _builder_3.append("scenarios[i] = (");
      _builder_3.append(this.frameworkBase, "\t\t");
      _builder_3.append(".prototype.IUsageScenario) classes[i].newInstance();");
      _builder_3.newLineIfNotEmpty();
      _builder_3.append("\t");
      _builder_3.append("}");
      _builder_3.newLine();
      _builder_3.append("} catch (Exception e) {");
      _builder_3.newLine();
      _builder_3.append("\t");
      _builder_3.append("throw new RuntimeException(e);");
      _builder_3.newLine();
      _builder_3.append("}");
      _builder_3.newLine();
      _builder_3.newLine();
      _builder_3.append("return scenarios;");
      _builder_3.newLine();
      JMethod _withImplementation_1 = _withName_1.withImplementation(_builder_3.toString());
      _xblockexpression = Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation, _withImplementation_1));
    }
    return _xblockexpression;
  }
  
  @Override
  public String filePath() {
    return "/src/main/Main.java";
  }
}
