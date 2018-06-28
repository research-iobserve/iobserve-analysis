/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.iobserve.adaptation.executionplan.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ExecutionplanFactoryImpl extends EFactoryImpl implements ExecutionplanFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ExecutionplanFactory init() {
        try {
            ExecutionplanFactory theExecutionplanFactory = (ExecutionplanFactory)EPackage.Registry.INSTANCE.getEFactory(ExecutionplanPackage.eNS_URI);
            if (theExecutionplanFactory != null) {
                return theExecutionplanFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ExecutionplanFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecutionplanFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case ExecutionplanPackage.EXECUTION_PLAN: return createExecutionPlan();
            case ExecutionplanPackage.DEPLOY_COMPONENT_ACTION: return createDeployComponentAction();
            case ExecutionplanPackage.UNDEPLOY_COMPONENT_ACTION: return createUndeployComponentAction();
            case ExecutionplanPackage.MIGRATE_COMPONENT_STATE_ACTION: return createMigrateComponentStateAction();
            case ExecutionplanPackage.CONNECT_COMPONENT_ACTION: return createConnectComponentAction();
            case ExecutionplanPackage.BLOCK_REQUESTS_TO_COMPONENT_ACTION: return createBlockRequestsToComponentAction();
            case ExecutionplanPackage.DISCONNECT_COMPONENT_ACTION: return createDisconnectComponentAction();
            case ExecutionplanPackage.FINISH_COMPONENT_ACTION: return createFinishComponentAction();
            case ExecutionplanPackage.ALLOCATE_NODE_ACTION: return createAllocateNodeAction();
            case ExecutionplanPackage.DEALLOCATE_NODE_ACTION: return createDeallocateNodeAction();
            case ExecutionplanPackage.CONNECT_NODE_ACTION: return createConnectNodeAction();
            case ExecutionplanPackage.DISCONNECT_NODE_ACTION: return createDisconnectNodeAction();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecutionPlan createExecutionPlan() {
        ExecutionPlanImpl executionPlan = new ExecutionPlanImpl();
        return executionPlan;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DeployComponentAction createDeployComponentAction() {
        DeployComponentActionImpl deployComponentAction = new DeployComponentActionImpl();
        return deployComponentAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UndeployComponentAction createUndeployComponentAction() {
        UndeployComponentActionImpl undeployComponentAction = new UndeployComponentActionImpl();
        return undeployComponentAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MigrateComponentStateAction createMigrateComponentStateAction() {
        MigrateComponentStateActionImpl migrateComponentStateAction = new MigrateComponentStateActionImpl();
        return migrateComponentStateAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConnectComponentAction createConnectComponentAction() {
        ConnectComponentActionImpl connectComponentAction = new ConnectComponentActionImpl();
        return connectComponentAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BlockRequestsToComponentAction createBlockRequestsToComponentAction() {
        BlockRequestsToComponentActionImpl blockRequestsToComponentAction = new BlockRequestsToComponentActionImpl();
        return blockRequestsToComponentAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DisconnectComponentAction createDisconnectComponentAction() {
        DisconnectComponentActionImpl disconnectComponentAction = new DisconnectComponentActionImpl();
        return disconnectComponentAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FinishComponentAction createFinishComponentAction() {
        FinishComponentActionImpl finishComponentAction = new FinishComponentActionImpl();
        return finishComponentAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllocateNodeAction createAllocateNodeAction() {
        AllocateNodeActionImpl allocateNodeAction = new AllocateNodeActionImpl();
        return allocateNodeAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DeallocateNodeAction createDeallocateNodeAction() {
        DeallocateNodeActionImpl deallocateNodeAction = new DeallocateNodeActionImpl();
        return deallocateNodeAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConnectNodeAction createConnectNodeAction() {
        ConnectNodeActionImpl connectNodeAction = new ConnectNodeActionImpl();
        return connectNodeAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DisconnectNodeAction createDisconnectNodeAction() {
        DisconnectNodeActionImpl disconnectNodeAction = new DisconnectNodeActionImpl();
        return disconnectNodeAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecutionplanPackage getExecutionplanPackage() {
        return (ExecutionplanPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static ExecutionplanPackage getPackage() {
        return ExecutionplanPackage.eINSTANCE;
    }

} //ExecutionplanFactoryImpl
