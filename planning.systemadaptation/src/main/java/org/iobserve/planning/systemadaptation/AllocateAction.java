/**
 */
package org.iobserve.planning.systemadaptation;

import org.palladiosimulator.pcm.allocation.AllocationContext;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Allocate Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.AllocateAction#getNewAllocatinContext <em>New Allocatin Context</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getAllocateAction()
 * @model
 * @generated
 */
public interface AllocateAction extends AssemblyContextAction {

	/**
	 * Returns the value of the '<em><b>New Allocatin Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Allocatin Context</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Allocatin Context</em>' reference.
	 * @see #setNewAllocatinContext(AllocationContext)
	 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getAllocateAction_NewAllocatinContext()
	 * @model
	 * @generated
	 */
	AllocationContext getNewAllocatinContext();

	/**
	 * Sets the value of the '{@link org.iobserve.planning.systemadaptation.AllocateAction#getNewAllocatinContext <em>New Allocatin Context</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Allocatin Context</em>' reference.
	 * @see #getNewAllocatinContext()
	 * @generated
	 */
	void setNewAllocatinContext(AllocationContext value);
} // AllocateAction
