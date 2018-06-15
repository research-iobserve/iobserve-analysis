/**
 */
package org.iobserve.model.test.storage;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Other Interface</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.test.storage.OtherInterface#getNext <em>Next</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.test.storage.StoragePackage#getOtherInterface()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface OtherInterface extends EObject {

    /**
     * Returns the value of the '<em><b>Next</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Next</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Next</em>' containment reference.
     * @see #setNext(OtherInterface)
     * @see org.iobserve.model.test.storage.StoragePackage#getOtherInterface_Next()
     * @model containment="true"
     * @generated
     */
    OtherInterface getNext();

    /**
     * Sets the value of the '{@link org.iobserve.model.test.storage.OtherInterface#getNext <em>Next</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Next</em>' containment reference.
     * @see #getNext()
     * @generated
     */
    void setNext(OtherInterface value);
} // OtherInterface
