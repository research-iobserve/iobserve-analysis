package org.palladiosimulator.protocom.model.seff;

import org.palladiosimulator.pcm.seff.StopAction;
import org.palladiosimulator.protocom.model.seff.ActionAdapter;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class StopActionAdapter extends ActionAdapter<StopAction> {
  public StopActionAdapter(final StopAction entity) {
    super(entity);
  }
}
