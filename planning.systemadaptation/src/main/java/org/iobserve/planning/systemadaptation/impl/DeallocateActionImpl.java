/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.systemadaptationPackage;
import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deallocate Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl#getOldAllocationContext <em>Old Allocation Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeallocateActionImpl extends AssemblyContextActionImpl implements DeallocateAction {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeallocateActionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return systemadaptationPackage.Literals.DEALLOCATE_ACTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AllocationContext getOldAllocationContext() {
		return (AllocationContext)eGet(systemadaptationPackage.Literals.DEALLOCATE_ACTION__OLD_ALLOCATION_CONTEXT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOldAllocationContext(AllocationContext newOldAllocationContext) {
		eSet(systemadaptationPackage.Literals.DEALLOCATE_ACTION__OLD_ALLOCATION_CONTEXT, newOldAllocationContext);
	}

} //DeallocateActionImpl
