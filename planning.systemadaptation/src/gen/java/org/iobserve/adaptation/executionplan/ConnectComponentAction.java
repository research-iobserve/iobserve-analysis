/**
 */
package org.iobserve.adaptation.executionplan;

import org.eclipse.emf.common.util.EList;

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connect Component Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.ConnectComponentAction#getProvidingAllocationContexts <em>Providing Allocation Contexts</em>}</li>
 *   <li>{@link org.iobserve.adaptation.executionplan.ConnectComponentAction#getRequiringAllocationContexts <em>Requiring Allocation Contexts</em>}</li>
 * </ul>
 *
 * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getConnectComponentAction()
 * @model
 * @generated
 */
public interface ConnectComponentAction extends AssemblyContextAction {
    /**
     * Returns the value of the '<em><b>Providing Allocation Contexts</b></em>' reference list.
     * The list contents are of type {@link org.palladiosimulator.pcm.allocation.AllocationContext}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Providing Allocation Contexts</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Providing Allocation Contexts</em>' reference list.
     * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getConnectComponentAction_ProvidingAllocationContexts()
     * @model
     * @generated
     */
    EList<AllocationContext> getProvidingAllocationContexts();

    /**
     * Returns the value of the '<em><b>Requiring Allocation Contexts</b></em>' reference list.
     * The list contents are of type {@link org.palladiosimulator.pcm.allocation.AllocationContext}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Requiring Allocation Contexts</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Requiring Allocation Contexts</em>' reference list.
     * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getConnectComponentAction_RequiringAllocationContexts()
     * @model
     * @generated
     */
    EList<AllocationContext> getRequiringAllocationContexts();

} // ConnectComponentAction
