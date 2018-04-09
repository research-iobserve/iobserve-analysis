/**
 */
package org.iobserve.adaptation.executionplan;

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
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.iobserve.adaptation.executionplan.ExecutionplanFactory
 * @model kind="package"
 * @generated
 */
public interface ExecutionplanPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "executionplan";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://iobserve.org/ExecutionPlan";

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
    ExecutionplanPackage eINSTANCE = org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl.init();

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.ExecutionPlanImpl <em>Execution Plan</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionPlanImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getExecutionPlan()
     * @generated
     */
    int EXECUTION_PLAN = 0;

    /**
     * The feature id for the '<em><b>Actions</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN__ACTIONS = 0;

    /**
     * The number of structural features of the '<em>Execution Plan</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EXECUTION_PLAN_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.ActionImpl <em>Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.ActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getAction()
     * @generated
     */
    int ACTION = 1;

    /**
     * The number of structural features of the '<em>Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ACTION_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.AssemblyContextActionImpl <em>Assembly Context Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.AssemblyContextActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getAssemblyContextAction()
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
    int ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT = ACTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Assembly Context Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT = ACTION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.ResourceContainerActionImpl <em>Resource Container Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.ResourceContainerActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getResourceContainerAction()
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
    int RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER = ACTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Resource Container Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESOURCE_CONTAINER_ACTION_FEATURE_COUNT = ACTION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.DeployComponentActionImpl <em>Deploy Component Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.DeployComponentActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getDeployComponentAction()
     * @generated
     */
    int DEPLOY_COMPONENT_ACTION = 4;

    /**
     * The feature id for the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPLOY_COMPONENT_ACTION__TARGET_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT;

    /**
     * The number of structural features of the '<em>Deploy Component Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPLOY_COMPONENT_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.UndeployComponentActionImpl <em>Undeploy Component Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.UndeployComponentActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getUndeployComponentAction()
     * @generated
     */
    int UNDEPLOY_COMPONENT_ACTION = 5;

    /**
     * The feature id for the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNDEPLOY_COMPONENT_ACTION__TARGET_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT;

    /**
     * The number of structural features of the '<em>Undeploy Component Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UNDEPLOY_COMPONENT_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.MigrateComponentStateActionImpl <em>Migrate Component State Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.MigrateComponentStateActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getMigrateComponentStateAction()
     * @generated
     */
    int MIGRATE_COMPONENT_STATE_ACTION = 6;

    /**
     * The feature id for the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATE_COMPONENT_STATE_ACTION__TARGET_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT;

    /**
     * The feature id for the '<em><b>Source Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATE_COMPONENT_STATE_ACTION__SOURCE_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Migrate Component State Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int MIGRATE_COMPONENT_STATE_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.ConnectComponentActionImpl <em>Connect Component Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.ConnectComponentActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getConnectComponentAction()
     * @generated
     */
    int CONNECT_COMPONENT_ACTION = 7;

    /**
     * The feature id for the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECT_COMPONENT_ACTION__TARGET_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT;

    /**
     * The feature id for the '<em><b>Target Providing Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECT_COMPONENT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Target Requiring Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECT_COMPONENT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Connect Component Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECT_COMPONENT_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.BlockRequestsToComponentActionImpl <em>Block Requests To Component Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.BlockRequestsToComponentActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getBlockRequestsToComponentAction()
     * @generated
     */
    int BLOCK_REQUESTS_TO_COMPONENT_ACTION = 8;

    /**
     * The feature id for the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_REQUESTS_TO_COMPONENT_ACTION__TARGET_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT;

    /**
     * The feature id for the '<em><b>Target Requiring Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_REQUESTS_TO_COMPONENT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Block Requests To Component Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BLOCK_REQUESTS_TO_COMPONENT_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.DisconnectComponentActionImpl <em>Disconnect Component Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.DisconnectComponentActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getDisconnectComponentAction()
     * @generated
     */
    int DISCONNECT_COMPONENT_ACTION = 9;

    /**
     * The feature id for the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISCONNECT_COMPONENT_ACTION__TARGET_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT;

    /**
     * The feature id for the '<em><b>Target Providing Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISCONNECT_COMPONENT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Target Requiring Allocation Contexts</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISCONNECT_COMPONENT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Disconnect Component Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISCONNECT_COMPONENT_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.FinishComponentActionImpl <em>Finish Component Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.FinishComponentActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getFinishComponentAction()
     * @generated
     */
    int FINISH_COMPONENT_ACTION = 10;

    /**
     * The feature id for the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FINISH_COMPONENT_ACTION__TARGET_ALLOCATION_CONTEXT = ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT;

    /**
     * The number of structural features of the '<em>Finish Component Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int FINISH_COMPONENT_ACTION_FEATURE_COUNT = ASSEMBLY_CONTEXT_ACTION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.AllocateNodeActionImpl <em>Allocate Node Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.AllocateNodeActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getAllocateNodeAction()
     * @generated
     */
    int ALLOCATE_NODE_ACTION = 11;

    /**
     * The feature id for the '<em><b>Target Resource Container</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOCATE_NODE_ACTION__TARGET_RESOURCE_CONTAINER = RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER;

    /**
     * The number of structural features of the '<em>Allocate Node Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOCATE_NODE_ACTION_FEATURE_COUNT = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.DeallocateNodeActionImpl <em>Deallocate Node Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.DeallocateNodeActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getDeallocateNodeAction()
     * @generated
     */
    int DEALLOCATE_NODE_ACTION = 12;

    /**
     * The feature id for the '<em><b>Target Resource Container</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEALLOCATE_NODE_ACTION__TARGET_RESOURCE_CONTAINER = RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER;

    /**
     * The number of structural features of the '<em>Deallocate Node Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEALLOCATE_NODE_ACTION_FEATURE_COUNT = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.ConnectNodeActionImpl <em>Connect Node Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.ConnectNodeActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getConnectNodeAction()
     * @generated
     */
    int CONNECT_NODE_ACTION = 13;

    /**
     * The feature id for the '<em><b>Target Resource Container</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECT_NODE_ACTION__TARGET_RESOURCE_CONTAINER = RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER;

    /**
     * The feature id for the '<em><b>Target Connectors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECT_NODE_ACTION__TARGET_CONNECTORS = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Connect Node Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONNECT_NODE_ACTION_FEATURE_COUNT = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link org.iobserve.adaptation.executionplan.impl.DisconnectNodeActionImpl <em>Disconnect Node Action</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.adaptation.executionplan.impl.DisconnectNodeActionImpl
     * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getDisconnectNodeAction()
     * @generated
     */
    int DISCONNECT_NODE_ACTION = 14;

    /**
     * The feature id for the '<em><b>Target Resource Container</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISCONNECT_NODE_ACTION__TARGET_RESOURCE_CONTAINER = RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER;

    /**
     * The feature id for the '<em><b>Target Connectors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISCONNECT_NODE_ACTION__TARGET_CONNECTORS = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Disconnect Node Action</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DISCONNECT_NODE_ACTION_FEATURE_COUNT = RESOURCE_CONTAINER_ACTION_FEATURE_COUNT + 1;

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.ExecutionPlan <em>Execution Plan</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Execution Plan</em>'.
     * @see org.iobserve.adaptation.executionplan.ExecutionPlan
     * @generated
     */
    EClass getExecutionPlan();

    /**
     * Returns the meta object for the containment reference list '{@link org.iobserve.adaptation.executionplan.ExecutionPlan#getActions <em>Actions</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Actions</em>'.
     * @see org.iobserve.adaptation.executionplan.ExecutionPlan#getActions()
     * @see #getExecutionPlan()
     * @generated
     */
    EReference getExecutionPlan_Actions();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.Action <em>Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Action</em>'.
     * @see org.iobserve.adaptation.executionplan.Action
     * @generated
     */
    EClass getAction();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.AssemblyContextAction <em>Assembly Context Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Assembly Context Action</em>'.
     * @see org.iobserve.adaptation.executionplan.AssemblyContextAction
     * @generated
     */
    EClass getAssemblyContextAction();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.adaptation.executionplan.AssemblyContextAction#getTargetAllocationContext <em>Target Allocation Context</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Target Allocation Context</em>'.
     * @see org.iobserve.adaptation.executionplan.AssemblyContextAction#getTargetAllocationContext()
     * @see #getAssemblyContextAction()
     * @generated
     */
    EReference getAssemblyContextAction_TargetAllocationContext();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.ResourceContainerAction <em>Resource Container Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Resource Container Action</em>'.
     * @see org.iobserve.adaptation.executionplan.ResourceContainerAction
     * @generated
     */
    EClass getResourceContainerAction();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.adaptation.executionplan.ResourceContainerAction#getTargetResourceContainer <em>Target Resource Container</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Target Resource Container</em>'.
     * @see org.iobserve.adaptation.executionplan.ResourceContainerAction#getTargetResourceContainer()
     * @see #getResourceContainerAction()
     * @generated
     */
    EReference getResourceContainerAction_TargetResourceContainer();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.DeployComponentAction <em>Deploy Component Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Deploy Component Action</em>'.
     * @see org.iobserve.adaptation.executionplan.DeployComponentAction
     * @generated
     */
    EClass getDeployComponentAction();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.UndeployComponentAction <em>Undeploy Component Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Undeploy Component Action</em>'.
     * @see org.iobserve.adaptation.executionplan.UndeployComponentAction
     * @generated
     */
    EClass getUndeployComponentAction();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.MigrateComponentStateAction <em>Migrate Component State Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Migrate Component State Action</em>'.
     * @see org.iobserve.adaptation.executionplan.MigrateComponentStateAction
     * @generated
     */
    EClass getMigrateComponentStateAction();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.adaptation.executionplan.MigrateComponentStateAction#getSourceAllocationContext <em>Source Allocation Context</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Source Allocation Context</em>'.
     * @see org.iobserve.adaptation.executionplan.MigrateComponentStateAction#getSourceAllocationContext()
     * @see #getMigrateComponentStateAction()
     * @generated
     */
    EReference getMigrateComponentStateAction_SourceAllocationContext();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.ConnectComponentAction <em>Connect Component Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Connect Component Action</em>'.
     * @see org.iobserve.adaptation.executionplan.ConnectComponentAction
     * @generated
     */
    EClass getConnectComponentAction();

    /**
     * Returns the meta object for the reference list '{@link org.iobserve.adaptation.executionplan.ConnectComponentAction#getTargetProvidingAllocationContexts <em>Target Providing Allocation Contexts</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Target Providing Allocation Contexts</em>'.
     * @see org.iobserve.adaptation.executionplan.ConnectComponentAction#getTargetProvidingAllocationContexts()
     * @see #getConnectComponentAction()
     * @generated
     */
    EReference getConnectComponentAction_TargetProvidingAllocationContexts();

    /**
     * Returns the meta object for the reference list '{@link org.iobserve.adaptation.executionplan.ConnectComponentAction#getTargetRequiringAllocationContexts <em>Target Requiring Allocation Contexts</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Target Requiring Allocation Contexts</em>'.
     * @see org.iobserve.adaptation.executionplan.ConnectComponentAction#getTargetRequiringAllocationContexts()
     * @see #getConnectComponentAction()
     * @generated
     */
    EReference getConnectComponentAction_TargetRequiringAllocationContexts();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.BlockRequestsToComponentAction <em>Block Requests To Component Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Block Requests To Component Action</em>'.
     * @see org.iobserve.adaptation.executionplan.BlockRequestsToComponentAction
     * @generated
     */
    EClass getBlockRequestsToComponentAction();

    /**
     * Returns the meta object for the reference list '{@link org.iobserve.adaptation.executionplan.BlockRequestsToComponentAction#getTargetRequiringAllocationContexts <em>Target Requiring Allocation Contexts</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Target Requiring Allocation Contexts</em>'.
     * @see org.iobserve.adaptation.executionplan.BlockRequestsToComponentAction#getTargetRequiringAllocationContexts()
     * @see #getBlockRequestsToComponentAction()
     * @generated
     */
    EReference getBlockRequestsToComponentAction_TargetRequiringAllocationContexts();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.DisconnectComponentAction <em>Disconnect Component Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Disconnect Component Action</em>'.
     * @see org.iobserve.adaptation.executionplan.DisconnectComponentAction
     * @generated
     */
    EClass getDisconnectComponentAction();

    /**
     * Returns the meta object for the reference list '{@link org.iobserve.adaptation.executionplan.DisconnectComponentAction#getTargetProvidingAllocationContexts <em>Target Providing Allocation Contexts</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Target Providing Allocation Contexts</em>'.
     * @see org.iobserve.adaptation.executionplan.DisconnectComponentAction#getTargetProvidingAllocationContexts()
     * @see #getDisconnectComponentAction()
     * @generated
     */
    EReference getDisconnectComponentAction_TargetProvidingAllocationContexts();

    /**
     * Returns the meta object for the reference list '{@link org.iobserve.adaptation.executionplan.DisconnectComponentAction#getTargetRequiringAllocationContexts <em>Target Requiring Allocation Contexts</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Target Requiring Allocation Contexts</em>'.
     * @see org.iobserve.adaptation.executionplan.DisconnectComponentAction#getTargetRequiringAllocationContexts()
     * @see #getDisconnectComponentAction()
     * @generated
     */
    EReference getDisconnectComponentAction_TargetRequiringAllocationContexts();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.FinishComponentAction <em>Finish Component Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Finish Component Action</em>'.
     * @see org.iobserve.adaptation.executionplan.FinishComponentAction
     * @generated
     */
    EClass getFinishComponentAction();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.AllocateNodeAction <em>Allocate Node Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Allocate Node Action</em>'.
     * @see org.iobserve.adaptation.executionplan.AllocateNodeAction
     * @generated
     */
    EClass getAllocateNodeAction();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.DeallocateNodeAction <em>Deallocate Node Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Deallocate Node Action</em>'.
     * @see org.iobserve.adaptation.executionplan.DeallocateNodeAction
     * @generated
     */
    EClass getDeallocateNodeAction();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.ConnectNodeAction <em>Connect Node Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Connect Node Action</em>'.
     * @see org.iobserve.adaptation.executionplan.ConnectNodeAction
     * @generated
     */
    EClass getConnectNodeAction();

    /**
     * Returns the meta object for the reference list '{@link org.iobserve.adaptation.executionplan.ConnectNodeAction#getTargetConnectors <em>Target Connectors</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Target Connectors</em>'.
     * @see org.iobserve.adaptation.executionplan.ConnectNodeAction#getTargetConnectors()
     * @see #getConnectNodeAction()
     * @generated
     */
    EReference getConnectNodeAction_TargetConnectors();

    /**
     * Returns the meta object for class '{@link org.iobserve.adaptation.executionplan.DisconnectNodeAction <em>Disconnect Node Action</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Disconnect Node Action</em>'.
     * @see org.iobserve.adaptation.executionplan.DisconnectNodeAction
     * @generated
     */
    EClass getDisconnectNodeAction();

    /**
     * Returns the meta object for the reference list '{@link org.iobserve.adaptation.executionplan.DisconnectNodeAction#getTargetConnectors <em>Target Connectors</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Target Connectors</em>'.
     * @see org.iobserve.adaptation.executionplan.DisconnectNodeAction#getTargetConnectors()
     * @see #getDisconnectNodeAction()
     * @generated
     */
    EReference getDisconnectNodeAction_TargetConnectors();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    ExecutionplanFactory getExecutionplanFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each operation of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.ExecutionPlanImpl <em>Execution Plan</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionPlanImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getExecutionPlan()
         * @generated
         */
        EClass EXECUTION_PLAN = eINSTANCE.getExecutionPlan();

        /**
         * The meta object literal for the '<em><b>Actions</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference EXECUTION_PLAN__ACTIONS = eINSTANCE.getExecutionPlan_Actions();

        /**
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.ActionImpl <em>Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.ActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getAction()
         * @generated
         */
        EClass ACTION = eINSTANCE.getAction();

        /**
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.AssemblyContextActionImpl <em>Assembly Context Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.AssemblyContextActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getAssemblyContextAction()
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
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.ResourceContainerActionImpl <em>Resource Container Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.ResourceContainerActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getResourceContainerAction()
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
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.DeployComponentActionImpl <em>Deploy Component Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.DeployComponentActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getDeployComponentAction()
         * @generated
         */
        EClass DEPLOY_COMPONENT_ACTION = eINSTANCE.getDeployComponentAction();

        /**
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.UndeployComponentActionImpl <em>Undeploy Component Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.UndeployComponentActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getUndeployComponentAction()
         * @generated
         */
        EClass UNDEPLOY_COMPONENT_ACTION = eINSTANCE.getUndeployComponentAction();

        /**
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.MigrateComponentStateActionImpl <em>Migrate Component State Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.MigrateComponentStateActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getMigrateComponentStateAction()
         * @generated
         */
        EClass MIGRATE_COMPONENT_STATE_ACTION = eINSTANCE.getMigrateComponentStateAction();

        /**
         * The meta object literal for the '<em><b>Source Allocation Context</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference MIGRATE_COMPONENT_STATE_ACTION__SOURCE_ALLOCATION_CONTEXT = eINSTANCE.getMigrateComponentStateAction_SourceAllocationContext();

        /**
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.ConnectComponentActionImpl <em>Connect Component Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.ConnectComponentActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getConnectComponentAction()
         * @generated
         */
        EClass CONNECT_COMPONENT_ACTION = eINSTANCE.getConnectComponentAction();

        /**
         * The meta object literal for the '<em><b>Target Providing Allocation Contexts</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONNECT_COMPONENT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS = eINSTANCE.getConnectComponentAction_TargetProvidingAllocationContexts();

        /**
         * The meta object literal for the '<em><b>Target Requiring Allocation Contexts</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONNECT_COMPONENT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS = eINSTANCE.getConnectComponentAction_TargetRequiringAllocationContexts();

        /**
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.BlockRequestsToComponentActionImpl <em>Block Requests To Component Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.BlockRequestsToComponentActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getBlockRequestsToComponentAction()
         * @generated
         */
        EClass BLOCK_REQUESTS_TO_COMPONENT_ACTION = eINSTANCE.getBlockRequestsToComponentAction();

        /**
         * The meta object literal for the '<em><b>Target Requiring Allocation Contexts</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BLOCK_REQUESTS_TO_COMPONENT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS = eINSTANCE.getBlockRequestsToComponentAction_TargetRequiringAllocationContexts();

        /**
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.DisconnectComponentActionImpl <em>Disconnect Component Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.DisconnectComponentActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getDisconnectComponentAction()
         * @generated
         */
        EClass DISCONNECT_COMPONENT_ACTION = eINSTANCE.getDisconnectComponentAction();

        /**
         * The meta object literal for the '<em><b>Target Providing Allocation Contexts</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DISCONNECT_COMPONENT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS = eINSTANCE.getDisconnectComponentAction_TargetProvidingAllocationContexts();

        /**
         * The meta object literal for the '<em><b>Target Requiring Allocation Contexts</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DISCONNECT_COMPONENT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS = eINSTANCE.getDisconnectComponentAction_TargetRequiringAllocationContexts();

        /**
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.FinishComponentActionImpl <em>Finish Component Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.FinishComponentActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getFinishComponentAction()
         * @generated
         */
        EClass FINISH_COMPONENT_ACTION = eINSTANCE.getFinishComponentAction();

        /**
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.AllocateNodeActionImpl <em>Allocate Node Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.AllocateNodeActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getAllocateNodeAction()
         * @generated
         */
        EClass ALLOCATE_NODE_ACTION = eINSTANCE.getAllocateNodeAction();

        /**
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.DeallocateNodeActionImpl <em>Deallocate Node Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.DeallocateNodeActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getDeallocateNodeAction()
         * @generated
         */
        EClass DEALLOCATE_NODE_ACTION = eINSTANCE.getDeallocateNodeAction();

        /**
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.ConnectNodeActionImpl <em>Connect Node Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.ConnectNodeActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getConnectNodeAction()
         * @generated
         */
        EClass CONNECT_NODE_ACTION = eINSTANCE.getConnectNodeAction();

        /**
         * The meta object literal for the '<em><b>Target Connectors</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONNECT_NODE_ACTION__TARGET_CONNECTORS = eINSTANCE.getConnectNodeAction_TargetConnectors();

        /**
         * The meta object literal for the '{@link org.iobserve.adaptation.executionplan.impl.DisconnectNodeActionImpl <em>Disconnect Node Action</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.adaptation.executionplan.impl.DisconnectNodeActionImpl
         * @see org.iobserve.adaptation.executionplan.impl.ExecutionplanPackageImpl#getDisconnectNodeAction()
         * @generated
         */
        EClass DISCONNECT_NODE_ACTION = eINSTANCE.getDisconnectNodeAction();

        /**
         * The meta object literal for the '<em><b>Target Connectors</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DISCONNECT_NODE_ACTION__TARGET_CONNECTORS = eINSTANCE.getDisconnectNodeAction_TargetConnectors();

    }

} //ExecutionplanPackage
