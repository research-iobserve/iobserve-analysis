/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.SystemadaptationPackage;

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Assembly Context Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl#getTargetAllocationContext <em>Target Allocation Context</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl#getTargetProvidingAllocationContexts <em>Target Providing Allocation Contexts</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl#getTargetRequiringAllocationContexts <em>Target Requiring Allocation Contexts</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AssemblyContextActionImpl extends ComposedActionImpl implements AssemblyContextAction {
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
        return SystemadaptationPackage.Literals.ASSEMBLY_CONTEXT_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllocationContext getTargetAllocationContext() {
        return (AllocationContext)eGet(SystemadaptationPackage.Literals.ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTargetAllocationContext(AllocationContext newTargetAllocationContext) {
        eSet(SystemadaptationPackage.Literals.ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT, newTargetAllocationContext);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<AllocationContext> getTargetProvidingAllocationContexts() {
        return (EList<AllocationContext>)eGet(SystemadaptationPackage.Literals.ASSEMBLY_CONTEXT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<AllocationContext> getTargetRequiringAllocationContexts() {
        return (EList<AllocationContext>)eGet(SystemadaptationPackage.Literals.ASSEMBLY_CONTEXT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS, true);
    }

} //AssemblyContextActionImpl
