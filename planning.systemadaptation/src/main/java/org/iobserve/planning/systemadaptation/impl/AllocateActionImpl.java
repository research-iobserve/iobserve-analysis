/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.systemadaptationPackage;
import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Allocate Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.AllocateActionImpl#getNewAllocatinContext <em>New Allocatin Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AllocateActionImpl extends AssemblyContextActionImpl implements AllocateAction {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AllocateActionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return systemadaptationPackage.Literals.ALLOCATE_ACTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AllocationContext getNewAllocatinContext() {
		return (AllocationContext)eGet(systemadaptationPackage.Literals.ALLOCATE_ACTION__NEW_ALLOCATIN_CONTEXT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewAllocatinContext(AllocationContext newNewAllocatinContext) {
		eSet(systemadaptationPackage.Literals.ALLOCATE_ACTION__NEW_ALLOCATIN_CONTEXT, newNewAllocatinContext);
	}

} //AllocateActionImpl
