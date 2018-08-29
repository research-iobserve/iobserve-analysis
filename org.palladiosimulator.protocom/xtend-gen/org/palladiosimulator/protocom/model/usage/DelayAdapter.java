package org.palladiosimulator.protocom.model.usage;

import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.Delay;
import org.palladiosimulator.protocom.model.usage.UserActionAdapter;

/**
 * Adapter class for PCM Delay user actions.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class DelayAdapter extends UserActionAdapter<Delay> {
  public DelayAdapter(final Delay entity) {
    super(entity);
  }
  
  public String getDelay() {
    PCMRandomVariable _timeSpecification_Delay = this.entity.getTimeSpecification_Delay();
    return _timeSpecification_Delay.getSpecification();
  }
}
