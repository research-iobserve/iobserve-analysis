package org.palladiosimulator.protocom.model.seff;

import org.palladiosimulator.pcm.seff.ProbabilisticBranchTransition;
import org.palladiosimulator.protocom.model.seff.BranchTransitionAdapter;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class ProbabilisticBranchTransitionAdapter extends BranchTransitionAdapter<ProbabilisticBranchTransition> {
  public ProbabilisticBranchTransitionAdapter(final ProbabilisticBranchTransition entity) {
    super(entity);
  }
  
  public double getProbability() {
    return this.entity.getBranchProbability();
  }
}
