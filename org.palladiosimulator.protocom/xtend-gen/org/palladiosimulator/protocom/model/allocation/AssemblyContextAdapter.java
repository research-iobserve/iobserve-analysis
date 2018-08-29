package org.palladiosimulator.protocom.model.allocation;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.repository.BasicComponentAdapter;

/**
 * Adapter class for PCM AssemblyContext entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class AssemblyContextAdapter extends ModelAdapter<AssemblyContext> {
  public AssemblyContextAdapter(final AssemblyContext entity) {
    super(entity);
  }
  
  /**
   * Gets the ID.
   * @return a string containing the ID
   */
  public String getId() {
    return this.entity.getId();
  }
  
  public BasicComponentAdapter getEncapsulatedComponent() {
    RepositoryComponent _encapsulatedComponent__AssemblyContext = this.entity.getEncapsulatedComponent__AssemblyContext();
    return new BasicComponentAdapter(((BasicComponent) _encapsulatedComponent__AssemblyContext));
  }
}
