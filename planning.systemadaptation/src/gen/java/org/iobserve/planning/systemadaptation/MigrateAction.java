/**
 */
package org.iobserve.planning.systemadaptation;

import org.eclipse.emf.common.util.EList;

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Migrate Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.MigrateAction#getSourceAllocationContext <em>Source Allocation Context</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.MigrateAction#getSourceProvidingAllocationContexts <em>Source Providing Allocation Contexts</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.MigrateAction#getSourceRequiringAllocationContexts <em>Source Requiring Allocation Contexts</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getMigrateAction()
 * @model
 * @generated
 */
public interface MigrateAction extends AssemblyContextAction {
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
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getMigrateAction_SourceAllocationContext()
     * @model required="true"
     * @generated
     */
    AllocationContext getSourceAllocationContext();

    /**
     * Sets the value of the '{@link org.iobserve.planning.systemadaptation.MigrateAction#getSourceAllocationContext <em>Source Allocation Context</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source Allocation Context</em>' reference.
     * @see #getSourceAllocationContext()
     * @generated
     */
    void setSourceAllocationContext(AllocationContext value);

    /**
     * Returns the value of the '<em><b>Source Providing Allocation Contexts</b></em>' reference list.
     * The list contents are of type {@link org.palladiosimulator.pcm.allocation.AllocationContext}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source Providing Allocation Contexts</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source Providing Allocation Contexts</em>' reference list.
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getMigrateAction_SourceProvidingAllocationContexts()
     * @model
     * @generated
     */
    EList<AllocationContext> getSourceProvidingAllocationContexts();

    /**
     * Returns the value of the '<em><b>Source Requiring Allocation Contexts</b></em>' reference list.
     * The list contents are of type {@link org.palladiosimulator.pcm.allocation.AllocationContext}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source Requiring Allocation Contexts</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source Requiring Allocation Contexts</em>' reference list.
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getMigrateAction_SourceRequiringAllocationContexts()
     * @model
     * @generated
     */
    EList<AllocationContext> getSourceRequiringAllocationContexts();

} // MigrateAction
