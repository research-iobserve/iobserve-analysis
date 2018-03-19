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
import org.iobserve.planning.systemadaptation.DereplicateAction;
import org.iobserve.planning.systemadaptation.SystemadaptationPackage;
import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Dereplicate
 * Action</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.systemadaptation.impl.DereplicateActionImpl#getOldAllocationContext
 * <em>Old Allocation Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DereplicateActionImpl extends AssemblyContextActionImpl implements DereplicateAction {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected DereplicateActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SystemadaptationPackage.Literals.DEREPLICATE_ACTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public AllocationContext getOldAllocationContext() {
        return (AllocationContext) this
                .eGet(SystemadaptationPackage.Literals.DEREPLICATE_ACTION__OLD_ALLOCATION_CONTEXT, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setOldAllocationContext(final AllocationContext newOldAllocationContext) {
        this.eSet(SystemadaptationPackage.Literals.DEREPLICATE_ACTION__OLD_ALLOCATION_CONTEXT, newOldAllocationContext);
    }

} // DereplicateActionImpl
