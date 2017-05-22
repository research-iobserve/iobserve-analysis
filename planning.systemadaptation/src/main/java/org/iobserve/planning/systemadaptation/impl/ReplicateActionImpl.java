/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.systemadaptationPackage;

import org.palladiosimulator.pcm.allocation.AllocationContext;

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Replicate Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl#getNewResourceContainer <em>New Resource Container</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl#getSourceAllocationContext <em>Source Allocation Context</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl#getNewAllocationContext <em>New Allocation Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReplicateActionImpl extends ResourceContainerActionImpl implements ReplicateAction {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ReplicateActionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return systemadaptationPackage.Literals.REPLICATE_ACTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceContainer getNewResourceContainer() {
		return (ResourceContainer)eGet(systemadaptationPackage.Literals.REPLICATE_ACTION__NEW_RESOURCE_CONTAINER, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewResourceContainer(ResourceContainer newNewResourceContainer) {
		eSet(systemadaptationPackage.Literals.REPLICATE_ACTION__NEW_RESOURCE_CONTAINER, newNewResourceContainer);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<AllocationContext> getSourceAllocationContext() {
		return (EList<AllocationContext>)eGet(systemadaptationPackage.Literals.REPLICATE_ACTION__SOURCE_ALLOCATION_CONTEXT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<AllocationContext> getNewAllocationContext() {
		return (EList<AllocationContext>)eGet(systemadaptationPackage.Literals.REPLICATE_ACTION__NEW_ALLOCATION_CONTEXT, true);
	}

} //ReplicateActionImpl
