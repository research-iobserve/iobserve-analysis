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

import org.eclipse.emf.ecore.EClass;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.systemadaptationPackage;
import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Deallocate
 * Action</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.systemadaptation.impl.DeallocateActionImpl#getOldAllocationContext
 * <em>Old Allocation Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeallocateActionImpl extends AssemblyContextActionImpl implements DeallocateAction {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected DeallocateActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return systemadaptationPackage.Literals.DEALLOCATE_ACTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public AllocationContext getOldAllocationContext() {
        return (AllocationContext) this.eGet(systemadaptationPackage.Literals.DEALLOCATE_ACTION__OLD_ALLOCATION_CONTEXT,
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setOldAllocationContext(final AllocationContext newOldAllocationContext) {
        this.eSet(systemadaptationPackage.Literals.DEALLOCATE_ACTION__OLD_ALLOCATION_CONTEXT, newOldAllocationContext);
    }

} // DeallocateActionImpl
