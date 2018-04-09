/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.SystemadaptationPackage;

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Migrate Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl#getSourceAllocationContext <em>Source Allocation Context</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl#getSourceProvidingAllocationContexts <em>Source Providing Allocation Contexts</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl#getSourceRequiringAllocationContexts <em>Source Requiring Allocation Contexts</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MigrateActionImpl extends AssemblyContextActionImpl implements MigrateAction {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MigrateActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SystemadaptationPackage.Literals.MIGRATE_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllocationContext getSourceAllocationContext() {
        return (AllocationContext)eGet(SystemadaptationPackage.Literals.MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSourceAllocationContext(AllocationContext newSourceAllocationContext) {
        eSet(SystemadaptationPackage.Literals.MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT, newSourceAllocationContext);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<AllocationContext> getSourceProvidingAllocationContexts() {
        return (EList<AllocationContext>)eGet(SystemadaptationPackage.Literals.MIGRATE_ACTION__SOURCE_PROVIDING_ALLOCATION_CONTEXTS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<AllocationContext> getSourceRequiringAllocationContexts() {
        return (EList<AllocationContext>)eGet(SystemadaptationPackage.Literals.MIGRATE_ACTION__SOURCE_REQUIRING_ALLOCATION_CONTEXTS, true);
    }

} //MigrateActionImpl
