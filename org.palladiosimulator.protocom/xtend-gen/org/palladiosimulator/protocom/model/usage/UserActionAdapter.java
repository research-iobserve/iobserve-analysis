package org.palladiosimulator.protocom.model.usage;

import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.Delay;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.usage.BranchAdapter;
import org.palladiosimulator.protocom.model.usage.DelayAdapter;
import org.palladiosimulator.protocom.model.usage.EntryLevelSystemCallAdapter;
import org.palladiosimulator.protocom.model.usage.LoopAdapter;
import org.palladiosimulator.protocom.model.usage.StartAdapter;
import org.palladiosimulator.protocom.model.usage.StopAdapter;

/**
 * Abstract base class for PCM user actions.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class UserActionAdapter<E extends AbstractUserAction> extends ModelAdapter<E> {
  public UserActionAdapter(final E entity) {
    super(entity);
  }
  
  /**
   * Gets the successor action.
   * @return an adapter for the successor action
   */
  public UserActionAdapter<? extends AbstractUserAction> getSuccessor() {
    UserActionAdapter<? extends AbstractUserAction> _xblockexpression = null;
    {
      final AbstractUserAction successor = this.entity.getSuccessor();
      UserActionAdapter<? extends AbstractUserAction> _switchResult = null;
      boolean _matched = false;
      if (!_matched) {
        if (successor instanceof Branch) {
          _matched=true;
          _switchResult = new BranchAdapter(((Branch)successor));
        }
      }
      if (!_matched) {
        if (successor instanceof Delay) {
          _matched=true;
          _switchResult = new DelayAdapter(((Delay)successor));
        }
      }
      if (!_matched) {
        if (successor instanceof EntryLevelSystemCall) {
          _matched=true;
          _switchResult = new EntryLevelSystemCallAdapter(((EntryLevelSystemCall)successor));
        }
      }
      if (!_matched) {
        if (successor instanceof Loop) {
          _matched=true;
          _switchResult = new LoopAdapter(((Loop)successor));
        }
      }
      if (!_matched) {
        if (successor instanceof Start) {
          _matched=true;
          _switchResult = new StartAdapter(((Start)successor));
        }
      }
      if (!_matched) {
        if (successor instanceof Stop) {
          _matched=true;
          _switchResult = new StopAdapter(((Stop)successor));
        }
      }
      if (!_matched) {
        throw new RuntimeException("unknown action type");
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
}
