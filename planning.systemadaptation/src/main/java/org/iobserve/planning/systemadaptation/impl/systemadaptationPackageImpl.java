/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.SystemAdaptation;
import org.iobserve.planning.systemadaptation.TerminateAction;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;
import org.iobserve.planning.systemadaptation.systemadaptationPackage;

import org.palladiosimulator.pcm.PcmPackage;

import org.palladiosimulator.pcm.allocation.AllocationPackage;

import org.palladiosimulator.pcm.core.composition.CompositionPackage;

import org.palladiosimulator.pcm.repository.RepositoryPackage;

import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class systemadaptationPackageImpl extends EPackageImpl implements systemadaptationPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass systemAdaptationEClass = null;

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
	private EClass assemblyContextActionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resourceContainerActionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass changeRepositoryComponentActionEClass = null;

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
	private EClass deallocateActionEClass = null;

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
	private EClass acquireActionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass terminateActionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass replicateActionEClass = null;

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
	 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private systemadaptationPackageImpl() {
		super(eNS_URI, systemadaptationFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link systemadaptationPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static systemadaptationPackage init() {
		if (isInited) return (systemadaptationPackage)EPackage.Registry.INSTANCE.getEPackage(systemadaptationPackage.eNS_URI);

		// Obtain or create and register package
		systemadaptationPackageImpl thesystemadaptationPackage = (systemadaptationPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof systemadaptationPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new systemadaptationPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		PcmPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		thesystemadaptationPackage.createPackageContents();

		// Initialize created meta-data
		thesystemadaptationPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thesystemadaptationPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(systemadaptationPackage.eNS_URI, thesystemadaptationPackage);
		return thesystemadaptationPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSystemAdaptation() {
		return systemAdaptationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSystemAdaptation_Actions() {
		return (EReference)systemAdaptationEClass.getEStructuralFeatures().get(0);
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
	public EClass getAssemblyContextAction() {
		return assemblyContextActionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAssemblyContextAction_SourceAssemblyContext() {
		return (EReference)assemblyContextActionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getResourceContainerAction() {
		return resourceContainerActionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getResourceContainerAction_SourceResourceContainer() {
		return (EReference)resourceContainerActionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getChangeRepositoryComponentAction() {
		return changeRepositoryComponentActionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getChangeRepositoryComponentAction_NewRepositoryComponent() {
		return (EReference)changeRepositoryComponentActionEClass.getEStructuralFeatures().get(0);
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
	public EReference getAllocateAction_NewAllocationContext() {
		return (EReference)allocateActionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDeallocateAction() {
		return deallocateActionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDeallocateAction_OldAllocationContext() {
		return (EReference)deallocateActionEClass.getEStructuralFeatures().get(0);
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
	public EReference getMigrateAction_NewAllocationContext() {
		return (EReference)migrateActionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMigrateAction_SourceAllocationContext() {
		return (EReference)migrateActionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAcquireAction() {
		return acquireActionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTerminateAction() {
		return terminateActionEClass;
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
	public EReference getReplicateAction_NewResourceContainer() {
		return (EReference)replicateActionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReplicateAction_SourceAllocationContext() {
		return (EReference)replicateActionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReplicateAction_NewAllocationContext() {
		return (EReference)replicateActionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public systemadaptationFactory getsystemadaptationFactory() {
		return (systemadaptationFactory)getEFactoryInstance();
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
		systemAdaptationEClass = createEClass(SYSTEM_ADAPTATION);
		createEReference(systemAdaptationEClass, SYSTEM_ADAPTATION__ACTIONS);

		actionEClass = createEClass(ACTION);
		createEReference(actionEClass, ACTION__RESOURCE_CONTAINER);

		assemblyContextActionEClass = createEClass(ASSEMBLY_CONTEXT_ACTION);
		createEReference(assemblyContextActionEClass, ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT);

		resourceContainerActionEClass = createEClass(RESOURCE_CONTAINER_ACTION);
		createEReference(resourceContainerActionEClass, RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER);

		changeRepositoryComponentActionEClass = createEClass(CHANGE_REPOSITORY_COMPONENT_ACTION);
		createEReference(changeRepositoryComponentActionEClass, CHANGE_REPOSITORY_COMPONENT_ACTION__NEW_REPOSITORY_COMPONENT);

		allocateActionEClass = createEClass(ALLOCATE_ACTION);
		createEReference(allocateActionEClass, ALLOCATE_ACTION__NEW_ALLOCATION_CONTEXT);

		deallocateActionEClass = createEClass(DEALLOCATE_ACTION);
		createEReference(deallocateActionEClass, DEALLOCATE_ACTION__OLD_ALLOCATION_CONTEXT);

		migrateActionEClass = createEClass(MIGRATE_ACTION);
		createEReference(migrateActionEClass, MIGRATE_ACTION__NEW_ALLOCATION_CONTEXT);
		createEReference(migrateActionEClass, MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT);

		acquireActionEClass = createEClass(ACQUIRE_ACTION);

		terminateActionEClass = createEClass(TERMINATE_ACTION);

		replicateActionEClass = createEClass(REPLICATE_ACTION);
		createEReference(replicateActionEClass, REPLICATE_ACTION__NEW_RESOURCE_CONTAINER);
		createEReference(replicateActionEClass, REPLICATE_ACTION__SOURCE_ALLOCATION_CONTEXT);
		createEReference(replicateActionEClass, REPLICATE_ACTION__NEW_ALLOCATION_CONTEXT);
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
		ResourceenvironmentPackage theResourceenvironmentPackage = (ResourceenvironmentPackage)EPackage.Registry.INSTANCE.getEPackage(ResourceenvironmentPackage.eNS_URI);
		CompositionPackage theCompositionPackage = (CompositionPackage)EPackage.Registry.INSTANCE.getEPackage(CompositionPackage.eNS_URI);
		RepositoryPackage theRepositoryPackage = (RepositoryPackage)EPackage.Registry.INSTANCE.getEPackage(RepositoryPackage.eNS_URI);
		AllocationPackage theAllocationPackage = (AllocationPackage)EPackage.Registry.INSTANCE.getEPackage(AllocationPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		assemblyContextActionEClass.getESuperTypes().add(this.getAction());
		resourceContainerActionEClass.getESuperTypes().add(this.getAction());
		changeRepositoryComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
		allocateActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
		deallocateActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
		migrateActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
		acquireActionEClass.getESuperTypes().add(this.getResourceContainerAction());
		terminateActionEClass.getESuperTypes().add(this.getResourceContainerAction());
		replicateActionEClass.getESuperTypes().add(this.getResourceContainerAction());

		// Initialize classes and features; add operations and parameters
		initEClass(systemAdaptationEClass, SystemAdaptation.class, "SystemAdaptation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSystemAdaptation_Actions(), this.getAction(), null, "actions", null, 0, -1, SystemAdaptation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(actionEClass, Action.class, "Action", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAction_ResourceContainer(), theResourceenvironmentPackage.getResourceContainer(), null, "resourceContainer", null, 1, 1, Action.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(assemblyContextActionEClass, AssemblyContextAction.class, "AssemblyContextAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAssemblyContextAction_SourceAssemblyContext(), theCompositionPackage.getAssemblyContext(), null, "sourceAssemblyContext", null, 0, 1, AssemblyContextAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resourceContainerActionEClass, ResourceContainerAction.class, "ResourceContainerAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getResourceContainerAction_SourceResourceContainer(), theResourceenvironmentPackage.getResourceContainer(), null, "sourceResourceContainer", null, 0, 1, ResourceContainerAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(changeRepositoryComponentActionEClass, ChangeRepositoryComponentAction.class, "ChangeRepositoryComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getChangeRepositoryComponentAction_NewRepositoryComponent(), theRepositoryPackage.getRepositoryComponent(), null, "newRepositoryComponent", null, 1, 1, ChangeRepositoryComponentAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(allocateActionEClass, AllocateAction.class, "AllocateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAllocateAction_NewAllocationContext(), theAllocationPackage.getAllocationContext(), null, "newAllocationContext", null, 1, 1, AllocateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deallocateActionEClass, DeallocateAction.class, "DeallocateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDeallocateAction_OldAllocationContext(), theAllocationPackage.getAllocationContext(), null, "oldAllocationContext", null, 0, 1, DeallocateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(migrateActionEClass, MigrateAction.class, "MigrateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMigrateAction_NewAllocationContext(), theAllocationPackage.getAllocationContext(), null, "newAllocationContext", null, 1, 1, MigrateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMigrateAction_SourceAllocationContext(), theAllocationPackage.getAllocationContext(), null, "sourceAllocationContext", null, 1, 1, MigrateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(acquireActionEClass, AcquireAction.class, "AcquireAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(terminateActionEClass, TerminateAction.class, "TerminateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(replicateActionEClass, ReplicateAction.class, "ReplicateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getReplicateAction_NewResourceContainer(), theResourceenvironmentPackage.getResourceContainer(), null, "newResourceContainer", null, 1, 1, ReplicateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getReplicateAction_SourceAllocationContext(), theAllocationPackage.getAllocationContext(), null, "sourceAllocationContext", null, 1, -1, ReplicateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getReplicateAction_NewAllocationContext(), theAllocationPackage.getAllocationContext(), null, "newAllocationContext", null, 1, -1, ReplicateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //systemadaptationPackageImpl
