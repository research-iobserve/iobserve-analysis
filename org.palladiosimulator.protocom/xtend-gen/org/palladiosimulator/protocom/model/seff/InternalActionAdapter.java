package org.palladiosimulator.protocom.model.seff;

import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.seff_performance.ParametricResourceDemand;
import org.palladiosimulator.protocom.model.seff.ActionAdapter;
import org.palladiosimulator.protocom.model.seff.ParametricResourceDemandAdapter;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class InternalActionAdapter extends ActionAdapter<InternalAction> {
  public InternalActionAdapter(final InternalAction entity) {
    super(entity);
  }
  
  public List<ParametricResourceDemandAdapter> getResourceDemands() {
    EList<ParametricResourceDemand> _resourceDemand_Action = this.entity.getResourceDemand_Action();
    final Function1<ParametricResourceDemand, ParametricResourceDemandAdapter> _function = (ParametricResourceDemand it) -> {
      return new ParametricResourceDemandAdapter(it);
    };
    return ListExtensions.<ParametricResourceDemand, ParametricResourceDemandAdapter>map(_resourceDemand_Action, _function);
  }
}
