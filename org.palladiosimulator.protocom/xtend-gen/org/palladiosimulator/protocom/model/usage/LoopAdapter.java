package org.palladiosimulator.protocom.model.usage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.protocom.model.usage.ScenarioBehaviourAdapter;
import org.palladiosimulator.protocom.model.usage.StartAdapter;
import org.palladiosimulator.protocom.model.usage.UserActionAdapter;

/**
 * Adapter class for PCM Loop user actions.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class LoopAdapter extends UserActionAdapter<Loop> {
  public LoopAdapter(final Loop entity) {
    super(entity);
  }
  
  public String getIterationCount() {
    PCMRandomVariable _loopIteration_Loop = this.entity.getLoopIteration_Loop();
    return _loopIteration_Loop.getSpecification();
  }
  
  public StartAdapter getStart() {
    StartAdapter _xblockexpression = null;
    {
      ScenarioBehaviour _bodyBehaviour_Loop = this.entity.getBodyBehaviour_Loop();
      final EList<AbstractUserAction> actions = _bodyBehaviour_Loop.getActions_ScenarioBehaviour();
      final Function1<AbstractUserAction, Boolean> _function = (AbstractUserAction it) -> {
        return Boolean.valueOf(Start.class.isInstance(it));
      };
      final AbstractUserAction start = IterableExtensions.<AbstractUserAction>findFirst(actions, _function);
      _xblockexpression = new StartAdapter(((Start) start));
    }
    return _xblockexpression;
  }
  
  public ScenarioBehaviourAdapter getScenarioBehaviour() {
    ScenarioBehaviour _bodyBehaviour_Loop = this.entity.getBodyBehaviour_Loop();
    return new ScenarioBehaviourAdapter(_bodyBehaviour_Loop);
  }
}
