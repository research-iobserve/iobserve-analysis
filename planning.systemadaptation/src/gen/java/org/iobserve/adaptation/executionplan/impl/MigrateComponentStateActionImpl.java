/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.ecore.EClass;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;
import org.iobserve.adaptation.executionplan.MigrateComponentStateAction;

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Migrate Component State Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.MigrateComponentStateActionImpl#getSourceAllocationContext <em>Source Allocation Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MigrateComponentStateActionImpl extends AssemblyContextActionImpl implements MigrateComponentStateAction {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MigrateComponentStateActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExecutionplanPackage.Literals.MIGRATE_COMPONENT_STATE_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllocationContext getSourceAllocationContext() {
        return (AllocationContext)eGet(ExecutionplanPackage.Literals.MIGRATE_COMPONENT_STATE_ACTION__SOURCE_ALLOCATION_CONTEXT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSourceAllocationContext(AllocationContext newSourceAllocationContext) {
        eSet(ExecutionplanPackage.Literals.MIGRATE_COMPONENT_STATE_ACTION__SOURCE_ALLOCATION_CONTEXT, newSourceAllocationContext);
    }

} //MigrateComponentStateActionImpl
