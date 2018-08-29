package org.palladiosimulator.protocom.tech.iiop.util;

import java.util.Arrays;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.palladiosimulator.pcm.parameter.VariableUsage;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.AcquireAction;
import org.palladiosimulator.pcm.seff.BranchAction;
import org.palladiosimulator.pcm.seff.CollectionIteratorAction;
import org.palladiosimulator.pcm.seff.EmitEventAction;
import org.palladiosimulator.pcm.seff.ExternalCallAction;
import org.palladiosimulator.pcm.seff.ForkAction;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.LoopAction;
import org.palladiosimulator.pcm.seff.ReleaseAction;
import org.palladiosimulator.pcm.seff.SetVariableAction;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.pcm.seff.StopAction;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;
import org.palladiosimulator.protocom.lang.java.util.PcmCommons;
import org.palladiosimulator.protocom.lang.java.util.PcmProtoAction;

/**
 * Defines templates for actions of both kinds: SEFF actions.
 * 
 * @author Thomas Zolynski, Sebastian Lehrig, Daria Giacinto
 */
@SuppressWarnings("all")
public class PcmIIOPProtoAction extends PcmProtoAction {
  /**
   * ExternalCallAction calls a remote service.
   * 
   * TODO: Implement method body regarding java ee
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
    String _firstLower = StringExtensions.toFirstLower(_javaName);
    String _plus = (_firstLower + ".");
    EList<VariableUsage> _inputVariableUsages__CallAction = action.getInputVariableUsages__CallAction();
    EList<VariableUsage> _returnVariableUsage__CallReturnAction = action.getReturnVariableUsage__CallReturnAction();
    String _call = PcmCommons.call(_calledService_ExternalService, _calledService_ExternalService_1, _plus, _inputVariableUsages__CallAction, _returnVariableUsage__CallReturnAction);
    _builder.append(_call, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("ctx.getStack().removeStackFrame();");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
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
}
