/**
 */
package org.iobserve.model.correspondence;

import org.palladiosimulator.pcm.repository.DataType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Type Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.DataTypeEntry#getDataTypeEntry <em>Data Type Entry</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence.CorrespondencePackage#getDataTypeEntry()
 * @model
 * @generated
 */
public interface DataTypeEntry extends AbstractEntry {
    /**
     * Returns the value of the '<em><b>Data Type Entry</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Data Type Entry</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Data Type Entry</em>' reference.
     * @see #setDataTypeEntry(DataType)
     * @see org.iobserve.model.correspondence.CorrespondencePackage#getDataTypeEntry_DataTypeEntry()
     * @model required="true"
     * @generated
     */
    DataType getDataTypeEntry();

    /**
     * Sets the value of the '{@link org.iobserve.model.correspondence.DataTypeEntry#getDataTypeEntry <em>Data Type Entry</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Type Entry</em>' reference.
     * @see #getDataTypeEntry()
     * @generated
     */
    void setDataTypeEntry(DataType value);

} // DataTypeEntry
