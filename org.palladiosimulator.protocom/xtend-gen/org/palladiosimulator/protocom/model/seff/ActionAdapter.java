package org.palladiosimulator.protocom.model.seff;

import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.AcquireAction;
import org.palladiosimulator.pcm.seff.BranchAction;
import org.palladiosimulator.pcm.seff.ExternalCallAction;
import org.palladiosimulator.pcm.seff.ForkAction;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.ReleaseAction;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.pcm.seff.StopAction;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.seff.AcquireActionAdapter;
import org.palladiosimulator.protocom.model.seff.BranchActionAdapter;
import org.palladiosimulator.protocom.model.seff.ExternalCallActionAdapter;
import org.palladiosimulator.protocom.model.seff.ForkActionAdapter;
import org.palladiosimulator.protocom.model.seff.InternalActionAdapter;
import org.palladiosimulator.protocom.model.seff.ReleaseActionAdapter;
import org.palladiosimulator.protocom.model.seff.StartActionAdapter;
import org.palladiosimulator.protocom.model.seff.StopActionAdapter;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class ActionAdapter<E extends AbstractAction> extends ModelAdapter<E> {
  public ActionAdapter(final E entity) {
    super(entity);
  }
  
  public String getId() {
    return this.entity.getId();
  }
  
  public ActionAdapter<? extends AbstractAction> getSuccessor() {
    ActionAdapter<? extends AbstractAction> _xblockexpression = null;
    {
      final AbstractAction successor = this.entity.getSuccessor_AbstractAction();
      ActionAdapter<? extends AbstractAction> _switchResult = null;
      boolean _matched = false;
      if (!_matched) {
        if (successor instanceof BranchAction) {
          _matched=true;
          _switchResult = new BranchActionAdapter(((BranchAction)successor));
        }
      }
      if (!_matched) {
        if (successor instanceof ExternalCallAction) {
          _matched=true;
          _switchResult = new ExternalCallActionAdapter(((ExternalCallAction)successor));
        }
      }
      if (!_matched) {
        if (successor instanceof InternalAction) {
          _matched=true;
          _switchResult = new InternalActionAdapter(((InternalAction)successor));
        }
      }
      if (!_matched) {
        if (successor instanceof StartAction) {
          _matched=true;
          _switchResult = new StartActionAdapter(((StartAction)successor));
        }
      }
      if (!_matched) {
        if (successor instanceof StopAction) {
          _matched=true;
          _switchResult = new StopActionAdapter(((StopAction)successor));
        }
      }
      if (!_matched) {
        if (successor instanceof ForkAction) {
          _matched=true;
          _switchResult = new ForkActionAdapter(((ForkAction)successor));
        }
      }
      if (!_matched) {
        if (successor instanceof AcquireAction) {
          _matched=true;
          _switchResult = new AcquireActionAdapter(((AcquireAction)successor));
        }
      }
      if (!_matched) {
        if (successor instanceof ReleaseAction) {
          _matched=true;
          _switchResult = new ReleaseActionAdapter(((ReleaseAction)successor));
        }
      }
      if (!_matched) {
        String _string = successor.toString();
        String _plus = ("unknown action type (" + _string);
        String _plus_1 = (_plus + ")");
        throw new RuntimeException(_plus_1);
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
  
  public Object getSafeId() {
    return null;
  }
}
