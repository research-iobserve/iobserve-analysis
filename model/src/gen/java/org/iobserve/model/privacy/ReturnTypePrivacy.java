/**
 */
package org.iobserve.model.privacy;

import org.palladiosimulator.pcm.repository.OperationSignature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Return Type Privacy</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.privacy.ReturnTypePrivacy#getOperationSignature <em>Operation Signature</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.privacy.PrivacyPackage#getReturnTypePrivacy()
 * @model
 * @generated
 */
public interface ReturnTypePrivacy extends IPrivacyAnnotation {
    /**
     * Returns the value of the '<em><b>Operation Signature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operation Signature</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operation Signature</em>' reference.
     * @see #setOperationSignature(OperationSignature)
     * @see org.iobserve.model.privacy.PrivacyPackage#getReturnTypePrivacy_OperationSignature()
     * @model required="true"
     * @generated
     */
    OperationSignature getOperationSignature();

    /**
     * Sets the value of the '{@link org.iobserve.model.privacy.ReturnTypePrivacy#getOperationSignature <em>Operation Signature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation Signature</em>' reference.
     * @see #getOperationSignature()
     * @generated
     */
    void setOperationSignature(OperationSignature value);

} // ReturnTypePrivacy
