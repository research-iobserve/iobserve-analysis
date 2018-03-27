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

import org.iobserve.adaptation.executionplan.Action;
import org.iobserve.adaptation.executionplan.AllocateNodeAction;
import org.iobserve.adaptation.executionplan.AssemblyContextAction;
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

import org.palladiosimulator.pcm.core.composition.CompositionPackage;

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
    public EClass getAction() {
        return actionEClass;
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
    public EClass getDeployComponentAction() {
        return deployComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDeployComponentAction_TargetResourceContainer() {
        return (EReference)deployComponentActionEClass.getEStructuralFeatures().get(0);
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
    public EReference getUndeployComponentAction_SourceResourceContainer() {
        return (EReference)undeployComponentActionEClass.getEStructuralFeatures().get(0);
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
    public EClass getConnectComponentAction() {
        return connectComponentActionEClass;
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
    public EClass getDisconnectComponentAction() {
        return disconnectComponentActionEClass;
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
    public EClass getDisconnectNodeAction() {
        return disconnectNodeActionEClass;
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

        actionEClass = createEClass(ACTION);

        assemblyContextActionEClass = createEClass(ASSEMBLY_CONTEXT_ACTION);
        createEReference(assemblyContextActionEClass, ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT);

        resourceContainerActionEClass = createEClass(RESOURCE_CONTAINER_ACTION);
        createEReference(resourceContainerActionEClass, RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER);

        deployComponentActionEClass = createEClass(DEPLOY_COMPONENT_ACTION);
        createEReference(deployComponentActionEClass, DEPLOY_COMPONENT_ACTION__TARGET_RESOURCE_CONTAINER);

        undeployComponentActionEClass = createEClass(UNDEPLOY_COMPONENT_ACTION);
        createEReference(undeployComponentActionEClass, UNDEPLOY_COMPONENT_ACTION__SOURCE_RESOURCE_CONTAINER);

        migrateComponentStateActionEClass = createEClass(MIGRATE_COMPONENT_STATE_ACTION);

        connectComponentActionEClass = createEClass(CONNECT_COMPONENT_ACTION);

        blockRequestsToComponentActionEClass = createEClass(BLOCK_REQUESTS_TO_COMPONENT_ACTION);

        disconnectComponentActionEClass = createEClass(DISCONNECT_COMPONENT_ACTION);

        finishComponentActionEClass = createEClass(FINISH_COMPONENT_ACTION);

        allocateNodeActionEClass = createEClass(ALLOCATE_NODE_ACTION);

        deallocateNodeActionEClass = createEClass(DEALLOCATE_NODE_ACTION);

        connectNodeActionEClass = createEClass(CONNECT_NODE_ACTION);

        disconnectNodeActionEClass = createEClass(DISCONNECT_NODE_ACTION);
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
        CompositionPackage theCompositionPackage = (CompositionPackage)EPackage.Registry.INSTANCE.getEPackage(CompositionPackage.eNS_URI);
        ResourceenvironmentPackage theResourceenvironmentPackage = (ResourceenvironmentPackage)EPackage.Registry.INSTANCE.getEPackage(ResourceenvironmentPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        assemblyContextActionEClass.getESuperTypes().add(this.getAction());
        resourceContainerActionEClass.getESuperTypes().add(this.getAction());
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

        // Initialize classes, features, and operations; add parameters
        initEClass(executionPlanEClass, ExecutionPlan.class, "ExecutionPlan", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExecutionPlan_Actions(), this.getAction(), null, "actions", null, 0, -1, ExecutionPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(actionEClass, Action.class, "Action", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(assemblyContextActionEClass, AssemblyContextAction.class, "AssemblyContextAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAssemblyContextAction_SourceAssemblyContext(), theCompositionPackage.getAssemblyContext(), null, "sourceAssemblyContext", null, 0, 1, AssemblyContextAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(resourceContainerActionEClass, ResourceContainerAction.class, "ResourceContainerAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getResourceContainerAction_SourceResourceContainer(), theResourceenvironmentPackage.getResourceContainer(), null, "sourceResourceContainer", null, 0, 1, ResourceContainerAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(deployComponentActionEClass, DeployComponentAction.class, "DeployComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDeployComponentAction_TargetResourceContainer(), theResourceenvironmentPackage.getResourceContainer(), null, "targetResourceContainer", null, 0, 1, DeployComponentAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(undeployComponentActionEClass, UndeployComponentAction.class, "UndeployComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getUndeployComponentAction_SourceResourceContainer(), theResourceenvironmentPackage.getResourceContainer(), null, "sourceResourceContainer", null, 0, 1, UndeployComponentAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(migrateComponentStateActionEClass, MigrateComponentStateAction.class, "MigrateComponentStateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(connectComponentActionEClass, ConnectComponentAction.class, "ConnectComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(blockRequestsToComponentActionEClass, BlockRequestsToComponentAction.class, "BlockRequestsToComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(disconnectComponentActionEClass, DisconnectComponentAction.class, "DisconnectComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(finishComponentActionEClass, FinishComponentAction.class, "FinishComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(allocateNodeActionEClass, AllocateNodeAction.class, "AllocateNodeAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(deallocateNodeActionEClass, DeallocateNodeAction.class, "DeallocateNodeAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(connectNodeActionEClass, ConnectNodeAction.class, "ConnectNodeAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(disconnectNodeActionEClass, DisconnectNodeAction.class, "DisconnectNodeAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);
    }

} //ExecutionplanPackageImpl
