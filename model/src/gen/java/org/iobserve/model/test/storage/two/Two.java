/**
 */
package org.iobserve.model.test.storage.two;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Two</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.test.storage.two.Two#getLinks <em>Links</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.test.storage.two.TwoPackage#getTwo()
 * @model
 * @generated
 */
public interface Two extends EObject {
    /**
     * Returns the value of the '<em><b>Links</b></em>' containment reference list.
     * The list contents are of type {@link org.iobserve.model.test.storage.two.Link}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Links</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Links</em>' containment reference list.
     * @see org.iobserve.model.test.storage.two.TwoPackage#getTwo_Links()
     * @model containment="true"
     * @generated
     */
    EList<Link> getLinks();

} // Two
