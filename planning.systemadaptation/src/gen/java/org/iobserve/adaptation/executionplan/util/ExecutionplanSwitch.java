/**
 */
package org.iobserve.adaptation.executionplan.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.iobserve.adaptation.executionplan.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage
 * @generated
 */
public class ExecutionplanSwitch<T> extends Switch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static ExecutionplanPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecutionplanSwitch() {
        if (modelPackage == null) {
            modelPackage = ExecutionplanPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage) {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case ExecutionplanPackage.EXECUTION_PLAN: {
                ExecutionPlan executionPlan = (ExecutionPlan)theEObject;
                T result = caseExecutionPlan(executionPlan);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.ATOMIC_ACTION: {
                AtomicAction atomicAction = (AtomicAction)theEObject;
                T result = caseAtomicAction(atomicAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.ASSEMBLY_CONTEXT_ACTION: {
                AssemblyContextAction assemblyContextAction = (AssemblyContextAction)theEObject;
                T result = caseAssemblyContextAction(assemblyContextAction);
                if (result == null) result = caseAtomicAction(assemblyContextAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.RESOURCE_CONTAINER_ACTION: {
                ResourceContainerAction resourceContainerAction = (ResourceContainerAction)theEObject;
                T result = caseResourceContainerAction(resourceContainerAction);
                if (result == null) result = caseAtomicAction(resourceContainerAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.DEPLOY_COMPONENT_ACTION: {
                DeployComponentAction deployComponentAction = (DeployComponentAction)theEObject;
                T result = caseDeployComponentAction(deployComponentAction);
                if (result == null) result = caseAssemblyContextAction(deployComponentAction);
                if (result == null) result = caseAtomicAction(deployComponentAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.UNDEPLOY_COMPONENT_ACTION: {
                UndeployComponentAction undeployComponentAction = (UndeployComponentAction)theEObject;
                T result = caseUndeployComponentAction(undeployComponentAction);
                if (result == null) result = caseAssemblyContextAction(undeployComponentAction);
                if (result == null) result = caseAtomicAction(undeployComponentAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.MIGRATE_COMPONENT_STATE_ACTION: {
                MigrateComponentStateAction migrateComponentStateAction = (MigrateComponentStateAction)theEObject;
                T result = caseMigrateComponentStateAction(migrateComponentStateAction);
                if (result == null) result = caseAssemblyContextAction(migrateComponentStateAction);
                if (result == null) result = caseAtomicAction(migrateComponentStateAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.CONNECT_COMPONENT_ACTION: {
                ConnectComponentAction connectComponentAction = (ConnectComponentAction)theEObject;
                T result = caseConnectComponentAction(connectComponentAction);
                if (result == null) result = caseAssemblyContextAction(connectComponentAction);
                if (result == null) result = caseAtomicAction(connectComponentAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.BLOCK_REQUESTS_TO_COMPONENT_ACTION: {
                BlockRequestsToComponentAction blockRequestsToComponentAction = (BlockRequestsToComponentAction)theEObject;
                T result = caseBlockRequestsToComponentAction(blockRequestsToComponentAction);
                if (result == null) result = caseAssemblyContextAction(blockRequestsToComponentAction);
                if (result == null) result = caseAtomicAction(blockRequestsToComponentAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.DISCONNECT_COMPONENT_ACTION: {
                DisconnectComponentAction disconnectComponentAction = (DisconnectComponentAction)theEObject;
                T result = caseDisconnectComponentAction(disconnectComponentAction);
                if (result == null) result = caseAssemblyContextAction(disconnectComponentAction);
                if (result == null) result = caseAtomicAction(disconnectComponentAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.FINISH_COMPONENT_ACTION: {
                FinishComponentAction finishComponentAction = (FinishComponentAction)theEObject;
                T result = caseFinishComponentAction(finishComponentAction);
                if (result == null) result = caseAssemblyContextAction(finishComponentAction);
                if (result == null) result = caseAtomicAction(finishComponentAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.ALLOCATE_NODE_ACTION: {
                AllocateNodeAction allocateNodeAction = (AllocateNodeAction)theEObject;
                T result = caseAllocateNodeAction(allocateNodeAction);
                if (result == null) result = caseResourceContainerAction(allocateNodeAction);
                if (result == null) result = caseAtomicAction(allocateNodeAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.DEALLOCATE_NODE_ACTION: {
                DeallocateNodeAction deallocateNodeAction = (DeallocateNodeAction)theEObject;
                T result = caseDeallocateNodeAction(deallocateNodeAction);
                if (result == null) result = caseResourceContainerAction(deallocateNodeAction);
                if (result == null) result = caseAtomicAction(deallocateNodeAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.CONNECT_NODE_ACTION: {
                ConnectNodeAction connectNodeAction = (ConnectNodeAction)theEObject;
                T result = caseConnectNodeAction(connectNodeAction);
                if (result == null) result = caseResourceContainerAction(connectNodeAction);
                if (result == null) result = caseAtomicAction(connectNodeAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExecutionplanPackage.DISCONNECT_NODE_ACTION: {
                DisconnectNodeAction disconnectNodeAction = (DisconnectNodeAction)theEObject;
                T result = caseDisconnectNodeAction(disconnectNodeAction);
                if (result == null) result = caseResourceContainerAction(disconnectNodeAction);
                if (result == null) result = caseAtomicAction(disconnectNodeAction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Execution Plan</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Execution Plan</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExecutionPlan(ExecutionPlan object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Atomic Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Atomic Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAtomicAction(AtomicAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Assembly Context Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Assembly Context Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssemblyContextAction(AssemblyContextAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Resource Container Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Resource Container Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseResourceContainerAction(ResourceContainerAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Deploy Component Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Deploy Component Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDeployComponentAction(DeployComponentAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Undeploy Component Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Undeploy Component Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUndeployComponentAction(UndeployComponentAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Migrate Component State Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Migrate Component State Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMigrateComponentStateAction(MigrateComponentStateAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connect Component Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connect Component Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnectComponentAction(ConnectComponentAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Block Requests To Component Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Block Requests To Component Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBlockRequestsToComponentAction(BlockRequestsToComponentAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Disconnect Component Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Disconnect Component Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDisconnectComponentAction(DisconnectComponentAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Finish Component Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Finish Component Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFinishComponentAction(FinishComponentAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Allocate Node Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Allocate Node Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAllocateNodeAction(AllocateNodeAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Deallocate Node Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Deallocate Node Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDeallocateNodeAction(DeallocateNodeAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connect Node Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connect Node Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnectNodeAction(ConnectNodeAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Disconnect Node Action</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Disconnect Node Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDisconnectNodeAction(DisconnectNodeAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object) {
        return null;
    }

} //ExecutionplanSwitch
