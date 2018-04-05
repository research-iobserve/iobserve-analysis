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

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Resource Container
 * Action</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.ResourceContainerAction#getTargetResourceContainer <em>Target Resource Container</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.ResourceContainerAction#getTargetLinkingResources <em>Target Linking Resources</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getResourceContainerAction()
 * @model
 * @generated
 */
public interface ResourceContainerAction extends Action {
    /**
     * Returns the value of the '<em><b>Target Resource Container</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Resource Container</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Resource Container</em>' reference.
     * @see #setTargetResourceContainer(ResourceContainer)
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getResourceContainerAction_TargetResourceContainer()
     * @model required="true"
     * @generated
     */
    ResourceContainer getTargetResourceContainer();

    /**
     * Sets the value of the '{@link org.iobserve.planning.systemadaptation.ResourceContainerAction#getTargetResourceContainer <em>Target Resource Container</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target Resource Container</em>' reference.
     * @see #getTargetResourceContainer()
     * @generated
     */
    void setTargetResourceContainer(ResourceContainer value);

    /**
     * Returns the value of the '<em><b>Target Linking Resources</b></em>' reference list.
     * The list contents are of type {@link org.palladiosimulator.pcm.resourceenvironment.LinkingResource}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Linking Resources</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Linking Resources</em>' reference list.
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getResourceContainerAction_TargetLinkingResources()
     * @model
     * @generated
     */
    EList<LinkingResource> getTargetLinkingResources();

} // ResourceContainerAction
