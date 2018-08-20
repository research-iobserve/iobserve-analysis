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
 *   <li>{@link org.iobserve.model.correspondence.AllocationEntry#getTechnology <em>Technology</em>}</li>
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

    /**
     * Returns the value of the '<em><b>Technology</b></em>' attribute.
     * The literals are from the enumeration {@link org.iobserve.model.correspondence.EServiceTechnology}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Technology</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Technology</em>' attribute.
     * @see org.iobserve.model.correspondence.EServiceTechnology
     * @see #setTechnology(EServiceTechnology)
     * @see org.iobserve.model.correspondence.CorrespondencePackage#getAllocationEntry_Technology()
     * @model required="true"
     * @generated
     */
    EServiceTechnology getTechnology();

    /**
     * Sets the value of the '{@link org.iobserve.model.correspondence.AllocationEntry#getTechnology <em>Technology</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Technology</em>' attribute.
     * @see org.iobserve.model.correspondence.EServiceTechnology
     * @see #getTechnology()
     * @generated
     */
    void setTechnology(EServiceTechnology value);

} // AllocationEntry
