package org.palladiosimulator.protocom.lang.java.util;

import java.util.Arrays;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.pcm.parameter.VariableUsage;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.AcquireAction;
import org.palladiosimulator.pcm.seff.BranchAction;
import org.palladiosimulator.pcm.seff.CollectionIteratorAction;
import org.palladiosimulator.pcm.seff.EmitEventAction;
import org.palladiosimulator.pcm.seff.ExternalCallAction;
import org.palladiosimulator.pcm.seff.ForkAction;
import org.palladiosimulator.pcm.seff.GuardedBranchTransition;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.LoopAction;
import org.palladiosimulator.pcm.seff.ProbabilisticBranchTransition;
import org.palladiosimulator.pcm.seff.ReleaseAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour;
import org.palladiosimulator.pcm.seff.SetVariableAction;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.pcm.seff.StopAction;
import org.palladiosimulator.protocom.lang.java.util.DataTypes;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.PcmAction;
import org.palladiosimulator.protocom.lang.java.util.PcmCommons;

/**
 * Defines templates for actions of both kinds: SEFF actions and user actions.
 * 
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PcmStubAction extends PcmAction {
  /**
   * No idea. We didn't implement this for the last ProtoCom either.
   * FIXME Implement this as it is a crucial part of the behavior (lehrig)
   */
  @Override
  protected String _action(final CollectionIteratorAction action) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * LoopAction is transformed into a simple FOR statement.
   */
  @Override
  protected String _action(final LoopAction action) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// TODO Configure maxIterationCount ");
    _builder.newLine();
    _builder.append("int maxIterationCount");
    String _id = action.getId();
    String _javaVariableName = JavaNames.javaVariableName(_id);
    _builder.append(_javaVariableName, "");
    _builder.append(" = 1;");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("for (int iterationCount");
    String _id_1 = action.getId();
    String _javaVariableName_1 = JavaNames.javaVariableName(_id_1);
    _builder.append(_javaVariableName_1, "");
    _builder.append(" = 0; iterationCount");
    String _id_2 = action.getId();
    String _javaVariableName_2 = JavaNames.javaVariableName(_id_2);
    _builder.append(_javaVariableName_2, "");
    _builder.append(" < maxIterationCount");
    String _id_3 = action.getId();
    String _javaVariableName_3 = JavaNames.javaVariableName(_id_3);
    _builder.append(_javaVariableName_3, "");
    _builder.append("; iterationCount");
    String _id_4 = action.getId();
    String _javaVariableName_4 = JavaNames.javaVariableName(_id_4);
    _builder.append(_javaVariableName_4, "");
    _builder.append(" ++) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    ResourceDemandingBehaviour _bodyBehaviour_Loop = action.getBodyBehaviour_Loop();
    EList<AbstractAction> _steps_Behaviour = _bodyBehaviour_Loop.getSteps_Behaviour();
    StartAction _findStart = PcmAction.findStart(_steps_Behaviour);
    String _actions = this.actions(_findStart);
    _builder.append(_actions, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  /**
   * ExternalCallAction calls a remote service.
   * 
   * TODO: Move exception handling to RMI tech.
   */
  @Override
  protected String _action(final ExternalCallAction action) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("// TODO Initialize parameters");
    _builder.newLine();
    {
      OperationSignature _calledService_ExternalService = action.getCalledService_ExternalService();
      EList<Parameter> _parameters__OperationSignature = _calledService_ExternalService.getParameters__OperationSignature();
      for(final Parameter parameter : _parameters__OperationSignature) {
        _builder.append("\t\t");
        DataType _dataType__Parameter = parameter.getDataType__Parameter();
        String _dataType = DataTypes.getDataType(_dataType__Parameter);
        String _plus = (_dataType + " param_");
        String _parameterName = parameter.getParameterName();
        String _plus_1 = (_plus + _parameterName);
        String _plus_2 = (_plus_1 + " = null;");
        _builder.append(_plus_2, "\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    OperationSignature _calledService_ExternalService_1 = action.getCalledService_ExternalService();
    OperationSignature _calledService_ExternalService_2 = action.getCalledService_ExternalService();
    OperationRequiredRole _role_ExternalService = action.getRole_ExternalService();
    String _javaName = JavaNames.javaName(_role_ExternalService);
    String _plus_3 = ("myContext.getRole" + _javaName);
    String _plus_4 = (_plus_3 + "().");
    EList<VariableUsage> _inputVariableUsages__CallAction = action.getInputVariableUsages__CallAction();
    EList<VariableUsage> _returnVariableUsage__CallReturnAction = action.getReturnVariableUsage__CallReturnAction();
    String _callStub = PcmCommons.callStub(_calledService_ExternalService_1, _calledService_ExternalService_2, _plus_4, _inputVariableUsages__CallAction, _returnVariableUsage__CallReturnAction);
    _builder.append(_callStub, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  /**
   * Currently, InteralActions are removed for stubs.
   */
  @Override
  protected String _action(final InternalAction action) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * BranchAction is implemented as an IF condition. A BranchAction may have two different transition types:
   * ProbabilisticBranchTransition and GuardedBranchTransition. For code stubs, they are both treated as a
   * normal if-else-if-else series.
   */
  @Override
  protected String _action(final BranchAction action) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// TODO Set condition.");
    _builder.newLine();
    _builder.append("Boolean condition = true;");
    _builder.newLine();
    {
      EList<AbstractBranchTransition> _branches_Branch = action.getBranches_Branch();
      boolean _hasElements = false;
      for(final AbstractBranchTransition branch : _branches_Branch) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(" else ", "");
        }
        CharSequence _branchTransition = this.branchTransition(action, branch);
        _builder.append(_branchTransition, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  /**
   * Branch transition for ProbabilisticBranchTransition entities. Handled like GuardedBranchTransition for code stubs.
   */
  @Override
  protected CharSequence _branchTransition(final BranchAction action, final ProbabilisticBranchTransition transition) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if (condition) {");
    _builder.newLine();
    _builder.append("\t");
    ResourceDemandingBehaviour _branchBehaviour_BranchTransition = transition.getBranchBehaviour_BranchTransition();
    EList<AbstractAction> _steps_Behaviour = _branchBehaviour_BranchTransition.getSteps_Behaviour();
    StartAction _findStart = PcmAction.findStart(_steps_Behaviour);
    String _actions = this.actions(_findStart);
    _builder.append(_actions, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  /**
   * Branch transition for GuardedBranchTransition.
   */
  @Override
  protected CharSequence _branchTransition(final BranchAction action, final GuardedBranchTransition transition) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if (condition) {");
    _builder.newLine();
    _builder.append("\t");
    ResourceDemandingBehaviour _branchBehaviour_BranchTransition = transition.getBranchBehaviour_BranchTransition();
    EList<AbstractAction> _steps_Behaviour = _branchBehaviour_BranchTransition.getSteps_Behaviour();
    StartAction _findStart = PcmAction.findStart(_steps_Behaviour);
    String _actions = this.actions(_findStart);
    _builder.append(_actions, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  /**
   * TODO Check whether SetVariableAction is needed.
   */
  @Override
  protected String _action(final SetVariableAction action) {
    return null;
  }
  
  /**
   * A ForkAction spawns a new thread for each defined behavior. These should be processed asynchronously in parallel.
   * Please note that manually spawning new threads is discouraged on certain middlewares (like JavaEE)!
   * 
   * TODO Check whether ForkAction is needed.
   */
  @Override
  protected String _action(final ForkAction action) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  public String action(final AbstractAction action) {
    if (action instanceof CollectionIteratorAction) {
      return _action((CollectionIteratorAction)action);
    } else if (action instanceof LoopAction) {
      return _action((LoopAction)action);
    } else if (action instanceof AcquireAction) {
      return _action((AcquireAction)action);
    } else if (action instanceof BranchAction) {
      return _action((BranchAction)action);
    } else if (action instanceof ExternalCallAction) {
      return _action((ExternalCallAction)action);
    } else if (action instanceof ForkAction) {
      return _action((ForkAction)action);
    } else if (action instanceof InternalAction) {
      return _action((InternalAction)action);
    } else if (action instanceof ReleaseAction) {
      return _action((ReleaseAction)action);
    } else if (action instanceof SetVariableAction) {
      return _action((SetVariableAction)action);
    } else if (action instanceof StartAction) {
      return _action((StartAction)action);
    } else if (action instanceof StopAction) {
      return _action((StopAction)action);
    } else if (action instanceof EmitEventAction) {
      return _action((EmitEventAction)action);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(action).toString());
    }
  }
  
  public CharSequence branchTransition(final BranchAction action, final AbstractBranchTransition transition) {
    if (transition instanceof GuardedBranchTransition) {
      return _branchTransition(action, (GuardedBranchTransition)transition);
    } else if (transition instanceof ProbabilisticBranchTransition) {
      return _branchTransition(action, (ProbabilisticBranchTransition)transition);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(action, transition).toString());
    }
  }
}
