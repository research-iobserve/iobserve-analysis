/**
 */
package org.iobserve.planning.changegroup;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.iobserve.planning.changegroup.ChangegroupFactory
 * @model kind="package"
 * @generated
 */
public interface ChangegroupPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "changegroup";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://iobserve.org/ChangeGroup";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.iobserve";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ChangegroupPackage eINSTANCE = org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.iobserve.planning.changegroup.impl.ChangeGroupImpl <em>Change Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.changegroup.impl.ChangeGroupImpl
	 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getChangeGroup()
	 * @generated
	 */
	int CHANGE_GROUP = 0;

	/**
	 * The feature id for the '<em><b>Actions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_GROUP__ACTIONS = 0;

	/**
	 * The number of structural features of the '<em>Change Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_GROUP_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.changegroup.impl.ActionImpl <em>Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.changegroup.impl.ActionImpl
	 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getAction()
	 * @generated
	 */
	int ACTION = 1;

	/**
	 * The feature id for the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION__RESOURCE_CONTAINER = 0;

	/**
	 * The number of structural features of the '<em>Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACTION_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.changegroup.impl.ReplicateActionImpl <em>Replicate Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.changegroup.impl.ReplicateActionImpl
	 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getReplicateAction()
	 * @generated
	 */
	int REPLICATE_ACTION = 2;

	/**
	 * The feature id for the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPLICATE_ACTION__RESOURCE_CONTAINER = ACTION__RESOURCE_CONTAINER;

	/**
	 * The feature id for the '<em><b>New Count</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPLICATE_ACTION__NEW_COUNT = ACTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Old Count</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPLICATE_ACTION__OLD_COUNT = ACTION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Replicate Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPLICATE_ACTION_FEATURE_COUNT = ACTION_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.changegroup.impl.MigrateActionImpl <em>Migrate Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.changegroup.impl.MigrateActionImpl
	 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getMigrateAction()
	 * @generated
	 */
	int MIGRATE_ACTION = 3;

	/**
	 * The feature id for the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATE_ACTION__RESOURCE_CONTAINER = ACTION__RESOURCE_CONTAINER;

	/**
	 * The feature id for the '<em><b>Migration Target Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATE_ACTION__MIGRATION_TARGET_CONTAINER = ACTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Migrate Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATE_ACTION_FEATURE_COUNT = ACTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.changegroup.impl.ChangeGroupRepositoryImpl <em>Change Group Repository</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.changegroup.impl.ChangeGroupRepositoryImpl
	 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getChangeGroupRepository()
	 * @generated
	 */
	int CHANGE_GROUP_REPOSITORY = 4;

	/**
	 * The feature id for the '<em><b>Change Groups</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_GROUP_REPOSITORY__CHANGE_GROUPS = 0;

	/**
	 * The number of structural features of the '<em>Change Group Repository</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_GROUP_REPOSITORY_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.changegroup.impl.AllocateActionImpl <em>Allocate Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.changegroup.impl.AllocateActionImpl
	 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getAllocateAction()
	 * @generated
	 */
	int ALLOCATE_ACTION = 5;

	/**
	 * The feature id for the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALLOCATE_ACTION__RESOURCE_CONTAINER = ACTION__RESOURCE_CONTAINER;

	/**
	 * The number of structural features of the '<em>Allocate Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALLOCATE_ACTION_FEATURE_COUNT = ACTION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.changegroup.impl.ConfigureActionImpl <em>Configure Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.changegroup.impl.ConfigureActionImpl
	 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getConfigureAction()
	 * @generated
	 */
	int CONFIGURE_ACTION = 6;

	/**
	 * The feature id for the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURE_ACTION__RESOURCE_CONTAINER = ACTION__RESOURCE_CONTAINER;

	/**
	 * The number of structural features of the '<em>Configure Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURE_ACTION_FEATURE_COUNT = ACTION_FEATURE_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.changegroup.ChangeGroup <em>Change Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Group</em>'.
	 * @see org.iobserve.planning.changegroup.ChangeGroup
	 * @generated
	 */
	EClass getChangeGroup();

	/**
	 * Returns the meta object for the containment reference list '{@link org.iobserve.planning.changegroup.ChangeGroup#getActions <em>Actions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Actions</em>'.
	 * @see org.iobserve.planning.changegroup.ChangeGroup#getActions()
	 * @see #getChangeGroup()
	 * @generated
	 */
	EReference getChangeGroup_Actions();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.changegroup.Action <em>Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Action</em>'.
	 * @see org.iobserve.planning.changegroup.Action
	 * @generated
	 */
	EClass getAction();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.planning.changegroup.Action#getResourceContainer <em>Resource Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Resource Container</em>'.
	 * @see org.iobserve.planning.changegroup.Action#getResourceContainer()
	 * @see #getAction()
	 * @generated
	 */
	EReference getAction_ResourceContainer();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.changegroup.ReplicateAction <em>Replicate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Replicate Action</em>'.
	 * @see org.iobserve.planning.changegroup.ReplicateAction
	 * @generated
	 */
	EClass getReplicateAction();

	/**
	 * Returns the meta object for the attribute list '{@link org.iobserve.planning.changegroup.ReplicateAction#getNewCount <em>New Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>New Count</em>'.
	 * @see org.iobserve.planning.changegroup.ReplicateAction#getNewCount()
	 * @see #getReplicateAction()
	 * @generated
	 */
	EAttribute getReplicateAction_NewCount();

	/**
	 * Returns the meta object for the attribute list '{@link org.iobserve.planning.changegroup.ReplicateAction#getOldCount <em>Old Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Old Count</em>'.
	 * @see org.iobserve.planning.changegroup.ReplicateAction#getOldCount()
	 * @see #getReplicateAction()
	 * @generated
	 */
	EAttribute getReplicateAction_OldCount();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.changegroup.MigrateAction <em>Migrate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Migrate Action</em>'.
	 * @see org.iobserve.planning.changegroup.MigrateAction
	 * @generated
	 */
	EClass getMigrateAction();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.planning.changegroup.MigrateAction#getMigrationTargetContainer <em>Migration Target Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Migration Target Container</em>'.
	 * @see org.iobserve.planning.changegroup.MigrateAction#getMigrationTargetContainer()
	 * @see #getMigrateAction()
	 * @generated
	 */
	EReference getMigrateAction_MigrationTargetContainer();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.changegroup.ChangeGroupRepository <em>Change Group Repository</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Group Repository</em>'.
	 * @see org.iobserve.planning.changegroup.ChangeGroupRepository
	 * @generated
	 */
	EClass getChangeGroupRepository();

	/**
	 * Returns the meta object for the reference list '{@link org.iobserve.planning.changegroup.ChangeGroupRepository#getChangeGroups <em>Change Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Change Groups</em>'.
	 * @see org.iobserve.planning.changegroup.ChangeGroupRepository#getChangeGroups()
	 * @see #getChangeGroupRepository()
	 * @generated
	 */
	EReference getChangeGroupRepository_ChangeGroups();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.changegroup.AllocateAction <em>Allocate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Allocate Action</em>'.
	 * @see org.iobserve.planning.changegroup.AllocateAction
	 * @generated
	 */
	EClass getAllocateAction();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.changegroup.ConfigureAction <em>Configure Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Configure Action</em>'.
	 * @see org.iobserve.planning.changegroup.ConfigureAction
	 * @generated
	 */
	EClass getConfigureAction();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ChangegroupFactory getChangegroupFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.iobserve.planning.changegroup.impl.ChangeGroupImpl <em>Change Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.changegroup.impl.ChangeGroupImpl
		 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getChangeGroup()
		 * @generated
		 */
		EClass CHANGE_GROUP = eINSTANCE.getChangeGroup();

		/**
		 * The meta object literal for the '<em><b>Actions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_GROUP__ACTIONS = eINSTANCE.getChangeGroup_Actions();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.changegroup.impl.ActionImpl <em>Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.changegroup.impl.ActionImpl
		 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getAction()
		 * @generated
		 */
		EClass ACTION = eINSTANCE.getAction();

		/**
		 * The meta object literal for the '<em><b>Resource Container</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ACTION__RESOURCE_CONTAINER = eINSTANCE.getAction_ResourceContainer();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.changegroup.impl.ReplicateActionImpl <em>Replicate Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.changegroup.impl.ReplicateActionImpl
		 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getReplicateAction()
		 * @generated
		 */
		EClass REPLICATE_ACTION = eINSTANCE.getReplicateAction();

		/**
		 * The meta object literal for the '<em><b>New Count</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REPLICATE_ACTION__NEW_COUNT = eINSTANCE.getReplicateAction_NewCount();

		/**
		 * The meta object literal for the '<em><b>Old Count</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REPLICATE_ACTION__OLD_COUNT = eINSTANCE.getReplicateAction_OldCount();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.changegroup.impl.MigrateActionImpl <em>Migrate Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.changegroup.impl.MigrateActionImpl
		 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getMigrateAction()
		 * @generated
		 */
		EClass MIGRATE_ACTION = eINSTANCE.getMigrateAction();

		/**
		 * The meta object literal for the '<em><b>Migration Target Container</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MIGRATE_ACTION__MIGRATION_TARGET_CONTAINER = eINSTANCE.getMigrateAction_MigrationTargetContainer();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.changegroup.impl.ChangeGroupRepositoryImpl <em>Change Group Repository</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.changegroup.impl.ChangeGroupRepositoryImpl
		 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getChangeGroupRepository()
		 * @generated
		 */
		EClass CHANGE_GROUP_REPOSITORY = eINSTANCE.getChangeGroupRepository();

		/**
		 * The meta object literal for the '<em><b>Change Groups</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_GROUP_REPOSITORY__CHANGE_GROUPS = eINSTANCE.getChangeGroupRepository_ChangeGroups();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.changegroup.impl.AllocateActionImpl <em>Allocate Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.changegroup.impl.AllocateActionImpl
		 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getAllocateAction()
		 * @generated
		 */
		EClass ALLOCATE_ACTION = eINSTANCE.getAllocateAction();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.changegroup.impl.ConfigureActionImpl <em>Configure Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.changegroup.impl.ConfigureActionImpl
		 * @see org.iobserve.planning.changegroup.impl.ChangegroupPackageImpl#getConfigureAction()
		 * @generated
		 */
		EClass CONFIGURE_ACTION = eINSTANCE.getConfigureAction();

	}

} //ChangegroupPackage
