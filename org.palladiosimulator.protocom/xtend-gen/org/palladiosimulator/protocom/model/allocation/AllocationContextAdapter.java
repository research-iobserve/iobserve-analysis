package org.palladiosimulator.protocom.model.allocation;

import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.allocation.AssemblyContextAdapter;
import org.palladiosimulator.protocom.model.resourceenvironment.ResourceContainerAdapter;

/**
 * Adapter class for PCM AllocationContext entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class AllocationContextAdapter extends ModelAdapter<AllocationContext> {
  public AllocationContextAdapter(final AllocationContext entity) {
    super(entity);
  }
  
  /**
   * Gets the resource container.
   * @return an adapter for the resource container
   */
  public ResourceContainerAdapter getResourceContainer() {
    ResourceContainer _resourceContainer_AllocationContext = this.entity.getResourceContainer_AllocationContext();
    return new ResourceContainerAdapter(_resourceContainer_AllocationContext);
  }
  
  /**
   * Gets the assembly context.
   * @return an adapter for the assembly context
   */
  public AssemblyContextAdapter getAssemblyContext() {
    AssemblyContext _assemblyContext_AllocationContext = this.entity.getAssemblyContext_AllocationContext();
    return new AssemblyContextAdapter(_assemblyContext_AllocationContext);
  }
}
