/**
 */
package org.iobserve.adaptation.executionplan.impl;

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

import de.uka.ipd.sdq.identifier.IdentifierPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 *
 * @generated
 */
public class ExecutionplanPackageImpl extends EPackageImpl implements ExecutionplanPackage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass executionPlanEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass actionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass assemblyContextActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass resourceContainerActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass deployComponentActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass undeployComponentActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass migrateComponentStateActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass connectComponentActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass blockRequestsToComponentActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass disconnectComponentActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass finishComponentActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass allocateNodeActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass deallocateNodeActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass connectNodeActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass disconnectNodeActionEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI
     * value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init
     * init()}, which also performs initialization of the package, or returns the registered
     * package, if one already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private ExecutionplanPackageImpl() {
        super(ExecutionplanPackage.eNS_URI, ExecutionplanFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others
     * upon which it depends.
     *
     * <p>
     * This method is used to initialize {@link ExecutionplanPackage#eINSTANCE} when that field is
     * accessed. Clients should not invoke it directly. Instead, they should simply access that
     * field to obtain the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static ExecutionplanPackage init() {
        if (ExecutionplanPackageImpl.isInited) {
            return (ExecutionplanPackage) EPackage.Registry.INSTANCE.getEPackage(ExecutionplanPackage.eNS_URI);
        }

        // Obtain or create and register package
        final ExecutionplanPackageImpl theExecutionplanPackage = (ExecutionplanPackageImpl) (EPackage.Registry.INSTANCE
                .get(ExecutionplanPackage.eNS_URI) instanceof ExecutionplanPackageImpl
                        ? EPackage.Registry.INSTANCE.get(ExecutionplanPackage.eNS_URI)
                        : new ExecutionplanPackageImpl());

        ExecutionplanPackageImpl.isInited = true;

        // Initialize simple dependencies
        IdentifierPackage.eINSTANCE.eClass();
        PcmPackage.eINSTANCE.eClass();

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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getExecutionPlan() {
        return this.executionPlanEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getExecutionPlan_Actions() {
        return (EReference) this.executionPlanEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAction() {
        return this.actionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAssemblyContextAction() {
        return this.assemblyContextActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAssemblyContextAction_SourceAssemblyContext() {
        return (EReference) this.assemblyContextActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getResourceContainerAction() {
        return this.resourceContainerActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getResourceContainerAction_SourceResourceContainer() {
        return (EReference) this.resourceContainerActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDeployComponentAction() {
        return this.deployComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDeployComponentAction_TargetResourceContainer() {
        return (EReference) this.deployComponentActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getUndeployComponentAction() {
        return this.undeployComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUndeployComponentAction_SourceResourceContainer() {
        return (EReference) this.undeployComponentActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMigrateComponentStateAction() {
        return this.migrateComponentStateActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConnectComponentAction() {
        return this.connectComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getBlockRequestsToComponentAction() {
        return this.blockRequestsToComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDisconnectComponentAction() {
        return this.disconnectComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFinishComponentAction() {
        return this.finishComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAllocateNodeAction() {
        return this.allocateNodeActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDeallocateNodeAction() {
        return this.deallocateNodeActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConnectNodeAction() {
        return this.connectNodeActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDisconnectNodeAction() {
        return this.disconnectNodeActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ExecutionplanFactory getExecutionplanFactory() {
        return (ExecutionplanFactory) this.getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package. This method is guarded to have no affect on
     * any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public void createPackageContents() {
        if (this.isCreated) {
            return;
        }
        this.isCreated = true;

        // Create classes and their features
        this.executionPlanEClass = this.createEClass(ExecutionplanPackage.EXECUTION_PLAN);
        this.createEReference(this.executionPlanEClass, ExecutionplanPackage.EXECUTION_PLAN__ACTIONS);

        this.actionEClass = this.createEClass(ExecutionplanPackage.ACTION);

        this.assemblyContextActionEClass = this.createEClass(ExecutionplanPackage.ASSEMBLY_CONTEXT_ACTION);
        this.createEReference(this.assemblyContextActionEClass,
                ExecutionplanPackage.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT);

        this.resourceContainerActionEClass = this.createEClass(ExecutionplanPackage.RESOURCE_CONTAINER_ACTION);
        this.createEReference(this.resourceContainerActionEClass,
                ExecutionplanPackage.RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER);

        this.deployComponentActionEClass = this.createEClass(ExecutionplanPackage.DEPLOY_COMPONENT_ACTION);
        this.createEReference(this.deployComponentActionEClass,
                ExecutionplanPackage.DEPLOY_COMPONENT_ACTION__TARGET_RESOURCE_CONTAINER);

        this.undeployComponentActionEClass = this.createEClass(ExecutionplanPackage.UNDEPLOY_COMPONENT_ACTION);
        this.createEReference(this.undeployComponentActionEClass,
                ExecutionplanPackage.UNDEPLOY_COMPONENT_ACTION__SOURCE_RESOURCE_CONTAINER);

        this.migrateComponentStateActionEClass = this.createEClass(ExecutionplanPackage.MIGRATE_COMPONENT_STATE_ACTION);

        this.connectComponentActionEClass = this.createEClass(ExecutionplanPackage.CONNECT_COMPONENT_ACTION);

        this.blockRequestsToComponentActionEClass = this
                .createEClass(ExecutionplanPackage.BLOCK_REQUESTS_TO_COMPONENT_ACTION);

        this.disconnectComponentActionEClass = this.createEClass(ExecutionplanPackage.DISCONNECT_COMPONENT_ACTION);

        this.finishComponentActionEClass = this.createEClass(ExecutionplanPackage.FINISH_COMPONENT_ACTION);

        this.allocateNodeActionEClass = this.createEClass(ExecutionplanPackage.ALLOCATE_NODE_ACTION);

        this.deallocateNodeActionEClass = this.createEClass(ExecutionplanPackage.DEALLOCATE_NODE_ACTION);

        this.connectNodeActionEClass = this.createEClass(ExecutionplanPackage.CONNECT_NODE_ACTION);

        this.disconnectNodeActionEClass = this.createEClass(ExecutionplanPackage.DISCONNECT_NODE_ACTION);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model. This method is guarded to have
     * no affect on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public void initializePackageContents() {
        if (this.isInitialized) {
            return;
        }
        this.isInitialized = true;

        // Initialize package
        this.setName(ExecutionplanPackage.eNAME);
        this.setNsPrefix(ExecutionplanPackage.eNS_PREFIX);
        this.setNsURI(ExecutionplanPackage.eNS_URI);

        // Obtain other dependent packages
        final CompositionPackage theCompositionPackage = (CompositionPackage) EPackage.Registry.INSTANCE
                .getEPackage(CompositionPackage.eNS_URI);
        final ResourceenvironmentPackage theResourceenvironmentPackage = (ResourceenvironmentPackage) EPackage.Registry.INSTANCE
                .getEPackage(ResourceenvironmentPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        this.assemblyContextActionEClass.getESuperTypes().add(this.getAction());
        this.resourceContainerActionEClass.getESuperTypes().add(this.getAction());
        this.deployComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        this.undeployComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        this.migrateComponentStateActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        this.connectComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        this.blockRequestsToComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        this.disconnectComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        this.finishComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        this.allocateNodeActionEClass.getESuperTypes().add(this.getResourceContainerAction());
        this.deallocateNodeActionEClass.getESuperTypes().add(this.getResourceContainerAction());
        this.connectNodeActionEClass.getESuperTypes().add(this.getResourceContainerAction());
        this.disconnectNodeActionEClass.getESuperTypes().add(this.getResourceContainerAction());

        // Initialize classes, features, and operations; add parameters
        this.initEClass(this.executionPlanEClass, ExecutionPlan.class, "ExecutionPlan", !EPackageImpl.IS_ABSTRACT,
                !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getExecutionPlan_Actions(), this.getAction(), null, "actions", null, 0, -1,
                ExecutionPlan.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE,
                EPackageImpl.IS_COMPOSITE, !EPackageImpl.IS_RESOLVE_PROXIES, !EPackageImpl.IS_UNSETTABLE,
                EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.actionEClass, Action.class, "Action", !EPackageImpl.IS_ABSTRACT,
                !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.assemblyContextActionEClass, AssemblyContextAction.class, "AssemblyContextAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAssemblyContextAction_SourceAssemblyContext(),
                theCompositionPackage.getAssemblyContext(), null, "sourceAssemblyContext", null, 0, 1,
                AssemblyContextAction.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE, EPackageImpl.IS_RESOLVE_PROXIES,
                !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.resourceContainerActionEClass, ResourceContainerAction.class, "ResourceContainerAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getResourceContainerAction_SourceResourceContainer(),
                theResourceenvironmentPackage.getResourceContainer(), null, "sourceResourceContainer", null, 0, 1,
                ResourceContainerAction.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE, EPackageImpl.IS_RESOLVE_PROXIES,
                !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.deployComponentActionEClass, DeployComponentAction.class, "DeployComponentAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getDeployComponentAction_TargetResourceContainer(),
                theResourceenvironmentPackage.getResourceContainer(), null, "targetResourceContainer", null, 0, 1,
                DeployComponentAction.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE, EPackageImpl.IS_RESOLVE_PROXIES,
                !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.undeployComponentActionEClass, UndeployComponentAction.class, "UndeployComponentAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getUndeployComponentAction_SourceResourceContainer(),
                theResourceenvironmentPackage.getResourceContainer(), null, "sourceResourceContainer", null, 0, 1,
                UndeployComponentAction.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE, EPackageImpl.IS_RESOLVE_PROXIES,
                !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.migrateComponentStateActionEClass, MigrateComponentStateAction.class,
                "MigrateComponentStateAction", !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE,
                EPackageImpl.IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.connectComponentActionEClass, ConnectComponentAction.class, "ConnectComponentAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.blockRequestsToComponentActionEClass, BlockRequestsToComponentAction.class,
                "BlockRequestsToComponentAction", !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE,
                EPackageImpl.IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.disconnectComponentActionEClass, DisconnectComponentAction.class,
                "DisconnectComponentAction", !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE,
                EPackageImpl.IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.finishComponentActionEClass, FinishComponentAction.class, "FinishComponentAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.allocateNodeActionEClass, AllocateNodeAction.class, "AllocateNodeAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.deallocateNodeActionEClass, DeallocateNodeAction.class, "DeallocateNodeAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.connectNodeActionEClass, ConnectNodeAction.class, "ConnectNodeAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.disconnectNodeActionEClass, DisconnectNodeAction.class, "DisconnectNodeAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        this.createResource(ExecutionplanPackage.eNS_URI);
    }

} // ExecutionplanPackageImpl
