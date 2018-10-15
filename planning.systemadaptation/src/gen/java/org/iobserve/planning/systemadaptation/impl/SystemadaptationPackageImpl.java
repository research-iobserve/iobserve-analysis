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

import de.uka.ipd.sdq.identifier.IdentifierPackage;
import de.uka.ipd.sdq.probfunction.ProbfunctionPackage;
import de.uka.ipd.sdq.stoex.StoexPackage;
import de.uka.ipd.sdq.units.UnitsPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.ComposedAction;
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
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class SystemadaptationPackageImpl extends EPackageImpl implements SystemadaptationPackage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass systemAdaptationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass composedActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass assemblyContextActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass resourceContainerActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass changeRepositoryComponentActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass allocateActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass deallocateActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass migrateActionEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass replicateActionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dereplicateActionEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private SystemadaptationPackageImpl() {
        super(eNS_URI, SystemadaptationFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link SystemadaptationPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static SystemadaptationPackage init() {
        if (isInited) return (SystemadaptationPackage)EPackage.Registry.INSTANCE.getEPackage(SystemadaptationPackage.eNS_URI);

        // Obtain or create and register package
        SystemadaptationPackageImpl theSystemadaptationPackage = (SystemadaptationPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SystemadaptationPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SystemadaptationPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        IdentifierPackage.eINSTANCE.eClass();
        PcmPackage.eINSTANCE.eClass();
        ProbfunctionPackage.eINSTANCE.eClass();
        StoexPackage.eINSTANCE.eClass();
        UnitsPackage.eINSTANCE.eClass();

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
     * @generated
     */
    @Override
    public EClass getSystemAdaptation() {
        return systemAdaptationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSystemAdaptation_Actions() {
        return (EReference)systemAdaptationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getComposedAction() {
        return composedActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAssemblyContextAction() {
        return assemblyContextActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAssemblyContextAction_TargetAllocationContext() {
        return (EReference)assemblyContextActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAssemblyContextAction_TargetProvidingAllocationContexts() {
        return (EReference)assemblyContextActionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAssemblyContextAction_TargetRequiringAllocationContexts() {
        return (EReference)assemblyContextActionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getResourceContainerAction() {
        return resourceContainerActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getResourceContainerAction_TargetResourceContainer() {
        return (EReference)resourceContainerActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getResourceContainerAction_TargetLinkingResources() {
        return (EReference)resourceContainerActionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getChangeRepositoryComponentAction() {
        return changeRepositoryComponentActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getChangeRepositoryComponentAction_SourceAllocationContext() {
        return (EReference)changeRepositoryComponentActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAllocateAction() {
        return allocateActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDeallocateAction() {
        return deallocateActionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SystemadaptationFactory getSystemadaptationFactory() {
        return (SystemadaptationFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMigrateAction() {
        return migrateActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMigrateAction_SourceAllocationContext() {
        return (EReference)migrateActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMigrateAction_SourceProvidingAllocationContexts() {
        return (EReference)migrateActionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMigrateAction_SourceRequiringAllocationContexts() {
        return (EReference)migrateActionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getReplicateAction() {
        return replicateActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getReplicateAction_SourceAllocationContext() {
        return (EReference)replicateActionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDereplicateAction() {
        return dereplicateActionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        systemAdaptationEClass = createEClass(SYSTEM_ADAPTATION);
        createEReference(systemAdaptationEClass, SYSTEM_ADAPTATION__ACTIONS);

        composedActionEClass = createEClass(COMPOSED_ACTION);

        assemblyContextActionEClass = createEClass(ASSEMBLY_CONTEXT_ACTION);
        createEReference(assemblyContextActionEClass, ASSEMBLY_CONTEXT_ACTION__TARGET_ALLOCATION_CONTEXT);
        createEReference(assemblyContextActionEClass, ASSEMBLY_CONTEXT_ACTION__TARGET_PROVIDING_ALLOCATION_CONTEXTS);
        createEReference(assemblyContextActionEClass, ASSEMBLY_CONTEXT_ACTION__TARGET_REQUIRING_ALLOCATION_CONTEXTS);

        resourceContainerActionEClass = createEClass(RESOURCE_CONTAINER_ACTION);
        createEReference(resourceContainerActionEClass, RESOURCE_CONTAINER_ACTION__TARGET_RESOURCE_CONTAINER);
        createEReference(resourceContainerActionEClass, RESOURCE_CONTAINER_ACTION__TARGET_LINKING_RESOURCES);

        changeRepositoryComponentActionEClass = createEClass(CHANGE_REPOSITORY_COMPONENT_ACTION);
        createEReference(changeRepositoryComponentActionEClass, CHANGE_REPOSITORY_COMPONENT_ACTION__SOURCE_ALLOCATION_CONTEXT);

        replicateActionEClass = createEClass(REPLICATE_ACTION);
        createEReference(replicateActionEClass, REPLICATE_ACTION__SOURCE_ALLOCATION_CONTEXT);

        dereplicateActionEClass = createEClass(DEREPLICATE_ACTION);

        migrateActionEClass = createEClass(MIGRATE_ACTION);
        createEReference(migrateActionEClass, MIGRATE_ACTION__SOURCE_ALLOCATION_CONTEXT);
        createEReference(migrateActionEClass, MIGRATE_ACTION__SOURCE_PROVIDING_ALLOCATION_CONTEXTS);
        createEReference(migrateActionEClass, MIGRATE_ACTION__SOURCE_REQUIRING_ALLOCATION_CONTEXTS);

        allocateActionEClass = createEClass(ALLOCATE_ACTION);

        deallocateActionEClass = createEClass(DEALLOCATE_ACTION);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        AllocationPackage theAllocationPackage = (AllocationPackage)EPackage.Registry.INSTANCE.getEPackage(AllocationPackage.eNS_URI);
        ResourceenvironmentPackage theResourceenvironmentPackage = (ResourceenvironmentPackage)EPackage.Registry.INSTANCE.getEPackage(ResourceenvironmentPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        assemblyContextActionEClass.getESuperTypes().add(this.getComposedAction());
        resourceContainerActionEClass.getESuperTypes().add(this.getComposedAction());
        changeRepositoryComponentActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        replicateActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        dereplicateActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        migrateActionEClass.getESuperTypes().add(this.getAssemblyContextAction());
        allocateActionEClass.getESuperTypes().add(this.getResourceContainerAction());
        deallocateActionEClass.getESuperTypes().add(this.getResourceContainerAction());

        // Initialize classes and features; add operations and parameters
        initEClass(systemAdaptationEClass, SystemAdaptation.class, "SystemAdaptation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSystemAdaptation_Actions(), this.getComposedAction(), null, "actions", null, 0, -1, SystemAdaptation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(composedActionEClass, ComposedAction.class, "ComposedAction", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(assemblyContextActionEClass, AssemblyContextAction.class, "AssemblyContextAction", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAssemblyContextAction_TargetAllocationContext(), theAllocationPackage.getAllocationContext(), null, "targetAllocationContext", null, 1, 1, AssemblyContextAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAssemblyContextAction_TargetProvidingAllocationContexts(), theAllocationPackage.getAllocationContext(), null, "targetProvidingAllocationContexts", null, 0, -1, AssemblyContextAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getAssemblyContextAction_TargetRequiringAllocationContexts(), theAllocationPackage.getAllocationContext(), null, "targetRequiringAllocationContexts", null, 0, -1, AssemblyContextAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(resourceContainerActionEClass, ResourceContainerAction.class, "ResourceContainerAction", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getResourceContainerAction_TargetResourceContainer(), theResourceenvironmentPackage.getResourceContainer(), null, "targetResourceContainer", null, 1, 1, ResourceContainerAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getResourceContainerAction_TargetLinkingResources(), theResourceenvironmentPackage.getLinkingResource(), null, "targetLinkingResources", null, 0, -1, ResourceContainerAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(changeRepositoryComponentActionEClass, ChangeRepositoryComponentAction.class, "ChangeRepositoryComponentAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getChangeRepositoryComponentAction_SourceAllocationContext(), theAllocationPackage.getAllocationContext(), null, "sourceAllocationContext", null, 1, 1, ChangeRepositoryComponentAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(replicateActionEClass, ReplicateAction.class, "ReplicateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getReplicateAction_SourceAllocationContext(), theAllocationPackage.getAllocationContext(), null, "sourceAllocationContext", null, 1, 1, ReplicateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dereplicateActionEClass, DereplicateAction.class, "DereplicateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(migrateActionEClass, MigrateAction.class, "MigrateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMigrateAction_SourceAllocationContext(), theAllocationPackage.getAllocationContext(), null, "sourceAllocationContext", null, 1, 1, MigrateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMigrateAction_SourceProvidingAllocationContexts(), theAllocationPackage.getAllocationContext(), null, "sourceProvidingAllocationContexts", null, 0, -1, MigrateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMigrateAction_SourceRequiringAllocationContexts(), theAllocationPackage.getAllocationContext(), null, "sourceRequiringAllocationContexts", null, 0, -1, MigrateAction.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(allocateActionEClass, AllocateAction.class, "AllocateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(deallocateActionEClass, DeallocateAction.class, "DeallocateAction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);
    }

} // systemadaptationPackageImpl
