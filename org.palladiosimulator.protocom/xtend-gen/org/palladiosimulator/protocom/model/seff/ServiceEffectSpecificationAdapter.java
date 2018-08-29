package org.palladiosimulator.protocom.model.seff;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.repository.Signature;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.repository.SignatureAdapter;
import org.palladiosimulator.protocom.model.seff.StartActionAdapter;

/**
 * Adapter class for PCM ServiceEffectSpecification entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class ServiceEffectSpecificationAdapter extends ModelAdapter<ServiceEffectSpecification> {
  public ServiceEffectSpecificationAdapter(final ServiceEffectSpecification entity) {
    super(entity);
  }
  
  /**
   * Gets the signature.
   * @return an adapter for the signature
   */
  public SignatureAdapter getSignature() {
    Signature _describedService__SEFF = this.entity.getDescribedService__SEFF();
    return new SignatureAdapter(_describedService__SEFF);
  }
  
  public StartActionAdapter getStart() {
    StartActionAdapter _xblockexpression = null;
    {
      final EList<AbstractAction> steps = ((ResourceDemandingBehaviour) this.entity).getSteps_Behaviour();
      AbstractAction _get = steps.get(0);
      _xblockexpression = new StartActionAdapter(((StartAction) _get));
    }
    return _xblockexpression;
  }
}
