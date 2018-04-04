/**
 */
package org.iobserve.adaptation.executionplan.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.iobserve.adaptation.executionplan.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage
 * @generated
 */
public class ExecutionplanAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static ExecutionplanPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecutionplanAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = ExecutionplanPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ExecutionplanSwitch<Adapter> modelSwitch =
        new ExecutionplanSwitch<Adapter>() {
            @Override
            public Adapter caseExecutionPlan(ExecutionPlan object) {
                return createExecutionPlanAdapter();
            }
            @Override
            public Adapter caseAction(Action object) {
                return createActionAdapter();
            }
            @Override
            public Adapter caseAssemblyContextAction(AssemblyContextAction object) {
                return createAssemblyContextActionAdapter();
            }
            @Override
            public Adapter caseResourceContainerAction(ResourceContainerAction object) {
                return createResourceContainerActionAdapter();
            }
            @Override
            public Adapter caseDeployComponentAction(DeployComponentAction object) {
                return createDeployComponentActionAdapter();
            }
            @Override
            public Adapter caseUndeployComponentAction(UndeployComponentAction object) {
                return createUndeployComponentActionAdapter();
            }
            @Override
            public Adapter caseMigrateComponentStateAction(MigrateComponentStateAction object) {
                return createMigrateComponentStateActionAdapter();
            }
            @Override
            public Adapter caseConnectComponentAction(ConnectComponentAction object) {
                return createConnectComponentActionAdapter();
            }
            @Override
            public Adapter caseBlockRequestsToComponentAction(BlockRequestsToComponentAction object) {
                return createBlockRequestsToComponentActionAdapter();
            }
            @Override
            public Adapter caseDisconnectComponentAction(DisconnectComponentAction object) {
                return createDisconnectComponentActionAdapter();
            }
            @Override
            public Adapter caseFinishComponentAction(FinishComponentAction object) {
                return createFinishComponentActionAdapter();
            }
            @Override
            public Adapter caseAllocateNodeAction(AllocateNodeAction object) {
                return createAllocateNodeActionAdapter();
            }
            @Override
            public Adapter caseDeallocateNodeAction(DeallocateNodeAction object) {
                return createDeallocateNodeActionAdapter();
            }
            @Override
            public Adapter caseConnectNodeAction(ConnectNodeAction object) {
                return createConnectNodeActionAdapter();
            }
            @Override
            public Adapter caseDisconnectNodeAction(DisconnectNodeAction object) {
                return createDisconnectNodeActionAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.ExecutionPlan <em>Execution Plan</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.ExecutionPlan
     * @generated
     */
    public Adapter createExecutionPlanAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.Action <em>Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.Action
     * @generated
     */
    public Adapter createActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.AssemblyContextAction <em>Assembly Context Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.AssemblyContextAction
     * @generated
     */
    public Adapter createAssemblyContextActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.ResourceContainerAction <em>Resource Container Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.ResourceContainerAction
     * @generated
     */
    public Adapter createResourceContainerActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.DeployComponentAction <em>Deploy Component Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.DeployComponentAction
     * @generated
     */
    public Adapter createDeployComponentActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.UndeployComponentAction <em>Undeploy Component Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.UndeployComponentAction
     * @generated
     */
    public Adapter createUndeployComponentActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.MigrateComponentStateAction <em>Migrate Component State Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.MigrateComponentStateAction
     * @generated
     */
    public Adapter createMigrateComponentStateActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.ConnectComponentAction <em>Connect Component Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.ConnectComponentAction
     * @generated
     */
    public Adapter createConnectComponentActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.BlockRequestsToComponentAction <em>Block Requests To Component Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.BlockRequestsToComponentAction
     * @generated
     */
    public Adapter createBlockRequestsToComponentActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.DisconnectComponentAction <em>Disconnect Component Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.DisconnectComponentAction
     * @generated
     */
    public Adapter createDisconnectComponentActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.FinishComponentAction <em>Finish Component Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.FinishComponentAction
     * @generated
     */
    public Adapter createFinishComponentActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.AllocateNodeAction <em>Allocate Node Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.AllocateNodeAction
     * @generated
     */
    public Adapter createAllocateNodeActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.DeallocateNodeAction <em>Deallocate Node Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.DeallocateNodeAction
     * @generated
     */
    public Adapter createDeallocateNodeActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.ConnectNodeAction <em>Connect Node Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.ConnectNodeAction
     * @generated
     */
    public Adapter createConnectNodeActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.iobserve.adaptation.executionplan.DisconnectNodeAction <em>Disconnect Node Action</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.iobserve.adaptation.executionplan.DisconnectNodeAction
     * @generated
     */
    public Adapter createDisconnectNodeActionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //ExecutionplanAdapterFactory
