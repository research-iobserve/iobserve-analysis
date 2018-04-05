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
import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Assembly Context
 * Action</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.AssemblyContextAction#getTargetAllocationContext <em>Target Allocation Context</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.AssemblyContextAction#getTargetProvidingAllocationContexts <em>Target Providing Allocation Contexts</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.AssemblyContextAction#getTargetRequiringAllocationContexts <em>Target Requiring Allocation Contexts</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getAssemblyContextAction()
 * @model
 * @generated
 */
public interface AssemblyContextAction extends Action {
    /**
     * Returns the value of the '<em><b>Target Allocation Context</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Allocation Context</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Allocation Context</em>' reference.
     * @see #setTargetAllocationContext(AllocationContext)
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getAssemblyContextAction_TargetAllocationContext()
     * @model required="true"
     * @generated
     */
    AllocationContext getTargetAllocationContext();

    /**
     * Sets the value of the '{@link org.iobserve.planning.systemadaptation.AssemblyContextAction#getTargetAllocationContext <em>Target Allocation Context</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target Allocation Context</em>' reference.
     * @see #getTargetAllocationContext()
     * @generated
     */
    void setTargetAllocationContext(AllocationContext value);

    /**
     * Returns the value of the '<em><b>Target Providing Allocation Contexts</b></em>' reference list.
     * The list contents are of type {@link org.palladiosimulator.pcm.allocation.AllocationContext}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Providing Allocation Contexts</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Providing Allocation Contexts</em>' reference list.
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getAssemblyContextAction_TargetProvidingAllocationContexts()
     * @model
     * @generated
     */
    EList<AllocationContext> getTargetProvidingAllocationContexts();

    /**
     * Returns the value of the '<em><b>Target Requiring Allocation Contexts</b></em>' reference list.
     * The list contents are of type {@link org.palladiosimulator.pcm.allocation.AllocationContext}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Requiring Allocation Contexts</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Requiring Allocation Contexts</em>' reference list.
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getAssemblyContextAction_TargetRequiringAllocationContexts()
     * @model
     * @generated
     */
    EList<AllocationContext> getTargetRequiringAllocationContexts();

} // AssemblyContextAction
