package org.palladiosimulator.protocom.tech.servlet.usage;

import java.util.Collection;
import java.util.Collections;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.model.usage.UsageScenarioAdapter;
import org.palladiosimulator.protocom.tech.servlet.ServletClass;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class ServletUsageScenario extends ServletClass<UsageScenario> {
  private final UsageScenarioAdapter entity;
  
  public ServletUsageScenario(final UsageScenarioAdapter entity, final UsageScenario pcmEntity) {
    super(pcmEntity);
    this.entity = entity;
  }
  
  private CharSequence testPlanPath() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/usagescenarios/jmx/");
    String _safeName = this.entity.getSafeName();
    _builder.append(_safeName, "");
    _builder.append(".jmx");
    return _builder;
  }
  
  @Override
  public String packageName() {
    return "usagescenarios";
  }
  
  @Override
  public Collection<String> interfaces() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.frameworkBase, "");
    _builder.append(".prototype.IUsageScenario");
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(_builder.toString()));
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    JMethod _jMethod = new JMethod();
    JMethod _withVisibilityModifier = _jMethod.withVisibilityModifier("public");
    JMethod _withReturnType = _withVisibilityModifier.withReturnType("String");
    JMethod _withName = _withReturnType.withName("getId");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("return \"");
    String _id = this.entity.getId();
    _builder.append(_id, "");
    _builder.append("\";");
    _builder.newLineIfNotEmpty();
    JMethod _withImplementation = _withName.withImplementation(_builder.toString());
    JMethod _jMethod_1 = new JMethod();
    JMethod _withVisibilityModifier_1 = _jMethod_1.withVisibilityModifier("public");
    JMethod _withReturnType_1 = _withVisibilityModifier_1.withReturnType("String");
    JMethod _withName_1 = _withReturnType_1.withName("getName");
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("return \"");
    String _name = this.entity.getName();
    _builder_1.append(_name, "");
    _builder_1.append("\";");
    _builder_1.newLineIfNotEmpty();
    JMethod _withImplementation_1 = _withName_1.withImplementation(_builder_1.toString());
    JMethod _jMethod_2 = new JMethod();
    JMethod _withVisibilityModifier_2 = _jMethod_2.withVisibilityModifier("public");
    JMethod _withReturnType_2 = _withVisibilityModifier_2.withReturnType("java.net.URL");
    JMethod _withName_2 = _withReturnType_2.withName("getFileUrl");
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append("ClassLoader classLoader = getClass().getClassLoader();");
    _builder_2.newLine();
    _builder_2.append("return classLoader.getResource(\"");
    CharSequence _testPlanPath = this.testPlanPath();
    _builder_2.append(_testPlanPath, "");
    _builder_2.append("\");");
    _builder_2.newLineIfNotEmpty();
    JMethod _withImplementation_2 = _withName_2.withImplementation(_builder_2.toString());
    JMethod _jMethod_3 = new JMethod();
    JMethod _withVisibilityModifier_3 = _jMethod_3.withVisibilityModifier("public");
    JMethod _withReturnType_3 = _withVisibilityModifier_3.withReturnType("String");
    JMethod _withName_3 = _withReturnType_3.withName("getFileName");
    StringConcatenation _builder_3 = new StringConcatenation();
    _builder_3.append("return \"");
    String _safeName = this.entity.getSafeName();
    _builder_3.append(_safeName, "");
    _builder_3.append(".jmx\";");
    _builder_3.newLineIfNotEmpty();
    JMethod _withImplementation_3 = _withName_3.withImplementation(_builder_3.toString());
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation, _withImplementation_1, _withImplementation_2, _withImplementation_3));
  }
  
  @Override
  public String filePath() {
    String _safeName = this.entity.getSafeName();
    String _plus = ("/src/usagescenarios/" + _safeName);
    return (_plus + ".java");
  }
}
