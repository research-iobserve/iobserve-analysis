package org.palladiosimulator.protocom.model.repository;

import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.repository.OperationInterfaceAdapter;

/**
 * Adapter class for PCM OperationProvidedRole entities.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class OperationProvidedRoleAdapter extends ModelAdapter<OperationProvidedRole> {
  public OperationProvidedRoleAdapter(final OperationProvidedRole entity) {
    super(entity);
  }
  
  public OperationInterfaceAdapter getProvidedInterface() {
    OperationInterface _providedInterface__OperationProvidedRole = this.entity.getProvidedInterface__OperationProvidedRole();
    return new OperationInterfaceAdapter(_providedInterface__OperationProvidedRole);
  }
  
  public String getPortClassName() {
    OperationInterface _providedInterface__OperationProvidedRole = this.entity.getProvidedInterface__OperationProvidedRole();
    String _safeName = this.getSafeName(_providedInterface__OperationProvidedRole);
    String _plus = (_safeName + "_");
    InterfaceProvidingEntity _providingEntity_ProvidedRole = this.entity.getProvidingEntity_ProvidedRole();
    String _safeName_1 = this.getSafeName(_providingEntity_ProvidedRole);
    return (_plus + _safeName_1);
  }
}
