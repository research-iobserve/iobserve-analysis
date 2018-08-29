package org.palladiosimulator.protocom.model.seff;

import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;
import org.palladiosimulator.protocom.model.ModelAdapter;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class ParametricResourceDemandAdapter extends ModelAdapter<ParametricResourceDemand> {
  public ParametricResourceDemandAdapter(final ParametricResourceDemand entity) {
    super(entity);
  }
  
  public String getSpecification() {
    PCMRandomVariable _specification_ParametericResourceDemand = this.entity.getSpecification_ParametericResourceDemand();
    return _specification_ParametericResourceDemand.getSpecification();
  }
  
  public String getType() {
    ProcessingResourceType _requiredResource_ParametricResourceDemand = this.entity.getRequiredResource_ParametricResourceDemand();
    String _entityName = _requiredResource_ParametricResourceDemand.getEntityName();
    return _entityName.toLowerCase();
  }
}
