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
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.SystemadaptationPackage;
import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Migrate Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl#getNewAllocationContext
 * <em>New Allocation Context</em>}</li>
 * <li>{@link org.iobserve.planning.systemadaptation.impl.MigrateActionImpl#getSourceAllocationContext
 * <em>Source Allocation Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MigrateActionImpl extends AssemblyContextActionImpl implements MigrateAction {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected MigrateActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SystemadaptationPackage.Literals.MIGRATE_ACTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public AllocationContext getNewAllocationContext() {
        return (AllocationContext) this.eGet(SystemadaptationPackage.Literals.MIGRATE_ACTION__NEW_ALLOCATION_CONTEXT,
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setNewAllocationContext(final AllocationContext newNewAllocationContext) {
        this.eSet(SystemadaptationPackage.Literals.MIGRATE_ACTION__NEW_ALLOCATION_CONTEXT, newNewAllocationContext);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public AllocationContext getSourceAllocationContext() {
        return (AllocationContext) this.eGet(SystemadaptationPackage.Literals.MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT,
                true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setSourceAllocationContext(final AllocationContext newSourceAllocationContext) {
        this.eSet(SystemadaptationPackage.Literals.MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT,
                newSourceAllocationContext);
    }

} // MigrateActionImpl
