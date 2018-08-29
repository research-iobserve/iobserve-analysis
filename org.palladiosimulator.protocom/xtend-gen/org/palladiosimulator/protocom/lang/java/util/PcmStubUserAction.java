package org.palladiosimulator.protocom.lang.java.util;

import java.util.Arrays;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.pcm.parameter.VariableUsage;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.Delay;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.PcmCommons;
import org.palladiosimulator.protocom.lang.java.util.PcmUserAction;

/**
 * Defines templates for actions of both kinds: SEFF actions and user actions.
 * 
 * 
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PcmStubUserAction extends PcmUserAction {
  /**
   * EntryLevelSystemCall is an user action which calls a system service from an usage scenario.
   */
  @Override
  protected String _userAction(final EntryLevelSystemCall userAction) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("try {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// EntryLevelSystemCall!");
    _builder.newLine();
    _builder.append("\t");
    OperationSignature _operationSignature__EntryLevelSystemCall = userAction.getOperationSignature__EntryLevelSystemCall();
    OperationProvidedRole _providedRole_EntryLevelSystemCall = userAction.getProvidedRole_EntryLevelSystemCall();
    String _portMemberVar = JavaNames.portMemberVar(_providedRole_EntryLevelSystemCall);
    String _plus = (_portMemberVar + ".");
    EList<VariableUsage> _inputParameterUsages_EntryLevelSystemCall = userAction.getInputParameterUsages_EntryLevelSystemCall();
    String _call = PcmCommons.call(_operationSignature__EntryLevelSystemCall, 
      null, _plus, _inputParameterUsages_EntryLevelSystemCall, 
      null);
    _builder.append(_call, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t\t\t\t\t\t");
    _builder.newLine();
    _builder.append("} catch (java.rmi.RemoteException e) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// TODO: Logger!");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  /**
   * Loop actions are transformed into a simple FOR statement.
   */
  @Override
  protected String _userAction(final Loop userAction) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// TODO Configure maxIterationCount ");
    _builder.newLine();
    _builder.append("int maxIterationCount");
    String _id = userAction.getId();
    String _javaVariableName = JavaNames.javaVariableName(_id);
    _builder.append(_javaVariableName, "");
    _builder.append(" = 1;");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("for (int iterationCount_");
    String _id_1 = userAction.getId();
    String _javaVariableName_1 = JavaNames.javaVariableName(_id_1);
    _builder.append(_javaVariableName_1, "");
    _builder.append(" = 0; ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("iterationCount_");
    String _id_2 = userAction.getId();
    String _javaVariableName_2 = JavaNames.javaVariableName(_id_2);
    _builder.append(_javaVariableName_2, "\t");
    _builder.append(" < maxIterationCount_");
    String _id_3 = userAction.getId();
    String _javaVariableName_3 = JavaNames.javaVariableName(_id_3);
    _builder.append(_javaVariableName_3, "\t");
    _builder.append("; iterationCount_");
    String _id_4 = userAction.getId();
    String _javaVariableName_4 = JavaNames.javaVariableName(_id_4);
    _builder.append(_javaVariableName_4, "\t");
    _builder.append(" ++) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    ScenarioBehaviour _bodyBehaviour_Loop = userAction.getBodyBehaviour_Loop();
    EList<AbstractUserAction> _actions_ScenarioBehaviour = _bodyBehaviour_Loop.getActions_ScenarioBehaviour();
    Start _findUserStart = PcmUserAction.findUserStart(_actions_ScenarioBehaviour);
    String _userActions = this.userActions(_findUserStart);
    _builder.append(_userActions, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("} ");
    _builder.newLine();
    return _builder.toString();
  }
  
  /**
   * UserActions only have probabilistic transitions. That might even make sense for test generation
   * for code stubs. Therefore, This might be added here.
   * 
   * TODO Branch user actions are currently not generated with code stubs. AS probabilities make sense here the ctx object might be useful. In any case, for test generation, user actions should be included in future.
   */
  @Override
  protected String _userAction(final Branch userAction) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("double u");
    String _id = userAction.getId();
    String _javaVariableName = JavaNames.javaVariableName(_id);
    _builder.append(_javaVariableName, "");
    _builder.append(" = ctx.evaluate(\"DoublePDF[(1.0;1.0)]\", Double.class);");
    _builder.newLineIfNotEmpty();
    _builder.append("double sum");
    String _id_1 = userAction.getId();
    String _javaVariableName_1 = JavaNames.javaVariableName(_id_1);
    _builder.append(_javaVariableName_1, "");
    _builder.append(" = 0;");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      EList<BranchTransition> _branchTransitions_Branch = userAction.getBranchTransitions_Branch();
      for(final BranchTransition transition : _branchTransitions_Branch) {
        _builder.append("if (sum");
        String _id_2 = userAction.getId();
        String _javaVariableName_2 = JavaNames.javaVariableName(_id_2);
        _builder.append(_javaVariableName_2, "");
        _builder.append(" <= u");
        String _id_3 = userAction.getId();
        String _javaVariableName_3 = JavaNames.javaVariableName(_id_3);
        _builder.append(_javaVariableName_3, "");
        _builder.append(" && u");
        String _id_4 = userAction.getId();
        String _javaVariableName_4 = JavaNames.javaVariableName(_id_4);
        _builder.append(_javaVariableName_4, "");
        _builder.append(" < sum");
        String _id_5 = userAction.getId();
        String _javaVariableName_5 = JavaNames.javaVariableName(_id_5);
        _builder.append(_javaVariableName_5, "");
        _builder.append(" + ");
        double _branchProbability = transition.getBranchProbability();
        _builder.append(_branchProbability, "");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        ScenarioBehaviour _branchedBehaviour_BranchTransition = transition.getBranchedBehaviour_BranchTransition();
        EList<AbstractUserAction> _actions_ScenarioBehaviour = _branchedBehaviour_BranchTransition.getActions_ScenarioBehaviour();
        Start _findUserStart = PcmUserAction.findUserStart(_actions_ScenarioBehaviour);
        String _userActions = this.userActions(_findUserStart);
        _builder.append(_userActions, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}\t");
        _builder.newLine();
        _builder.append("sum");
        String _id_6 = userAction.getId();
        String _javaVariableName_6 = JavaNames.javaVariableName(_id_6);
        _builder.append(_javaVariableName_6, "");
        _builder.append(" += ");
        double _branchProbability_1 = transition.getBranchProbability();
        _builder.append(_branchProbability_1, "");
        _builder.append(";\t");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  public String userAction(final AbstractUserAction userAction) {
    if (userAction instanceof Branch) {
      return _userAction((Branch)userAction);
    } else if (userAction instanceof Delay) {
      return _userAction((Delay)userAction);
    } else if (userAction instanceof EntryLevelSystemCall) {
      return _userAction((EntryLevelSystemCall)userAction);
    } else if (userAction instanceof Loop) {
      return _userAction((Loop)userAction);
    } else if (userAction instanceof Start) {
      return _userAction((Start)userAction);
    } else if (userAction instanceof Stop) {
      return _userAction((Stop)userAction);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(userAction).toString());
    }
  }
}
