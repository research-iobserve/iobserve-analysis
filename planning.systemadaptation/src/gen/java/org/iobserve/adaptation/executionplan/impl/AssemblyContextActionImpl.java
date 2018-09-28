/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.adaptation.executionplan.AssemblyContextAction;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Assembly Context Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.AssemblyContextActionImpl#getTargetAllocationContext <em>Target Allocation Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AssemblyContextActionImpl extends AtomicActionImpl implements AssemblyContextAction {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AssemblyContextActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExecutionplanPackage.Literals.ASSEMBLY_CONTEXT_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllocationContext getTargetAllocationContext() {
        return (AllocationContext)eGet(ExecutionplanPackage.Literals.ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTargetAllocationContext(AllocationContext newTargetAllocationContext) {
        eSet(ExecutionplanPackage.Literals.ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT, newTargetAllocationContext);
    }

} //AssemblyContextActionImpl
