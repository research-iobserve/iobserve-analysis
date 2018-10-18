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

import org.iobserve.planning.systemadaptation.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SystemadaptationFactoryImpl extends EFactoryImpl implements SystemadaptationFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static SystemadaptationFactory init() {
        try {
            SystemadaptationFactory theSystemadaptationFactory = (SystemadaptationFactory)EPackage.Registry.INSTANCE.getEFactory(SystemadaptationPackage.eNS_URI);
            if (theSystemadaptationFactory != null) {
                return theSystemadaptationFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new SystemadaptationFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SystemadaptationFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case SystemadaptationPackage.SYSTEM_ADAPTATION: return createSystemAdaptation();
            case SystemadaptationPackage.CHANGE_REPOSITORY_COMPONENT_ACTION: return createChangeRepositoryComponentAction();
            case SystemadaptationPackage.REPLICATE_ACTION: return createReplicateAction();
            case SystemadaptationPackage.DEREPLICATE_ACTION: return createDereplicateAction();
            case SystemadaptationPackage.MIGRATE_ACTION: return createMigrateAction();
            case SystemadaptationPackage.ALLOCATE_ACTION: return createAllocateAction();
            case SystemadaptationPackage.DEALLOCATE_ACTION: return createDeallocateAction();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SystemAdaptation createSystemAdaptation() {
        SystemAdaptationImpl systemAdaptation = new SystemAdaptationImpl();
        return systemAdaptation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ChangeRepositoryComponentAction createChangeRepositoryComponentAction() {
        ChangeRepositoryComponentActionImpl changeRepositoryComponentAction = new ChangeRepositoryComponentActionImpl();
        return changeRepositoryComponentAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReplicateAction createReplicateAction() {
        ReplicateActionImpl replicateAction = new ReplicateActionImpl();
        return replicateAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DereplicateAction createDereplicateAction() {
        DereplicateActionImpl dereplicateAction = new DereplicateActionImpl();
        return dereplicateAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MigrateAction createMigrateAction() {
        MigrateActionImpl migrateAction = new MigrateActionImpl();
        return migrateAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllocateAction createAllocateAction() {
        AllocateActionImpl allocateAction = new AllocateActionImpl();
        return allocateAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DeallocateAction createDeallocateAction() {
        DeallocateActionImpl deallocateAction = new DeallocateActionImpl();
        return deallocateAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SystemadaptationPackage getSystemadaptationPackage() {
        return (SystemadaptationPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static SystemadaptationPackage getPackage() {
        return SystemadaptationPackage.eINSTANCE;
    }

} //SystemadaptationFactoryImpl
