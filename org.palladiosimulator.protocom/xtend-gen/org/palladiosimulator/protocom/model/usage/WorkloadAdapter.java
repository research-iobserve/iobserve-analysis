package org.palladiosimulator.protocom.model.usage;

import org.palladiosimulator.pcm.usagemodel.Workload;
import org.palladiosimulator.protocom.model.ModelAdapter;

/**
 * Abstract base class for PCM Workload adapters.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public abstract class WorkloadAdapter<E extends Workload> extends ModelAdapter<E> {
  public WorkloadAdapter(final E entity) {
    super(entity);
  }
}
