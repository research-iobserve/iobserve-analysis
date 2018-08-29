package org.palladiosimulator.protocom.model.seff;

import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.seff.GuardedBranchTransition;
import org.palladiosimulator.protocom.model.seff.BranchTransitionAdapter;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class GuardedBranchTransitionAdapter extends BranchTransitionAdapter<GuardedBranchTransition> {
  public GuardedBranchTransitionAdapter(final GuardedBranchTransition entity) {
    super(entity);
  }
  
  public String getCondition() {
    PCMRandomVariable _branchCondition_GuardedBranchTransition = this.entity.getBranchCondition_GuardedBranchTransition();
    return _branchCondition_GuardedBranchTransition.getSpecification();
  }
}
