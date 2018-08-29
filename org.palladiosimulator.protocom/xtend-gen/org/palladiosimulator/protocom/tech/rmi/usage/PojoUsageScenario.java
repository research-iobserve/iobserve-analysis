package org.palladiosimulator.protocom.tech.rmi.usage;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.protocom.lang.java.IJField;
import org.palladiosimulator.protocom.lang.java.IJMethod;
import org.palladiosimulator.protocom.lang.java.impl.JField;
import org.palladiosimulator.protocom.lang.java.impl.JMethod;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.PcmCalls;
import org.palladiosimulator.protocom.lang.java.util.PcmCommons;
import org.palladiosimulator.protocom.tech.rmi.PojoClass;
import org.palladiosimulator.protocom.tech.rmi.util.PcmRMIProtoUserAction;

/**
 * @author Thomas Zolynski, Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PojoUsageScenario extends PojoClass<UsageScenario> {
  public PojoUsageScenario(final UsageScenario pcmEntity) {
    super(pcmEntity);
  }
  
  @Override
  public Collection<? extends IJMethod> constructors() {
    JMethod _jMethod = new JMethod();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("ctx = new de.uka.ipd.sdq.simucomframework.variables.StackContext();");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    {
      ScenarioBehaviour _scenarioBehaviour_UsageScenario = this.pcmEntity.getScenarioBehaviour_UsageScenario();
      Iterable<EntryLevelSystemCall> _querySystemCalls = PcmCalls.querySystemCalls(_scenarioBehaviour_UsageScenario);
      final Function1<EntryLevelSystemCall, OperationProvidedRole> _function = (EntryLevelSystemCall it) -> {
        return it.getProvidedRole_EntryLevelSystemCall();
      };
      Iterable<OperationProvidedRole> _map = IterableExtensions.<EntryLevelSystemCall, OperationProvidedRole>map(_querySystemCalls, _function);
      Set<OperationProvidedRole> _set = IterableExtensions.<OperationProvidedRole>toSet(_map);
      for(final OperationProvidedRole providedRole : _set) {
        _builder.append("\t");
        String _contextInit = this.contextInit(providedRole);
        _builder.append(_contextInit, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("expRun = org.palladiosimulator.protocom.framework.java.se.experiment.ExperimentManager.getLatestExperimentRun();");
    _builder.newLine();
    _builder.append("ctx.getStack().createAndPushNewStackFrame();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("de.uka.ipd.sdq.probfunction.math.IProbabilityFunctionFactory probFunctionFactory = de.uka.ipd.sdq.probfunction.math.impl.ProbabilityFunctionFactoryImpl.getInstance();");
    _builder.newLine();
    _builder.newLine();
    _builder.append("probFunctionFactory.setRandomGenerator(new de.uka.ipd.sdq.probfunction.math.impl.DefaultRandomGenerator());");
    _builder.newLine();
    _builder.append("de.uka.ipd.sdq.simucomframework.variables.cache.StoExCache.initialiseStoExCache(probFunctionFactory);");
    _builder.newLine();
    JMethod _withImplementation = _jMethod.withImplementation(_builder.toString());
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation));
  }
  
  public String contextInit(final OperationProvidedRole role) {
    StringConcatenation _builder = new StringConcatenation();
    String _portMemberVar = JavaNames.portMemberVar(role);
    _builder.append(_portMemberVar, "");
    _builder.append(" = (");
    OperationInterface _providedInterface__OperationProvidedRole = role.getProvidedInterface__OperationProvidedRole();
    String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
    _builder.append(_fqn, "");
    _builder.append(")org.palladiosimulator.protocom.framework.java.se.registry.RmiRegistry.lookup(\"");
    String _portClassName = JavaNames.portClassName(role);
    _builder.append(_portClassName, "");
    _builder.append("_\");");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  @Override
  public Collection<String> interfaces() {
    return Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("java.lang.Runnable"));
  }
  
  @Override
  public Collection<? extends IJMethod> methods() {
    JMethod _jMethod = new JMethod();
    JMethod _withName = _jMethod.withName("run");
    JMethod _withImplementation = _withName.withImplementation("scenarioRunner();");
    JMethod _jMethod_1 = new JMethod();
    JMethod _withName_1 = _jMethod_1.withName("scenarioRunner");
    StringConcatenation _builder = new StringConcatenation();
    PcmRMIProtoUserAction _pcmRMIProtoUserAction = new PcmRMIProtoUserAction();
    ScenarioBehaviour _scenarioBehaviour_UsageScenario = this.pcmEntity.getScenarioBehaviour_UsageScenario();
    EList<AbstractUserAction> _actions_ScenarioBehaviour = _scenarioBehaviour_UsageScenario.getActions_ScenarioBehaviour();
    final Function1<AbstractUserAction, Boolean> _function = (AbstractUserAction it) -> {
      return Boolean.valueOf(Start.class.isInstance(it));
    };
    Iterable<AbstractUserAction> _filter = IterableExtensions.<AbstractUserAction>filter(_actions_ScenarioBehaviour, _function);
    AbstractUserAction _get = ((AbstractUserAction[])Conversions.unwrapArray(_filter, AbstractUserAction.class))[0];
    String _userActions = _pcmRMIProtoUserAction.userActions(_get);
    _builder.append(_userActions, "");
    _builder.newLineIfNotEmpty();
    JMethod _withImplementation_1 = _withName_1.withImplementation(_builder.toString());
    return Collections.<IJMethod>unmodifiableList(CollectionLiterals.<IJMethod>newArrayList(_withImplementation, _withImplementation_1));
  }
  
  @Override
  public Collection<? extends IJField> fields() {
    LinkedList<JField> _xblockexpression = null;
    {
      final LinkedList<JField> results = CollectionLiterals.<JField>newLinkedList();
      JField _jField = new JField();
      JField _withName = _jField.withName("expRun");
      JField _withType = _withName.withType("de.uka.ipd.sdq.sensorframework.entities.ExperimentRun");
      results.add(_withType);
      JField _jField_1 = new JField();
      JField _withName_1 = _jField_1.withName("ctx");
      String _stackContextClass = PcmCommons.stackContextClass();
      JField _withType_1 = _withName_1.withType(_stackContextClass);
      results.add(_withType_1);
      ScenarioBehaviour _scenarioBehaviour_UsageScenario = this.pcmEntity.getScenarioBehaviour_UsageScenario();
      Iterable<EntryLevelSystemCall> _querySystemCalls = PcmCalls.querySystemCalls(_scenarioBehaviour_UsageScenario);
      final Function1<EntryLevelSystemCall, OperationProvidedRole> _function = (EntryLevelSystemCall it) -> {
        return it.getProvidedRole_EntryLevelSystemCall();
      };
      Iterable<OperationProvidedRole> _map = IterableExtensions.<EntryLevelSystemCall, OperationProvidedRole>map(_querySystemCalls, _function);
      Set<OperationProvidedRole> _set = IterableExtensions.<OperationProvidedRole>toSet(_map);
      final Function1<OperationProvidedRole, JField> _function_1 = (OperationProvidedRole it) -> {
        JField _jField_2 = new JField();
        String _portMemberVar = JavaNames.portMemberVar(((OperationProvidedRole) it));
        JField _withName_2 = _jField_2.withName(_portMemberVar);
        OperationInterface _providedInterface__OperationProvidedRole = ((OperationProvidedRole) it).getProvidedInterface__OperationProvidedRole();
        String _fqn = JavaNames.fqn(_providedInterface__OperationProvidedRole);
        return _withName_2.withType(_fqn);
      };
      Iterable<JField> _map_1 = IterableExtensions.<OperationProvidedRole, JField>map(_set, _function_1);
      Iterables.<JField>addAll(results, _map_1);
      _xblockexpression = results;
    }
    return _xblockexpression;
  }
  
  @Override
  public String filePath() {
    String _implementationPackage = JavaNames.implementationPackage(this.pcmEntity);
    String _fqnToDirectoryPath = JavaNames.fqnToDirectoryPath(_implementationPackage);
    String _plus = ("/src/" + _fqnToDirectoryPath);
    String _plus_1 = (_plus + "/");
    String _javaName = JavaNames.javaName(this.pcmEntity);
    String _plus_2 = (_plus_1 + _javaName);
    return (_plus_2 + ".java");
  }
}
