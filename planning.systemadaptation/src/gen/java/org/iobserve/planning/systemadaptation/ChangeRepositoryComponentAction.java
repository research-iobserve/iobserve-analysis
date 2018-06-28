/**
 */
package org.iobserve.planning.systemadaptation;

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Repository Component Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction#getSourceAllocationContext <em>Source Allocation Context</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getChangeRepositoryComponentAction()
 * @model
 * @generated
 */
public interface ChangeRepositoryComponentAction extends AssemblyContextAction {
    /**
     * Returns the value of the '<em><b>Source Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source Allocation Context</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source Allocation Context</em>' reference.
     * @see #setSourceAllocationContext(AllocationContext)
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getChangeRepositoryComponentAction_SourceAllocationContext()
     * @model required="true"
     * @generated
     */
    AllocationContext getSourceAllocationContext();

    /**
     * Sets the value of the '{@link org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction#getSourceAllocationContext <em>Source Allocation Context</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source Allocation Context</em>' reference.
     * @see #getSourceAllocationContext()
     * @generated
     */
    void setSourceAllocationContext(AllocationContext value);

} // ChangeRepositoryComponentAction
