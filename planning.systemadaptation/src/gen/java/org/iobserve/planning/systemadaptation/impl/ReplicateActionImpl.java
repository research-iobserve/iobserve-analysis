/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.SystemadaptationPackage;

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Replicate Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl#getSourceAllocationContext <em>Source Allocation Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReplicateActionImpl extends AssemblyContextActionImpl implements ReplicateAction {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ReplicateActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SystemadaptationPackage.Literals.REPLICATE_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllocationContext getSourceAllocationContext() {
        return (AllocationContext)eGet(SystemadaptationPackage.Literals.REPLICATE_ACTION__SOURCE_ALLOCATION_CONTEXT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSourceAllocationContext(AllocationContext newSourceAllocationContext) {
        eSet(SystemadaptationPackage.Literals.REPLICATE_ACTION__SOURCE_ALLOCATION_CONTEXT, newSourceAllocationContext);
    }

} //ReplicateActionImpl
