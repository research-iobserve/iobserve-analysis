/**
 */
package org.iobserve.model.test.storage.two;

import org.eclipse.emf.ecore.EObject;

import org.iobserve.model.test.storage.one.Other;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Link</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.test.storage.two.Link#getReference <em>Reference</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.test.storage.two.TwoPackage#getLink()
 * @model
 * @generated
 */
public interface Link extends EObject {
    /**
     * Returns the value of the '<em><b>Reference</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Reference</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Reference</em>' reference.
     * @see #setReference(Other)
     * @see org.iobserve.model.test.storage.two.TwoPackage#getLink_Reference()
     * @model
     * @generated
     */
    Other getReference();

    /**
     * Sets the value of the '{@link org.iobserve.model.test.storage.two.Link#getReference <em>Reference</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference</em>' reference.
     * @see #getReference()
     * @generated
     */
    void setReference(Other value);

} // Link
