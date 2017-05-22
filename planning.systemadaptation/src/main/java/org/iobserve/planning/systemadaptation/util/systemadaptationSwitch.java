/**
 */
package org.iobserve.planning.systemadaptation.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.iobserve.planning.systemadaptation.*;

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
 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage
 * @generated
 */
public class systemadaptationSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static systemadaptationPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public systemadaptationSwitch() {
		if (modelPackage == null) {
			modelPackage = systemadaptationPackage.eINSTANCE;
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
			case systemadaptationPackage.SYSTEM_ADAPTATION: {
				SystemAdaptation systemAdaptation = (SystemAdaptation)theEObject;
				T result = caseSystemAdaptation(systemAdaptation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case systemadaptationPackage.ACTION: {
				Action action = (Action)theEObject;
				T result = caseAction(action);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case systemadaptationPackage.ASSEMBLY_CONTEXT_ACTION: {
				AssemblyContextAction assemblyContextAction = (AssemblyContextAction)theEObject;
				T result = caseAssemblyContextAction(assemblyContextAction);
				if (result == null) result = caseAction(assemblyContextAction);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case systemadaptationPackage.RESOURCE_CONTAINER_ACTION: {
				ResourceContainerAction resourceContainerAction = (ResourceContainerAction)theEObject;
				T result = caseResourceContainerAction(resourceContainerAction);
				if (result == null) result = caseAction(resourceContainerAction);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case systemadaptationPackage.CHANGE_REPOSITORY_COMPONENT_ACTION: {
				ChangeRepositoryComponentAction changeRepositoryComponentAction = (ChangeRepositoryComponentAction)theEObject;
				T result = caseChangeRepositoryComponentAction(changeRepositoryComponentAction);
				if (result == null) result = caseAssemblyContextAction(changeRepositoryComponentAction);
				if (result == null) result = caseAction(changeRepositoryComponentAction);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case systemadaptationPackage.ALLOCATE_ACTION: {
				AllocateAction allocateAction = (AllocateAction)theEObject;
				T result = caseAllocateAction(allocateAction);
				if (result == null) result = caseAssemblyContextAction(allocateAction);
				if (result == null) result = caseAction(allocateAction);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case systemadaptationPackage.DEALLOCATE_ACTION: {
				DeallocateAction deallocateAction = (DeallocateAction)theEObject;
				T result = caseDeallocateAction(deallocateAction);
				if (result == null) result = caseAssemblyContextAction(deallocateAction);
				if (result == null) result = caseAction(deallocateAction);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case systemadaptationPackage.MIGRATE_ACTION: {
				MigrateAction migrateAction = (MigrateAction)theEObject;
				T result = caseMigrateAction(migrateAction);
				if (result == null) result = caseAssemblyContextAction(migrateAction);
				if (result == null) result = caseAction(migrateAction);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case systemadaptationPackage.ACQUIRE_ACTION: {
				AcquireAction acquireAction = (AcquireAction)theEObject;
				T result = caseAcquireAction(acquireAction);
				if (result == null) result = caseResourceContainerAction(acquireAction);
				if (result == null) result = caseAction(acquireAction);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case systemadaptationPackage.TERMINATE_ACTION: {
				TerminateAction terminateAction = (TerminateAction)theEObject;
				T result = caseTerminateAction(terminateAction);
				if (result == null) result = caseResourceContainerAction(terminateAction);
				if (result == null) result = caseAction(terminateAction);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case systemadaptationPackage.REPLICATE_ACTION: {
				ReplicateAction replicateAction = (ReplicateAction)theEObject;
				T result = caseReplicateAction(replicateAction);
				if (result == null) result = caseResourceContainerAction(replicateAction);
				if (result == null) result = caseAction(replicateAction);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>System Adaptation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>System Adaptation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSystemAdaptation(SystemAdaptation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Action</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Action</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAction(Action object) {
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
	 * Returns the result of interpreting the object as an instance of '<em>Change Repository Component Action</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Change Repository Component Action</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseChangeRepositoryComponentAction(ChangeRepositoryComponentAction object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Allocate Action</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Allocate Action</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAllocateAction(AllocateAction object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deallocate Action</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deallocate Action</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDeallocateAction(DeallocateAction object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Migrate Action</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Migrate Action</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMigrateAction(MigrateAction object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Acquire Action</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Acquire Action</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAcquireAction(AcquireAction object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Terminate Action</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Terminate Action</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTerminateAction(TerminateAction object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Replicate Action</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Replicate Action</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReplicateAction(ReplicateAction object) {
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

} //systemadaptationSwitch
