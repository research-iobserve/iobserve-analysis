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

import org.iobserve.model.privacy.EDataPrivacyLevel;
import org.iobserve.model.privacy.EISOCode;
import org.iobserve.model.privacy.GeoLocation;
import org.iobserve.model.privacy.ParameterPrivacy;
import org.iobserve.model.privacy.PrivacyFactory;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.privacy.PrivacyPackage;

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
    private EClass privacyModelEClass = null;

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
    private EClass parameterPrivacyEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum eDataPrivacyLevelEEnum = null;

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
        PrivacyPackageImpl thePrivacyPackage = (PrivacyPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof PrivacyPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new PrivacyPackageImpl());

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
    public EClass getPrivacyModel() {
        return privacyModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPrivacyModel_ResourceContainerLocations() {
        return (EReference)privacyModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPrivacyModel_ParameterPrivacyLevels() {
        return (EReference)privacyModelEClass.getEStructuralFeatures().get(1);
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
    public EClass getParameterPrivacy() {
        return parameterPrivacyEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getParameterPrivacy_Level() {
        return (EAttribute)parameterPrivacyEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getParameterPrivacy_Parameter() {
        return (EReference)parameterPrivacyEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getEDataPrivacyLevel() {
        return eDataPrivacyLevelEEnum;
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
        privacyModelEClass = createEClass(PRIVACY_MODEL);
        createEReference(privacyModelEClass, PRIVACY_MODEL__RESOURCE_CONTAINER_LOCATIONS);
        createEReference(privacyModelEClass, PRIVACY_MODEL__PARAMETER_PRIVACY_LEVELS);

        geoLocationEClass = createEClass(GEO_LOCATION);
        createEAttribute(geoLocationEClass, GEO_LOCATION__ISOCODE);
        createEReference(geoLocationEClass, GEO_LOCATION__RESOURCE_CONTAINER);

        parameterPrivacyEClass = createEClass(PARAMETER_PRIVACY);
        createEAttribute(parameterPrivacyEClass, PARAMETER_PRIVACY__LEVEL);
        createEReference(parameterPrivacyEClass, PARAMETER_PRIVACY__PARAMETER);

        // Create enums
        eDataPrivacyLevelEEnum = createEEnum(EDATA_PRIVACY_LEVEL);
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
        ResourceenvironmentPackage theResourceenvironmentPackage = (ResourceenvironmentPackage)EPackage.Registry.INSTANCE.getEPackage(ResourceenvironmentPackage.eNS_URI);
        RepositoryPackage theRepositoryPackage = (RepositoryPackage)EPackage.Registry.INSTANCE.getEPackage(RepositoryPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes

        // Initialize classes, features, and operations; add parameters
        initEClass(privacyModelEClass, PrivacyModel.class, "PrivacyModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPrivacyModel_ResourceContainerLocations(), this.getGeoLocation(), null, "resourceContainerLocations", null, 0, -1, PrivacyModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPrivacyModel_ParameterPrivacyLevels(), this.getParameterPrivacy(), null, "parameterPrivacyLevels", null, 0, -1, PrivacyModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(geoLocationEClass, GeoLocation.class, "GeoLocation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getGeoLocation_Isocode(), this.getEISOCode(), "isocode", null, 0, 1, GeoLocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getGeoLocation_ResourceContainer(), theResourceenvironmentPackage.getResourceContainer(), null, "resourceContainer", null, 1, 1, GeoLocation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(parameterPrivacyEClass, ParameterPrivacy.class, "ParameterPrivacy", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getParameterPrivacy_Level(), this.getEDataPrivacyLevel(), "level", null, 1, 1, ParameterPrivacy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getParameterPrivacy_Parameter(), theRepositoryPackage.getParameter(), null, "parameter", null, 1, 1, ParameterPrivacy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(eDataPrivacyLevelEEnum, EDataPrivacyLevel.class, "EDataPrivacyLevel");
        addEEnumLiteral(eDataPrivacyLevelEEnum, EDataPrivacyLevel.ANONYMOUS);
        addEEnumLiteral(eDataPrivacyLevelEEnum, EDataPrivacyLevel.DEPERSONALIZED);
        addEEnumLiteral(eDataPrivacyLevelEEnum, EDataPrivacyLevel.PERSONAL);

        initEEnum(eisoCodeEEnum, EISOCode.class, "EISOCode");
        addEEnumLiteral(eisoCodeEEnum, EISOCode.USA);
        addEEnumLiteral(eisoCodeEEnum, EISOCode.GERMANY);

        // Create resource
        createResource(eNS_URI);
    }

} //PrivacyPackageImpl
