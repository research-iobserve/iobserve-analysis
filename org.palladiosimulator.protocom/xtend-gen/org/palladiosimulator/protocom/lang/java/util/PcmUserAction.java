package org.palladiosimulator.protocom.lang.java.util;

import java.util.Arrays;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.Delay;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;

/**
 * Abstract class for implementing PCM actions, i.e., the behavior of components as specified
 * via SEFFs. Refinements of this class could, for instance, add simulate stack frames for performance
 * prototypes or provide code stub generation.
 * 
 * @author Sebastian Lehrig, Daria Giacinto
 */
@SuppressWarnings("all")
public abstract class PcmUserAction {
  /**
   * Follows the user action path and calls "userAction" for each action in it.
   * Note that actions do not branch! Branching is solved by a Branch action, therefore
   * at most one successor is given at any time.
   */
  public String userActions(final AbstractUserAction userAction) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* ");
    Class<? extends AbstractUserAction> _class = userAction.getClass();
    String _simpleName = _class.getSimpleName();
    _builder.append(_simpleName, " ");
    _builder.append(" (");
    _builder.append(userAction, " ");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    String _userAction = this.userAction(userAction);
    _builder.append(_userAction, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      boolean _isInstance = Stop.class.isInstance(userAction);
      boolean _not = (!_isInstance);
      if (_not) {
        AbstractUserAction _successor = userAction.getSuccessor();
        String _userActions = this.userActions(_successor);
        _builder.append(_userActions, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder.toString();
  }
  
  /**
   * EntryLevelSystemCall is an user action which calls a system service from an usage scenario.
   */
  protected String _userAction(final EntryLevelSystemCall userAction) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * FIXME Implement and test this action with Thread.sleep
   */
  protected String _userAction(final Delay userAction) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  protected String _userAction(final Start userAction) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  protected String _userAction(final Stop userAction) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * Loop actions are transformed into a simple FOR statement.
   */
  protected String _userAction(final Loop userAction) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * UserActions only have probabilistic transitions.
   */
  protected String _userAction(final Branch userAction) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  /**
   * And another helper method, since Actions and UserActions are *obviously* so
   * different that they cannot have a common supertype...
   */
  public static Start findUserStart(final Iterable<AbstractUserAction> actions) {
    final Function1<AbstractUserAction, Boolean> _function = (AbstractUserAction it) -> {
      return Boolean.valueOf(Start.class.isInstance(it));
    };
    AbstractUserAction _findFirst = IterableExtensions.<AbstractUserAction>findFirst(actions, _function);
    return ((Start) _findFirst);
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
