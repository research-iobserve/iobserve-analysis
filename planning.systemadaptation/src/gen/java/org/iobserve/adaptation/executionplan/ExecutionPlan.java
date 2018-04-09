/**
 */
package org.iobserve.adaptation.executionplan;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Execution Plan</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.ExecutionPlan#getActions <em>Actions</em>}</li>
 * </ul>
 *
 * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getExecutionPlan()
 * @model
 * @generated
 */
public interface ExecutionPlan extends EObject {
    /**
     * Returns the value of the '<em><b>Actions</b></em>' containment reference list.
     * The list contents are of type {@link org.iobserve.adaptation.executionplan.AtomicAction}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Actions</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Actions</em>' containment reference list.
     * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getExecutionPlan_Actions()
     * @model containment="true"
     * @generated
     */
    EList<AtomicAction> getActions();

} // ExecutionPlan
