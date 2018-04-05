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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.SystemadaptationPackage;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Resource Container
 * Action</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl#getTargetResourceContainer <em>Target Resource Container</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.ResourceContainerActionImpl#getTargetLinkingResources <em>Target Linking Resources</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ResourceContainerActionImpl extends ActionImpl implements ResourceContainerAction {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected ResourceContainerActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SystemadaptationPackage.Literals.RESOURCE_CONTAINER_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResourceContainer getTargetResourceContainer() {
        return (ResourceContainer)eGet(SystemadaptationPackage.Literals.RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTargetResourceContainer(ResourceContainer newTargetResourceContainer) {
        eSet(SystemadaptationPackage.Literals.RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER, newTargetResourceContainer);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<LinkingResource> getTargetLinkingResources() {
        return (EList<LinkingResource>)eGet(SystemadaptationPackage.Literals.RESOURCE_CONTAINER_ACTION__TARGET_LINKING_RESOURCES, true);
    }

} // ResourceContainerActionImpl
