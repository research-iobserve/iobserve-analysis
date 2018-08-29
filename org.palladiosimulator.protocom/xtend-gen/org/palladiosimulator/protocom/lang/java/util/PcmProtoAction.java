package org.palladiosimulator.protocom.lang.java.util;

import java.util.Arrays;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.parameter.VariableUsage;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
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
import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.PcmAction;
import org.palladiosimulator.protocom.lang.java.util.PcmCommons;

/**
 * Defines templates for SEFF actions.
 * 
 * @author Thomas Zolynski, Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PcmProtoAction extends PcmAction {
  /**
   * No idea. We didn't implement this for the last ProtoCom either.
   * FIXME Implement this as it is a crucial part of the bahavior (lehrig)
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
    _builder.append("int maxIterationCount");
    String _id = action.getId();
    String _javaVariableName = JavaNames.javaVariableName(_id);
    _builder.append(_javaVariableName, "");
    _builder.append(" = ctx.evaluate(\"");
    PCMRandomVariable _iterationCount_LoopAction = action.getIterationCount_LoopAction();
    String _specification = _iterationCount_LoopAction.getSpecification();
    String _specificationString = JavaNames.specificationString(_specification);
    _builder.append(_specificationString, "");
    _builder.append("\", Integer.class);");
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
    _builder.append("// Start Simulate an external call");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> currentFrame = ctx.getStack().currentStackFrame();");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("// prepare stackframe");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> stackframe = ctx.getStack().createAndPushNewStackFrame();");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    OperationSignature _calledService_ExternalService = action.getCalledService_ExternalService();
    OperationSignature _calledService_ExternalService_1 = action.getCalledService_ExternalService();
    OperationRequiredRole _role_ExternalService = action.getRole_ExternalService();
    String _javaName = JavaNames.javaName(_role_ExternalService);
    String _plus = ("myContext.getRole" + _javaName);
    String _plus_1 = (_plus + "().");
    EList<VariableUsage> _inputVariableUsages__CallAction = action.getInputVariableUsages__CallAction();
    EList<VariableUsage> _returnVariableUsage__CallReturnAction = action.getReturnVariableUsage__CallReturnAction();
    String _call = PcmCommons.call(_calledService_ExternalService, _calledService_ExternalService_1, _plus_1, _inputVariableUsages__CallAction, _returnVariableUsage__CallReturnAction);
    _builder.append(_call, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("ctx.getStack().removeStackFrame();\t\t\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  /**
   * InteralAction uses a load generator to simulate CPU/HDD usage.
   * Note that ProtoCom does NOT use InfrastructureCalls from the PCM model, because these
   * should be reflected by the underlying middleware and OS!
   */
  @Override
  protected String _action(final InternalAction action) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ParametricResourceDemand> _resourceDemand_Action = action.getResourceDemand_Action();
      for(final ParametricResourceDemand resourceDemand : _resourceDemand_Action) {
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("double demand = ctx.evaluate(\"");
        PCMRandomVariable _specification_ParametericResourceDemand = resourceDemand.getSpecification_ParametericResourceDemand();
        String _specification = _specification_ParametericResourceDemand.getSpecification();
        String _specificationString = JavaNames.specificationString(_specification);
        _builder.append(_specificationString, "\t");
        _builder.append("\", Double.class);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        {
          ProcessingResourceType _requiredResource_ParametricResourceDemand = resourceDemand.getRequiredResource_ParametricResourceDemand();
          String _entityName = _requiredResource_ParametricResourceDemand.getEntityName();
          String _lowerCase = _entityName.toLowerCase();
          boolean _matches = _lowerCase.matches("cpu");
          if (_matches) {
            _builder.append("\t");
            _builder.append("org.palladiosimulator.protocom.framework.java.se.strategies.DemandConsumerStrategiesRegistry.singleton().getStrategyFor(org.palladiosimulator.protocom.resourcestrategies.activeresource.ResourceTypeEnum.CPU).consume(demand);");
            _builder.newLine();
          } else {
            ProcessingResourceType _requiredResource_ParametricResourceDemand_1 = resourceDemand.getRequiredResource_ParametricResourceDemand();
            String _entityName_1 = _requiredResource_ParametricResourceDemand_1.getEntityName();
            String _lowerCase_1 = _entityName_1.toLowerCase();
            boolean _matches_1 = _lowerCase_1.matches("hdd");
            if (_matches_1) {
              _builder.append("\t");
              _builder.append("org.palladiosimulator.protocom.framework.java.se.strategies.DemandConsumerStrategiesRegistry.singleton().getStrategyFor(org.palladiosimulator.protocom.resourcestrategies.activeresource.ResourceTypeEnum.HDD).consume(demand);");
              _builder.newLine();
            } else {
              ProcessingResourceType _requiredResource_ParametricResourceDemand_2 = resourceDemand.getRequiredResource_ParametricResourceDemand();
              String _entityName_2 = _requiredResource_ParametricResourceDemand_2.getEntityName();
              String _lowerCase_2 = _entityName_2.toLowerCase();
              boolean _matches_2 = _lowerCase_2.matches("delay");
              if (_matches_2) {
                _builder.append("\t");
                _builder.append("org.palladiosimulator.protocom.framework.AbstractResourceEnvironment.performDelay(demand);");
                _builder.newLine();
              } else {
                _builder.append("\t");
                _builder.append("throw new java.lang.UnsupportedOperationException(\"Resourcetype not yet supported in prototype\");");
                _builder.newLine();
              }
            }
          }
        }
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder.toString();
  }
  
  /**
   * BranchAction is implemented as an IF condition. A BranchAction may have two different transition types:
   * ProbabilisticBranchTransition and GuardedBranchTransition.
   */
  @Override
  protected String _action(final BranchAction action) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<AbstractBranchTransition> _branches_Branch = action.getBranches_Branch();
      AbstractBranchTransition _get = _branches_Branch.get(0);
      boolean _isInstance = ProbabilisticBranchTransition.class.isInstance(_get);
      if (_isInstance) {
        _builder.append("double u");
        String _id = action.getId();
        String _javaVariableName = JavaNames.javaVariableName(_id);
        String _javaVariableName_1 = JavaNames.javaVariableName(_javaVariableName);
        _builder.append(_javaVariableName_1, "");
        _builder.append(" = (Double) ctx.evaluate(\"DoublePDF[(1.0;1.0)]\", Double.class);");
        _builder.newLineIfNotEmpty();
        _builder.append("double sum");
        String _id_1 = action.getId();
        String _javaVariableName_2 = JavaNames.javaVariableName(_id_1);
        String _javaVariableName_3 = JavaNames.javaVariableName(_javaVariableName_2);
        _builder.append(_javaVariableName_3, "");
        _builder.append(" = 0;");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        {
          EList<AbstractBranchTransition> _branches_Branch_1 = action.getBranches_Branch();
          for(final AbstractBranchTransition branch : _branches_Branch_1) {
            CharSequence _branchTransition = this.branchTransition(action, branch);
            _builder.append(_branchTransition, "");
            _builder.newLineIfNotEmpty();
          }
        }
      } else {
        {
          EList<AbstractBranchTransition> _branches_Branch_2 = action.getBranches_Branch();
          boolean _hasElements = false;
          for(final AbstractBranchTransition branch_1 : _branches_Branch_2) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(" else ", "");
            }
            CharSequence _branchTransition_1 = this.branchTransition(action, branch_1);
            _builder.append(_branchTransition_1, "");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder.toString();
  }
  
  /**
   * Branch transition for ProbabilisticBranchTransition entities.
   */
  @Override
  protected CharSequence _branchTransition(final BranchAction action, final ProbabilisticBranchTransition transition) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if (sum");
    String _id = action.getId();
    String _javaVariableName = JavaNames.javaVariableName(_id);
    _builder.append(_javaVariableName, "");
    _builder.append(" <= u");
    String _id_1 = action.getId();
    String _javaVariableName_1 = JavaNames.javaVariableName(_id_1);
    _builder.append(_javaVariableName_1, "");
    _builder.append(" && u");
    String _id_2 = action.getId();
    String _javaVariableName_2 = JavaNames.javaVariableName(_id_2);
    _builder.append(_javaVariableName_2, "");
    _builder.append(" < sum");
    String _id_3 = action.getId();
    String _javaVariableName_3 = JavaNames.javaVariableName(_id_3);
    _builder.append(_javaVariableName_3, "");
    _builder.append(" + ");
    double _branchProbability = transition.getBranchProbability();
    _builder.append(_branchProbability, "");
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
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
    _builder.append("if (ctx.evaluate(\"");
    PCMRandomVariable _branchCondition_GuardedBranchTransition = transition.getBranchCondition_GuardedBranchTransition();
    String _specification = _branchCondition_GuardedBranchTransition.getSpecification();
    String _specificationString = JavaNames.specificationString(_specification);
    _builder.append(_specificationString, "");
    _builder.append("\", Boolean.class) == true) {");
    _builder.newLineIfNotEmpty();
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
