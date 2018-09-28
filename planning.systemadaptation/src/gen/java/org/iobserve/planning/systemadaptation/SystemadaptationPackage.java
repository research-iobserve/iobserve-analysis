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
 * @see org.iobserve.planning.systemadaptation.SystemadaptationFactory
 * @model kind="package"
 * @generated
 */
public interface SystemadaptationPackage extends EPackage {
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
    SystemadaptationPackage eINSTANCE = org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl.init();

    /**
     * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.SystemAdaptationImpl <em>System Adaptation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.planning.systemadaptation.impl.SystemAdaptationImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getSystemAdaptation()
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
     * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.ComposedActionImpl <em>Composed Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.planning.systemadaptation.impl.ComposedActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getComposedAction()
     * @generated
     */
    int COMPOSED_ACTION = 1;

    /**
     * The number of structural features of the '<em>Composed Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPOSED_ACTION_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl <em>Assembly Context Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getAssemblyContextAction()
     * @generated
     */
    int ASSEMBLY_CONTEXT_ACTION = 2;

    /**
     * The feature id for the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT = COMPOSED_ACTION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Target Providing Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSEMBLY_CONTEXT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS = COMPOSED_ACTION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Target Requiring Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSEMBLY_CONTEXT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS = COMPOSED_ACTION_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Assembly Context Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT = COMPOSED_ACTION_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl <em>Resource Container Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getResourceContainerAction()
     * @generated
     */
    int RESOURCE_CONTAINER_ACTION = 3;

    /**
     * The feature id for the '<em><b>Target Resource Container</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER = COMPOSED_ACTION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Target Linking Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_CONTAINER_ACTION__TARGET_LINKING_RESOURCES = COMPOSED_ACTION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Resource Container Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_CONTAINER_ACTION_FEATURE_COUNT = COMPOSED_ACTION_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl <em>Change Repository Component Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getChangeRepositoryComponentAction()
     * @generated
     */
    int CHANGE_REPOSITORY_COMPONENT_ACTION = 4;

    /**
     * The feature id for the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHANGE_REPOSITORY_COMPONENT_ACTION__TARGET_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT;

    /**
     * The feature id for the '<em><b>Target Providing Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHANGE_REPOSITORY_COMPONENT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS;

    /**
     * The feature id for the '<em><b>Target Requiring Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHANGE_REPOSITORY_COMPONENT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS;

    /**
     * The feature id for the '<em><b>Source Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHANGE_REPOSITORY_COMPONENT_ACTION__SOURCE_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Change Repository Component Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CHANGE_REPOSITORY_COMPONENT_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl <em>Replicate Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getReplicateAction()
     * @generated
     */
    int REPLICATE_ACTION = 5;

    /**
     * The feature id for the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPLICATE_ACTION__TARGET_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT;

    /**
     * The feature id for the '<em><b>Target Providing Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPLICATE_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS;

    /**
     * The feature id for the '<em><b>Target Requiring Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPLICATE_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS;

    /**
     * The feature id for the '<em><b>Source Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPLICATE_ACTION__SOURCE_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Replicate Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int REPLICATE_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.DereplicateActionImpl <em>Dereplicate Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.planning.systemadaptation.impl.DereplicateActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getDereplicateAction()
     * @generated
     */
    int DEREPLICATE_ACTION = 6;

    /**
     * The feature id for the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEREPLICATE_ACTION__TARGET_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT;

    /**
     * The feature id for the '<em><b>Target Providing Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEREPLICATE_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS;

    /**
     * The feature id for the '<em><b>Target Requiring Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEREPLICATE_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS;

    /**
     * The number of structural features of the '<em>Dereplicate Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEREPLICATE_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl <em>Migrate Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.planning.systemadaptation.impl.MigrateActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getMigrateAction()
     * @generated
     */
    int MIGRATE_ACTION = 7;

    /**
     * The feature id for the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATE_ACTION__TARGET_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT;

    /**
     * The feature id for the '<em><b>Target Providing Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATE_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS;

    /**
     * The feature id for the '<em><b>Target Requiring Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATE_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS;

    /**
     * The feature id for the '<em><b>Source Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Source Providing Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATE_ACTION__SOURCE_PROVIDING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Source Requiring Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATE_ACTION__SOURCE_REQUIRING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Migrate Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATE_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.AllocateActionImpl <em>Allocate Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.planning.systemadaptation.impl.AllocateActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getAllocateAction()
     * @generated
     */
    int ALLOCATE_ACTION = 8;

    /**
     * The feature id for the '<em><b>Target Resource Container</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOCATE_ACTION__TARGET_RESOURCE_CONTAINER = RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER;

    /**
     * The feature id for the '<em><b>Target Linking Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOCATE_ACTION__TARGET_LINKING_RESOURCES = RESOURCE_CONTAINER_ACTION__TARGET_LINKING_RESOURCES;

    /**
     * The number of structural features of the '<em>Allocate Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOCATE_ACTION_FEATURE_COUNT = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl <em>Deallocate Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl
     * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getDeallocateAction()
     * @generated
     */
    int DEALLOCATE_ACTION = 9;

    /**
     * The feature id for the '<em><b>Target Resource Container</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEALLOCATE_ACTION__TARGET_RESOURCE_CONTAINER = RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER;

    /**
     * The feature id for the '<em><b>Target Linking Resources</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEALLOCATE_ACTION__TARGET_LINKING_RESOURCES = RESOURCE_CONTAINER_ACTION__TARGET_LINKING_RESOURCES;

    /**
     * The number of structural features of the '<em>Deallocate Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEALLOCATE_ACTION_FEATURE_COUNT = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 0;


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
     * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.ComposedAction <em>Composed Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Composed Action</em>'.
     * @see org.iobserve.planning.systemadaptation.ComposedAction
     * @generated
     */
    EClass getComposedAction();

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
     * Returns the meta object for the reference '{@link org.iobserve.planning.systemadaptation.AssemblyContextAction#getTargetAllocationContext <em>Target Allocation Context</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Target Allocation Context</em>'.
     * @see org.iobserve.planning.systemadaptation.AssemblyContextAction#getTargetAllocationContext()
     * @see #getAssemblyContextAction()
     * @generated
     */
    EReference getAssemblyContextAction_TargetAllocationContext();

    /**
     * Returns the meta object for the reference list '{@link org.iobserve.planning.systemadaptation.AssemblyContextAction#getTargetProvidingAllocationContexts <em>Target Providing Allocation Contexts</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Target Providing Allocation Contexts</em>'.
     * @see org.iobserve.planning.systemadaptation.AssemblyContextAction#getTargetProvidingAllocationContexts()
     * @see #getAssemblyContextAction()
     * @generated
     */
    EReference getAssemblyContextAction_TargetProvidingAllocationContexts();

    /**
     * Returns the meta object for the reference list '{@link org.iobserve.planning.systemadaptation.AssemblyContextAction#getTargetRequiringAllocationContexts <em>Target Requiring Allocation Contexts</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Target Requiring Allocation Contexts</em>'.
     * @see org.iobserve.planning.systemadaptation.AssemblyContextAction#getTargetRequiringAllocationContexts()
     * @see #getAssemblyContextAction()
     * @generated
     */
    EReference getAssemblyContextAction_TargetRequiringAllocationContexts();

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
     * Returns the meta object for the reference '{@link org.iobserve.planning.systemadaptation.ResourceContainerAction#getTargetResourceContainer <em>Target Resource Container</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Target Resource Container</em>'.
     * @see org.iobserve.planning.systemadaptation.ResourceContainerAction#getTargetResourceContainer()
     * @see #getResourceContainerAction()
     * @generated
     */
    EReference getResourceContainerAction_TargetResourceContainer();

    /**
     * Returns the meta object for the reference list '{@link org.iobserve.planning.systemadaptation.ResourceContainerAction#getTargetLinkingResources <em>Target Linking Resources</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Target Linking Resources</em>'.
     * @see org.iobserve.planning.systemadaptation.ResourceContainerAction#getTargetLinkingResources()
     * @see #getResourceContainerAction()
     * @generated
     */
    EReference getResourceContainerAction_TargetLinkingResources();

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
     * Returns the meta object for the reference '{@link org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction#getSourceAllocationContext <em>Source Allocation Context</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Source Allocation Context</em>'.
     * @see org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction#getSourceAllocationContext()
     * @see #getChangeRepositoryComponentAction()
     * @generated
     */
    EReference getChangeRepositoryComponentAction_SourceAllocationContext();

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
     * Returns the meta object for the reference '{@link org.iobserve.planning.systemadaptation.ReplicateAction#getSourceAllocationContext <em>Source Allocation Context</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Source Allocation Context</em>'.
     * @see org.iobserve.planning.systemadaptation.ReplicateAction#getSourceAllocationContext()
     * @see #getReplicateAction()
     * @generated
     */
    EReference getReplicateAction_SourceAllocationContext();

    /**
     * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.DereplicateAction <em>Dereplicate Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Dereplicate Action</em>'.
     * @see org.iobserve.planning.systemadaptation.DereplicateAction
     * @generated
     */
    EClass getDereplicateAction();

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
     * Returns the meta object for the reference list '{@link org.iobserve.planning.systemadaptation.MigrateAction#getSourceProvidingAllocationContexts <em>Source Providing Allocation Contexts</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Source Providing Allocation Contexts</em>'.
     * @see org.iobserve.planning.systemadaptation.MigrateAction#getSourceProvidingAllocationContexts()
     * @see #getMigrateAction()
     * @generated
     */
    EReference getMigrateAction_SourceProvidingAllocationContexts();

    /**
     * Returns the meta object for the reference list '{@link org.iobserve.planning.systemadaptation.MigrateAction#getSourceRequiringAllocationContexts <em>Source Requiring Allocation Contexts</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Source Requiring Allocation Contexts</em>'.
     * @see org.iobserve.planning.systemadaptation.MigrateAction#getSourceRequiringAllocationContexts()
     * @see #getMigrateAction()
     * @generated
     */
    EReference getMigrateAction_SourceRequiringAllocationContexts();

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
     * Returns the meta object for class '{@link org.iobserve.planning.systemadaptation.DeallocateAction <em>Deallocate Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Deallocate Action</em>'.
     * @see org.iobserve.planning.systemadaptation.DeallocateAction
     * @generated
     */
    EClass getDeallocateAction();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    SystemadaptationFactory getSystemadaptationFactory();

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
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getSystemAdaptation()
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
         * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.ComposedActionImpl <em>Composed Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.planning.systemadaptation.impl.ComposedActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getComposedAction()
         * @generated
         */
        EClass COMPOSED_ACTION = eINSTANCE.getComposedAction();

        /**
         * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl <em>Assembly Context Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.planning.systemadaptation.impl.AssemblyContextActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getAssemblyContextAction()
         * @generated
         */
        EClass ASSEMBLY_CONTEXT_ACTION = eINSTANCE.getAssemblyContextAction();

        /**
         * The meta object literal for the '<em><b>Target Allocation Context</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT = eINSTANCE.getAssemblyContextAction_TargetAllocationContext();

        /**
         * The meta object literal for the '<em><b>Target Providing Allocation Contexts</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ASSEMBLY_CONTEXT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS = eINSTANCE.getAssemblyContextAction_TargetProvidingAllocationContexts();

        /**
         * The meta object literal for the '<em><b>Target Requiring Allocation Contexts</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ASSEMBLY_CONTEXT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS = eINSTANCE.getAssemblyContextAction_TargetRequiringAllocationContexts();

        /**
         * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl <em>Resource Container Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getResourceContainerAction()
         * @generated
         */
        EClass RESOURCE_CONTAINER_ACTION = eINSTANCE.getResourceContainerAction();

        /**
         * The meta object literal for the '<em><b>Target Resource Container</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER = eINSTANCE.getResourceContainerAction_TargetResourceContainer();

        /**
         * The meta object literal for the '<em><b>Target Linking Resources</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RESOURCE_CONTAINER_ACTION__TARGET_LINKING_RESOURCES = eINSTANCE.getResourceContainerAction_TargetLinkingResources();

        /**
         * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl <em>Change Repository Component Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getChangeRepositoryComponentAction()
         * @generated
         */
        EClass CHANGE_REPOSITORY_COMPONENT_ACTION = eINSTANCE.getChangeRepositoryComponentAction();

        /**
         * The meta object literal for the '<em><b>Source Allocation Context</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CHANGE_REPOSITORY_COMPONENT_ACTION__SOURCE_ALLOCATION_CONTEXT = eINSTANCE.getChangeRepositoryComponentAction_SourceAllocationContext();

        /**
         * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl <em>Replicate Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getReplicateAction()
         * @generated
         */
        EClass REPLICATE_ACTION = eINSTANCE.getReplicateAction();

        /**
         * The meta object literal for the '<em><b>Source Allocation Context</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference REPLICATE_ACTION__SOURCE_ALLOCATION_CONTEXT = eINSTANCE.getReplicateAction_SourceAllocationContext();

        /**
         * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.DereplicateActionImpl <em>Dereplicate Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.planning.systemadaptation.impl.DereplicateActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getDereplicateAction()
         * @generated
         */
        EClass DEREPLICATE_ACTION = eINSTANCE.getDereplicateAction();

        /**
         * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl <em>Migrate Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.planning.systemadaptation.impl.MigrateActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getMigrateAction()
         * @generated
         */
        EClass MIGRATE_ACTION = eINSTANCE.getMigrateAction();

        /**
         * The meta object literal for the '<em><b>Source Allocation Context</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT = eINSTANCE.getMigrateAction_SourceAllocationContext();

        /**
         * The meta object literal for the '<em><b>Source Providing Allocation Contexts</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MIGRATE_ACTION__SOURCE_PROVIDING_ALLOCATION_CONTEXTS = eINSTANCE.getMigrateAction_SourceProvidingAllocationContexts();

        /**
         * The meta object literal for the '<em><b>Source Requiring Allocation Contexts</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MIGRATE_ACTION__SOURCE_REQUIRING_ALLOCATION_CONTEXTS = eINSTANCE.getMigrateAction_SourceRequiringAllocationContexts();

        /**
         * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.AllocateActionImpl <em>Allocate Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.planning.systemadaptation.impl.AllocateActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getAllocateAction()
         * @generated
         */
        EClass ALLOCATE_ACTION = eINSTANCE.getAllocateAction();

        /**
         * The meta object literal for the '{@link org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl <em>Deallocate Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl
         * @see org.iobserve.planning.systemadaptation.impl.SystemadaptationPackageImpl#getDeallocateAction()
         * @generated
         */
        EClass DEALLOCATE_ACTION = eINSTANCE.getDeallocateAction();

    }

} //SystemadaptationPackage
