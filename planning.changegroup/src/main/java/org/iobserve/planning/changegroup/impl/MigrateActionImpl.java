/**
 */
package org.iobserve.planning.changegroup.impl;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.changegroup.ChangegroupPackage;
import org.iobserve.planning.changegroup.MigrateAction;

import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Migrate Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.changegroup.impl.MigrateActionImpl#getMigrationTargetContainer <em>Migration Target Container</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MigrateActionImpl extends ActionImpl implements MigrateAction {
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
		return ChangegroupPackage.Literals.MIGRATE_ACTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceContainerCloud getMigrationTargetContainer() {
		return (ResourceContainerCloud)eGet(ChangegroupPackage.Literals.MIGRATE_ACTION__MIGRATION_TARGET_CONTAINER, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMigrationTargetContainer(ResourceContainerCloud newMigrationTargetContainer) {
		eSet(ChangegroupPackage.Literals.MIGRATE_ACTION__MIGRATION_TARGET_CONTAINER, newMigrationTargetContainer);
	}

} //MigrateActionImpl
