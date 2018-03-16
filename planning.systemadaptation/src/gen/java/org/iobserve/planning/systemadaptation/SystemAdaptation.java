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
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>System Adaptation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.systemadaptation.SystemAdaptation#getActions
 * <em>Actions</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getSystemAdaptation()
 * @model
 * @generated
 */
public interface SystemAdaptation extends EObject {
    /**
     * Returns the value of the '<em><b>Actions</b></em>' containment reference list. The list
     * contents are of type {@link org.iobserve.planning.systemadaptation.Action}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Actions</em>' containment reference list isn't clear, there really
     * should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Actions</em>' containment reference list.
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#getSystemAdaptation_Actions()
     * @model containment="true"
     * @generated
     */
    EList<Action> getActions();

} // SystemAdaptation
