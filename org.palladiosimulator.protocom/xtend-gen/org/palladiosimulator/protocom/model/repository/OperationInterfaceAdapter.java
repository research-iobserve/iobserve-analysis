package org.palladiosimulator.protocom.model.repository;

import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.protocom.model.ModelAdapter;

@SuppressWarnings("all")
public class OperationInterfaceAdapter extends ModelAdapter<OperationInterface> {
  public OperationInterfaceAdapter(final OperationInterface entity) {
    super(entity);
  }
  
  public String getInterfaceFqn() {
    Repository _repository__Interface = this.entity.getRepository__Interface();
    String _basePackageName = this.getBasePackageName(_repository__Interface);
    String _plus = (_basePackageName + ".");
    String _safeName = this.getSafeName(this.entity);
    return (_plus + _safeName);
  }
}
