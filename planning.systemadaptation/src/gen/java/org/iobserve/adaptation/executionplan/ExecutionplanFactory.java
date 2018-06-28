/**
 */
package org.iobserve.adaptation.executionplan;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage
 * @generated
 */
public interface ExecutionplanFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ExecutionplanFactory eINSTANCE = org.iobserve.adaptation.executionplan.impl.ExecutionplanFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Execution Plan</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Execution Plan</em>'.
     * @generated
     */
    ExecutionPlan createExecutionPlan();

    /**
     * Returns a new object of class '<em>Deploy Component Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Deploy Component Action</em>'.
     * @generated
     */
    DeployComponentAction createDeployComponentAction();

    /**
     * Returns a new object of class '<em>Undeploy Component Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Undeploy Component Action</em>'.
     * @generated
     */
    UndeployComponentAction createUndeployComponentAction();

    /**
     * Returns a new object of class '<em>Migrate Component State Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Migrate Component State Action</em>'.
     * @generated
     */
    MigrateComponentStateAction createMigrateComponentStateAction();

    /**
     * Returns a new object of class '<em>Connect Component Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Connect Component Action</em>'.
     * @generated
     */
    ConnectComponentAction createConnectComponentAction();

    /**
     * Returns a new object of class '<em>Block Requests To Component Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Block Requests To Component Action</em>'.
     * @generated
     */
    BlockRequestsToComponentAction createBlockRequestsToComponentAction();

    /**
     * Returns a new object of class '<em>Disconnect Component Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Disconnect Component Action</em>'.
     * @generated
     */
    DisconnectComponentAction createDisconnectComponentAction();

    /**
     * Returns a new object of class '<em>Finish Component Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Finish Component Action</em>'.
     * @generated
     */
    FinishComponentAction createFinishComponentAction();

    /**
     * Returns a new object of class '<em>Allocate Node Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Allocate Node Action</em>'.
     * @generated
     */
    AllocateNodeAction createAllocateNodeAction();

    /**
     * Returns a new object of class '<em>Deallocate Node Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Deallocate Node Action</em>'.
     * @generated
     */
    DeallocateNodeAction createDeallocateNodeAction();

    /**
     * Returns a new object of class '<em>Connect Node Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Connect Node Action</em>'.
     * @generated
     */
    ConnectNodeAction createConnectNodeAction();

    /**
     * Returns a new object of class '<em>Disconnect Node Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Disconnect Node Action</em>'.
     * @generated
     */
    DisconnectNodeAction createDisconnectNodeAction();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    ExecutionplanPackage getExecutionplanPackage();

} //ExecutionplanFactory
