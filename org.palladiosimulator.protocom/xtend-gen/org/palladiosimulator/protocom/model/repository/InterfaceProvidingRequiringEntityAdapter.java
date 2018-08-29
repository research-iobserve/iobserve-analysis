package org.palladiosimulator.protocom.model.repository;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingRequiringEntity;
import org.palladiosimulator.pcm.repository.InfrastructureProvidedRole;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.RequiredRole;
import org.palladiosimulator.protocom.model.ModelAdapter;
import org.palladiosimulator.protocom.model.repository.InfrastructureProvidedRoleAdapter;
import org.palladiosimulator.protocom.model.repository.OperationProvidedRoleAdapter;
import org.palladiosimulator.protocom.model.repository.OperationRequiredRoleAdapter;

/**
 * Abstract base class for PCM InterfaceProvidingRequiring entity adapters.
 * @author Christian Klaussner
 */
@SuppressWarnings("all")
public abstract class InterfaceProvidingRequiringEntityAdapter<E extends InterfaceProvidingRequiringEntity> extends ModelAdapter<E> {
  public InterfaceProvidingRequiringEntityAdapter(final E entity) {
    super(entity);
  }
  
  /**
   * Gets the operation provided roles.
   * @return a list of adapters for the operation provided roles
   */
  public Iterable<OperationProvidedRoleAdapter> getOperationProvidedRoles() {
    EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = this.entity.getProvidedRoles_InterfaceProvidingEntity();
    final Function1<ProvidedRole, Boolean> _function = (ProvidedRole it) -> {
      return Boolean.valueOf(OperationProvidedRole.class.isInstance(it));
    };
    Iterable<ProvidedRole> _filter = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity, _function);
    final Function1<ProvidedRole, OperationProvidedRoleAdapter> _function_1 = (ProvidedRole it) -> {
      return new OperationProvidedRoleAdapter(((OperationProvidedRole) it));
    };
    return IterableExtensions.<ProvidedRole, OperationProvidedRoleAdapter>map(_filter, _function_1);
  }
  
  /**
   * Gets the infrastructure provided roles.
   * @return a list of adapters for the infrastructure provided roles
   */
  public Iterable<InfrastructureProvidedRoleAdapter> getInfrastructureProvidedRoles() {
    EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = this.entity.getProvidedRoles_InterfaceProvidingEntity();
    final Function1<ProvidedRole, Boolean> _function = (ProvidedRole it) -> {
      return Boolean.valueOf(InfrastructureProvidedRole.class.isInstance(it));
    };
    Iterable<ProvidedRole> _filter = IterableExtensions.<ProvidedRole>filter(_providedRoles_InterfaceProvidingEntity, _function);
    final Function1<ProvidedRole, InfrastructureProvidedRoleAdapter> _function_1 = (ProvidedRole it) -> {
      return new InfrastructureProvidedRoleAdapter(((InfrastructureProvidedRole) it));
    };
    return IterableExtensions.<ProvidedRole, InfrastructureProvidedRoleAdapter>map(_filter, _function_1);
  }
  
  /**
   * Gets the operation required roles.
   * @return a list of adapters for the operation required roles
   */
  public Iterable<OperationRequiredRoleAdapter> getOperationRequiredRoles() {
    EList<RequiredRole> _requiredRoles_InterfaceRequiringEntity = this.entity.getRequiredRoles_InterfaceRequiringEntity();
    final Function1<RequiredRole, Boolean> _function = (RequiredRole it) -> {
      return Boolean.valueOf(OperationRequiredRole.class.isInstance(it));
    };
    Iterable<RequiredRole> _filter = IterableExtensions.<RequiredRole>filter(_requiredRoles_InterfaceRequiringEntity, _function);
    final Function1<RequiredRole, OperationRequiredRoleAdapter> _function_1 = (RequiredRole it) -> {
      return new OperationRequiredRoleAdapter(((OperationRequiredRole) it));
    };
    return IterableExtensions.<RequiredRole, OperationRequiredRoleAdapter>map(_filter, _function_1);
  }
}
