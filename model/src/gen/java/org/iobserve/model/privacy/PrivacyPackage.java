/**
 */
package org.iobserve.model.privacy;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.iobserve.model.privacy.PrivacyFactory
 * @model kind="package"
 * @generated
 */
public interface PrivacyPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "privacy";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "https://www.iobserve-devops.net/model/0.0.3/privacy";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "privacy";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    PrivacyPackage eINSTANCE = org.iobserve.model.privacy.impl.PrivacyPackageImpl.init();

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.impl.DataProtectionModelImpl <em>Data Protection Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.impl.DataProtectionModelImpl
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getDataProtectionModel()
     * @generated
     */
    int DATA_PROTECTION_MODEL = 0;

    /**
     * The feature id for the '<em><b>Resource Container Locations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_PROTECTION_MODEL__RESOURCE_CONTAINER_LOCATIONS = 0;

    /**
     * The feature id for the '<em><b>Privacy Levels</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_PROTECTION_MODEL__PRIVACY_LEVELS = 1;

    /**
     * The feature id for the '<em><b>Encapsulated Data Sources</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_PROTECTION_MODEL__ENCAPSULATED_DATA_SOURCES = 2;

    /**
     * The number of structural features of the '<em>Data Protection Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_PROTECTION_MODEL_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Data Protection Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_PROTECTION_MODEL_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.IDataProtectionAnnotation <em>IData Protection Annotation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.IDataProtectionAnnotation
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getIDataProtectionAnnotation()
     * @generated
     */
    int IDATA_PROTECTION_ANNOTATION = 2;

    /**
     * The feature id for the '<em><b>Level</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDATA_PROTECTION_ANNOTATION__LEVEL = 0;

    /**
     * The number of structural features of the '<em>IData Protection Annotation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDATA_PROTECTION_ANNOTATION_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>IData Protection Annotation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IDATA_PROTECTION_ANNOTATION_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.impl.ReturnTypeDataProtectionImpl <em>Return Type Data Protection</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.impl.ReturnTypeDataProtectionImpl
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getReturnTypeDataProtection()
     * @generated
     */
    int RETURN_TYPE_DATA_PROTECTION = 1;

    /**
     * The feature id for the '<em><b>Level</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_TYPE_DATA_PROTECTION__LEVEL = IDATA_PROTECTION_ANNOTATION__LEVEL;

    /**
     * The feature id for the '<em><b>Operation Signature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_TYPE_DATA_PROTECTION__OPERATION_SIGNATURE = IDATA_PROTECTION_ANNOTATION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Return Type Data Protection</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_TYPE_DATA_PROTECTION_FEATURE_COUNT = IDATA_PROTECTION_ANNOTATION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Return Type Data Protection</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_TYPE_DATA_PROTECTION_OPERATION_COUNT = IDATA_PROTECTION_ANNOTATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.impl.GeoLocationImpl <em>Geo Location</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.impl.GeoLocationImpl
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getGeoLocation()
     * @generated
     */
    int GEO_LOCATION = 3;

    /**
     * The feature id for the '<em><b>Isocode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GEO_LOCATION__ISOCODE = 0;

    /**
     * The feature id for the '<em><b>Resource Container</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GEO_LOCATION__RESOURCE_CONTAINER = 1;

    /**
     * The number of structural features of the '<em>Geo Location</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GEO_LOCATION_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Geo Location</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int GEO_LOCATION_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.impl.ParameterDataProtectionImpl <em>Parameter Data Protection</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.impl.ParameterDataProtectionImpl
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getParameterDataProtection()
     * @generated
     */
    int PARAMETER_DATA_PROTECTION = 4;

    /**
     * The feature id for the '<em><b>Level</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_DATA_PROTECTION__LEVEL = IDATA_PROTECTION_ANNOTATION__LEVEL;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_DATA_PROTECTION__PARAMETER = IDATA_PROTECTION_ANNOTATION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Parameter Data Protection</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_DATA_PROTECTION_FEATURE_COUNT = IDATA_PROTECTION_ANNOTATION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Parameter Data Protection</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_DATA_PROTECTION_OPERATION_COUNT = IDATA_PROTECTION_ANNOTATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.impl.EncapsulatedDataSourceImpl <em>Encapsulated Data Source</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.impl.EncapsulatedDataSourceImpl
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getEncapsulatedDataSource()
     * @generated
     */
    int ENCAPSULATED_DATA_SOURCE = 5;

    /**
     * The feature id for the '<em><b>Data Source</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENCAPSULATED_DATA_SOURCE__DATA_SOURCE = 0;

    /**
     * The feature id for the '<em><b>Component</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENCAPSULATED_DATA_SOURCE__COMPONENT = 1;

    /**
     * The number of structural features of the '<em>Encapsulated Data Source</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENCAPSULATED_DATA_SOURCE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Encapsulated Data Source</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ENCAPSULATED_DATA_SOURCE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.EDataProtectionLevel <em>EData Protection Level</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.EDataProtectionLevel
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getEDataProtectionLevel()
     * @generated
     */
    int EDATA_PROTECTION_LEVEL = 6;

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.EISOCode <em>EISO Code</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.EISOCode
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getEISOCode()
     * @generated
     */
    int EISO_CODE = 7;


    /**
     * Returns the meta object for class '{@link org.iobserve.model.privacy.DataProtectionModel <em>Data Protection Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Data Protection Model</em>'.
     * @see org.iobserve.model.privacy.DataProtectionModel
     * @generated
     */
    EClass getDataProtectionModel();

    /**
     * Returns the meta object for the containment reference list '{@link org.iobserve.model.privacy.DataProtectionModel#getResourceContainerLocations <em>Resource Container Locations</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Resource Container Locations</em>'.
     * @see org.iobserve.model.privacy.DataProtectionModel#getResourceContainerLocations()
     * @see #getDataProtectionModel()
     * @generated
     */
    EReference getDataProtectionModel_ResourceContainerLocations();

    /**
     * Returns the meta object for the containment reference list '{@link org.iobserve.model.privacy.DataProtectionModel#getPrivacyLevels <em>Privacy Levels</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Privacy Levels</em>'.
     * @see org.iobserve.model.privacy.DataProtectionModel#getDataProectionLevels()
     * @see #getDataProtectionModel()
     * @generated
     */
    EReference getDataProtectionModel_PrivacyLevels();

    /**
     * Returns the meta object for the containment reference list '{@link org.iobserve.model.privacy.DataProtectionModel#getEncapsulatedDataSources <em>Encapsulated Data Sources</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Encapsulated Data Sources</em>'.
     * @see org.iobserve.model.privacy.DataProtectionModel#getEncapsulatedDataSources()
     * @see #getDataProtectionModel()
     * @generated
     */
    EReference getDataProtectionModel_EncapsulatedDataSources();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.privacy.ReturnTypeDataProtection <em>Return Type Data Protection</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Return Type Data Protection</em>'.
     * @see org.iobserve.model.privacy.ReturnTypeDataProtection
     * @generated
     */
    EClass getReturnTypeDataProtection();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.privacy.ReturnTypeDataProtection#getOperationSignature <em>Operation Signature</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Operation Signature</em>'.
     * @see org.iobserve.model.privacy.ReturnTypeDataProtection#getOperationSignature()
     * @see #getReturnTypeDataProtection()
     * @generated
     */
    EReference getReturnTypeDataProtection_OperationSignature();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.privacy.IDataProtectionAnnotation <em>IData Protection Annotation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>IData Protection Annotation</em>'.
     * @see org.iobserve.model.privacy.IDataProtectionAnnotation
     * @generated
     */
    EClass getIDataProtectionAnnotation();

    /**
     * Returns the meta object for the attribute '{@link org.iobserve.model.privacy.IDataProtectionAnnotation#getLevel <em>Level</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Level</em>'.
     * @see org.iobserve.model.privacy.IDataProtectionAnnotation#getLevel()
     * @see #getIDataProtectionAnnotation()
     * @generated
     */
    EAttribute getIDataProtectionAnnotation_Level();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.privacy.GeoLocation <em>Geo Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Geo Location</em>'.
     * @see org.iobserve.model.privacy.GeoLocation
     * @generated
     */
    EClass getGeoLocation();

    /**
     * Returns the meta object for the attribute '{@link org.iobserve.model.privacy.GeoLocation#getIsocode <em>Isocode</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Isocode</em>'.
     * @see org.iobserve.model.privacy.GeoLocation#getIsocode()
     * @see #getGeoLocation()
     * @generated
     */
    EAttribute getGeoLocation_Isocode();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.privacy.GeoLocation#getResourceContainer <em>Resource Container</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Resource Container</em>'.
     * @see org.iobserve.model.privacy.GeoLocation#getResourceContainer()
     * @see #getGeoLocation()
     * @generated
     */
    EReference getGeoLocation_ResourceContainer();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.privacy.ParameterDataProtection <em>Parameter Data Protection</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Parameter Data Protection</em>'.
     * @see org.iobserve.model.privacy.ParameterDataProtection
     * @generated
     */
    EClass getParameterDataProtection();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.privacy.ParameterDataProtection#getParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Parameter</em>'.
     * @see org.iobserve.model.privacy.ParameterDataProtection#getParameter()
     * @see #getParameterDataProtection()
     * @generated
     */
    EReference getParameterDataProtection_Parameter();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.privacy.EncapsulatedDataSource <em>Encapsulated Data Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Encapsulated Data Source</em>'.
     * @see org.iobserve.model.privacy.EncapsulatedDataSource
     * @generated
     */
    EClass getEncapsulatedDataSource();

    /**
     * Returns the meta object for the attribute '{@link org.iobserve.model.privacy.EncapsulatedDataSource#isDataSource <em>Data Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Data Source</em>'.
     * @see org.iobserve.model.privacy.EncapsulatedDataSource#isDataSource()
     * @see #getEncapsulatedDataSource()
     * @generated
     */
    EAttribute getEncapsulatedDataSource_DataSource();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.privacy.EncapsulatedDataSource#getComponent <em>Component</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Component</em>'.
     * @see org.iobserve.model.privacy.EncapsulatedDataSource#getComponent()
     * @see #getEncapsulatedDataSource()
     * @generated
     */
    EReference getEncapsulatedDataSource_Component();

    /**
     * Returns the meta object for enum '{@link org.iobserve.model.privacy.EDataProtectionLevel <em>EData Protection Level</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>EData Protection Level</em>'.
     * @see org.iobserve.model.privacy.EDataProtectionLevel
     * @generated
     */
    EEnum getEDataProtectionLevel();

    /**
     * Returns the meta object for enum '{@link org.iobserve.model.privacy.EISOCode <em>EISO Code</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>EISO Code</em>'.
     * @see org.iobserve.model.privacy.EISOCode
     * @generated
     */
    EEnum getEISOCode();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    PrivacyFactory getPrivacyFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each operation of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.impl.DataProtectionModelImpl <em>Data Protection Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.impl.DataProtectionModelImpl
         * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getDataProtectionModel()
         * @generated
         */
        EClass DATA_PROTECTION_MODEL = eINSTANCE.getDataProtectionModel();

        /**
         * The meta object literal for the '<em><b>Resource Container Locations</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATA_PROTECTION_MODEL__RESOURCE_CONTAINER_LOCATIONS = eINSTANCE.getDataProtectionModel_ResourceContainerLocations();

        /**
         * The meta object literal for the '<em><b>Privacy Levels</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATA_PROTECTION_MODEL__PRIVACY_LEVELS = eINSTANCE.getDataProtectionModel_PrivacyLevels();

        /**
         * The meta object literal for the '<em><b>Encapsulated Data Sources</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATA_PROTECTION_MODEL__ENCAPSULATED_DATA_SOURCES = eINSTANCE.getDataProtectionModel_EncapsulatedDataSources();

        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.impl.ReturnTypeDataProtectionImpl <em>Return Type Data Protection</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.impl.ReturnTypeDataProtectionImpl
         * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getReturnTypeDataProtection()
         * @generated
         */
        EClass RETURN_TYPE_DATA_PROTECTION = eINSTANCE.getReturnTypeDataProtection();

        /**
         * The meta object literal for the '<em><b>Operation Signature</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RETURN_TYPE_DATA_PROTECTION__OPERATION_SIGNATURE = eINSTANCE.getReturnTypeDataProtection_OperationSignature();

        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.IDataProtectionAnnotation <em>IData Protection Annotation</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.IDataProtectionAnnotation
         * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getIDataProtectionAnnotation()
         * @generated
         */
        EClass IDATA_PROTECTION_ANNOTATION = eINSTANCE.getIDataProtectionAnnotation();

        /**
         * The meta object literal for the '<em><b>Level</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute IDATA_PROTECTION_ANNOTATION__LEVEL = eINSTANCE.getIDataProtectionAnnotation_Level();

        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.impl.GeoLocationImpl <em>Geo Location</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.impl.GeoLocationImpl
         * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getGeoLocation()
         * @generated
         */
        EClass GEO_LOCATION = eINSTANCE.getGeoLocation();

        /**
         * The meta object literal for the '<em><b>Isocode</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute GEO_LOCATION__ISOCODE = eINSTANCE.getGeoLocation_Isocode();

        /**
         * The meta object literal for the '<em><b>Resource Container</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference GEO_LOCATION__RESOURCE_CONTAINER = eINSTANCE.getGeoLocation_ResourceContainer();

        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.impl.ParameterDataProtectionImpl <em>Parameter Data Protection</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.impl.ParameterDataProtectionImpl
         * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getParameterDataProtection()
         * @generated
         */
        EClass PARAMETER_DATA_PROTECTION = eINSTANCE.getParameterDataProtection();

        /**
         * The meta object literal for the '<em><b>Parameter</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PARAMETER_DATA_PROTECTION__PARAMETER = eINSTANCE.getParameterDataProtection_Parameter();

        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.impl.EncapsulatedDataSourceImpl <em>Encapsulated Data Source</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.impl.EncapsulatedDataSourceImpl
         * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getEncapsulatedDataSource()
         * @generated
         */
        EClass ENCAPSULATED_DATA_SOURCE = eINSTANCE.getEncapsulatedDataSource();

        /**
         * The meta object literal for the '<em><b>Data Source</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ENCAPSULATED_DATA_SOURCE__DATA_SOURCE = eINSTANCE.getEncapsulatedDataSource_DataSource();

        /**
         * The meta object literal for the '<em><b>Component</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ENCAPSULATED_DATA_SOURCE__COMPONENT = eINSTANCE.getEncapsulatedDataSource_Component();

        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.EDataProtectionLevel <em>EData Protection Level</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.EDataProtectionLevel
         * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getEDataProtectionLevel()
         * @generated
         */
        EEnum EDATA_PROTECTION_LEVEL = eINSTANCE.getEDataProtectionLevel();

        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.EISOCode <em>EISO Code</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.EISOCode
         * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getEISOCode()
         * @generated
         */
        EEnum EISO_CODE = eINSTANCE.getEISOCode();

    }

} //PrivacyPackage
