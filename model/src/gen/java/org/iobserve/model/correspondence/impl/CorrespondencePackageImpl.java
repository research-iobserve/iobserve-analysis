/**
 */
package org.iobserve.model.correspondence.impl;

import de.uka.ipd.sdq.identifier.IdentifierPackage;
import de.uka.ipd.sdq.probfunction.ProbfunctionPackage;
import de.uka.ipd.sdq.stoex.StoexPackage;
import de.uka.ipd.sdq.units.UnitsPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.iobserve.model.correspondence.AbstractEntry;
import org.iobserve.model.correspondence.AllocationEntry;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.correspondence.ComponentEntry;
import org.iobserve.model.correspondence.CorrespondenceFactory;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.DataTypeEntry;
import org.iobserve.model.correspondence.EServiceTechnology;
import org.iobserve.model.correspondence.OperationEntry;
import org.iobserve.model.correspondence.Part;
import org.palladiosimulator.pcm.PcmPackage;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.composition.CompositionPackage;
import org.palladiosimulator.pcm.repository.RepositoryPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CorrespondencePackageImpl extends EPackageImpl implements CorrespondencePackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass correspondenceModelEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dataTypeEntryEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass partEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass abstractEntryEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentEntryEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass allocationEntryEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass assemblyEntryEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operationEntryEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum eServiceTechnologyEEnum = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.iobserve.model.correspondence.CorrespondencePackage#eNS_URI
     * @see #init()
     * @generated
     */
    private CorrespondencePackageImpl() {
        super(eNS_URI, CorrespondenceFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link CorrespondencePackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static CorrespondencePackage init() {
        if (isInited) return (CorrespondencePackage)EPackage.Registry.INSTANCE.getEPackage(CorrespondencePackage.eNS_URI);

        // Obtain or create and register package
        CorrespondencePackageImpl theCorrespondencePackage = (CorrespondencePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof CorrespondencePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new CorrespondencePackageImpl());

        isInited = true;

        // Initialize simple dependencies
        IdentifierPackage.eINSTANCE.eClass();
        PcmPackage.eINSTANCE.eClass();
        ProbfunctionPackage.eINSTANCE.eClass();
        StoexPackage.eINSTANCE.eClass();
        UnitsPackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theCorrespondencePackage.createPackageContents();

        // Initialize created meta-data
        theCorrespondencePackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theCorrespondencePackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(CorrespondencePackage.eNS_URI, theCorrespondencePackage);
        return theCorrespondencePackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCorrespondenceModel() {
        return correspondenceModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getCorrespondenceModel_Parts() {
        return (EReference)correspondenceModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDataTypeEntry() {
        return dataTypeEntryEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDataTypeEntry_DataTypeEntry() {
        return (EReference)dataTypeEntryEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPart() {
        return partEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPart_ModelType() {
        return (EReference)partEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPart_Entries() {
        return (EReference)partEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAbstractEntry() {
        return abstractEntryEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAbstractEntry_ImplementationId() {
        return (EAttribute)abstractEntryEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getComponentEntry() {
        return componentEntryEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComponentEntry_Component() {
        return (EReference)componentEntryEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAllocationEntry() {
        return allocationEntryEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAllocationEntry_Allocation() {
        return (EReference)allocationEntryEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAllocationEntry_Technology() {
        return (EAttribute)allocationEntryEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAssemblyEntry() {
        return assemblyEntryEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getAssemblyEntry_Assembly() {
        return (EReference)assemblyEntryEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOperationEntry() {
        return operationEntryEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOperationEntry_Operation() {
        return (EReference)operationEntryEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getEServiceTechnology() {
        return eServiceTechnologyEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CorrespondenceFactory getCorrespondenceFactory() {
        return (CorrespondenceFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        correspondenceModelEClass = createEClass(CORRESPONDENCE_MODEL);
        createEReference(correspondenceModelEClass, CORRESPONDENCE_MODEL__PARTS);

        dataTypeEntryEClass = createEClass(DATA_TYPE_ENTRY);
        createEReference(dataTypeEntryEClass, DATA_TYPE_ENTRY__DATA_TYPE_ENTRY);

        partEClass = createEClass(PART);
        createEReference(partEClass, PART__MODEL_TYPE);
        createEReference(partEClass, PART__ENTRIES);

        abstractEntryEClass = createEClass(ABSTRACT_ENTRY);
        createEAttribute(abstractEntryEClass, ABSTRACT_ENTRY__IMPLEMENTATION_ID);

        componentEntryEClass = createEClass(COMPONENT_ENTRY);
        createEReference(componentEntryEClass, COMPONENT_ENTRY__COMPONENT);

        allocationEntryEClass = createEClass(ALLOCATION_ENTRY);
        createEReference(allocationEntryEClass, ALLOCATION_ENTRY__ALLOCATION);
        createEAttribute(allocationEntryEClass, ALLOCATION_ENTRY__TECHNOLOGY);

        assemblyEntryEClass = createEClass(ASSEMBLY_ENTRY);
        createEReference(assemblyEntryEClass, ASSEMBLY_ENTRY__ASSEMBLY);

        operationEntryEClass = createEClass(OPERATION_ENTRY);
        createEReference(operationEntryEClass, OPERATION_ENTRY__OPERATION);

        // Create enums
        eServiceTechnologyEEnum = createEEnum(ESERVICE_TECHNOLOGY);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
        RepositoryPackage theRepositoryPackage = (RepositoryPackage)EPackage.Registry.INSTANCE.getEPackage(RepositoryPackage.eNS_URI);
        AllocationPackage theAllocationPackage = (AllocationPackage)EPackage.Registry.INSTANCE.getEPackage(AllocationPackage.eNS_URI);
        CompositionPackage theCompositionPackage = (CompositionPackage)EPackage.Registry.INSTANCE.getEPackage(CompositionPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        dataTypeEntryEClass.getESuperTypes().add(this.getAbstractEntry());
        componentEntryEClass.getESuperTypes().add(this.getAbstractEntry());
        allocationEntryEClass.getESuperTypes().add(this.getAbstractEntry());
        assemblyEntryEClass.getESuperTypes().add(this.getAbstractEntry());
        operationEntryEClass.getESuperTypes().add(this.getAbstractEntry());

        // Initialize classes, features, and operations; add parameters
        initEClass(correspondenceModelEClass, CorrespondenceModel.class, "CorrespondenceModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCorrespondenceModel_Parts(), this.getPart(), null, "parts", null, 0, -1, CorrespondenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dataTypeEntryEClass, DataTypeEntry.class, "DataTypeEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDataTypeEntry_DataTypeEntry(), theRepositoryPackage.getDataType(), null, "dataTypeEntry", null, 1, 1, DataTypeEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(partEClass, Part.class, "Part", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPart_ModelType(), ecorePackage.getEObject(), null, "modelType", null, 1, 1, Part.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPart_Entries(), this.getAbstractEntry(), null, "entries", null, 0, -1, Part.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(abstractEntryEClass, AbstractEntry.class, "AbstractEntry", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAbstractEntry_ImplementationId(), ecorePackage.getEString(), "implementationId", null, 0, 1, AbstractEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(componentEntryEClass, ComponentEntry.class, "ComponentEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComponentEntry_Component(), theRepositoryPackage.getRepositoryComponent(), null, "component", null, 1, 1, ComponentEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(allocationEntryEClass, AllocationEntry.class, "AllocationEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAllocationEntry_Allocation(), theAllocationPackage.getAllocationContext(), null, "allocation", null, 1, 1, AllocationEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAllocationEntry_Technology(), this.getEServiceTechnology(), "technology", null, 1, 1, AllocationEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(assemblyEntryEClass, AssemblyEntry.class, "AssemblyEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAssemblyEntry_Assembly(), theCompositionPackage.getAssemblyContext(), null, "assembly", null, 1, 1, AssemblyEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(operationEntryEClass, OperationEntry.class, "OperationEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOperationEntry_Operation(), theRepositoryPackage.getOperationSignature(), null, "operation", null, 1, 1, OperationEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(eServiceTechnologyEEnum, EServiceTechnology.class, "EServiceTechnology");
        addEEnumLiteral(eServiceTechnologyEEnum, EServiceTechnology.SERVLET);
        addEEnumLiteral(eServiceTechnologyEEnum, EServiceTechnology.EJB);
        addEEnumLiteral(eServiceTechnologyEEnum, EServiceTechnology.ASPECT_J);

        // Create resource
        createResource(eNS_URI);
    }

} //CorrespondencePackageImpl
