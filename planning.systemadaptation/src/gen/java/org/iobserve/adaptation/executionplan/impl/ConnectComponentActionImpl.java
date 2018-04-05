/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.iobserve.adaptation.executionplan.ConnectComponentAction;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Connect Component Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.ConnectComponentActionImpl#getProvidingAllocationContexts <em>Providing Allocation Contexts</em>}</li>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.ConnectComponentActionImpl#getRequiringAllocationContexts <em>Requiring Allocation Contexts</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConnectComponentActionImpl extends AssemblyContextActionImpl implements ConnectComponentAction {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConnectComponentActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExecutionplanPackage.Literals.CONNECT_COMPONENT_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<AllocationContext> getProvidingAllocationContexts() {
        return (EList<AllocationContext>)eGet(ExecutionplanPackage.Literals.CONNECT_COMPONENT_ACTION__PROVIDING_ALLOCATION_CONTEXTS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<AllocationContext> getRequiringAllocationContexts() {
        return (EList<AllocationContext>)eGet(ExecutionplanPackage.Literals.CONNECT_COMPONENT_ACTION__REQUIRING_ALLOCATION_CONTEXTS, true);
    }

} //ConnectComponentActionImpl
