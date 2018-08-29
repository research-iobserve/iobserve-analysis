package org.palladiosimulator.protocom.model.usage;

import org.palladiosimulator.pcm.usagemodel.OpenWorkload;
import org.palladiosimulator.protocom.model.usage.WorkloadAdapter;

/**
 * Adapter class for PCM OpenWorkload entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class OpenWorkloadAdapter extends WorkloadAdapter<OpenWorkload> {
  public OpenWorkloadAdapter(final OpenWorkload entity) {
    super(entity);
  }
}
