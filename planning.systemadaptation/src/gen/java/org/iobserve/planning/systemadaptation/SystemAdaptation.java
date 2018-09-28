/**
 */
package org.iobserve.planning.systemadaptation;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>System Adaptation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.SystemAdaptation#getActions <em>Actions</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getSystemAdaptation()
 * @model
 * @generated
 */
public interface SystemAdaptation extends EObject {
    /**
     * Returns the value of the '<em><b>Actions</b></em>' containment reference list.
     * The list contents are of type {@link org.iobserve.planning.systemadaptation.ComposedAction}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Actions</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Actions</em>' containment reference list.
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getSystemAdaptation_Actions()
     * @model containment="true"
     * @generated
     */
    EList<ComposedAction> getActions();

} // SystemAdaptation
