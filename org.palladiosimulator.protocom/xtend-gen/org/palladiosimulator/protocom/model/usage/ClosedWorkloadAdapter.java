package org.palladiosimulator.protocom.model.usage;

import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.ClosedWorkload;
import org.palladiosimulator.protocom.model.usage.WorkloadAdapter;

/**
 * Adapter class for PCM ClosedWorkload entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class ClosedWorkloadAdapter extends WorkloadAdapter<ClosedWorkload> {
  public ClosedWorkloadAdapter(final ClosedWorkload entity) {
    super(entity);
  }
  
  public String getThinkTime() {
    PCMRandomVariable _thinkTime_ClosedWorkload = this.entity.getThinkTime_ClosedWorkload();
    return _thinkTime_ClosedWorkload.getSpecification();
  }
}
