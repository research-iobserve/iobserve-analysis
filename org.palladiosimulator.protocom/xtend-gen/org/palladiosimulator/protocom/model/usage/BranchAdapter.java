package org.palladiosimulator.protocom.model.usage;

import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.protocom.model.usage.BranchTransitionAdapter;
import org.palladiosimulator.protocom.model.usage.UserActionAdapter;

/**
 * Adapter class for PCM Branch user actions.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class BranchAdapter extends UserActionAdapter<Branch> {
  public BranchAdapter(final Branch entity) {
    super(entity);
  }
  
  /**
   * Gets the branch transitions.
   * @return a list of adapters for the branch transitions
   */
  public List<BranchTransitionAdapter> getBranchTransitions() {
    EList<BranchTransition> _branchTransitions_Branch = this.entity.getBranchTransitions_Branch();
    final Function1<BranchTransition, BranchTransitionAdapter> _function = (BranchTransition it) -> {
      return new BranchTransitionAdapter(it);
    };
    return ListExtensions.<BranchTransition, BranchTransitionAdapter>map(_branchTransitions_Branch, _function);
  }
}
