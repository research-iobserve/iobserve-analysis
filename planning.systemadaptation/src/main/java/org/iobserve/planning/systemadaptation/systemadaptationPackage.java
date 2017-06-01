/**
 */
package org.iobserve.planning.systemadaptation;

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
 * @see org.iobserve.planning.systemadaptation.systemadaptationFactory
 * @model kind="package"
 * @generated
 */
public interface systemadaptationPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "systemadaptation";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://iobserve.org/SystemAdaptation";

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
	systemadaptationPackage eINSTANCE = org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.SystemAdaptationImpl <em>System Adaptation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.systemadaptation.impl.SystemAdaptationImpl
	 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getSystemAdaptation()
	 * @generated
	 */
	int SYSTEM_ADAPTATION = 0;

	/**
	 * The feature id for the '<em><b>Actions</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYSTEM_ADAPTATION__ACTIONS = 0;

	/**
	 * The number of structural features of the '<em>System Adaptation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYSTEM_ADAPTATION_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.ActionImpl <em>Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.systemadaptation.impl.ActionImpl
	 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getAction()
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
	 * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl <em>Assembly Context Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl
	 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getAssemblyContextAction()
	 * @generated
	 */
	int ASSEMBLY_CONTEXT_ACTION = 2;

	/**
	 * The feature id for the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_CONTEXT_ACTION__RESOURCE_CONTAINER = ACTION__RESOURCE_CONTAINER;

	/**
	 * The feature id for the '<em><b>Source Assembly Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT = ACTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Assembly Context Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT = ACTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl <em>Resource Container Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl
	 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getResourceContainerAction()
	 * @generated
	 */
	int RESOURCE_CONTAINER_ACTION = 3;

	/**
	 * The feature id for the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONTAINER_ACTION__RESOURCE_CONTAINER = ACTION__RESOURCE_CONTAINER;

	/**
	 * The feature id for the '<em><b>Source Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER = ACTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Resource Container Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONTAINER_ACTION_FEATURE_COUNT = ACTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl <em>Change Repository Component Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl
	 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getChangeRepositoryComponentAction()
	 * @generated
	 */
	int CHANGE_REPOSITORY_COMPONENT_ACTION = 4;

	/**
	 * The feature id for the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_REPOSITORY_COMPONENT_ACTION__RESOURCE_CONTAINER = ASSEMBLY_CONTEXT_ACTION__RESOURCE_CONTAINER;

	/**
	 * The feature id for the '<em><b>Source Assembly Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_REPOSITORY_COMPONENT_ACTION__SOURCE_ASSEMBLY_CONTEXT = ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT;

	/**
	 * The feature id for the '<em><b>New Repository Component</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_REPOSITORY_COMPONENT_ACTION__NEW_REPOSITORY_COMPONENT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Change Repository Component Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHANGE_REPOSITORY_COMPONENT_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.AllocateActionImpl <em>Allocate Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.systemadaptation.impl.AllocateActionImpl
	 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getAllocateAction()
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
	int ALLOCATE_ACTION__RESOURCE_CONTAINER = ASSEMBLY_CONTEXT_ACTION__RESOURCE_CONTAINER;

	/**
	 * The feature id for the '<em><b>Source Assembly Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALLOCATE_ACTION__SOURCE_ASSEMBLY_CONTEXT = ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT;

	/**
	 * The feature id for the '<em><b>New Allocation Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALLOCATE_ACTION__NEW_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Allocate Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALLOCATE_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl <em>Deallocate Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl
	 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getDeallocateAction()
	 * @generated
	 */
	int DEALLOCATE_ACTION = 6;

	/**
	 * The feature id for the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEALLOCATE_ACTION__RESOURCE_CONTAINER = ASSEMBLY_CONTEXT_ACTION__RESOURCE_CONTAINER;

	/**
	 * The feature id for the '<em><b>Source Assembly Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEALLOCATE_ACTION__SOURCE_ASSEMBLY_CONTEXT = ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT;

	/**
	 * The feature id for the '<em><b>Old Allocation Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEALLOCATE_ACTION__OLD_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Deallocate Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEALLOCATE_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl <em>Migrate Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.systemadaptation.impl.MigrateActionImpl
	 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getMigrateAction()
	 * @generated
	 */
	int MIGRATE_ACTION = 7;

	/**
	 * The feature id for the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATE_ACTION__RESOURCE_CONTAINER = ASSEMBLY_CONTEXT_ACTION__RESOURCE_CONTAINER;

	/**
	 * The feature id for the '<em><b>Source Assembly Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATE_ACTION__SOURCE_ASSEMBLY_CONTEXT = ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT;

	/**
	 * The feature id for the '<em><b>New Allocation Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATE_ACTION__NEW_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Source Allocation Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Migrate Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATE_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.AcquireActionImpl <em>Acquire Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.systemadaptation.impl.AcquireActionImpl
	 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getAcquireAction()
	 * @generated
	 */
	int ACQUIRE_ACTION = 8;

	/**
	 * The feature id for the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACQUIRE_ACTION__RESOURCE_CONTAINER = RESOURCE_CONTAINER_ACTION__RESOURCE_CONTAINER;

	/**
	 * The feature id for the '<em><b>Source Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACQUIRE_ACTION__SOURCE_RESOURCE_CONTAINER = RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER;

	/**
	 * The number of structural features of the '<em>Acquire Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACQUIRE_ACTION_FEATURE_COUNT = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.TerminateActionImpl <em>Terminate Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.systemadaptation.impl.TerminateActionImpl
	 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getTerminateAction()
	 * @generated
	 */
	int TERMINATE_ACTION = 9;

	/**
	 * The feature id for the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINATE_ACTION__RESOURCE_CONTAINER = RESOURCE_CONTAINER_ACTION__RESOURCE_CONTAINER;

	/**
	 * The feature id for the '<em><b>Source Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINATE_ACTION__SOURCE_RESOURCE_CONTAINER = RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER;

	/**
	 * The number of structural features of the '<em>Terminate Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINATE_ACTION_FEATURE_COUNT = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl <em>Replicate Action</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl
	 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getReplicateAction()
	 * @generated
	 */
	int REPLICATE_ACTION = 10;

	/**
	 * The feature id for the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPLICATE_ACTION__RESOURCE_CONTAINER = RESOURCE_CONTAINER_ACTION__RESOURCE_CONTAINER;

	/**
	 * The feature id for the '<em><b>Source Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPLICATE_ACTION__SOURCE_RESOURCE_CONTAINER = RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER;

	/**
	 * The feature id for the '<em><b>New Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPLICATE_ACTION__NEW_RESOURCE_CONTAINER = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Source Allocation Context</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPLICATE_ACTION__SOURCE_ALLOCATION_CONTEXT = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>New Allocation Context</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPLICATE_ACTION__NEW_ALLOCATION_CONTEXT = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Replicate Action</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REPLICATE_ACTION_FEATURE_COUNT = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 3;


	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.SystemAdaptation <em>System Adaptation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>System Adaptation</em>'.
	 * @see org.iobserve.planning.systemadaptation.SystemAdaptation
	 * @generated
	 */
	EClass getSystemAdaptation();

	/**
	 * Returns the meta object for the containment reference list '{@link org.iobserve.planning.systemadaptation.SystemAdaptation#getActions <em>Actions</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Actions</em>'.
	 * @see org.iobserve.planning.systemadaptation.SystemAdaptation#getActions()
	 * @see #getSystemAdaptation()
	 * @generated
	 */
	EReference getSystemAdaptation_Actions();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.Action <em>Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Action</em>'.
	 * @see org.iobserve.planning.systemadaptation.Action
	 * @generated
	 */
	EClass getAction();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.planning.systemadaptation.Action#getResourceContainer <em>Resource Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Resource Container</em>'.
	 * @see org.iobserve.planning.systemadaptation.Action#getResourceContainer()
	 * @see #getAction()
	 * @generated
	 */
	EReference getAction_ResourceContainer();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.AssemblyContextAction <em>Assembly Context Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Assembly Context Action</em>'.
	 * @see org.iobserve.planning.systemadaptation.AssemblyContextAction
	 * @generated
	 */
	EClass getAssemblyContextAction();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.planning.systemadaptation.AssemblyContextAction#getSourceAssemblyContext <em>Source Assembly Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source Assembly Context</em>'.
	 * @see org.iobserve.planning.systemadaptation.AssemblyContextAction#getSourceAssemblyContext()
	 * @see #getAssemblyContextAction()
	 * @generated
	 */
	EReference getAssemblyContextAction_SourceAssemblyContext();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.ResourceContainerAction <em>Resource Container Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Container Action</em>'.
	 * @see org.iobserve.planning.systemadaptation.ResourceContainerAction
	 * @generated
	 */
	EClass getResourceContainerAction();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.planning.systemadaptation.ResourceContainerAction#getSourceResourceContainer <em>Source Resource Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source Resource Container</em>'.
	 * @see org.iobserve.planning.systemadaptation.ResourceContainerAction#getSourceResourceContainer()
	 * @see #getResourceContainerAction()
	 * @generated
	 */
	EReference getResourceContainerAction_SourceResourceContainer();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction <em>Change Repository Component Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Change Repository Component Action</em>'.
	 * @see org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction
	 * @generated
	 */
	EClass getChangeRepositoryComponentAction();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction#getNewRepositoryComponent <em>New Repository Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Repository Component</em>'.
	 * @see org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction#getNewRepositoryComponent()
	 * @see #getChangeRepositoryComponentAction()
	 * @generated
	 */
	EReference getChangeRepositoryComponentAction_NewRepositoryComponent();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.AllocateAction <em>Allocate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Allocate Action</em>'.
	 * @see org.iobserve.planning.systemadaptation.AllocateAction
	 * @generated
	 */
	EClass getAllocateAction();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.planning.systemadaptation.AllocateAction#getNewAllocationContext <em>New Allocation Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Allocation Context</em>'.
	 * @see org.iobserve.planning.systemadaptation.AllocateAction#getNewAllocationContext()
	 * @see #getAllocateAction()
	 * @generated
	 */
	EReference getAllocateAction_NewAllocationContext();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.DeallocateAction <em>Deallocate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deallocate Action</em>'.
	 * @see org.iobserve.planning.systemadaptation.DeallocateAction
	 * @generated
	 */
	EClass getDeallocateAction();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.planning.systemadaptation.DeallocateAction#getOldAllocationContext <em>Old Allocation Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Old Allocation Context</em>'.
	 * @see org.iobserve.planning.systemadaptation.DeallocateAction#getOldAllocationContext()
	 * @see #getDeallocateAction()
	 * @generated
	 */
	EReference getDeallocateAction_OldAllocationContext();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.MigrateAction <em>Migrate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Migrate Action</em>'.
	 * @see org.iobserve.planning.systemadaptation.MigrateAction
	 * @generated
	 */
	EClass getMigrateAction();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.planning.systemadaptation.MigrateAction#getNewAllocationContext <em>New Allocation Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Allocation Context</em>'.
	 * @see org.iobserve.planning.systemadaptation.MigrateAction#getNewAllocationContext()
	 * @see #getMigrateAction()
	 * @generated
	 */
	EReference getMigrateAction_NewAllocationContext();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.planning.systemadaptation.MigrateAction#getSourceAllocationContext <em>Source Allocation Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source Allocation Context</em>'.
	 * @see org.iobserve.planning.systemadaptation.MigrateAction#getSourceAllocationContext()
	 * @see #getMigrateAction()
	 * @generated
	 */
	EReference getMigrateAction_SourceAllocationContext();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.AcquireAction <em>Acquire Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Acquire Action</em>'.
	 * @see org.iobserve.planning.systemadaptation.AcquireAction
	 * @generated
	 */
	EClass getAcquireAction();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.TerminateAction <em>Terminate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Terminate Action</em>'.
	 * @see org.iobserve.planning.systemadaptation.TerminateAction
	 * @generated
	 */
	EClass getTerminateAction();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.ReplicateAction <em>Replicate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Replicate Action</em>'.
	 * @see org.iobserve.planning.systemadaptation.ReplicateAction
	 * @generated
	 */
	EClass getReplicateAction();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.planning.systemadaptation.ReplicateAction#getNewResourceContainer <em>New Resource Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>New Resource Container</em>'.
	 * @see org.iobserve.planning.systemadaptation.ReplicateAction#getNewResourceContainer()
	 * @see #getReplicateAction()
	 * @generated
	 */
	EReference getReplicateAction_NewResourceContainer();

	/**
	 * Returns the meta object for the reference list '{@link org.iobserve.planning.systemadaptation.ReplicateAction#getSourceAllocationContext <em>Source Allocation Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Source Allocation Context</em>'.
	 * @see org.iobserve.planning.systemadaptation.ReplicateAction#getSourceAllocationContext()
	 * @see #getReplicateAction()
	 * @generated
	 */
	EReference getReplicateAction_SourceAllocationContext();

	/**
	 * Returns the meta object for the reference list '{@link org.iobserve.planning.systemadaptation.ReplicateAction#getNewAllocationContext <em>New Allocation Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>New Allocation Context</em>'.
	 * @see org.iobserve.planning.systemadaptation.ReplicateAction#getNewAllocationContext()
	 * @see #getReplicateAction()
	 * @generated
	 */
	EReference getReplicateAction_NewAllocationContext();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	systemadaptationFactory getsystemadaptationFactory();

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
		 * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.SystemAdaptationImpl <em>System Adaptation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.systemadaptation.impl.SystemAdaptationImpl
		 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getSystemAdaptation()
		 * @generated
		 */
		EClass SYSTEM_ADAPTATION = eINSTANCE.getSystemAdaptation();

		/**
		 * The meta object literal for the '<em><b>Actions</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SYSTEM_ADAPTATION__ACTIONS = eINSTANCE.getSystemAdaptation_Actions();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.ActionImpl <em>Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.systemadaptation.impl.ActionImpl
		 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getAction()
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
		 * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl <em>Assembly Context Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl
		 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getAssemblyContextAction()
		 * @generated
		 */
		EClass ASSEMBLY_CONTEXT_ACTION = eINSTANCE.getAssemblyContextAction();

		/**
		 * The meta object literal for the '<em><b>Source Assembly Context</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT = eINSTANCE.getAssemblyContextAction_SourceAssemblyContext();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl <em>Resource Container Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl
		 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getResourceContainerAction()
		 * @generated
		 */
		EClass RESOURCE_CONTAINER_ACTION = eINSTANCE.getResourceContainerAction();

		/**
		 * The meta object literal for the '<em><b>Source Resource Container</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER = eINSTANCE.getResourceContainerAction_SourceResourceContainer();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl <em>Change Repository Component Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl
		 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getChangeRepositoryComponentAction()
		 * @generated
		 */
		EClass CHANGE_REPOSITORY_COMPONENT_ACTION = eINSTANCE.getChangeRepositoryComponentAction();

		/**
		 * The meta object literal for the '<em><b>New Repository Component</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHANGE_REPOSITORY_COMPONENT_ACTION__NEW_REPOSITORY_COMPONENT = eINSTANCE.getChangeRepositoryComponentAction_NewRepositoryComponent();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.AllocateActionImpl <em>Allocate Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.systemadaptation.impl.AllocateActionImpl
		 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getAllocateAction()
		 * @generated
		 */
		EClass ALLOCATE_ACTION = eINSTANCE.getAllocateAction();

		/**
		 * The meta object literal for the '<em><b>New Allocation Context</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ALLOCATE_ACTION__NEW_ALLOCATION_CONTEXT = eINSTANCE.getAllocateAction_NewAllocationContext();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl <em>Deallocate Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl
		 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getDeallocateAction()
		 * @generated
		 */
		EClass DEALLOCATE_ACTION = eINSTANCE.getDeallocateAction();

		/**
		 * The meta object literal for the '<em><b>Old Allocation Context</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEALLOCATE_ACTION__OLD_ALLOCATION_CONTEXT = eINSTANCE.getDeallocateAction_OldAllocationContext();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl <em>Migrate Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.systemadaptation.impl.MigrateActionImpl
		 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getMigrateAction()
		 * @generated
		 */
		EClass MIGRATE_ACTION = eINSTANCE.getMigrateAction();

		/**
		 * The meta object literal for the '<em><b>New Allocation Context</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MIGRATE_ACTION__NEW_ALLOCATION_CONTEXT = eINSTANCE.getMigrateAction_NewAllocationContext();

		/**
		 * The meta object literal for the '<em><b>Source Allocation Context</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT = eINSTANCE.getMigrateAction_SourceAllocationContext();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.AcquireActionImpl <em>Acquire Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.systemadaptation.impl.AcquireActionImpl
		 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getAcquireAction()
		 * @generated
		 */
		EClass ACQUIRE_ACTION = eINSTANCE.getAcquireAction();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.TerminateActionImpl <em>Terminate Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.systemadaptation.impl.TerminateActionImpl
		 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getTerminateAction()
		 * @generated
		 */
		EClass TERMINATE_ACTION = eINSTANCE.getTerminateAction();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl <em>Replicate Action</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl
		 * @see org.iobserve.planning.systemadaptation.impl.systemadaptationPackageImpl#getReplicateAction()
		 * @generated
		 */
		EClass REPLICATE_ACTION = eINSTANCE.getReplicateAction();

		/**
		 * The meta object literal for the '<em><b>New Resource Container</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REPLICATE_ACTION__NEW_RESOURCE_CONTAINER = eINSTANCE.getReplicateAction_NewResourceContainer();

		/**
		 * The meta object literal for the '<em><b>Source Allocation Context</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REPLICATE_ACTION__SOURCE_ALLOCATION_CONTEXT = eINSTANCE.getReplicateAction_SourceAllocationContext();

		/**
		 * The meta object literal for the '<em><b>New Allocation Context</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REPLICATE_ACTION__NEW_ALLOCATION_CONTEXT = eINSTANCE.getReplicateAction_NewAllocationContext();

	}

} //systemadaptationPackage
