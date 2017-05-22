/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.systemadaptationPackage;

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Migrate Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl#getNewAllocationContext <em>New Allocation Context</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl#getSourceAllocationContext <em>Source Allocation Context</em>}</li>
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
		return systemadaptationPackage.Literals.MIGRATE_ACTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AllocationContext getNewAllocationContext() {
		return (AllocationContext)eGet(systemadaptationPackage.Literals.MIGRATE_ACTION__NEW_ALLOCATION_CONTEXT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewAllocationContext(AllocationContext newNewAllocationContext) {
		eSet(systemadaptationPackage.Literals.MIGRATE_ACTION__NEW_ALLOCATION_CONTEXT, newNewAllocationContext);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AllocationContext getSourceAllocationContext() {
		return (AllocationContext)eGet(systemadaptationPackage.Literals.MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSourceAllocationContext(AllocationContext newSourceAllocationContext) {
		eSet(systemadaptationPackage.Literals.MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT, newSourceAllocationContext);
	}

} //MigrateActionImpl
