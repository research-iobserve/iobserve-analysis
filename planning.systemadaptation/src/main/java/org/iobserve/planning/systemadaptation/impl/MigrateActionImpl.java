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
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl#getNewAllocatinContext <em>New Allocatin Context</em>}</li>
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
	public AllocationContext getNewAllocatinContext() {
		return (AllocationContext)eGet(systemadaptationPackage.Literals.MIGRATE_ACTION__NEW_ALLOCATIN_CONTEXT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewAllocatinContext(AllocationContext newNewAllocatinContext) {
		eSet(systemadaptationPackage.Literals.MIGRATE_ACTION__NEW_ALLOCATIN_CONTEXT, newNewAllocatinContext);
	}

} //MigrateActionImpl
