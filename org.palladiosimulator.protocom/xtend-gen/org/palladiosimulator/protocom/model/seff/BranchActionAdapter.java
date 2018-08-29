package org.palladiosimulator.protocom.model.seff;

import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.BranchAction;
import org.palladiosimulator.pcm.seff.GuardedBranchTransition;
import org.palladiosimulator.pcm.seff.ProbabilisticBranchTransition;
import org.palladiosimulator.protocom.model.seff.ActionAdapter;
import org.palladiosimulator.protocom.model.seff.BranchTransitionAdapter;
import org.palladiosimulator.protocom.model.seff.GuardedBranchTransitionAdapter;
import org.palladiosimulator.protocom.model.seff.ProbabilisticBranchTransitionAdapter;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class BranchActionAdapter extends ActionAdapter<BranchAction> {
  public BranchActionAdapter(final BranchAction entity) {
    super(entity);
  }
  
  public List<BranchTransitionAdapter<? extends AbstractBranchTransition>> getBranchTransitions() {
    EList<AbstractBranchTransition> _branches_Branch = this.entity.getBranches_Branch();
    final Function1<AbstractBranchTransition, BranchTransitionAdapter<? extends AbstractBranchTransition>> _function = (AbstractBranchTransition it) -> {
      BranchTransitionAdapter<? extends AbstractBranchTransition> _switchResult = null;
      boolean _matched = false;
      if (!_matched) {
        if (it instanceof ProbabilisticBranchTransition) {
          _matched=true;
          _switchResult = new ProbabilisticBranchTransitionAdapter(((ProbabilisticBranchTransition)it));
        }
      }
      if (!_matched) {
        if (it instanceof GuardedBranchTransition) {
          _matched=true;
          _switchResult = new GuardedBranchTransitionAdapter(((GuardedBranchTransition)it));
        }
      }
      return _switchResult;
    };
    return ListExtensions.<AbstractBranchTransition, BranchTransitionAdapter<? extends AbstractBranchTransition>>map(_branches_Branch, _function);
  }
}
