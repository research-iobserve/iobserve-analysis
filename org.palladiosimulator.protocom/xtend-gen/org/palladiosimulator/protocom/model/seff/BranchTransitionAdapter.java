package org.palladiosimulator.protocom.model.seff;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.AbstractBranchTransition;
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.seff.StartActionAdapter;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class BranchTransitionAdapter<E extends AbstractBranchTransition> extends ModelAdapter<E> {
  public BranchTransitionAdapter(final E entity) {
    super(entity);
  }
  
  public StartActionAdapter getStart() {
    StartActionAdapter _xblockexpression = null;
    {
      final ResourceDemandingBehaviour behaviour = this.entity.getBranchBehaviour_BranchTransition();
      EList<AbstractAction> _steps_Behaviour = behaviour.getSteps_Behaviour();
      final Function1<AbstractAction, Boolean> _function = (AbstractAction it) -> {
        return Boolean.valueOf(StartAction.class.isInstance(it));
      };
      final AbstractAction start = IterableExtensions.<AbstractAction>findFirst(_steps_Behaviour, _function);
      _xblockexpression = new StartActionAdapter(((StartAction) start));
    }
    return _xblockexpression;
  }
}
