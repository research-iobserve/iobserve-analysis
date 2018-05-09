/**
 */
package org.iobserve.model.correspondence;

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Allocation Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.AllocationEntry#getAllocation <em>Allocation</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence.CorrespondencePackage#getAllocationEntry()
 * @model
 * @generated
 */
public interface AllocationEntry extends AbstractEntry {
    /**
     * Returns the value of the '<em><b>Allocation</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Allocation</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Allocation</em>' reference.
     * @see #setAllocation(AllocationContext)
     * @see org.iobserve.model.correspondence.CorrespondencePackage#getAllocationEntry_Allocation()
     * @model required="true"
     * @generated
     */
    AllocationContext getAllocation();

    /**
     * Sets the value of the '{@link org.iobserve.model.correspondence.AllocationEntry#getAllocation <em>Allocation</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Allocation</em>' reference.
     * @see #getAllocation()
     * @generated
     */
    void setAllocation(AllocationContext value);

} // AllocationEntry
