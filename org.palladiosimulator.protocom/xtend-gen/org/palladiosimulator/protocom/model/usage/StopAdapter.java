package org.palladiosimulator.protocom.model.usage;

import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.protocom.model.usage.UserActionAdapter;

/**
 * Adapter class for PCM Stop user actions.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class StopAdapter extends UserActionAdapter<Stop> {
  public StopAdapter(final Stop entity) {
    super(entity);
  }
}
