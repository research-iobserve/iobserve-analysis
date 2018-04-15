/**
 */
package org.iobserve.model.privacy.privacy;

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
 * @see org.iobserve.model.privacy.privacy.PrivacyFactory
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
    PrivacyPackage eINSTANCE = org.iobserve.model.privacy.privacy.impl.PrivacyPackageImpl.init();

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.privacy.impl.PrivacyModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.privacy.impl.PrivacyModelImpl
     * @see org.iobserve.model.privacy.privacy.impl.PrivacyPackageImpl#getPrivacyModel()
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
     * The feature id for the '<em><b>Parameter Privacy Levels</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PRIVACY_MODEL__PARAMETER_PRIVACY_LEVELS = 1;

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
     * The meta object id for the '{@link org.iobserve.model.privacy.privacy.impl.GeoLocationImpl <em>Geo Location</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.privacy.impl.GeoLocationImpl
     * @see org.iobserve.model.privacy.privacy.impl.PrivacyPackageImpl#getGeoLocation()
     * @generated
     */
    int GEO_LOCATION = 1;

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
     * The meta object id for the '{@link org.iobserve.model.privacy.privacy.impl.ParameterPrivacyImpl <em>Parameter Privacy</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.privacy.impl.ParameterPrivacyImpl
     * @see org.iobserve.model.privacy.privacy.impl.PrivacyPackageImpl#getParameterPrivacy()
     * @generated
     */
    int PARAMETER_PRIVACY = 2;

    /**
     * The feature id for the '<em><b>Level</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_PRIVACY__LEVEL = 0;

    /**
     * The feature id for the '<em><b>Parameter</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_PRIVACY__PARAMETER = 1;

    /**
     * The number of structural features of the '<em>Parameter Privacy</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_PRIVACY_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Parameter Privacy</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PARAMETER_PRIVACY_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.privacy.EDataPrivacyLevel <em>EData Privacy Level</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.privacy.EDataPrivacyLevel
     * @see org.iobserve.model.privacy.privacy.impl.PrivacyPackageImpl#getEDataPrivacyLevel()
     * @generated
     */
    int EDATA_PRIVACY_LEVEL = 3;

    /**
     * The meta object id for the '{@link org.iobserve.model.privacy.privacy.EISOCode <em>EISO Code</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.privacy.privacy.EISOCode
     * @see org.iobserve.model.privacy.privacy.impl.PrivacyPackageImpl#getEISOCode()
     * @generated
     */
    int EISO_CODE = 4;


    /**
     * Returns the meta object for class '{@link org.iobserve.model.privacy.privacy.PrivacyModel <em>Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model</em>'.
     * @see org.iobserve.model.privacy.privacy.PrivacyModel
     * @generated
     */
    EClass getPrivacyModel();

    /**
     * Returns the meta object for the containment reference list '{@link org.iobserve.model.privacy.privacy.PrivacyModel#getResourceContainerLocations <em>Resource Container Locations</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Resource Container Locations</em>'.
     * @see org.iobserve.model.privacy.privacy.PrivacyModel#getResourceContainerLocations()
     * @see #getPrivacyModel()
     * @generated
     */
    EReference getPrivacyModel_ResourceContainerLocations();

    /**
     * Returns the meta object for the containment reference list '{@link org.iobserve.model.privacy.privacy.PrivacyModel#getParameterPrivacyLevels <em>Parameter Privacy Levels</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Parameter Privacy Levels</em>'.
     * @see org.iobserve.model.privacy.privacy.PrivacyModel#getParameterPrivacyLevels()
     * @see #getPrivacyModel()
     * @generated
     */
    EReference getPrivacyModel_ParameterPrivacyLevels();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.privacy.privacy.GeoLocation <em>Geo Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Geo Location</em>'.
     * @see org.iobserve.model.privacy.privacy.GeoLocation
     * @generated
     */
    EClass getGeoLocation();

    /**
     * Returns the meta object for the attribute '{@link org.iobserve.model.privacy.privacy.GeoLocation#getIsocode <em>Isocode</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Isocode</em>'.
     * @see org.iobserve.model.privacy.privacy.GeoLocation#getIsocode()
     * @see #getGeoLocation()
     * @generated
     */
    EAttribute getGeoLocation_Isocode();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.privacy.privacy.GeoLocation#getResourceContainer <em>Resource Container</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Resource Container</em>'.
     * @see org.iobserve.model.privacy.privacy.GeoLocation#getResourceContainer()
     * @see #getGeoLocation()
     * @generated
     */
    EReference getGeoLocation_ResourceContainer();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.privacy.privacy.ParameterPrivacy <em>Parameter Privacy</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Parameter Privacy</em>'.
     * @see org.iobserve.model.privacy.privacy.ParameterPrivacy
     * @generated
     */
    EClass getParameterPrivacy();

    /**
     * Returns the meta object for the attribute '{@link org.iobserve.model.privacy.privacy.ParameterPrivacy#getLevel <em>Level</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Level</em>'.
     * @see org.iobserve.model.privacy.privacy.ParameterPrivacy#getLevel()
     * @see #getParameterPrivacy()
     * @generated
     */
    EAttribute getParameterPrivacy_Level();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.privacy.privacy.ParameterPrivacy#getParameter <em>Parameter</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Parameter</em>'.
     * @see org.iobserve.model.privacy.privacy.ParameterPrivacy#getParameter()
     * @see #getParameterPrivacy()
     * @generated
     */
    EReference getParameterPrivacy_Parameter();

    /**
     * Returns the meta object for enum '{@link org.iobserve.model.privacy.privacy.EDataPrivacyLevel <em>EData Privacy Level</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>EData Privacy Level</em>'.
     * @see org.iobserve.model.privacy.privacy.EDataPrivacyLevel
     * @generated
     */
    EEnum getEDataPrivacyLevel();

    /**
     * Returns the meta object for enum '{@link org.iobserve.model.privacy.privacy.EISOCode <em>EISO Code</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>EISO Code</em>'.
     * @see org.iobserve.model.privacy.privacy.EISOCode
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
         * The meta object literal for the '{@link org.iobserve.model.privacy.privacy.impl.PrivacyModelImpl <em>Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.privacy.impl.PrivacyModelImpl
         * @see org.iobserve.model.privacy.privacy.impl.PrivacyPackageImpl#getPrivacyModel()
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
         * The meta object literal for the '<em><b>Parameter Privacy Levels</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PRIVACY_MODEL__PARAMETER_PRIVACY_LEVELS = eINSTANCE.getPrivacyModel_ParameterPrivacyLevels();

        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.privacy.impl.GeoLocationImpl <em>Geo Location</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.privacy.impl.GeoLocationImpl
         * @see org.iobserve.model.privacy.privacy.impl.PrivacyPackageImpl#getGeoLocation()
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
         * The meta object literal for the '{@link org.iobserve.model.privacy.privacy.impl.ParameterPrivacyImpl <em>Parameter Privacy</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.privacy.impl.ParameterPrivacyImpl
         * @see org.iobserve.model.privacy.privacy.impl.PrivacyPackageImpl#getParameterPrivacy()
         * @generated
         */
        EClass PARAMETER_PRIVACY = eINSTANCE.getParameterPrivacy();

        /**
         * The meta object literal for the '<em><b>Level</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute PARAMETER_PRIVACY__LEVEL = eINSTANCE.getParameterPrivacy_Level();

        /**
         * The meta object literal for the '<em><b>Parameter</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PARAMETER_PRIVACY__PARAMETER = eINSTANCE.getParameterPrivacy_Parameter();

        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.privacy.EDataPrivacyLevel <em>EData Privacy Level</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.privacy.EDataPrivacyLevel
         * @see org.iobserve.model.privacy.privacy.impl.PrivacyPackageImpl#getEDataPrivacyLevel()
         * @generated
         */
        EEnum EDATA_PRIVACY_LEVEL = eINSTANCE.getEDataPrivacyLevel();

        /**
         * The meta object literal for the '{@link org.iobserve.model.privacy.privacy.EISOCode <em>EISO Code</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.privacy.privacy.EISOCode
         * @see org.iobserve.model.privacy.privacy.impl.PrivacyPackageImpl#getEISOCode()
         * @generated
         */
        EEnum EISO_CODE = eINSTANCE.getEISOCode();

    }

} //PrivacyPackage
