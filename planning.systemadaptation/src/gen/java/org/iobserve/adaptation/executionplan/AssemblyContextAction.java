/**
 */
package org.iobserve.adaptation.executionplan;

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assembly Context Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.AssemblyContextAction#getTargetAllocationContext <em>Target Allocation Context</em>}</li>
 * </ul>
 *
 * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getAssemblyContextAction()
 * @model
 * @generated
 */
public interface AssemblyContextAction extends Action {
    /**
     * Returns the value of the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Allocation Context</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Allocation Context</em>' reference.
     * @see #setTargetAllocationContext(AllocationContext)
     * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getAssemblyContextAction_TargetAllocationContext()
     * @model
     * @generated
     */
    AllocationContext getTargetAllocationContext();

    /**
     * Sets the value of the '{@link org.iobserve.adaptation.executionplan.AssemblyContextAction#getTargetAllocationContext <em>Target Allocation Context</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target Allocation Context</em>' reference.
     * @see #getTargetAllocationContext()
     * @generated
     */
    void setTargetAllocationContext(AllocationContext value);

} // AssemblyContextAction
