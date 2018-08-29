package org.palladiosimulator.protocom.model.usage;

import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.protocom.model.usage.UserActionAdapter;

/**
 * Adapter class for PCM Start user actions.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class StartAdapter extends UserActionAdapter<Start> {
  public StartAdapter(final Start entity) {
    super(entity);
  }
}
