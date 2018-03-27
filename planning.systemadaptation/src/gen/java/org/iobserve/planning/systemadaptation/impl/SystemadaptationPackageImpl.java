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
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.DereplicateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.SystemAdaptation;
import org.iobserve.planning.systemadaptation.SystemadaptationFactory;
import org.iobserve.planning.systemadaptation.SystemadaptationPackage;
import org.palladiosimulator.pcm.PcmPackage;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.composition.CompositionPackage;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

import de.uka.ipd.sdq.identifier.IdentifierPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 *
 * @generated
 */
public class SystemadaptationPackageImpl extends EPackageImpl implements SystemadaptationPackage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass systemAdaptationEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass actionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass assemblyContextActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass resourceContainerActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass changeRepositoryComponentActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass replicateActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass dereplicateActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass migrateActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass allocateActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass deallocateActionEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI
     * value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init
     * init()}, which also performs initialization of the package, or returns the registered
     * package, if one already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private SystemadaptationPackageImpl() {
        super(SystemadaptationPackage.eNS_URI, SystemadaptationFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others
     * upon which it depends.
     *
     * <p>
     * This method is used to initialize {@link SystemadaptationPackage#eINSTANCE} when that field
     * is accessed. Clients should not invoke it directly. Instead, they should simply access that
     * field to obtain the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static SystemadaptationPackage init() {
        if (SystemadaptationPackageImpl.isInited) {
            return (SystemadaptationPackage) EPackage.Registry.INSTANCE.getEPackage(SystemadaptationPackage.eNS_URI);
        }

        // Obtain or create and register package
        final SystemadaptationPackageImpl theSystemadaptationPackage = (SystemadaptationPackageImpl) (EPackage.Registry.INSTANCE
                .get(SystemadaptationPackage.eNS_URI) instanceof SystemadaptationPackageImpl
                        ? EPackage.Registry.INSTANCE.get(SystemadaptationPackage.eNS_URI)
                        : new SystemadaptationPackageImpl());

        SystemadaptationPackageImpl.isInited = true;

        // Initialize simple dependencies
        IdentifierPackage.eINSTANCE.eClass();
        PcmPackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theSystemadaptationPackage.createPackageContents();

        // Initialize created meta-data
        theSystemadaptationPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theSystemadaptationPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(SystemadaptationPackage.eNS_URI, theSystemadaptationPackage);
        return theSystemadaptationPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSystemAdaptation() {
        return this.systemAdaptationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSystemAdaptation_Actions() {
        return (EReference) this.systemAdaptationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAction() {
        return this.actionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAction_ResourceContainer() {
        return (EReference) this.actionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAssemblyContextAction() {
        return this.assemblyContextActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAssemblyContextAction_SourceAssemblyContext() {
        return (EReference) this.assemblyContextActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getResourceContainerAction() {
        return this.resourceContainerActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getResourceContainerAction_SourceResourceContainer() {
        return (EReference) this.resourceContainerActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getChangeRepositoryComponentAction() {
        return this.changeRepositoryComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getChangeRepositoryComponentAction_NewRepositoryComponent() {
        return (EReference) this.changeRepositoryComponentActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getReplicateAction() {
        return this.replicateActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getReplicateAction_NewAllocationContext() {
        return (EReference) this.replicateActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDereplicateAction() {
        return this.dereplicateActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDereplicateAction_OldAllocationContext() {
        return (EReference) this.dereplicateActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMigrateAction() {
        return this.migrateActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getMigrateAction_NewAllocationContext() {
        return (EReference) this.migrateActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getMigrateAction_SourceAllocationContext() {
        return (EReference) this.migrateActionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAllocateAction() {
        return this.allocateActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDeallocateAction() {
        return this.deallocateActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SystemadaptationFactory getSystemadaptationFactory() {
        return (SystemadaptationFactory) this.getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package. This method is guarded to have no affect on
     * any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public void createPackageContents() {
        if (this.isCreated) {
            return;
        }
        this.isCreated = true;

        // Create classes and their features
        this.systemAdaptationEClass = this.createEClass(SystemadaptationPackage.SYSTEM_ADAPTATION);
        this.createEReference(this.systemAdaptationEClass, SystemadaptationPackage.SYSTEM_ADAPTATION__ACTIONS);

        this.actionEClass = this.createEClass(SystemadaptationPackage.ACTION);
        this.createEReference(this.actionEClass, SystemadaptationPackage.ACTION__RESOURCE_CONTAINER);

        this.assemblyContextActionEClass = this.createEClass(SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION);
        this.createEReference(this.assemblyContextActionEClass,
                SystemadaptationPackage.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT);

        this.resourceContainerActionEClass = this.createEClass(SystemadaptationPackage.RESOURCE_CONTAINER_ACTION);
        this.createEReference(this.resourceContainerActionEClass,
                SystemadaptationPackage.RESOURCE_CONTAINER_ACTION__SOURCE_RESOURCE_CONTAINER);

        this.changeRepositoryComponentActionEClass = this
                .createEClass(SystemadaptationPackage.CHANGE_REPOSITORY_COMPONENT_ACTION);
        this.createEReference(this.changeRepositoryComponentActionEClass,
                SystemadaptationPackage.CHANGE_REPOSITORY_COMPONENT_ACTION__NEW_REPOSITORY_COMPONENT);

        this.replicateActionEClass = this.createEClass(SystemadaptationPackage.REPLICATE_ACTION);
        this.createEReference(this.replicateActionEClass,
                SystemadaptationPackage.REPLICATE_ACTION__NEW_ALLOCATION_CONTEXT);

        this.dereplicateActionEClass = this.createEClass(SystemadaptationPackage.DEREPLICATE_ACTION);
        this.createEReference(this.dereplicateActionEClass,
                SystemadaptationPackage.DEREPLICATE_ACTION__OLD_ALLOCATION_CONTEXT);

        this.migrateActionEClass = this.createEClass(SystemadaptationPackage.MIGRATE_ACTION);
        this.createEReference(this.migrateActionEClass, SystemadaptationPackage.MIGRATE_ACTION__NEW_ALLOCATION_CONTEXT);
        this.createEReference(this.migrateActionEClass,
                SystemadaptationPackage.MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT);

        this.allocateActionEClass = this.createEClass(SystemadaptationPackage.ALLOCATE_ACTION);

        this.deallocateActionEClass = this.createEClass(SystemadaptationPackage.DEALLOCATE_ACTION);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model. This method is guarded to have
     * no affect on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public void initializePackageContents() {
        if (this.isInitialized) {
            return;
        }
        this.isInitialized = true;

        // Initialize package
        this.setName(SystemadaptationPackage.eNAME);
        this.setNsPrefix(SystemadaptationPackage.eNS_PREFIX);
        this.setNsURI(SystemadaptationPackage.eNS_URI);

        // Obtain other dependent packages
        final ResourceenvironmentPackage theResourceenvironmentPackage = (ResourceenvironmentPackage) EPackage.Registry.INSTANCE
                .getEPackage(ResourceenvironmentPackage.eNS_URI);
        final CompositionPackage theCompositionPackage = (CompositionPackage) EPackage.Registry.INSTANCE
                .getEPackage(CompositionPackage.eNS_URI);
        final RepositoryPackage theRepositoryPackage = (RepositoryPackage) EPackage.Registry.INSTANCE
                .getEPackage(RepositoryPackage.eNS_URI);
        final AllocationPackage theAllocationPackage = (AllocationPackage) EPackage.Registry.INSTANCE
                .getEPackage(AllocationPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        this.assemblyContextActionEClass.getESuperTypes().add(this.getAction());
        this.resourceContainerActionEClass.getESuperTypes().add(this.getAction());
        this.changeRepositoryComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        this.replicateActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        this.dereplicateActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        this.migrateActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        this.allocateActionEClass.getESuperTypes().add(this.getResourceContainerAction());
        this.deallocateActionEClass.getESuperTypes().add(this.getResourceContainerAction());

        // Initialize classes and features; add operations and parameters
        this.initEClass(this.systemAdaptationEClass, SystemAdaptation.class, "SystemAdaptation",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getSystemAdaptation_Actions(), this.getAction(), null, "actions", null, 0, -1,
                SystemAdaptation.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, EPackageImpl.IS_COMPOSITE, !EPackageImpl.IS_RESOLVE_PROXIES,
                !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.actionEClass, Action.class, "Action", !EPackageImpl.IS_ABSTRACT,
                !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAction_ResourceContainer(), theResourceenvironmentPackage.getResourceContainer(),
                null, "resourceContainer", null, 1, 1, Action.class, !EPackageImpl.IS_TRANSIENT,
                !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE,
                EPackageImpl.IS_RESOLVE_PROXIES, !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE,
                !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.assemblyContextActionEClass, AssemblyContextAction.class, "AssemblyContextAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getAssemblyContextAction_SourceAssemblyContext(),
                theCompositionPackage.getAssemblyContext(), null, "sourceAssemblyContext", null, 0, 1,
                AssemblyContextAction.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE, EPackageImpl.IS_RESOLVE_PROXIES,
                !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.resourceContainerActionEClass, ResourceContainerAction.class, "ResourceContainerAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getResourceContainerAction_SourceResourceContainer(),
                theResourceenvironmentPackage.getResourceContainer(), null, "sourceResourceContainer", null, 0, 1,
                ResourceContainerAction.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE, EPackageImpl.IS_RESOLVE_PROXIES,
                !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.changeRepositoryComponentActionEClass, ChangeRepositoryComponentAction.class,
                "ChangeRepositoryComponentAction", !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE,
                EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getChangeRepositoryComponentAction_NewRepositoryComponent(),
                theRepositoryPackage.getRepositoryComponent(), null, "newRepositoryComponent", null, 1, 1,
                ChangeRepositoryComponentAction.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE, EPackageImpl.IS_RESOLVE_PROXIES,
                !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED,
                !EPackageImpl.IS_ORDERED);

        this.initEClass(this.replicateActionEClass, ReplicateAction.class, "ReplicateAction", !EPackageImpl.IS_ABSTRACT,
                !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getReplicateAction_NewAllocationContext(), theAllocationPackage.getAllocationContext(),
                null, "newAllocationContext", null, 1, 1, ReplicateAction.class, !EPackageImpl.IS_TRANSIENT,
                !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE,
                EPackageImpl.IS_RESOLVE_PROXIES, !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE,
                !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.dereplicateActionEClass, DereplicateAction.class, "DereplicateAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getDereplicateAction_OldAllocationContext(),
                theAllocationPackage.getAllocationContext(), null, "oldAllocationContext", null, 0, 1,
                DereplicateAction.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE, EPackageImpl.IS_RESOLVE_PROXIES,
                !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.migrateActionEClass, MigrateAction.class, "MigrateAction", !EPackageImpl.IS_ABSTRACT,
                !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getMigrateAction_NewAllocationContext(), theAllocationPackage.getAllocationContext(),
                null, "newAllocationContext", null, 1, 1, MigrateAction.class, !EPackageImpl.IS_TRANSIENT,
                !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE,
                EPackageImpl.IS_RESOLVE_PROXIES, !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE,
                !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);
        this.initEReference(this.getMigrateAction_SourceAllocationContext(),
                theAllocationPackage.getAllocationContext(), null, "sourceAllocationContext", null, 1, 1,
                MigrateAction.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE,
                !EPackageImpl.IS_COMPOSITE, EPackageImpl.IS_RESOLVE_PROXIES, !EPackageImpl.IS_UNSETTABLE,
                EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.allocateActionEClass, AllocateAction.class, "AllocateAction", !EPackageImpl.IS_ABSTRACT,
                !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.deallocateActionEClass, DeallocateAction.class, "DeallocateAction",
                !EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        this.createResource(SystemadaptationPackage.eNS_URI);
    }

} // SystemadaptationPackageImpl
