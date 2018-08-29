package org.palladiosimulator.protocom.model.seff;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.seff.StartActionAdapter;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class ResourceDemandingBehaviourAdapter extends ModelAdapter<ResourceDemandingBehaviour> {
  public ResourceDemandingBehaviourAdapter(final ResourceDemandingBehaviour entity) {
    super(entity);
  }
  
  public StartActionAdapter getStart() {
    EList<AbstractAction> _steps_Behaviour = this.entity.getSteps_Behaviour();
    final Function1<AbstractAction, Boolean> _function = (AbstractAction it) -> {
      return Boolean.valueOf(StartAction.class.isInstance(it));
    };
    final AbstractAction start = IterableExtensions.<AbstractAction>findFirst(_steps_Behaviour, _function);
    return new StartActionAdapter(((StartAction) start));
  }
}
