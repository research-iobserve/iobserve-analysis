/**
 */
package org.iobserve.model.correspondence;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.CorrespondenceModel#getParts <em>Parts</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence.CorrespondencePackage#getCorrespondenceModel()
 * @model
 * @generated
 */
public interface CorrespondenceModel extends EObject {
    /**
     * Returns the value of the '<em><b>Parts</b></em>' containment reference list.
     * The list contents are of type {@link org.iobserve.model.correspondence.Part}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parts</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parts</em>' containment reference list.
     * @see org.iobserve.model.correspondence.CorrespondencePackage#getCorrespondenceModel_Parts()
     * @model containment="true"
     * @generated
     */
    EList<Part> getParts();

} // CorrespondenceModel
