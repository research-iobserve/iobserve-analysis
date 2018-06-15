/**
 */
package org.iobserve.model.correspondence;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.AbstractEntry#getImplementationId <em>Implementation Id</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence.CorrespondencePackage#getAbstractEntry()
 * @model abstract="true"
 * @generated
 */
public interface AbstractEntry extends EObject {
    /**
     * Returns the value of the '<em><b>Implementation Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Implementation Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Implementation Id</em>' attribute.
     * @see #setImplementationId(String)
     * @see org.iobserve.model.correspondence.CorrespondencePackage#getAbstractEntry_ImplementationId()
     * @model
     * @generated
     */
    String getImplementationId();

    /**
     * Sets the value of the '{@link org.iobserve.model.correspondence.AbstractEntry#getImplementationId <em>Implementation Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Implementation Id</em>' attribute.
     * @see #getImplementationId()
     * @generated
     */
    void setImplementationId(String value);

} // AbstractEntry
