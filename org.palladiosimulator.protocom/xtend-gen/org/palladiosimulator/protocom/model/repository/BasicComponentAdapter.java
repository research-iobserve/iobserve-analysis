package org.palladiosimulator.protocom.model.repository;

import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.protocom.model.repository.InterfaceProvidingRequiringEntityAdapter;
import org.palladiosimulator.protocom.model.seff.ServiceEffectSpecificationAdapter;

/**
 * Adapter class for PCM BasicComponent entities.
 * TODO: Super class -> InterfaceProvidingRequiringEntityAdapter
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public class BasicComponentAdapter extends InterfaceProvidingRequiringEntityAdapter<BasicComponent> {
  public BasicComponentAdapter(final BasicComponent entity) {
    super(entity);
  }
  
  /**
   * Get the 'Service Effect Specifications'.
   * @return a list of adapters for the 'Service Effect Specifications'
   */
  public List<ServiceEffectSpecificationAdapter> getServiceEffectSpecifications() {
    EList<ServiceEffectSpecification> _serviceEffectSpecifications__BasicComponent = this.entity.getServiceEffectSpecifications__BasicComponent();
    final Function1<ServiceEffectSpecification, ServiceEffectSpecificationAdapter> _function = (ServiceEffectSpecification it) -> {
      return new ServiceEffectSpecificationAdapter(it);
    };
    return ListExtensions.<ServiceEffectSpecification, ServiceEffectSpecificationAdapter>map(_serviceEffectSpecifications__BasicComponent, _function);
  }
  
  public String getClassFqn() {
    String _classPackageFqn = this.getClassPackageFqn();
    String _plus = (_classPackageFqn + ".");
    String _safeName = this.getSafeName(this.entity);
    return (_plus + _safeName);
  }
  
  public String getClassPackageFqn() {
    Repository _repository__RepositoryComponent = this.entity.getRepository__RepositoryComponent();
    String _basePackageName = this.getBasePackageName(_repository__RepositoryComponent);
    return (_basePackageName + ".impl");
  }
  
  public String getInterfaceName() {
    String _safeName = this.getSafeName(this.entity);
    return ("I" + _safeName);
  }
  
  public Object getContextPackageName() {
    return null;
  }
  
  public String getContextPackageFqn() {
    Repository _repository__RepositoryComponent = this.entity.getRepository__RepositoryComponent();
    String _basePackageName = this.getBasePackageName(_repository__RepositoryComponent);
    return (_basePackageName + ".impl.contexts");
  }
  
  public String getContextClassName() {
    String _safeName = this.getSafeName(this.entity);
    return (_safeName + "Context");
  }
  
  public String getContextInterfaceName() {
    String _contextClassName = this.getContextClassName();
    return ("I" + _contextClassName);
  }
  
  public String getContextInterfaceFqn() {
    String _contextPackageFqn = this.getContextPackageFqn();
    String _plus = (_contextPackageFqn + ".I");
    String _contextClassName = this.getContextClassName();
    return (_plus + _contextClassName);
  }
}
