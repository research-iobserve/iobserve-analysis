/**
 */
package org.iobserve.planning.systemadaptation;

import org.palladiosimulator.pcm.allocation.AllocationContext;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deallocate Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.DeallocateAction#getOldAllocationContext <em>Old Allocation Context</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getDeallocateAction()
 * @model
 * @generated
 */
public interface DeallocateAction extends AssemblyContextAction {

	/**
	 * Returns the value of the '<em><b>Old Allocation Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Allocation Context</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Old Allocation Context</em>' reference.
	 * @see #setOldAllocationContext(AllocationContext)
	 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getDeallocateAction_OldAllocationContext()
	 * @model
	 * @generated
	 */
	AllocationContext getOldAllocationContext();

	/**
	 * Sets the value of the '{@link org.iobserve.planning.systemadaptation.DeallocateAction#getOldAllocationContext <em>Old Allocation Context</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Old Allocation Context</em>' reference.
	 * @see #getOldAllocationContext()
	 * @generated
	 */
	void setOldAllocationContext(AllocationContext value);
} // DeallocateAction
