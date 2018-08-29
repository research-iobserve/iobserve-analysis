package org.palladiosimulator.protocom.tech.servlet.util;

import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.palladiosimulator.pcm.parameter.VariableUsage;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Signature;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.ForkedBehaviour;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.PcmCommons;
import org.palladiosimulator.protocom.model.repository.OperationRequiredRoleAdapter;
import org.palladiosimulator.protocom.model.repository.SignatureAdapter;
import org.palladiosimulator.protocom.model.seff.AcquireActionAdapter;
import org.palladiosimulator.protocom.model.seff.ActionAdapter;
import org.palladiosimulator.protocom.model.seff.BranchActionAdapter;
import org.palladiosimulator.protocom.model.seff.BranchTransitionAdapter;
import org.palladiosimulator.protocom.model.seff.ExternalCallActionAdapter;
import org.palladiosimulator.protocom.model.seff.ForkActionAdapter;
import org.palladiosimulator.protocom.model.seff.GuardedBranchTransitionAdapter;
import org.palladiosimulator.protocom.model.seff.InternalActionAdapter;
import org.palladiosimulator.protocom.model.seff.LoopActionAdapter;
import org.palladiosimulator.protocom.model.seff.ParametricResourceDemandAdapter;
import org.palladiosimulator.protocom.model.seff.ProbabilisticBranchTransitionAdapter;
import org.palladiosimulator.protocom.model.seff.ReleaseActionAdapter;
import org.palladiosimulator.protocom.model.seff.StartActionAdapter;
import org.palladiosimulator.protocom.model.seff.StopActionAdapter;

/**
 * @author Christian Klaussner
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class PcmServletProtoAction {
  protected final String frameworkBase = "org.palladiosimulator.protocom.framework.java.ee";
  
  public String actions(final ActionAdapter<? extends AbstractAction> action) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* ");
    AbstractAction _entity = action.getEntity();
    Class<? extends AbstractAction> _class = _entity.getClass();
    String _simpleName = _class.getSimpleName();
    _builder.append(_simpleName, " ");
    _builder.append(" (");
    AbstractAction _entity_1 = action.getEntity();
    _builder.append(_entity_1, " ");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    CharSequence _action = this.action(action);
    _builder.append(_action, "");
    _builder.newLineIfNotEmpty();
    {
      boolean _isInstance = StopActionAdapter.class.isInstance(action);
      boolean _not = (!_isInstance);
      if (_not) {
        ActionAdapter<? extends AbstractAction> _successor = action.getSuccessor();
        CharSequence _action_1 = this.action(_successor);
        _builder.append(_action_1, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  private CharSequence _action(final BranchActionAdapter action) {
    StringConcatenation _builder = new StringConcatenation();
    {
      List<BranchTransitionAdapter<? extends AbstractBranchTransition>> _branchTransitions = action.getBranchTransitions();
      BranchTransitionAdapter<? extends AbstractBranchTransition> _get = _branchTransitions.get(0);
      boolean _isInstance = ProbabilisticBranchTransitionAdapter.class.isInstance(_get);
      if (_isInstance) {
        _builder.append("double u");
        String _id = action.getId();
        String _javaVariableName = JavaNames.javaVariableName(_id);
        _builder.append(_javaVariableName, "");
        _builder.append(" = (Double) ctx.evaluate(\"DoublePDF[(1.0;1.0)]\", Double.class);");
        _builder.newLineIfNotEmpty();
        _builder.append("double sum");
        String _id_1 = action.getId();
        String _javaVariableName_1 = JavaNames.javaVariableName(_id_1);
        _builder.append(_javaVariableName_1, "");
        _builder.append(" = 0;");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        {
          List<BranchTransitionAdapter<? extends AbstractBranchTransition>> _branchTransitions_1 = action.getBranchTransitions();
          for(final BranchTransitionAdapter<? extends AbstractBranchTransition> transition : _branchTransitions_1) {
            CharSequence _branchTransition = this.branchTransition(action, transition);
            _builder.append(_branchTransition, "");
            _builder.newLineIfNotEmpty();
          }
        }
      } else {
        {
          List<BranchTransitionAdapter<? extends AbstractBranchTransition>> _branchTransitions_2 = action.getBranchTransitions();
          boolean _hasElements = false;
          for(final BranchTransitionAdapter<? extends AbstractBranchTransition> transition_1 : _branchTransitions_2) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(" else ", "");
            }
            CharSequence _branchTransition_1 = this.branchTransition(action, transition_1);
            _builder.append(_branchTransition_1, "");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  private CharSequence _branchTransition(final BranchActionAdapter action, final ProbabilisticBranchTransitionAdapter transition) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if (sum");
    String _id = action.getId();
    String _javaVariableName = JavaNames.javaVariableName(_id);
    _builder.append(_javaVariableName, "");
    _builder.append(" <= u");
    String _id_1 = action.getId();
    String _javaVariableName_1 = JavaNames.javaVariableName(_id_1);
    String _javaVariableName_2 = JavaNames.javaVariableName(_javaVariableName_1);
    _builder.append(_javaVariableName_2, "");
    _builder.append(" && u");
    String _id_2 = action.getId();
    String _javaVariableName_3 = JavaNames.javaVariableName(_id_2);
    String _javaVariableName_4 = JavaNames.javaVariableName(_javaVariableName_3);
    _builder.append(_javaVariableName_4, "");
    _builder.append(" < sum");
    String _id_3 = action.getId();
    String _javaVariableName_5 = JavaNames.javaVariableName(_id_3);
    String _javaVariableName_6 = JavaNames.javaVariableName(_javaVariableName_5);
    _builder.append(_javaVariableName_6, "");
    _builder.append(" + ");
    double _probability = transition.getProbability();
    _builder.append(_probability, "");
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    StartActionAdapter _start = transition.getStart();
    String _actions = this.actions(_start);
    _builder.append(_actions, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence _branchTransition(final BranchActionAdapter action, final GuardedBranchTransitionAdapter transition) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if (ctx.evaluate(\"");
    String _condition = transition.getCondition();
    String _safeSpecification = action.safeSpecification(_condition);
    _builder.append(_safeSpecification, "");
    _builder.append("\", Boolean.class) == true) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    StartActionAdapter _start = transition.getStart();
    String _actions = this.actions(_start);
    _builder.append(_actions, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence _action(final ForkActionAdapter action) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ForkedBehaviour> _asynchronousForkedBehaviours = action.getAsynchronousForkedBehaviours();
      for(final ForkedBehaviour forkedBehaviour : _asynchronousForkedBehaviours) {
        _builder.append("FIXME Add fork with ID ");
        String _id = forkedBehaviour.getId();
        _builder.append(_id, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  private CharSequence _action(final ExternalCallActionAdapter action) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("try {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("// Start Simulate an external call");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("// de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> currentFrame = ctx.getStack().currentStackFrame();");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("// prepare stackframe");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("// de.uka.ipd.sdq.simucomframework.variables.stackframe.SimulatedStackframe<Object> stackframe = ctx.getStack().createAndPushNewStackFrame();");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("ctx.getStack().createAndPushNewStackFrame();");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    SignatureAdapter _calledService = action.getCalledService();
    Signature _entity = _calledService.getEntity();
    SignatureAdapter _calledService_1 = action.getCalledService();
    Signature _entity_1 = _calledService_1.getEntity();
    OperationRequiredRoleAdapter _role = action.getRole();
    String _safeName = _role.getSafeName();
    String _plus = ("context.getPortFor" + _safeName);
    String _plus_1 = (_plus + "().");
    EList<VariableUsage> _inputVariableUsages = action.getInputVariableUsages();
    EList<VariableUsage> _returnVariableUsage = action.getReturnVariableUsage();
    String _call = PcmCommons.call(
      ((OperationSignature) _entity), 
      ((OperationSignature) _entity_1), _plus_1, _inputVariableUsages, _returnVariableUsage);
    _builder.append(_call, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("} catch (java.lang.Exception e) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("// TODO: add logging");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("} finally {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("ctx.getStack().removeStackFrame();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence _action(final InternalActionAdapter action) {
    StringConcatenation _builder = new StringConcatenation();
    {
      List<ParametricResourceDemandAdapter> _resourceDemands = action.getResourceDemands();
      for(final ParametricResourceDemandAdapter resourceDemand : _resourceDemands) {
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("double demand = ctx.evaluate(\"");
        String _specification = resourceDemand.getSpecification();
        String _safeSpecification = action.safeSpecification(_specification);
        _builder.append(_safeSpecification, "\t");
        _builder.append("\", Double.class);");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        {
          String _type = resourceDemand.getType();
          boolean _matches = _type.matches("cpu");
          if (_matches) {
            _builder.append("\t");
            _builder.append(this.frameworkBase, "\t");
            _builder.append(".prototype.StrategiesRegistry.getInstance().getStrategy(org.palladiosimulator.protocom.resourcestrategies.activeresource.ResourceTypeEnum.CPU).consume(demand);");
            _builder.newLineIfNotEmpty();
          } else {
            String _type_1 = resourceDemand.getType();
            boolean _matches_1 = _type_1.matches("hdd");
            if (_matches_1) {
              _builder.append("\t");
              _builder.append(this.frameworkBase, "\t");
              _builder.append(".prototype.StrategiesRegistry.getInstance().getStrategy(org.palladiosimulator.protocom.resourcestrategies.activeresource.ResourceTypeEnum.HDD).consume(demand);");
              _builder.newLineIfNotEmpty();
            } else {
              String _type_2 = resourceDemand.getType();
              boolean _matches_2 = _type_2.matches("delay");
              if (_matches_2) {
              } else {
                _builder.append("\t");
                _builder.append("throw new java.lang.UnsupportedOperationException(\"Resource type not yet supported\");");
                _builder.newLine();
              }
            }
          }
        }
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  private CharSequence _action(final LoopActionAdapter action) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("int maxIterationCount");
    String _id = action.getId();
    String _javaVariableName = JavaNames.javaVariableName(_id);
    _builder.append(_javaVariableName, "");
    _builder.append(" = ctx.evaluate(\"");
    String _iterationCount = action.getIterationCount();
    String _safeSpecification = action.safeSpecification(_iterationCount);
    _builder.append(_safeSpecification, "");
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
    _builder.append("++) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    StartActionAdapter _start = action.getStart();
    String _actions = this.actions(_start);
    _builder.append(_actions, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence _action(final AcquireActionAdapter action) {
    return "FIXME AcquireActions not supported yet";
  }
  
  private CharSequence _action(final ReleaseActionAdapter action) {
    return "FIXME ReleaseActions not supported yet";
  }
  
  private CharSequence _action(final StartActionAdapter action) {
    return "";
  }
  
  private CharSequence _action(final StopActionAdapter action) {
    return "";
  }
  
  private CharSequence action(final ActionAdapter<? extends AbstractAction> action) {
    if (action instanceof AcquireActionAdapter) {
      return _action((AcquireActionAdapter)action);
    } else if (action instanceof BranchActionAdapter) {
      return _action((BranchActionAdapter)action);
    } else if (action instanceof ExternalCallActionAdapter) {
      return _action((ExternalCallActionAdapter)action);
    } else if (action instanceof ForkActionAdapter) {
      return _action((ForkActionAdapter)action);
    } else if (action instanceof InternalActionAdapter) {
      return _action((InternalActionAdapter)action);
    } else if (action instanceof LoopActionAdapter) {
      return _action((LoopActionAdapter)action);
    } else if (action instanceof ReleaseActionAdapter) {
      return _action((ReleaseActionAdapter)action);
    } else if (action instanceof StartActionAdapter) {
      return _action((StartActionAdapter)action);
    } else if (action instanceof StopActionAdapter) {
      return _action((StopActionAdapter)action);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(action).toString());
    }
  }
  
  private CharSequence branchTransition(final BranchActionAdapter action, final BranchTransitionAdapter<? extends AbstractBranchTransition> transition) {
    if (transition instanceof GuardedBranchTransitionAdapter) {
      return _branchTransition(action, (GuardedBranchTransitionAdapter)transition);
    } else if (transition instanceof ProbabilisticBranchTransitionAdapter) {
      return _branchTransition(action, (ProbabilisticBranchTransitionAdapter)transition);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(action, transition).toString());
    }
  }
}
