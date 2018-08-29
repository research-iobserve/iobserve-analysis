package org.palladiosimulator.protocom.model.seff;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.parameter.VariableUsage;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.seff.ExternalCallAction;
import org.palladiosimulator.protocom.model.repository.OperationRequiredRoleAdapter;
import org.palladiosimulator.protocom.model.repository.SignatureAdapter;
import org.palladiosimulator.protocom.model.seff.ActionAdapter;

/**
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class ExternalCallActionAdapter extends ActionAdapter<ExternalCallAction> {
  public ExternalCallActionAdapter(final ExternalCallAction entity) {
    super(entity);
  }
  
  public SignatureAdapter getCalledService() {
    OperationSignature _calledService_ExternalService = this.entity.getCalledService_ExternalService();
    return new SignatureAdapter(_calledService_ExternalService);
  }
  
  public OperationRequiredRoleAdapter getRole() {
    OperationRequiredRole _role_ExternalService = this.entity.getRole_ExternalService();
    return new OperationRequiredRoleAdapter(_role_ExternalService);
  }
  
  public EList<VariableUsage> getInputVariableUsages() {
    return this.entity.getInputVariableUsages__CallAction();
  }
  
  public EList<VariableUsage> getReturnVariableUsage() {
    return this.entity.getReturnVariableUsage__CallReturnAction();
  }
}
