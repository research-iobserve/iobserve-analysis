/**
 */
package org.iobserve.adaptation.executionplan.impl;

import de.uka.ipd.sdq.identifier.IdentifierPackage;

import de.uka.ipd.sdq.probfunction.ProbfunctionPackage;

import de.uka.ipd.sdq.stoex.StoexPackage;

import de.uka.ipd.sdq.units.UnitsPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.iobserve.adaptation.executionplan.AllocateNodeAction;
import org.iobserve.adaptation.executionplan.AssemblyContextAction;
import org.iobserve.adaptation.executionplan.AtomicAction;
import org.iobserve.adaptation.executionplan.BlockRequestsToComponentAction;
import org.iobserve.adaptation.executionplan.ConnectComponentAction;
import org.iobserve.adaptation.executionplan.ConnectNodeAction;
import org.iobserve.adaptation.executionplan.DeallocateNodeAction;
import org.iobserve.adaptation.executionplan.DeployComponentAction;
import org.iobserve.adaptation.executionplan.DisconnectComponentAction;
import org.iobserve.adaptation.executionplan.DisconnectNodeAction;
import org.iobserve.adaptation.executionplan.ExecutionPlan;
import org.iobserve.adaptation.executionplan.ExecutionplanFactory;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;
import org.iobserve.adaptation.executionplan.FinishComponentAction;
import org.iobserve.adaptation.executionplan.MigrateComponentStateAction;
import org.iobserve.adaptation.executionplan.ResourceContainerAction;
import org.iobserve.adaptation.executionplan.UndeployComponentAction;

import org.palladiosimulator.pcm.PcmPackage;

import org.palladiosimulator.pcm.allocation.AllocationPackage;

import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ExecutionplanPackageImpl extends EPackageImpl implements ExecutionplanPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass executionPlanEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass atomicActionEClass = null;

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
    private EClass deployComponentActionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass undeployComponentActionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass migrateComponentStateActionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass connectComponentActionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass blockRequestsToComponentActionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass disconnectComponentActionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass finishComponentActionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass allocateNodeActionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass deallocateNodeActionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass connectNodeActionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass disconnectNodeActionEClass = null;

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
     * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private ExecutionplanPackageImpl() {
        super(eNS_URI, ExecutionplanFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link ExecutionplanPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static ExecutionplanPackage init() {
        if (isInited) return (ExecutionplanPackage)EPackage.Registry.INSTANCE.getEPackage(ExecutionplanPackage.eNS_URI);

        // Obtain or create and register package
        ExecutionplanPackageImpl theExecutionplanPackage = (ExecutionplanPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ExecutionplanPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ExecutionplanPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        IdentifierPackage.eINSTANCE.eClass();
        PcmPackage.eINSTANCE.eClass();
        ProbfunctionPackage.eINSTANCE.eClass();
        StoexPackage.eINSTANCE.eClass();
        UnitsPackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theExecutionplanPackage.createPackageContents();

        // Initialize created meta-data
        theExecutionplanPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theExecutionplanPackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(ExecutionplanPackage.eNS_URI, theExecutionplanPackage);
        return theExecutionplanPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getExecutionPlan() {
        return executionPlanEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getExecutionPlan_Actions() {
        return (EReference)executionPlanEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAtomicAction() {
        return atomicActionEClass;
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
    public EReference getAssemblyContextAction_TargetAllocationContext() {
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
    public EReference getResourceContainerAction_TargetResourceContainer() {
        return (EReference)resourceContainerActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDeployComponentAction() {
        return deployComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUndeployComponentAction() {
        return undeployComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMigrateComponentStateAction() {
        return migrateComponentStateActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMigrateComponentStateAction_SourceAllocationContext() {
        return (EReference)migrateComponentStateActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getConnectComponentAction() {
        return connectComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConnectComponentAction_TargetProvidingAllocationContexts() {
        return (EReference)connectComponentActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConnectComponentAction_TargetRequiringAllocationContexts() {
        return (EReference)connectComponentActionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBlockRequestsToComponentAction() {
        return blockRequestsToComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBlockRequestsToComponentAction_TargetRequiringAllocationContexts() {
        return (EReference)blockRequestsToComponentActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDisconnectComponentAction() {
        return disconnectComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDisconnectComponentAction_TargetProvidingAllocationContexts() {
        return (EReference)disconnectComponentActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDisconnectComponentAction_TargetRequiringAllocationContexts() {
        return (EReference)disconnectComponentActionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getFinishComponentAction() {
        return finishComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAllocateNodeAction() {
        return allocateNodeActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDeallocateNodeAction() {
        return deallocateNodeActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getConnectNodeAction() {
        return connectNodeActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getConnectNodeAction_TargetConnectors() {
        return (EReference)connectNodeActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDisconnectNodeAction() {
        return disconnectNodeActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDisconnectNodeAction_TargetConnectors() {
        return (EReference)disconnectNodeActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecutionplanFactory getExecutionplanFactory() {
        return (ExecutionplanFactory)getEFactoryInstance();
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
        executionPlanEClass = createEClass(EXECUTION_PLAN);
        createEReference(executionPlanEClass, EXECUTION_PLAN__ACTIONS);

        atomicActionEClass = createEClass(ATOMIC_ACTION);

        assemblyContextActionEClass = createEClass(ASSEMBLY_CONTEXT_ACTION);
        createEReference(assemblyContextActionEClass, ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT);

        resourceContainerActionEClass = createEClass(RESOURCE_CONTAINER_ACTION);
        createEReference(resourceContainerActionEClass, RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER);

        deployComponentActionEClass = createEClass(DEPLOY_COMPONENT_ACTION);

        undeployComponentActionEClass = createEClass(UNDEPLOY_COMPONENT_ACTION);

        migrateComponentStateActionEClass = createEClass(MIGRATE_COMPONENT_STATE_ACTION);
        createEReference(migrateComponentStateActionEClass, MIGRATE_COMPONENT_STATE_ACTION__SOURCE_ALLOCATION_CONTEXT);

        connectComponentActionEClass = createEClass(CONNECT_COMPONENT_ACTION);
        createEReference(connectComponentActionEClass, CONNECT_COMPONENT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS);
        createEReference(connectComponentActionEClass, CONNECT_COMPONENT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS);

        blockRequestsToComponentActionEClass = createEClass(BLOCK_REQUESTS_TO_COMPONENT_ACTION);
        createEReference(blockRequestsToComponentActionEClass, BLOCK_REQUESTS_TO_COMPONENT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS);

        disconnectComponentActionEClass = createEClass(DISCONNECT_COMPONENT_ACTION);
        createEReference(disconnectComponentActionEClass, DISCONNECT_COMPONENT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS);
        createEReference(disconnectComponentActionEClass, DISCONNECT_COMPONENT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS);

        finishComponentActionEClass = createEClass(FINISH_COMPONENT_ACTION);

        allocateNodeActionEClass = createEClass(ALLOCATE_NODE_ACTION);

        deallocateNodeActionEClass = createEClass(DEALLOCATE_NODE_ACTION);

        connectNodeActionEClass = createEClass(CONNECT_NODE_ACTION);
        createEReference(connectNodeActionEClass, CONNECT_NODE_ACTION__TARGET_CONNECTORS);

        disconnectNodeActionEClass = createEClass(DISCONNECT_NODE_ACTION);
        createEReference(disconnectNodeActionEClass, DISCONNECT_NODE_ACTION__TARGET_CONNECTORS);
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
        AllocationPackage theAllocationPackage = (AllocationPackage)EPackage.Registry.INSTANCE.getEPackage(AllocationPackage.eNS_URI);
        ResourceenvironmentPackage theResourceenvironmentPackage = (ResourceenvironmentPackage)EPackage.Registry.INSTANCE.getEPackage(ResourceenvironmentPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        assemblyContextActionEClass.getESuperTypes().add(this.getAtomicAction());
        resourceContainerActionEClass.getESuperTypes().add(this.getAtomicAction());
        deployComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        undeployComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        migrateComponentStateActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        connectComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        blockRequestsToComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        disconnectComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        finishComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        allocateNodeActionEClass.getESuperTypes().add(this.getResourceContainerAction());
        deallocateNodeActionEClass.getESuperTypes().add(this.getResourceContainerAction());
        connectNodeActionEClass.getESuperTypes().add(this.getResourceContainerAction());
        disconnectNodeActionEClass.getESuperTypes().add(this.getResourceContainerAction());

        // Initialize classes and features; add operations and parameters
        initEClass(executionPlanEClass, ExecutionPlan.class, "ExecutionPlan", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExecutionPlan_Actions(), this.getAtomicAction(), null, "actions", null, 0, -1, ExecutionPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(atomicActionEClass, AtomicAction.class, "AtomicAction", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(assemblyContextActionEClass, AssemblyContextAction.class, "AssemblyContextAction", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAssemblyContextAction_TargetAllocationContext(), theAllocationPackage.getAllocationContext(), null, "targetAllocationContext", null, 1, 1, AssemblyContextAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(resourceContainerActionEClass, ResourceContainerAction.class, "ResourceContainerAction", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getResourceContainerAction_TargetResourceContainer(), theResourceenvironmentPackage.getResourceContainer(), null, "targetResourceContainer", null, 1, 1, ResourceContainerAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(deployComponentActionEClass, DeployComponentAction.class, "DeployComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(undeployComponentActionEClass, UndeployComponentAction.class, "UndeployComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(migrateComponentStateActionEClass, MigrateComponentStateAction.class, "MigrateComponentStateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMigrateComponentStateAction_SourceAllocationContext(), theAllocationPackage.getAllocationContext(), null, "sourceAllocationContext", null, 1, 1, MigrateComponentStateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(connectComponentActionEClass, ConnectComponentAction.class, "ConnectComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConnectComponentAction_TargetProvidingAllocationContexts(), theAllocationPackage.getAllocationContext(), null, "targetProvidingAllocationContexts", null, 0, -1, ConnectComponentAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getConnectComponentAction_TargetRequiringAllocationContexts(), theAllocationPackage.getAllocationContext(), null, "targetRequiringAllocationContexts", null, 0, -1, ConnectComponentAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(blockRequestsToComponentActionEClass, BlockRequestsToComponentAction.class, "BlockRequestsToComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getBlockRequestsToComponentAction_TargetRequiringAllocationContexts(), theAllocationPackage.getAllocationContext(), null, "targetRequiringAllocationContexts", null, 0, -1, BlockRequestsToComponentAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(disconnectComponentActionEClass, DisconnectComponentAction.class, "DisconnectComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDisconnectComponentAction_TargetProvidingAllocationContexts(), theAllocationPackage.getAllocationContext(), null, "targetProvidingAllocationContexts", null, 0, -1, DisconnectComponentAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDisconnectComponentAction_TargetRequiringAllocationContexts(), theAllocationPackage.getAllocationContext(), null, "targetRequiringAllocationContexts", null, 0, -1, DisconnectComponentAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(finishComponentActionEClass, FinishComponentAction.class, "FinishComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(allocateNodeActionEClass, AllocateNodeAction.class, "AllocateNodeAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(deallocateNodeActionEClass, DeallocateNodeAction.class, "DeallocateNodeAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(connectNodeActionEClass, ConnectNodeAction.class, "ConnectNodeAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConnectNodeAction_TargetConnectors(), theResourceenvironmentPackage.getLinkingResource(), null, "targetConnectors", null, 0, -1, ConnectNodeAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(disconnectNodeActionEClass, DisconnectNodeAction.class, "DisconnectNodeAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDisconnectNodeAction_TargetConnectors(), theResourceenvironmentPackage.getLinkingResource(), null, "targetConnectors", null, 0, -1, DisconnectNodeAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Create resource
        createResource(eNS_URI);
    }

} //ExecutionplanPackageImpl
