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

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Resource Container
 * Action</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.systemadaptation.ResourceContainerAction#getSourceResourceContainer
 * <em>Source Resource Container</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getResourceContainerAction()
 * @model
 * @generated
 */
public interface ResourceContainerAction extends Action {
    /**
     * Returns the value of the '<em><b>Source Resource Container</b></em>' reference. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source Resource Container</em>' reference isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Source Resource Container</em>' reference.
     * @see #setSourceResourceContainer(ResourceContainer)
     * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getResourceContainerAction_SourceResourceContainer()
     * @model
     * @generated
     */
    ResourceContainer getSourceResourceContainer();

    /**
     * Sets the value of the
     * '{@link org.iobserve.planning.systemadaptation.ResourceContainerAction#getSourceResourceContainer
     * <em>Source Resource Container</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Source Resource Container</em>' reference.
     * @see #getSourceResourceContainer()
     * @generated
     */
    void setSourceResourceContainer(ResourceContainer value);

} // ResourceContainerAction
