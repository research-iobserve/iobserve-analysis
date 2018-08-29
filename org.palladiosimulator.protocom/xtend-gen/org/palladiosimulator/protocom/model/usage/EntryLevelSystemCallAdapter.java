package org.palladiosimulator.protocom.model.usage;

import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.protocom.model.repository.OperationProvidedRoleAdapter;
import org.palladiosimulator.protocom.model.repository.SignatureAdapter;
import org.palladiosimulator.protocom.model.usage.UserActionAdapter;

/**
 * Adapter class for PCM EntryLevelSystemCall user actions.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class EntryLevelSystemCallAdapter extends UserActionAdapter<EntryLevelSystemCall> {
  public EntryLevelSystemCallAdapter(final EntryLevelSystemCall entity) {
    super(entity);
  }
  
  public OperationProvidedRoleAdapter getProvidedRole() {
    OperationProvidedRole _providedRole_EntryLevelSystemCall = this.entity.getProvidedRole_EntryLevelSystemCall();
    return new OperationProvidedRoleAdapter(_providedRole_EntryLevelSystemCall);
  }
  
  public SignatureAdapter getOperationSignature() {
    OperationSignature _operationSignature__EntryLevelSystemCall = this.entity.getOperationSignature__EntryLevelSystemCall();
    return new SignatureAdapter(_operationSignature__EntryLevelSystemCall);
  }
}
