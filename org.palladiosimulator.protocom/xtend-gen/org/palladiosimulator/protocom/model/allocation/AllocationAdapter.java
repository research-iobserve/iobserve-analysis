package org.palladiosimulator.protocom.model.allocation;

import com.google.common.base.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.allocation.AllocationContextAdapter;

/**
 * Adapter class for PCM Allocation entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class AllocationAdapter extends ModelAdapter<Allocation> {
  public AllocationAdapter(final Allocation entity) {
    super(entity);
  }
  
  /**
   * Gets the allocation contexts.
   * @return a list of adapters for the allocation contexts
   */
  public Iterable<AllocationContextAdapter> getAllocationContexts() {
    EList<AllocationContext> _allocationContexts_Allocation = this.entity.getAllocationContexts_Allocation();
    final Function1<AllocationContext, Boolean> _function = (AllocationContext it) -> {
      AssemblyContext _assemblyContext_AllocationContext = it.getAssemblyContext_AllocationContext();
      return Boolean.valueOf((!Objects.equal(_assemblyContext_AllocationContext, null)));
    };
    Iterable<AllocationContext> _filter = IterableExtensions.<AllocationContext>filter(_allocationContexts_Allocation, _function);
    final Function1<AllocationContext, AllocationContextAdapter> _function_1 = (AllocationContext it) -> {
      return new AllocationContextAdapter(it);
    };
    return IterableExtensions.<AllocationContext, AllocationContextAdapter>map(_filter, _function_1);
  }
}
