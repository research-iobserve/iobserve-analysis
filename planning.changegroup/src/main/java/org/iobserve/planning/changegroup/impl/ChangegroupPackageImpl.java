/**
 */
package org.iobserve.planning.changegroup.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.iobserve.planning.changegroup.Action;
import org.iobserve.planning.changegroup.AllocateAction;
import org.iobserve.planning.changegroup.ChangeGroup;
import org.iobserve.planning.changegroup.ChangeGroupRepository;
import org.iobserve.planning.changegroup.ChangegroupFactory;
import org.iobserve.planning.changegroup.ChangegroupPackage;
import org.iobserve.planning.changegroup.ConfigureAction;
import org.iobserve.planning.changegroup.MigrateAction;
import org.iobserve.planning.changegroup.ReplicateAction;

import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceenvironmentcloudPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ChangegroupPackageImpl extends EPackageImpl implements ChangegroupPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass actionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass replicateActionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass migrateActionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeGroupRepositoryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass allocateActionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass configureActionEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.iobserve.planning.changegroup.ChangegroupPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private ChangegroupPackageImpl() {
		super(eNS_URI, ChangegroupFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link ChangegroupPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static ChangegroupPackage init() {
		if (isInited) return (ChangegroupPackage)EPackage.Registry.INSTANCE.getEPackage(ChangegroupPackage.eNS_URI);

		// Obtain or create and register package
		ChangegroupPackageImpl theChangegroupPackage = (ChangegroupPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ChangegroupPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ChangegroupPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		ResourceenvironmentcloudPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theChangegroupPackage.createPackageContents();

		// Initialize created meta-data
		theChangegroupPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theChangegroupPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(ChangegroupPackage.eNS_URI, theChangegroupPackage);
		return theChangegroupPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChangeGroup() {
		return changeGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeGroup_Actions() {
		return (EReference)changeGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAction() {
		return actionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAction_ResourceContainer() {
		return (EReference)actionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getReplicateAction() {
		return replicateActionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReplicateAction_NewCount() {
		return (EAttribute)replicateActionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReplicateAction_OldCount() {
		return (EAttribute)replicateActionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMigrateAction() {
		return migrateActionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMigrateAction_MigrationTargetContainer() {
		return (EReference)migrateActionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChangeGroupRepository() {
		return changeGroupRepositoryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeGroupRepository_ChangeGroups() {
		return (EReference)changeGroupRepositoryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAllocateAction() {
		return allocateActionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConfigureAction() {
		return configureActionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangegroupFactory getChangegroupFactory() {
		return (ChangegroupFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		changeGroupEClass = createEClass(CHANGE_GROUP);
		createEReference(changeGroupEClass, CHANGE_GROUP__ACTIONS);

		actionEClass = createEClass(ACTION);
		createEReference(actionEClass, ACTION__RESOURCE_CONTAINER);

		replicateActionEClass = createEClass(REPLICATE_ACTION);
		createEAttribute(replicateActionEClass, REPLICATE_ACTION__NEW_COUNT);
		createEAttribute(replicateActionEClass, REPLICATE_ACTION__OLD_COUNT);

		migrateActionEClass = createEClass(MIGRATE_ACTION);
		createEReference(migrateActionEClass, MIGRATE_ACTION__MIGRATION_TARGET_CONTAINER);

		changeGroupRepositoryEClass = createEClass(CHANGE_GROUP_REPOSITORY);
		createEReference(changeGroupRepositoryEClass, CHANGE_GROUP_REPOSITORY__CHANGE_GROUPS);

		allocateActionEClass = createEClass(ALLOCATE_ACTION);

		configureActionEClass = createEClass(CONFIGURE_ACTION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		ResourceenvironmentcloudPackage theResourceenvironmentcloudPackage = (ResourceenvironmentcloudPackage)EPackage.Registry.INSTANCE.getEPackage(ResourceenvironmentcloudPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		replicateActionEClass.getESuperTypes().add(this.getAction());
		migrateActionEClass.getESuperTypes().add(this.getAction());
		allocateActionEClass.getESuperTypes().add(this.getAction());
		configureActionEClass.getESuperTypes().add(this.getAction());

		// Initialize classes and features; add operations and parameters
		initEClass(changeGroupEClass, ChangeGroup.class, "ChangeGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeGroup_Actions(), this.getAction(), null, "actions", null, 0, -1, ChangeGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(actionEClass, Action.class, "Action", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAction_ResourceContainer(), theResourceenvironmentcloudPackage.getResourceContainerCloud(), null, "resourceContainer", null, 1, 1, Action.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(replicateActionEClass, ReplicateAction.class, "ReplicateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getReplicateAction_NewCount(), ecorePackage.getEInt(), "newCount", "0", 0, -1, ReplicateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReplicateAction_OldCount(), ecorePackage.getEInt(), "oldCount", "0", 0, -1, ReplicateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(migrateActionEClass, MigrateAction.class, "MigrateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMigrateAction_MigrationTargetContainer(), theResourceenvironmentcloudPackage.getResourceContainerCloud(), null, "migrationTargetContainer", null, 1, 1, MigrateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeGroupRepositoryEClass, ChangeGroupRepository.class, "ChangeGroupRepository", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeGroupRepository_ChangeGroups(), this.getChangeGroup(), null, "changeGroups", null, 0, -1, ChangeGroupRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(allocateActionEClass, AllocateAction.class, "AllocateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(configureActionEClass, ConfigureAction.class, "ConfigureAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //ChangegroupPackageImpl
