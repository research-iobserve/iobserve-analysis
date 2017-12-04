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

import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Allocate Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.systemadaptation.AllocateAction#getNewAllocationContext <em>New
 * Allocation Context</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getAllocateAction()
 * @model
 * @generated
 */
public interface AllocateAction extends AssemblyContextAction {
    /**
     * Returns the value of the '<em><b>New Allocation Context</b></em>' reference. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>New Allocation Context</em>' reference isn't clear, there really
     * should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>New Allocation Context</em>' reference.
     * @see #setNewAllocationContext(AllocationContext)
     * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getAllocateAction_NewAllocationContext()
     * @model required="true"
     * @generated
     */
    AllocationContext getNewAllocationContext();

    /**
     * Sets the value of the
     * '{@link org.iobserve.planning.systemadaptation.AllocateAction#getNewAllocationContext <em>New
     * Allocation Context</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>New Allocation Context</em>' reference.
     * @see #getNewAllocationContext()
     * @generated
     */
    void setNewAllocationContext(AllocationContext value);

} // AllocateAction
