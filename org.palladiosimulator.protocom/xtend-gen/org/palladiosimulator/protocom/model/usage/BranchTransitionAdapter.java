package org.palladiosimulator.protocom.model.usage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.usage.StartAdapter;

/**
 * Adapter class for PCM BranchTransition entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class BranchTransitionAdapter extends ModelAdapter<BranchTransition> {
  public BranchTransitionAdapter(final BranchTransition entity) {
    super(entity);
  }
  
  /**
   * Gets the start actions of the branch.
   * @return an adapter for the start action
   */
  public StartAdapter getStart() {
    StartAdapter _xblockexpression = null;
    {
      final ScenarioBehaviour behaviour = this.entity.getBranchedBehaviour_BranchTransition();
      EList<AbstractUserAction> _actions_ScenarioBehaviour = behaviour.getActions_ScenarioBehaviour();
      final Function1<AbstractUserAction, Boolean> _function = (AbstractUserAction it) -> {
        return Boolean.valueOf(Start.class.isInstance(it));
      };
      final AbstractUserAction start = IterableExtensions.<AbstractUserAction>findFirst(_actions_ScenarioBehaviour, _function);
      _xblockexpression = new StartAdapter(((Start) start));
    }
    return _xblockexpression;
  }
  
  /**
   * Gets the probability of the branch.
   * @return the probability of the branch
   */
  public double getProbability() {
    return this.entity.getBranchProbability();
  }
}
