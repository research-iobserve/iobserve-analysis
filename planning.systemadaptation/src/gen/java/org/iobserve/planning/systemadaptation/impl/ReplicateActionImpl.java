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
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.SystemadaptationPackage;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Replicate Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl#getNewResourceContainer
 * <em>New Resource Container</em>}</li>
 * <li>{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl#getSourceAllocationContext
 * <em>Source Allocation Context</em>}</li>
 * <li>{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl#getNewAllocationContext
 * <em>New Allocation Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReplicateActionImpl extends ResourceContainerActionImpl implements ReplicateAction {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected ReplicateActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SystemadaptationPackage.Literals.REPLICATE_ACTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ResourceContainer getNewResourceContainer() {
        return (ResourceContainer) this.eGet(SystemadaptationPackage.Literals.REPLICATE_ACTION__NEW_RESOURCE_CONTAINER,
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setNewResourceContainer(final ResourceContainer newNewResourceContainer) {
        this.eSet(SystemadaptationPackage.Literals.REPLICATE_ACTION__NEW_RESOURCE_CONTAINER, newNewResourceContainer);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    @SuppressWarnings("unchecked")
    public EList<AllocationContext> getSourceAllocationContext() {
        return (EList<AllocationContext>) this
                .eGet(SystemadaptationPackage.Literals.REPLICATE_ACTION__SOURCE_ALLOCATION_CONTEXT, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    @SuppressWarnings("unchecked")
    public EList<AllocationContext> getNewAllocationContext() {
        return (EList<AllocationContext>) this
                .eGet(SystemadaptationPackage.Literals.REPLICATE_ACTION__NEW_ALLOCATION_CONTEXT, true);
    }

} // ReplicateActionImpl
