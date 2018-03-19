/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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

import org.palladiosimulator.pcm.repository.RepositoryComponent;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Change Repository Component
 * Action</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction#getNewRepositoryComponent
 * <em>New Repository Component</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getChangeRepositoryComponentAction()
 * @model
 * @generated
 */
public interface ChangeRepositoryComponentAction extends AssemblyContextAction {
    /**
     * Returns the value of the '<em><b>New Repository Component</b></em>' reference. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>New Repository Component</em>' reference isn't clear, there really
     * should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>New Repository Component</em>' reference.
     * @see #setNewRepositoryComponent(RepositoryComponent)
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getChangeRepositoryComponentAction_NewRepositoryComponent()
     * @model required="true" ordered="false"
     * @generated
     */
    RepositoryComponent getNewRepositoryComponent();

    /**
     * Sets the value of the
     * '{@link org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction#getNewRepositoryComponent
     * <em>New Repository Component</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>New Repository Component</em>' reference.
     * @see #getNewRepositoryComponent()
     * @generated
     */
    void setNewRepositoryComponent(RepositoryComponent value);

} // ChangeRepositoryComponentAction
