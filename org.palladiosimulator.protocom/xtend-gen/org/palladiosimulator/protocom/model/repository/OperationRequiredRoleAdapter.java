package org.palladiosimulator.protocom.model.repository;

import org.palladiosimulator.pcm.core.entity.InterfaceRequiringEntity;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.repository.InterfaceRequiringEntityAdapter;
import org.palladiosimulator.protocom.model.repository.OperationInterfaceAdapter;

/**
 * Adapter class for PCM OperationRequiredRole entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class OperationRequiredRoleAdapter extends ModelAdapter<OperationRequiredRole> {
  public OperationRequiredRoleAdapter(final OperationRequiredRole entity) {
    super(entity);
  }
  
  /**
   * Gets the ID.
   * @return a string containing the ID
   */
  public String getId() {
    return this.entity.getId();
  }
  
  /**
   * Gets the requiring entity.
   * @return an adapter for the requiring entity
   */
  public InterfaceRequiringEntityAdapter getRequiringEntity() {
    InterfaceRequiringEntity _requiringEntity_RequiredRole = this.entity.getRequiringEntity_RequiredRole();
    return new InterfaceRequiringEntityAdapter(_requiringEntity_RequiredRole);
  }
  
  /**
   * Get the required interface.
   * @return an adapter for the required interface
   */
  public OperationInterfaceAdapter getRequiredInterface() {
    OperationInterface _requiredInterface__OperationRequiredRole = this.entity.getRequiredInterface__OperationRequiredRole();
    return new OperationInterfaceAdapter(_requiredInterface__OperationRequiredRole);
  }
}
