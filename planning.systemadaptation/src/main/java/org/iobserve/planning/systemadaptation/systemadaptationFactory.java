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
package org.iobserve.planning.systemadaptation;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each
 * non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage
 * @generated
 */
public interface systemadaptationFactory extends EFactory {
    /**
     * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    systemadaptationFactory eINSTANCE = org.iobserve.planning.systemadaptation.impl.systemadaptationFactoryImpl.init();

    /**
     * Returns a new object of class '<em>System Adaptation</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>System Adaptation</em>'.
     * @generated
     */
    SystemAdaptation createSystemAdaptation();

    /**
     * Returns a new object of class '<em>Action</em>'. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     * 
     * @return a new object of class '<em>Action</em>'.
     * @generated
     */
    Action createAction();

    /**
     * Returns a new object of class '<em>Assembly Context Action</em>'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return a new object of class '<em>Assembly Context Action</em>'.
     * @generated
     */
    AssemblyContextAction createAssemblyContextAction();

    /**
     * Returns a new object of class '<em>Resource Container Action</em>'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @return a new object of class '<em>Resource Container Action</em>'.
     * @generated
     */
    ResourceContainerAction createResourceContainerAction();

    /**
     * Returns a new object of class '<em>Change Repository Component Action</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return a new object of class '<em>Change Repository Component Action</em>'.
     * @generated
     */
    ChangeRepositoryComponentAction createChangeRepositoryComponentAction();

    /**
     * Returns a new object of class '<em>Allocate Action</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>Allocate Action</em>'.
     * @generated
     */
    AllocateAction createAllocateAction();

    /**
     * Returns a new object of class '<em>Deallocate Action</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>Deallocate Action</em>'.
     * @generated
     */
    DeallocateAction createDeallocateAction();

    /**
     * Returns a new object of class '<em>Migrate Action</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>Migrate Action</em>'.
     * @generated
     */
    MigrateAction createMigrateAction();

    /**
     * Returns a new object of class '<em>Acquire Action</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>Acquire Action</em>'.
     * @generated
     */
    AcquireAction createAcquireAction();

    /**
     * Returns a new object of class '<em>Terminate Action</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>Terminate Action</em>'.
     * @generated
     */
    TerminateAction createTerminateAction();

    /**
     * Returns a new object of class '<em>Replicate Action</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @return a new object of class '<em>Replicate Action</em>'.
     * @generated
     */
    ReplicateAction createReplicateAction();

    /**
     * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return the package supported by this factory.
     * @generated
     */
    systemadaptationPackage getsystemadaptationPackage();

} // systemadaptationFactory
