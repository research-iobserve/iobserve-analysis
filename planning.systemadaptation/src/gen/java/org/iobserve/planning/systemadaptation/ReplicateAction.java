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

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Replicate Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.systemadaptation.ReplicateAction#getNewResourceContainer <em>New
 * Resource Container</em>}</li>
 * <li>{@link org.iobserve.planning.systemadaptation.ReplicateAction#getSourceAllocationContext
 * <em>Source Allocation Context</em>}</li>
 * <li>{@link org.iobserve.planning.systemadaptation.ReplicateAction#getNewAllocationContext <em>New
 * Allocation Context</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getReplicateAction()
 * @model
 * @generated
 */
public interface ReplicateAction extends ResourceContainerAction {
    /**
     * Returns the value of the '<em><b>New Resource Container</b></em>' reference. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>New Resource Container</em>' reference isn't clear, there really
     * should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>New Resource Container</em>' reference.
     * @see #setNewResourceContainer(ResourceContainer)
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getReplicateAction_NewResourceContainer()
     * @model required="true"
     * @generated
     */
    ResourceContainer getNewResourceContainer();

    /**
     * Sets the value of the
     * '{@link org.iobserve.planning.systemadaptation.ReplicateAction#getNewResourceContainer
     * <em>New Resource Container</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>New Resource Container</em>' reference.
     * @see #getNewResourceContainer()
     * @generated
     */
    void setNewResourceContainer(ResourceContainer value);

    /**
     * Returns the value of the '<em><b>Source Allocation Context</b></em>' reference list. The list
     * contents are of type {@link org.palladiosimulator.pcm.allocation.AllocationContext}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source Allocation Context</em>' reference list isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Source Allocation Context</em>' reference list.
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getReplicateAction_SourceAllocationContext()
     * @model required="true"
     * @generated
     */
    EList<AllocationContext> getSourceAllocationContext();

    /**
     * Returns the value of the '<em><b>New Allocation Context</b></em>' reference list. The list
     * contents are of type {@link org.palladiosimulator.pcm.allocation.AllocationContext}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>New Allocation Context</em>' reference list isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>New Allocation Context</em>' reference list.
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getReplicateAction_NewAllocationContext()
     * @model required="true"
     * @generated
     */
    EList<AllocationContext> getNewAllocationContext();

} // ReplicateAction
