/**
 */
package org.iobserve.model.privacy.impl;

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
import org.iobserve.model.privacy.DataProtectionModel;
import org.iobserve.model.privacy.EDataProtectionLevel;
import org.iobserve.model.privacy.EISOCode;
import org.iobserve.model.privacy.EncapsulatedDataSource;
import org.iobserve.model.privacy.GeoLocation;
import org.iobserve.model.privacy.IDataProtectionAnnotation;
import org.iobserve.model.privacy.ParameterDataProtection;
import org.iobserve.model.privacy.PrivacyFactory;
import org.iobserve.model.privacy.PrivacyPackage;
import org.iobserve.model.privacy.ReturnTypeDataProtection;
import org.palladiosimulator.pcm.PcmPackage;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PrivacyPackageImpl extends EPackageImpl implements PrivacyPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dataProtectionModelEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass returnTypeDataProtectionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass iDataProtectionAnnotationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass geoLocationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass parameterDataProtectionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass encapsulatedDataSourceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum eDataProtectionLevelEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum eisoCodeEEnum = null;

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
     * @see org.iobserve.model.privacy.PrivacyPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private PrivacyPackageImpl() {
        super(eNS_URI, PrivacyFactory.eINSTANCE);
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
     * <p>This method is used to initialize {@link PrivacyPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static PrivacyPackage init() {
        if (isInited) return (PrivacyPackage)EPackage.Registry.INSTANCE.getEPackage(PrivacyPackage.eNS_URI);

        // Obtain or create and register package
        Object registeredPrivacyPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        PrivacyPackageImpl thePrivacyPackage = registeredPrivacyPackage instanceof PrivacyPackageImpl ? (PrivacyPackageImpl)registeredPrivacyPackage : new PrivacyPackageImpl();

        isInited = true;

        // Initialize simple dependencies
        IdentifierPackage.eINSTANCE.eClass();
        PcmPackage.eINSTANCE.eClass();
        ProbfunctionPackage.eINSTANCE.eClass();
        StoexPackage.eINSTANCE.eClass();
        UnitsPackage.eINSTANCE.eClass();

        // Create package meta-data objects
        thePrivacyPackage.createPackageContents();

        // Initialize created meta-data
        thePrivacyPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        thePrivacyPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(PrivacyPackage.eNS_URI, thePrivacyPackage);
        return thePrivacyPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDataProtectionModel() {
        return dataProtectionModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDataProtectionModel_ResourceContainerLocations() {
        return (EReference)dataProtectionModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDataProtectionModel_PrivacyLevels() {
        return (EReference)dataProtectionModelEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDataProtectionModel_EncapsulatedDataSources() {
        return (EReference)dataProtectionModelEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getReturnTypeDataProtection() {
        return returnTypeDataProtectionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getReturnTypeDataProtection_OperationSignature() {
        return (EReference)returnTypeDataProtectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getIDataProtectionAnnotation() {
        return iDataProtectionAnnotationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getIDataProtectionAnnotation_Level() {
        return (EAttribute)iDataProtectionAnnotationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getGeoLocation() {
        return geoLocationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getGeoLocation_Isocode() {
        return (EAttribute)geoLocationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getGeoLocation_ResourceContainer() {
        return (EReference)geoLocationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getParameterDataProtection() {
        return parameterDataProtectionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getParameterDataProtection_Parameter() {
        return (EReference)parameterDataProtectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEncapsulatedDataSource() {
        return encapsulatedDataSourceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEncapsulatedDataSource_DataSource() {
        return (EAttribute)encapsulatedDataSourceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getEncapsulatedDataSource_Component() {
        return (EReference)encapsulatedDataSourceEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getEDataProtectionLevel() {
        return eDataProtectionLevelEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getEISOCode() {
        return eisoCodeEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PrivacyFactory getPrivacyFactory() {
        return (PrivacyFactory)getEFactoryInstance();
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
        dataProtectionModelEClass = createEClass(DATA_PROTECTION_MODEL);
        createEReference(dataProtectionModelEClass, DATA_PROTECTION_MODEL__RESOURCE_CONTAINER_LOCATIONS);
        createEReference(dataProtectionModelEClass, DATA_PROTECTION_MODEL__PRIVACY_LEVELS);
        createEReference(dataProtectionModelEClass, DATA_PROTECTION_MODEL__ENCAPSULATED_DATA_SOURCES);

        returnTypeDataProtectionEClass = createEClass(RETURN_TYPE_DATA_PROTECTION);
        createEReference(returnTypeDataProtectionEClass, RETURN_TYPE_DATA_PROTECTION__OPERATION_SIGNATURE);

        iDataProtectionAnnotationEClass = createEClass(IDATA_PROTECTION_ANNOTATION);
        createEAttribute(iDataProtectionAnnotationEClass, IDATA_PROTECTION_ANNOTATION__LEVEL);

        geoLocationEClass = createEClass(GEO_LOCATION);
        createEAttribute(geoLocationEClass, GEO_LOCATION__ISOCODE);
        createEReference(geoLocationEClass, GEO_LOCATION__RESOURCE_CONTAINER);

        parameterDataProtectionEClass = createEClass(PARAMETER_DATA_PROTECTION);
        createEReference(parameterDataProtectionEClass, PARAMETER_DATA_PROTECTION__PARAMETER);

        encapsulatedDataSourceEClass = createEClass(ENCAPSULATED_DATA_SOURCE);
        createEAttribute(encapsulatedDataSourceEClass, ENCAPSULATED_DATA_SOURCE__DATA_SOURCE);
        createEReference(encapsulatedDataSourceEClass, ENCAPSULATED_DATA_SOURCE__COMPONENT);

        // Create enums
        eDataProtectionLevelEEnum = createEEnum(EDATA_PROTECTION_LEVEL);
        eisoCodeEEnum = createEEnum(EISO_CODE);
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
        ResourceenvironmentPackage theResourceenvironmentPackage = (ResourceenvironmentPackage)EPackage.Registry.INSTANCE.getEPackage(ResourceenvironmentPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        returnTypeDataProtectionEClass.getESuperTypes().add(this.getIDataProtectionAnnotation());
        parameterDataProtectionEClass.getESuperTypes().add(this.getIDataProtectionAnnotation());

        // Initialize classes, features, and operations; add parameters
        initEClass(dataProtectionModelEClass, DataProtectionModel.class, "DataProtectionModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDataProtectionModel_ResourceContainerLocations(), this.getGeoLocation(), null, "resourceContainerLocations", null, 0, -1, DataProtectionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDataProtectionModel_PrivacyLevels(), this.getIDataProtectionAnnotation(), null, "privacyLevels", null, 0, -1, DataProtectionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDataProtectionModel_EncapsulatedDataSources(), this.getEncapsulatedDataSource(), null, "encapsulatedDataSources", null, 0, -1, DataProtectionModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(returnTypeDataProtectionEClass, ReturnTypeDataProtection.class, "ReturnTypeDataProtection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getReturnTypeDataProtection_OperationSignature(), theRepositoryPackage.getOperationSignature(), null, "operationSignature", null, 1, 1, ReturnTypeDataProtection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(iDataProtectionAnnotationEClass, IDataProtectionAnnotation.class, "IDataProtectionAnnotation", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getIDataProtectionAnnotation_Level(), this.getEDataProtectionLevel(), "level", null, 1, 1, IDataProtectionAnnotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(geoLocationEClass, GeoLocation.class, "GeoLocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGeoLocation_Isocode(), this.getEISOCode(), "isocode", null, 0, 1, GeoLocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGeoLocation_ResourceContainer(), theResourceenvironmentPackage.getResourceContainer(), null, "resourceContainer", null, 1, 1, GeoLocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(parameterDataProtectionEClass, ParameterDataProtection.class, "ParameterDataProtection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getParameterDataProtection_Parameter(), theRepositoryPackage.getParameter(), null, "parameter", null, 1, 1, ParameterDataProtection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(encapsulatedDataSourceEClass, EncapsulatedDataSource.class, "EncapsulatedDataSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getEncapsulatedDataSource_DataSource(), ecorePackage.getEBoolean(), "dataSource", null, 0, 1, EncapsulatedDataSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getEncapsulatedDataSource_Component(), theRepositoryPackage.getBasicComponent(), null, "component", null, 1, 1, EncapsulatedDataSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(eDataProtectionLevelEEnum, EDataProtectionLevel.class, "EDataProtectionLevel");
        addEEnumLiteral(eDataProtectionLevelEEnum, EDataProtectionLevel.ANONYMOUS);
        addEEnumLiteral(eDataProtectionLevelEEnum, EDataProtectionLevel.DEPERSONALIZED);
        addEEnumLiteral(eDataProtectionLevelEEnum, EDataProtectionLevel.PERSONAL);

        initEEnum(eisoCodeEEnum, EISOCode.class, "EISOCode");
        addEEnumLiteral(eisoCodeEEnum, EISOCode.AFGHANISTAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ALBANIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ANTARCTICA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ALGERIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.AMERICAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ANDORRA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ANGOLA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ANTIGUA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.AZERBAIJAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ARGENTINA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.AUSTRALIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.AUSTRIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BAHAMAS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BAHRAIN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BANGLADESH);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ARMENIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BARBADOS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BELGIUM);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BERMUDA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BHUTAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BOLIVIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BOSNIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BOTSWANA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BOUVET);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BRAZIL);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BELIZE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BRITISH_INDIAN_OCEAN_TERRITORY);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SOLOMON_ISLANDS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.VIRGIN_ISLANDS_UK);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BRUNEI);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BULGARIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MYANMAR);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BURUNDI);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BELARUS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CAMBODIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CAMEROON);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CANADA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CABO_VERDE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CAYMAN_ISLANDS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CENTRAL_AFRICAN_REPUBLIC);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SRI_LANKA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CHAD);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CHILE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CHINA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.TAIWAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CHRISTMAS_ISLAND);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.COCOS_ISLANDS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.COLOMBIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.COMOROS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MAYOTTE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CONGO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.DR_CONGO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.COOK_ISLANDS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.COSTA_RICA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CROATIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CUBA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CYPRUS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CZECHIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BENIN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.DENMARK);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.DOMINICA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.DOMINICAN_REPUBLIC);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ECUADOR);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.EL_SALVADOR);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.EQUATORIAL_GUINEA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ETHIOPIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ERITREA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ESTONIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.FAROE_ISLANDS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.FALKLAND_ISLANDS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SOUTH_GEORGIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.FIJI);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.FINLAND);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ALAND);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.FRANCE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.FRENCH_GUINEA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.FRENCH_POLINESIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.FRENCH_SOUTHERN_TERRITORIES);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.DJIBOUTI);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GABON);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GEORGIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GAMBIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.PALESTINE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GERMANY);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GHANA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GIBRALTAR);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.KIRIBATI);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GREECE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GREENLAND);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GRENADA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GUADELOUPE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GUAM);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GUATEMALA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GUINEA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GUYANA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.HAITI);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.HEARD_ISLAND_AND_MC_DONALD_ISLANDS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.HOLY_SEE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.HONDURAS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.HONG_KONG);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.HUNGARY);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ICELAND);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.INDIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.INDONESIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.IRAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.IRAQ);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.IRELAND);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ISRAEL);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ITALY);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.COTE_DIVOIRE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.JAMAICA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.JAPAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.KAZAKHSTAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.JORDAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.KENYA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NORTH_KOREA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SOUTH_KOREA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.KUWAIT);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.KYRGYZSTAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.DEMOCRATIC_PEOPLE_SREPUBLIC_OF_LAO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.LEBANON);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.LESOTHO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.LATVIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.LIBERIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.LIBYA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.LIECHTENSTEIN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.LITHUANIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.LUXEMBOURG);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MACAO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MADAGASCAR);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MALAWI);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MALAYSIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MALDIVES);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MALI);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MALTA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MARTINIQUE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MAURITANIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MAURITIUS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MEXICO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MONACO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MONGOLIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MOLDOVA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MONTENEGRO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MONTSERRAT);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MOROCCO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MOZAMBIQUE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.OMAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NAMIBIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NAURU);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NEPAL);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NETHERLANDS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.CURACAO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ARUBA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SINT_MAARTEN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BONAIRE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NEW_CALEDONIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.VANUATU);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NEW_ZEALAND);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NICARAGUA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NIGER);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NIGERIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NIUE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NORFOLK_ISLAND);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NORWAY);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.NORTHERN_MARIANA_ISLANDS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.UNITED_STATES_MINOR_OUTLYING_ISLANDS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MICRONESIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MARSHALL_ISLANDS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.PALAU);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.PAKISTAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.PANAMA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.PAPUA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.PARAGUAY);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.PERU);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.PHILIPPINES);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.PITCAIRN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.POLAND);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.PORTUGAL);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GUINEA_BISSAU);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.TIMOR_LESTE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.PUERTO_RICO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.QATAR);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.REUNION);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ROMANIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.RUSSIAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.RWANDA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SAINT_BARTHELEMY);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SAINT_HELENA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SAINT_KITTS_AND_NAVIS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ANGUILLA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SAINT_LUCIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SAINT_MARTIN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SAINT_PIERRE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SAINT_VINCENT);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SAN_MARINO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SAO_TOME);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SAUDI_ARABIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SENEGAL);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SERBIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SEYCHELLES);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SIERRA_LEONE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SINGAPORE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SLOVAKIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.VIETNAM);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SLOVENIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SOMALIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SOUTH_AFRICA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ZIMBABWE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SPAIN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SOUTH_SUDAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SUDAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.WESTERN_SAHARA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SURINAME);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SVALBARD);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SWAZILAND);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SWEDEN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SWITZERLAND);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SYRIAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.TAJIKISTAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.THAILAND);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.TOGO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.TOKELAU);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.TONGA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.TRINIDAD);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.UNITED_ARAB_EMIRATES);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.TUNISIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.TURKEY);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.TURKMENISTAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.TURKS_ISLAND);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.TUVALU);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.UGANDA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.UKRAINE);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.MACEDONIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.EGYPT);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.UNITED_KINGDOM);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GUERNSEY);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.JERSEY);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ISLE_OF_MEN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.TANZANIA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.USA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.VIRGIN_ISLANDS_US);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.BURKINA_FASO);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.URUGUAY);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.UZBEKISTAN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.VENEZUELA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.WALLIS);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.SAMOA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.YEMEN);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.ZAMBIA);

        // Create resource
        createResource(eNS_URI);
    }

} //PrivacyPackageImpl
