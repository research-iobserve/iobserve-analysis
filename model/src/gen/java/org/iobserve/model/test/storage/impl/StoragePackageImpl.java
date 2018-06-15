/**
 */
package org.iobserve.model.test.storage.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.iobserve.model.test.storage.EnumValueExample;
import org.iobserve.model.test.storage.Other;
import org.iobserve.model.test.storage.OtherInterface;
import org.iobserve.model.test.storage.OtherSubType;
import org.iobserve.model.test.storage.Root;
import org.iobserve.model.test.storage.SpecialA;
import org.iobserve.model.test.storage.SpecialB;
import org.iobserve.model.test.storage.StorageFactory;
import org.iobserve.model.test.storage.StoragePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class StoragePackageImpl extends EPackageImpl implements StoragePackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass rootEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass otherEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass otherSubTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass otherInterfaceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass specialAEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass specialBEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum enumValueExampleEEnum = null;

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
     * @see org.iobserve.model.test.storage.StoragePackage#eNS_URI
     * @see #init()
     * @generated
     */
    private StoragePackageImpl() {
        super(eNS_URI, StorageFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link StoragePackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static StoragePackage init() {
        if (isInited) return (StoragePackage)EPackage.Registry.INSTANCE.getEPackage(StoragePackage.eNS_URI);

        // Obtain or create and register package
        StoragePackageImpl theStoragePackage = (StoragePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof StoragePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new StoragePackageImpl());

        isInited = true;

        // Create package meta-data objects
        theStoragePackage.createPackageContents();

        // Initialize created meta-data
        theStoragePackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theStoragePackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(StoragePackage.eNS_URI, theStoragePackage);
        return theStoragePackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getRoot() {
        return rootEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRoot_Name() {
        return (EAttribute)rootEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRoot_Labels() {
        return (EAttribute)rootEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRoot_Fixed() {
        return (EAttribute)rootEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getRoot_Enumerate() {
        return (EAttribute)rootEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRoot_Others() {
        return (EReference)rootEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getRoot_IfaceOthers() {
        return (EReference)rootEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOther() {
        return otherEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOther_Name() {
        return (EAttribute)otherEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOtherSubType() {
        return otherSubTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getOtherSubType_Label() {
        return (EAttribute)otherSubTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOtherSubType_Other() {
        return (EReference)otherSubTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOtherInterface() {
        return otherInterfaceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getOtherInterface_Next() {
        return (EReference)otherInterfaceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSpecialA() {
        return specialAEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSpecialA_Relate() {
        return (EReference)specialAEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSpecialB() {
        return specialBEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getEnumValueExample() {
        return enumValueExampleEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StorageFactory getStorageFactory() {
        return (StorageFactory)getEFactoryInstance();
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
        rootEClass = createEClass(ROOT);
        createEAttribute(rootEClass, ROOT__NAME);
        createEAttribute(rootEClass, ROOT__LABELS);
        createEAttribute(rootEClass, ROOT__FIXED);
        createEAttribute(rootEClass, ROOT__ENUMERATE);
        createEReference(rootEClass, ROOT__OTHERS);
        createEReference(rootEClass, ROOT__IFACE_OTHERS);

        otherEClass = createEClass(OTHER);
        createEAttribute(otherEClass, OTHER__NAME);

        otherSubTypeEClass = createEClass(OTHER_SUB_TYPE);
        createEAttribute(otherSubTypeEClass, OTHER_SUB_TYPE__LABEL);
        createEReference(otherSubTypeEClass, OTHER_SUB_TYPE__OTHER);

        otherInterfaceEClass = createEClass(OTHER_INTERFACE);
        createEReference(otherInterfaceEClass, OTHER_INTERFACE__NEXT);

        specialAEClass = createEClass(SPECIAL_A);
        createEReference(specialAEClass, SPECIAL_A__RELATE);

        specialBEClass = createEClass(SPECIAL_B);

        // Create enums
        enumValueExampleEEnum = createEEnum(ENUM_VALUE_EXAMPLE);
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

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        otherSubTypeEClass.getESuperTypes().add(this.getOther());
        specialAEClass.getESuperTypes().add(this.getOtherInterface());
        specialBEClass.getESuperTypes().add(this.getOtherInterface());

        // Initialize classes, features, and operations; add parameters
        initEClass(rootEClass, Root.class, "Root", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRoot_Name(), ecorePackage.getEString(), "name", null, 0, 1, Root.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRoot_Labels(), ecorePackage.getEString(), "labels", null, 0, -1, Root.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRoot_Fixed(), ecorePackage.getEString(), "fixed", null, 1, 1, Root.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getRoot_Enumerate(), this.getEnumValueExample(), "enumerate", null, 0, 1, Root.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRoot_Others(), this.getOther(), null, "others", null, 0, -1, Root.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRoot_IfaceOthers(), this.getOtherInterface(), null, "ifaceOthers", null, 0, -1, Root.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(otherEClass, Other.class, "Other", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getOther_Name(), ecorePackage.getEString(), "name", null, 0, 1, Other.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(otherSubTypeEClass, OtherSubType.class, "OtherSubType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getOtherSubType_Label(), ecorePackage.getEBoolean(), "label", null, 0, 1, OtherSubType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getOtherSubType_Other(), this.getOther(), null, "other", null, 0, 1, OtherSubType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(otherInterfaceEClass, OtherInterface.class, "OtherInterface", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getOtherInterface_Next(), this.getOtherInterface(), null, "next", null, 0, 1, OtherInterface.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(specialAEClass, SpecialA.class, "SpecialA", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSpecialA_Relate(), this.getOther(), null, "relate", null, 0, 1, SpecialA.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(specialBEClass, SpecialB.class, "SpecialB", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        // Initialize enums and add enum literals
        initEEnum(enumValueExampleEEnum, EnumValueExample.class, "EnumValueExample");
        addEEnumLiteral(enumValueExampleEEnum, EnumValueExample.A);
        addEEnumLiteral(enumValueExampleEEnum, EnumValueExample.B);

        // Create resource
        createResource(eNS_URI);
    }

} //StoragePackageImpl
