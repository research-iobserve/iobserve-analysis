package org.palladiosimulator.protocom.model.seff;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.LoopAction;
import org.palladiosimulator.pcm.seff.ResourceDemandingBehaviour;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.protocom.model.seff.ActionAdapter;
import org.palladiosimulator.protocom.model.seff.StartActionAdapter;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class LoopActionAdapter extends ActionAdapter<LoopAction> {
  public LoopActionAdapter(final LoopAction entity) {
    super(entity);
  }
  
  public String getIterationCount() {
    PCMRandomVariable _iterationCount_LoopAction = this.entity.getIterationCount_LoopAction();
    return _iterationCount_LoopAction.getSpecification();
  }
  
  public StartActionAdapter getStart() {
    StartActionAdapter _xblockexpression = null;
    {
      ResourceDemandingBehaviour _bodyBehaviour_Loop = this.entity.getBodyBehaviour_Loop();
      final EList<AbstractAction> actions = _bodyBehaviour_Loop.getSteps_Behaviour();
      final Function1<AbstractAction, Boolean> _function = (AbstractAction it) -> {
        return Boolean.valueOf(StartAction.class.isInstance(it));
      };
      final AbstractAction start = IterableExtensions.<AbstractAction>findFirst(actions, _function);
      _xblockexpression = new StartActionAdapter(((StartAction) start));
    }
    return _xblockexpression;
  }
}
