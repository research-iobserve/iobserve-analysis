/**
 */
package org.iobserve.model.correspondence;

import org.palladiosimulator.pcm.repository.OperationSignature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.OperationEntry#getOperation <em>Operation</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence.CorrespondencePackage#getOperationEntry()
 * @model
 * @generated
 */
public interface OperationEntry extends AbstractEntry {
    /**
     * Returns the value of the '<em><b>Operation</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operation</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operation</em>' reference.
     * @see #setOperation(OperationSignature)
     * @see org.iobserve.model.correspondence.CorrespondencePackage#getOperationEntry_Operation()
     * @model required="true"
     * @generated
     */
    OperationSignature getOperation();

    /**
     * Sets the value of the '{@link org.iobserve.model.correspondence.OperationEntry#getOperation <em>Operation</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation</em>' reference.
     * @see #getOperation()
     * @generated
     */
    void setOperation(OperationSignature value);

} // OperationEntry
