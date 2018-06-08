/**
 */
package org.iobserve.model.test.storage;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Other</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.test.storage.Other#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.test.storage.StoragePackage#getOther()
 * @model
 * @generated
 */
public interface Other extends EObject {
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.iobserve.model.test.storage.StoragePackage#getOther_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.iobserve.model.test.storage.Other#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

} // Other
