/**
 */
package org.iobserve.model.correspondence;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Part</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.Part#getModelType <em>Model Type</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence.Part#getEntries <em>Entries</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence.CorrespondencePackage#getPart()
 * @model
 * @generated
 */
public interface Part extends EObject {
    /**
     * Returns the value of the '<em><b>Model Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Model Type</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Model Type</em>' reference.
     * @see #setModelType(EObject)
     * @see org.iobserve.model.correspondence.CorrespondencePackage#getPart_ModelType()
     * @model required="true"
     * @generated
     */
    EObject getModelType();

    /**
     * Sets the value of the '{@link org.iobserve.model.correspondence.Part#getModelType <em>Model Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Model Type</em>' reference.
     * @see #getModelType()
     * @generated
     */
    void setModelType(EObject value);

    /**
     * Returns the value of the '<em><b>Entries</b></em>' containment reference list.
     * The list contents are of type {@link org.iobserve.model.correspondence.AbstractEntry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Entries</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Entries</em>' containment reference list.
     * @see org.iobserve.model.correspondence.CorrespondencePackage#getPart_Entries()
     * @model containment="true"
     * @generated
     */
    EList<AbstractEntry> getEntries();

} // Part
