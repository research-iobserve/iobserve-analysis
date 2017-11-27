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
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.SystemAdaptation;
import org.iobserve.planning.systemadaptation.systemadaptationPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>System
 * Adaptation</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.systemadaptation.impl.SystemAdaptationImpl#getActions
 * <em>Actions</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SystemAdaptationImpl extends MinimalEObjectImpl.Container implements SystemAdaptation {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected SystemAdaptationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return systemadaptationPackage.Literals.SYSTEM_ADAPTATION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected int eStaticFeatureCount() {
        return 0;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    @SuppressWarnings("unchecked")
    public EList<Action> getActions() {
        return (EList<Action>) this.eGet(systemadaptationPackage.Literals.SYSTEM_ADAPTATION__ACTIONS, true);
    }

} // SystemAdaptationImpl
