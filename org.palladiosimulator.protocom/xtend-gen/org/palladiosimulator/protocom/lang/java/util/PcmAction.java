package org.palladiosimulator.protocom.lang.java.util;

import java.util.Arrays;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.repository.PassiveResource;
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
import org.palladiosimulator.pcm.seff.SetVariableAction;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.pcm.seff.StopAction;
import org.palladiosimulator.protocom.lang.java.util.JavaNames;

/**
 * Abstract class for implementing PCM actions, i.e., the behavior of components as specified
 * via SEFFs. Refinements of this class could, for instance, add simulate stack frames for performance
 * prototypes or provide code stub generation.
 * 
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public abstract class PcmAction {
  /**
   * Follows the action path and calls "action" for each action in it.
   * Note that actions do not branch! Branching is solved by a BranchAction, therefore
   * at most one successor is given at any time.
   */
  public String actions(final AbstractAction action) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* ");
    Class<? extends AbstractAction> _class = action.getClass();
    String _simpleName = _class.getSimpleName();
    _builder.append(_simpleName, " ");
    _builder.append(" (");
    _builder.append(action, " ");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    String _action = this.action(action);
    _builder.append(_action, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      boolean _isInstance = StopAction.class.isInstance(action);
      boolean _not = (!_isInstance);
      if (_not) {
        AbstractAction _successor_AbstractAction = action.getSuccessor_AbstractAction();
        String _actions = this.actions(_successor_AbstractAction);
        _builder.append(_actions, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  /**
   * StartAction. Should be empty, I guess.
   */
  protected String _action(final StartAction action) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * StopAction. Nothing to see here either.
   */
  protected String _action(final StopAction action) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * No idea. We didn't implement this for the last ProtoCom either.
   * FIXME Implement this as it is a crucial part of the bahavior (lehrig)
   */
  protected String _action(final CollectionIteratorAction action) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * LoopAction is transformed into a simple FOR statement.
   */
  protected String _action(final LoopAction action) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * ExternalCallAction calls a remote service.
   */
  protected String _action(final ExternalCallAction action) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * InteralAction uses a load generator to simulate CPU/HDD usage.
   * Note that ProtoCom does NOT use InfrastructureCalls from the PCM model, because these
   * should be reflected by the underlying middleware and OS!
   */
  protected String _action(final InternalAction action) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * BranchAction is implemented as an IF condition. A BranchAction may have two different transition types:
   * ProbabilisticBranchTransition and GuardedBranchTransition.
   */
  protected String _action(final BranchAction action) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * Branch transition for ProbabilisticBranchTransition entities.
   */
  protected CharSequence _branchTransition(final BranchAction action, final ProbabilisticBranchTransition transition) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  /**
   * Branch transition for GuardedBranchTransition.
   */
  protected CharSequence _branchTransition(final BranchAction action, final GuardedBranchTransition transition) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  /**
   * AcquireAction is mapped to the acquire method of Java Collection's semaphore implementation.
   */
  protected String _action(final AcquireAction action) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("try {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//logger.debug(\"Acquiring passive resource ");
    PassiveResource _passiveresource_AcquireAction = action.getPassiveresource_AcquireAction();
    String _entityName = _passiveresource_AcquireAction.getEntityName();
    String _javaString = JavaNames.javaString(_entityName);
    _builder.append(_javaString, "\t");
    _builder.append("\");");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("passive_resource_");
    PassiveResource _passiveresource_AcquireAction_1 = action.getPassiveresource_AcquireAction();
    String _entityName_1 = _passiveresource_AcquireAction_1.getEntityName();
    String _javaVariableName = JavaNames.javaVariableName(_entityName_1);
    _builder.append(_javaVariableName, "\t");
    _builder.append(".acquire();");
    _builder.newLineIfNotEmpty();
    _builder.append("} catch (InterruptedException e) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//logger.error(\"Should never happen: Acquire of semaphore ");
    PassiveResource _passiveresource_AcquireAction_2 = action.getPassiveresource_AcquireAction();
    String _entityName_2 = _passiveresource_AcquireAction_2.getEntityName();
    String _javaString_1 = JavaNames.javaString(_entityName_2);
    _builder.append(_javaString_1, "\t");
    _builder.append(" interrupted\");");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("java.lang.System.exit(-1);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  /**
   * ReleaseAction is mapped to the release method of Java Collection's semaphore implementation.
   */
  protected String _action(final ReleaseAction action) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// Release ");
    PassiveResource _passiveResource_ReleaseAction = action.getPassiveResource_ReleaseAction();
    _builder.append(_passiveResource_ReleaseAction, "");
    _builder.newLineIfNotEmpty();
    _builder.append("//logger.debug(\"Releasing passive resource ");
    PassiveResource _passiveResource_ReleaseAction_1 = action.getPassiveResource_ReleaseAction();
    String _entityName = _passiveResource_ReleaseAction_1.getEntityName();
    String _javaString = JavaNames.javaString(_entityName);
    _builder.append(_javaString, "");
    _builder.append("\");");
    _builder.newLineIfNotEmpty();
    _builder.append("passive_resource_");
    PassiveResource _passiveResource_ReleaseAction_2 = action.getPassiveResource_ReleaseAction();
    String _entityName_1 = _passiveResource_ReleaseAction_2.getEntityName();
    String _javaVariableName = JavaNames.javaVariableName(_entityName_1);
    _builder.append(_javaVariableName, "");
    _builder.append(".release();");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  protected String _action(final SetVariableAction action) {
    return null;
  }
  
  /**
   * A ForkAction spawns a new thread for each defined behavior. These should be processed asynchronously in parallel.
   * Please note that manually spawning new threads is discouraged on certain middlewares (like JavaEE)!
   */
  protected String _action(final ForkAction action) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * TODO Think about EmitEventAction. JMS?
   */
  protected String _action(final EmitEventAction action) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// FIXME: Add EmitEventAction.");
    _builder.newLine();
    return _builder.toString();
  }
  
  /**
   * Helper method to find the first StartAction in a list of actions.
   */
  public static StartAction findStart(final Iterable<AbstractAction> actions) {
    final Function1<AbstractAction, Boolean> _function = (AbstractAction it) -> {
      return Boolean.valueOf(StartAction.class.isInstance(it));
    };
    AbstractAction _findFirst = IterableExtensions.<AbstractAction>findFirst(actions, _function);
    return ((StartAction) _findFirst);
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
