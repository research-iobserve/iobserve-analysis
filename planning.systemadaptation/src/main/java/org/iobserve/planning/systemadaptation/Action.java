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

import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Action</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.systemadaptation.Action#getResourceContainer <em>Resource
 * Container</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getAction()
 * @model
 * @generated
 */
public interface Action extends EObject {
    /**
     * Returns the value of the '<em><b>Resource Container</b></em>' reference. <!-- begin-user-doc
     * -->
     * <p>
     * If the meaning of the '<em>Resource Container</em>' reference isn't clear, there really
     * should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Resource Container</em>' reference.
     * @see #setResourceContainer(ResourceContainer)
     * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getAction_ResourceContainer()
     * @model required="true"
     * @generated
     */
    ResourceContainer getResourceContainer();

    /**
     * Sets the value of the
     * '{@link org.iobserve.planning.systemadaptation.Action#getResourceContainer <em>Resource
     * Container</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Resource Container</em>' reference.
     * @see #getResourceContainer()
     * @generated
     */
    void setResourceContainer(ResourceContainer value);

} // Action
