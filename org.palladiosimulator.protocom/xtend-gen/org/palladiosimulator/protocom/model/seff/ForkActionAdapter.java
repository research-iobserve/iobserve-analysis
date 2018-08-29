package org.palladiosimulator.protocom.model.seff;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.seff.ForkAction;
import org.palladiosimulator.pcm.seff.ForkedBehaviour;
import org.palladiosimulator.protocom.model.seff.ActionAdapter;

/**
 * @author Sebastian Lehrig
 */
@SuppressWarnings("all")
public class ForkActionAdapter extends ActionAdapter<ForkAction> {
  public ForkActionAdapter(final ForkAction entity) {
    super(entity);
    entity.getAsynchronousForkedBehaviours_ForkAction();
    entity.getSynchronisingBehaviours_ForkAction();
  }
  
  public EList<ForkedBehaviour> getAsynchronousForkedBehaviours() {
    return this.entity.getAsynchronousForkedBehaviours_ForkAction();
  }
}
