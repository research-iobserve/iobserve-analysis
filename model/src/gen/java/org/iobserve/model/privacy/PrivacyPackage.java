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
    String eNS_URI = "https://www.iobserve-devops.net/model/privacy";

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
     * The meta object id for the '{@link org.iobserve.model.privacy.impl.PrivacyModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.impl.PrivacyModelImpl
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getPrivacyModel()
     * @generated
     */
    int PRIVACY_MODEL = 0;

    /**
     * The feature id for the '<em><b>Resource Container Locations</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRIVACY_MODEL__RESOURCE_CONTAINER_LOCATIONS = 0;

    /**
     * The feature id for the '<em><b>Privacy Levels</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRIVACY_MODEL__PRIVACY_LEVELS = 1;

    /**
     * The number of structural features of the '<em>Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRIVACY_MODEL_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRIVACY_MODEL_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.IPrivacyAnnotation <em>IPrivacy Annotation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.IPrivacyAnnotation
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getIPrivacyAnnotation()
     * @generated
     */
    int IPRIVACY_ANNOTATION = 2;

    /**
     * The feature id for the '<em><b>Level</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IPRIVACY_ANNOTATION__LEVEL = 0;

    /**
     * The number of structural features of the '<em>IPrivacy Annotation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IPRIVACY_ANNOTATION_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>IPrivacy Annotation</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IPRIVACY_ANNOTATION_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.impl.ReturnTypePrivacyImpl <em>Return Type Privacy</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.impl.ReturnTypePrivacyImpl
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getReturnTypePrivacy()
     * @generated
     */
    int RETURN_TYPE_PRIVACY = 1;

    /**
     * The feature id for the '<em><b>Level</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_TYPE_PRIVACY__LEVEL = IPRIVACY_ANNOTATION__LEVEL;

    /**
     * The feature id for the '<em><b>Operation Signature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_TYPE_PRIVACY__OPERATION_SIGNATURE = IPRIVACY_ANNOTATION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Return Type Privacy</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_TYPE_PRIVACY_FEATURE_COUNT = IPRIVACY_ANNOTATION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Return Type Privacy</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RETURN_TYPE_PRIVACY_OPERATION_COUNT = IPRIVACY_ANNOTATION_OPERATION_COUNT + 0;

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
     * The meta object id for the '{@link org.iobserve.model.privacy.impl.ParameterPrivacyImpl <em>Parameter Privacy</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.impl.ParameterPrivacyImpl
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getParameterPrivacy()
     * @generated
     */
    int PARAMETER_PRIVACY = 4;

    /**
     * The feature id for the '<em><b>Level</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_PRIVACY__LEVEL = IPRIVACY_ANNOTATION__LEVEL;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_PRIVACY__PARAMETER = IPRIVACY_ANNOTATION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Parameter Privacy</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_PRIVACY_FEATURE_COUNT = IPRIVACY_ANNOTATION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Parameter Privacy</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_PRIVACY_OPERATION_COUNT = IPRIVACY_ANNOTATION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.EDataPrivacyLevel <em>EData Privacy Level</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.EDataPrivacyLevel
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getEDataPrivacyLevel()
     * @generated
     */
    int EDATA_PRIVACY_LEVEL = 5;

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.EISOCode <em>EISO Code</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.EISOCode
     * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getEISOCode()
     * @generated
     */
    int EISO_CODE = 6;


    /**
     * Returns the meta object for class '{@link org.iobserve.model.privacy.PrivacyModel <em>Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model</em>'.
     * @see org.iobserve.model.privacy.PrivacyModel
     * @generated
     */
    EClass getPrivacyModel();

    /**
     * Returns the meta object for the containment reference list '{@link org.iobserve.model.privacy.PrivacyModel#getResourceContainerLocations <em>Resource Container Locations</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Resource Container Locations</em>'.
     * @see org.iobserve.model.privacy.PrivacyModel#getResourceContainerLocations()
     * @see #getPrivacyModel()
     * @generated
     */
    EReference getPrivacyModel_ResourceContainerLocations();

    /**
     * Returns the meta object for the containment reference list '{@link org.iobserve.model.privacy.PrivacyModel#getPrivacyLevels <em>Privacy Levels</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Privacy Levels</em>'.
     * @see org.iobserve.model.privacy.PrivacyModel#getPrivacyLevels()
     * @see #getPrivacyModel()
     * @generated
     */
    EReference getPrivacyModel_PrivacyLevels();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.privacy.ReturnTypePrivacy <em>Return Type Privacy</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Return Type Privacy</em>'.
     * @see org.iobserve.model.privacy.ReturnTypePrivacy
     * @generated
     */
    EClass getReturnTypePrivacy();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.privacy.ReturnTypePrivacy#getOperationSignature <em>Operation Signature</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Operation Signature</em>'.
     * @see org.iobserve.model.privacy.ReturnTypePrivacy#getOperationSignature()
     * @see #getReturnTypePrivacy()
     * @generated
     */
    EReference getReturnTypePrivacy_OperationSignature();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.privacy.IPrivacyAnnotation <em>IPrivacy Annotation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>IPrivacy Annotation</em>'.
     * @see org.iobserve.model.privacy.IPrivacyAnnotation
     * @generated
     */
    EClass getIPrivacyAnnotation();

    /**
     * Returns the meta object for the attribute '{@link org.iobserve.model.privacy.IPrivacyAnnotation#getLevel <em>Level</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Level</em>'.
     * @see org.iobserve.model.privacy.IPrivacyAnnotation#getLevel()
     * @see #getIPrivacyAnnotation()
     * @generated
     */
    EAttribute getIPrivacyAnnotation_Level();

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
     * Returns the meta object for class '{@link org.iobserve.model.privacy.ParameterPrivacy <em>Parameter Privacy</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Parameter Privacy</em>'.
     * @see org.iobserve.model.privacy.ParameterPrivacy
     * @generated
     */
    EClass getParameterPrivacy();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.privacy.ParameterPrivacy#getParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Parameter</em>'.
     * @see org.iobserve.model.privacy.ParameterPrivacy#getParameter()
     * @see #getParameterPrivacy()
     * @generated
     */
    EReference getParameterPrivacy_Parameter();

    /**
     * Returns the meta object for enum '{@link org.iobserve.model.privacy.EDataPrivacyLevel <em>EData Privacy Level</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>EData Privacy Level</em>'.
     * @see org.iobserve.model.privacy.EDataPrivacyLevel
     * @generated
     */
    EEnum getEDataPrivacyLevel();

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
         * The meta object literal for the '{@link org.iobserve.model.privacy.impl.PrivacyModelImpl <em>Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.impl.PrivacyModelImpl
         * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getPrivacyModel()
         * @generated
         */
        EClass PRIVACY_MODEL = eINSTANCE.getPrivacyModel();

        /**
         * The meta object literal for the '<em><b>Resource Container Locations</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PRIVACY_MODEL__RESOURCE_CONTAINER_LOCATIONS = eINSTANCE.getPrivacyModel_ResourceContainerLocations();

        /**
         * The meta object literal for the '<em><b>Privacy Levels</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PRIVACY_MODEL__PRIVACY_LEVELS = eINSTANCE.getPrivacyModel_PrivacyLevels();

        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.impl.ReturnTypePrivacyImpl <em>Return Type Privacy</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.impl.ReturnTypePrivacyImpl
         * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getReturnTypePrivacy()
         * @generated
         */
        EClass RETURN_TYPE_PRIVACY = eINSTANCE.getReturnTypePrivacy();

        /**
         * The meta object literal for the '<em><b>Operation Signature</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RETURN_TYPE_PRIVACY__OPERATION_SIGNATURE = eINSTANCE.getReturnTypePrivacy_OperationSignature();

        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.IPrivacyAnnotation <em>IPrivacy Annotation</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.IPrivacyAnnotation
         * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getIPrivacyAnnotation()
         * @generated
         */
        EClass IPRIVACY_ANNOTATION = eINSTANCE.getIPrivacyAnnotation();

        /**
         * The meta object literal for the '<em><b>Level</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute IPRIVACY_ANNOTATION__LEVEL = eINSTANCE.getIPrivacyAnnotation_Level();

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
         * The meta object literal for the '{@link org.iobserve.model.privacy.impl.ParameterPrivacyImpl <em>Parameter Privacy</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.impl.ParameterPrivacyImpl
         * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getParameterPrivacy()
         * @generated
         */
        EClass PARAMETER_PRIVACY = eINSTANCE.getParameterPrivacy();

        /**
         * The meta object literal for the '<em><b>Parameter</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PARAMETER_PRIVACY__PARAMETER = eINSTANCE.getParameterPrivacy_Parameter();

        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.EDataPrivacyLevel <em>EData Privacy Level</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.EDataPrivacyLevel
         * @see org.iobserve.model.privacy.impl.PrivacyPackageImpl#getEDataPrivacyLevel()
         * @generated
         */
        EEnum EDATA_PRIVACY_LEVEL = eINSTANCE.getEDataPrivacyLevel();

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
