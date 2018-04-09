/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.iobserve.adaptation.executionplan.BlockRequestsToComponentAction;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;
import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Block Requests To Component Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.BlockRequestsToComponentActionImpl#getTargetRequiringAllocationContexts <em>Target Requiring Allocation Contexts</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BlockRequestsToComponentActionImpl extends AssemblyContextActionImpl implements BlockRequestsToComponentAction {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected BlockRequestsToComponentActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExecutionplanPackage.Literals.BLOCK_REQUESTS_TO_COMPONENT_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<AllocationContext> getTargetRequiringAllocationContexts() {
        return (EList<AllocationContext>)eGet(ExecutionplanPackage.Literals.BLOCK_REQUESTS_TO_COMPONENT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS, true);
    }

} //BlockRequestsToComponentActionImpl
