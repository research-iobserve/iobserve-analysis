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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.SystemAdaptation;
import org.iobserve.planning.systemadaptation.SystemadaptationFactory;
import org.iobserve.planning.systemadaptation.SystemadaptationPackage;
import org.iobserve.planning.systemadaptation.TerminateAction;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class SystemadaptationFactoryImpl extends EFactoryImpl implements SystemadaptationFactory {
    /**
     * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public static SystemadaptationFactory init() {
        try {
            final SystemadaptationFactory thesystemadaptationFactory = (SystemadaptationFactory) EPackage.Registry.INSTANCE
                    .getEFactory(SystemadaptationPackage.eNS_URI);
            if (thesystemadaptationFactory != null) {
                return thesystemadaptationFactory;
            }
        } catch (final Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new SystemadaptationFactoryImpl();
    }

    /**
     * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public SystemadaptationFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EObject create(final EClass eClass) {
        switch (eClass.getClassifierID()) {
        case SystemadaptationPackage.SYSTEM_ADAPTATION:
            return this.createSystemAdaptation();
        case SystemadaptationPackage.ACTION:
            return this.createAction();
        case SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION:
            return this.createAssemblyContextAction();
        case SystemadaptationPackage.RESOURCE_CONTAINER_ACTION:
            return this.createResourceContainerAction();
        case SystemadaptationPackage.CHANGE_REPOSITORY_COMPONENT_ACTION:
            return this.createChangeRepositoryComponentAction();
        case SystemadaptationPackage.ALLOCATE_ACTION:
            return this.createAllocateAction();
        case SystemadaptationPackage.DEALLOCATE_ACTION:
            return this.createDeallocateAction();
        case SystemadaptationPackage.MIGRATE_ACTION:
            return this.createMigrateAction();
        case SystemadaptationPackage.ACQUIRE_ACTION:
            return this.createAcquireAction();
        case SystemadaptationPackage.TERMINATE_ACTION:
            return this.createTerminateAction();
        case SystemadaptationPackage.REPLICATE_ACTION:
            return this.createReplicateAction();
        default:
            throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public SystemAdaptation createSystemAdaptation() {
        final SystemAdaptationImpl systemAdaptation = new SystemAdaptationImpl();
        return systemAdaptation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Action createAction() {
        final ActionImpl action = new ActionImpl();
        return action;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public AssemblyContextAction createAssemblyContextAction() {
        final AssemblyContextActionImpl assemblyContextAction = new AssemblyContextActionImpl();
        return assemblyContextAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ResourceContainerAction createResourceContainerAction() {
        final ResourceContainerActionImpl resourceContainerAction = new ResourceContainerActionImpl();
        return resourceContainerAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ChangeRepositoryComponentAction createChangeRepositoryComponentAction() {
        final ChangeRepositoryComponentActionImpl changeRepositoryComponentAction = new ChangeRepositoryComponentActionImpl();
        return changeRepositoryComponentAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public AllocateAction createAllocateAction() {
        final AllocateActionImpl allocateAction = new AllocateActionImpl();
        return allocateAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public DeallocateAction createDeallocateAction() {
        final DeallocateActionImpl deallocateAction = new DeallocateActionImpl();
        return deallocateAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public MigrateAction createMigrateAction() {
        final MigrateActionImpl migrateAction = new MigrateActionImpl();
        return migrateAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public AcquireAction createAcquireAction() {
        final AcquireActionImpl acquireAction = new AcquireActionImpl();
        return acquireAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public TerminateAction createTerminateAction() {
        final TerminateActionImpl terminateAction = new TerminateActionImpl();
        return terminateAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ReplicateAction createReplicateAction() {
        final ReplicateActionImpl replicateAction = new ReplicateActionImpl();
        return replicateAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public SystemadaptationPackage getsystemadaptationPackage() {
        return (SystemadaptationPackage) this.getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @deprecated
     * @generated
     */
    @Deprecated
    public static SystemadaptationPackage getPackage() {
        return SystemadaptationPackage.eINSTANCE;
    }

} // systemadaptationFactoryImpl
