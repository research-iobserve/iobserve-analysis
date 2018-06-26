/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.planning.systemadaptation.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.SystemAdaptation;
import org.iobserve.planning.systemadaptation.SystemadaptationPackage;
import org.iobserve.planning.systemadaptation.TerminateAction;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the
 * call {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for
 * each class of the model, starting with the actual class of the object and proceeding up the
 * inheritance hierarchy until a non-null result is returned, which is the result of the switch.
 * <!-- end-user-doc -->
 * 
 * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage
 * @generated
 */
public class systemadaptationSwitch<T> extends Switch<T> {
    /**
     * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected static SystemadaptationPackage modelPackage;

    /**
     * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public systemadaptationSwitch() {
        if (systemadaptationSwitch.modelPackage == null) {
            systemadaptationSwitch.modelPackage = SystemadaptationPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @param ePackage
     *            the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(final EPackage ePackage) {
        return ePackage == systemadaptationSwitch.modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result;
     * it yields that result. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(final int classifierID, final EObject theEObject) {
        switch (classifierID) {
        case SystemadaptationPackage.SYSTEM_ADAPTATION: {
            final SystemAdaptation systemAdaptation = (SystemAdaptation) theEObject;
            T result = this.caseSystemAdaptation(systemAdaptation);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case SystemadaptationPackage.ACTION: {
            final Action action = (Action) theEObject;
            T result = this.caseAction(action);
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION: {
            final AssemblyContextAction assemblyContextAction = (AssemblyContextAction) theEObject;
            T result = this.caseAssemblyContextAction(assemblyContextAction);
            if (result == null) {
                result = this.caseAction(assemblyContextAction);
            }
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case SystemadaptationPackage.RESOURCE_CONTAINER_ACTION: {
            final ResourceContainerAction resourceContainerAction = (ResourceContainerAction) theEObject;
            T result = this.caseResourceContainerAction(resourceContainerAction);
            if (result == null) {
                result = this.caseAction(resourceContainerAction);
            }
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case SystemadaptationPackage.CHANGE_REPOSITORY_COMPONENT_ACTION: {
            final ChangeRepositoryComponentAction changeRepositoryComponentAction = (ChangeRepositoryComponentAction) theEObject;
            T result = this.caseChangeRepositoryComponentAction(changeRepositoryComponentAction);
            if (result == null) {
                result = this.caseAssemblyContextAction(changeRepositoryComponentAction);
            }
            if (result == null) {
                result = this.caseAction(changeRepositoryComponentAction);
            }
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case SystemadaptationPackage.ALLOCATE_ACTION: {
            final AllocateAction allocateAction = (AllocateAction) theEObject;
            T result = this.caseAllocateAction(allocateAction);
            if (result == null) {
                result = this.caseAssemblyContextAction(allocateAction);
            }
            if (result == null) {
                result = this.caseAction(allocateAction);
            }
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case SystemadaptationPackage.DEALLOCATE_ACTION: {
            final DeallocateAction deallocateAction = (DeallocateAction) theEObject;
            T result = this.caseDeallocateAction(deallocateAction);
            if (result == null) {
                result = this.caseAssemblyContextAction(deallocateAction);
            }
            if (result == null) {
                result = this.caseAction(deallocateAction);
            }
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case SystemadaptationPackage.MIGRATE_ACTION: {
            final MigrateAction migrateAction = (MigrateAction) theEObject;
            T result = this.caseMigrateAction(migrateAction);
            if (result == null) {
                result = this.caseAssemblyContextAction(migrateAction);
            }
            if (result == null) {
                result = this.caseAction(migrateAction);
            }
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case SystemadaptationPackage.ACQUIRE_ACTION: {
            final AcquireAction acquireAction = (AcquireAction) theEObject;
            T result = this.caseAcquireAction(acquireAction);
            if (result == null) {
                result = this.caseResourceContainerAction(acquireAction);
            }
            if (result == null) {
                result = this.caseAction(acquireAction);
            }
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case SystemadaptationPackage.TERMINATE_ACTION: {
            final TerminateAction terminateAction = (TerminateAction) theEObject;
            T result = this.caseTerminateAction(terminateAction);
            if (result == null) {
                result = this.caseResourceContainerAction(terminateAction);
            }
            if (result == null) {
                result = this.caseAction(terminateAction);
            }
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        case SystemadaptationPackage.REPLICATE_ACTION: {
            final ReplicateAction replicateAction = (ReplicateAction) theEObject;
            T result = this.caseReplicateAction(replicateAction);
            if (result == null) {
                result = this.caseResourceContainerAction(replicateAction);
            }
            if (result == null) {
                result = this.caseAction(replicateAction);
            }
            if (result == null) {
                result = this.defaultCase(theEObject);
            }
            return result;
        }
        default:
            return this.defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>System Adaptation</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>System Adaptation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSystemAdaptation(final SystemAdaptation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Action</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAction(final Action object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Assembly Context
     * Action</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Assembly Context
     *         Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssemblyContextAction(final AssemblyContextAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Resource Container
     * Action</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null
     * result will terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Resource Container
     *         Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseResourceContainerAction(final ResourceContainerAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Change Repository
     * Component Action</em>'. <!-- begin-user-doc --> This implementation returns null; returning a
     * non-null result will terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Change Repository
     *         Component Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseChangeRepositoryComponentAction(final ChangeRepositoryComponentAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Allocate Action</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Allocate Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAllocateAction(final AllocateAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Deallocate Action</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Deallocate Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDeallocateAction(final DeallocateAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Migrate Action</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Migrate Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMigrateAction(final MigrateAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Acquire Action</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Acquire Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAcquireAction(final AcquireAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Terminate Action</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Terminate Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTerminateAction(final TerminateAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Replicate Action</em>'.
     * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Replicate Action</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReplicateAction(final ReplicateAction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'. <!--
     * begin-user-doc --> This implementation returns null; returning a non-null result will
     * terminate the switch, but this is the last case anyway. <!-- end-user-doc -->
     * 
     * @param object
     *            the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(final EObject object) {
        return null;
    }

} // systemadaptationSwitch
