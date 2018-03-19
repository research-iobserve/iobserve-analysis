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
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.ecore.EClass;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.SystemadaptationPackage;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Change Repository Component
 * Action</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl#getNewRepositoryComponent
 * <em>New Repository Component</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeRepositoryComponentActionImpl extends AssemblyContextActionImpl
        implements ChangeRepositoryComponentAction {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected ChangeRepositoryComponentActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SystemadaptationPackage.Literals.CHANGE_REPOSITORY_COMPONENT_ACTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public RepositoryComponent getNewRepositoryComponent() {
        return (RepositoryComponent) this.eGet(
                SystemadaptationPackage.Literals.CHANGE_REPOSITORY_COMPONENT_ACTION__NEW_REPOSITORY_COMPONENT, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setNewRepositoryComponent(final RepositoryComponent newNewRepositoryComponent) {
        this.eSet(SystemadaptationPackage.Literals.CHANGE_REPOSITORY_COMPONENT_ACTION__NEW_REPOSITORY_COMPONENT,
                newNewRepositoryComponent);
    }

} // ChangeRepositoryComponentActionImpl
