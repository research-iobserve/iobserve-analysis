package org.palladiosimulator.protocom.tech.rmi.usage;

import java.util.Collection;
import java.util.Collections;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.tech.rmi.PojoClass;

/**
 * @author Thomas Zolynski, Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PojoUsageClosedScenarioThread extends PojoClass<UsageScenario> {
  public PojoUsageClosedScenarioThread(final UsageScenario pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public String superClass() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("org.palladiosimulator.protocom.framework.java.se.usage.AbstractClosedScenarioThread");
    return _builder.toString();
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    JMethod _jMethod = new JMethod();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("de.uka.ipd.sdq.sensorframework.entities.Experiment exp, de.uka.ipd.sdq.sensorframework.entities.ExperimentRun expRun, org.palladiosimulator.protocom.framework.java.se.utils.RunProperties runProps");
    JMethod _withParameters = _jMethod.withParameters(_builder.toString());
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("super(exp, expRun, \"Response Time of ");
    String _entityName = this.pcmEntity.getEntityName();
    _builder_1.append(_entityName, "");
    _builder_1.append("\", runProps);");
    JMethod _withImplementation = _withParameters.withImplementation(_builder_1.toString());
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation));
  }
  
  @Override
  public String compilationUnitName() {
    String _javaName = JavaNames.javaName(this.pcmEntity);
    return (_javaName + "Thread");
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    JMethod _jMethod = new JMethod();
    JMethod _withName = _jMethod.withName("getScenarioRunner");
    JMethod _withParameters = _withName.withParameters("org.palladiosimulator.protocom.framework.java.se.utils.RunProperties runProps");
    JMethod _withReturnType = _withParameters.withReturnType("Runnable");
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if (runProps.hasOption(\'R\')) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.setRemoteAddress(runProps.getOptionValue(\'R\'));");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("else {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.setRemoteAddress(org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.LOCALHOST);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("return new ");
    String _fqn = JavaNames.fqn(this.pcmEntity);
    _builder.append(_fqn, "");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    JMethod _withImplementation = _withReturnType.withImplementation(_builder.toString());
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation));
  }
  
  @Override
  public String filePath() {
    String _implementationPackage = JavaNames.implementationPackage(this.pcmEntity);
    String _fqnToDirectoryPath = JavaNames.fqnToDirectoryPath(_implementationPackage);
    String _plus = ("/src/" + _fqnToDirectoryPath);
    String _plus_1 = (_plus + "/");
    String _javaName = JavaNames.javaName(this.pcmEntity);
    String _plus_2 = (_plus_1 + _javaName);
    return (_plus_2 + "Thread.java");
  }
}
