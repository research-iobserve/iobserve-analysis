/**
 */
package org.iobserve.planning.cloudprofile;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.iobserve.planning.cloudprofile.cloudprofileFactory
 * @model kind="package"
 * @generated
 */
public interface cloudprofilePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "cloudprofile";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://iobserve.org/CloudProfile";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.iobserve";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	cloudprofilePackage eINSTANCE = org.iobserve.planning.cloudprofile.impl.cloudprofilePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.iobserve.planning.cloudprofile.impl.CloudProfileImpl <em>Cloud Profile</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.cloudprofile.impl.CloudProfileImpl
	 * @see org.iobserve.planning.cloudprofile.impl.cloudprofilePackageImpl#getCloudProfile()
	 * @generated
	 */
	int CLOUD_PROFILE = 0;

	/**
	 * The feature id for the '<em><b>Cloud Providers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOUD_PROFILE__CLOUD_PROVIDERS = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOUD_PROFILE__NAME = 1;

	/**
	 * The number of structural features of the '<em>Cloud Profile</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOUD_PROFILE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.cloudprofile.impl.CloudProviderImpl <em>Cloud Provider</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.cloudprofile.impl.CloudProviderImpl
	 * @see org.iobserve.planning.cloudprofile.impl.cloudprofilePackageImpl#getCloudProvider()
	 * @generated
	 */
	int CLOUD_PROVIDER = 1;

	/**
	 * The feature id for the '<em><b>Cloud Resources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOUD_PROVIDER__CLOUD_RESOURCES = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOUD_PROVIDER__NAME = 1;

	/**
	 * The feature id for the '<em><b>Identity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOUD_PROVIDER__IDENTITY = 2;

	/**
	 * The feature id for the '<em><b>Credential</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOUD_PROVIDER__CREDENTIAL = 3;

	/**
	 * The number of structural features of the '<em>Cloud Provider</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOUD_PROVIDER_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.cloudprofile.impl.CloudResourceTypeImpl <em>Cloud Resource Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.cloudprofile.impl.CloudResourceTypeImpl
	 * @see org.iobserve.planning.cloudprofile.impl.cloudprofilePackageImpl#getCloudResourceType()
	 * @generated
	 */
	int CLOUD_RESOURCE_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Price Per Hour</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOUD_RESOURCE_TYPE__PRICE_PER_HOUR = 0;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOUD_RESOURCE_TYPE__LOCATION = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOUD_RESOURCE_TYPE__NAME = 2;

	/**
	 * The feature id for the '<em><b>Provider</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOUD_RESOURCE_TYPE__PROVIDER = 3;

	/**
	 * The number of structural features of the '<em>Cloud Resource Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLOUD_RESOURCE_TYPE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl <em>VM Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.planning.cloudprofile.impl.VMTypeImpl
	 * @see org.iobserve.planning.cloudprofile.impl.cloudprofilePackageImpl#getVMType()
	 * @generated
	 */
	int VM_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Price Per Hour</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VM_TYPE__PRICE_PER_HOUR = CLOUD_RESOURCE_TYPE__PRICE_PER_HOUR;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VM_TYPE__LOCATION = CLOUD_RESOURCE_TYPE__LOCATION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VM_TYPE__NAME = CLOUD_RESOURCE_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Provider</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VM_TYPE__PROVIDER = CLOUD_RESOURCE_TYPE__PROVIDER;

	/**
	 * The feature id for the '<em><b>Max Cores</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VM_TYPE__MAX_CORES = CLOUD_RESOURCE_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Min Cores</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VM_TYPE__MIN_CORES = CLOUD_RESOURCE_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Min RAM</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VM_TYPE__MIN_RAM = CLOUD_RESOURCE_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Max RAM</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VM_TYPE__MAX_RAM = CLOUD_RESOURCE_TYPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Min Processing Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VM_TYPE__MIN_PROCESSING_RATE = CLOUD_RESOURCE_TYPE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Max Processing Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VM_TYPE__MAX_PROCESSING_RATE = CLOUD_RESOURCE_TYPE_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>VM Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VM_TYPE_FEATURE_COUNT = CLOUD_RESOURCE_TYPE_FEATURE_COUNT + 6;


	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.cloudprofile.CloudProfile <em>Cloud Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cloud Profile</em>'.
	 * @see org.iobserve.planning.cloudprofile.CloudProfile
	 * @generated
	 */
	EClass getCloudProfile();

	/**
	 * Returns the meta object for the containment reference list '{@link org.iobserve.planning.cloudprofile.CloudProfile#getCloudProviders <em>Cloud Providers</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cloud Providers</em>'.
	 * @see org.iobserve.planning.cloudprofile.CloudProfile#getCloudProviders()
	 * @see #getCloudProfile()
	 * @generated
	 */
	EReference getCloudProfile_CloudProviders();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.planning.cloudprofile.CloudProfile#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.iobserve.planning.cloudprofile.CloudProfile#getName()
	 * @see #getCloudProfile()
	 * @generated
	 */
	EAttribute getCloudProfile_Name();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.cloudprofile.CloudProvider <em>Cloud Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cloud Provider</em>'.
	 * @see org.iobserve.planning.cloudprofile.CloudProvider
	 * @generated
	 */
	EClass getCloudProvider();

	/**
	 * Returns the meta object for the containment reference list '{@link org.iobserve.planning.cloudprofile.CloudProvider#getCloudResources <em>Cloud Resources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cloud Resources</em>'.
	 * @see org.iobserve.planning.cloudprofile.CloudProvider#getCloudResources()
	 * @see #getCloudProvider()
	 * @generated
	 */
	EReference getCloudProvider_CloudResources();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.planning.cloudprofile.CloudProvider#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.iobserve.planning.cloudprofile.CloudProvider#getName()
	 * @see #getCloudProvider()
	 * @generated
	 */
	EAttribute getCloudProvider_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.planning.cloudprofile.CloudProvider#getIdentity <em>Identity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Identity</em>'.
	 * @see org.iobserve.planning.cloudprofile.CloudProvider#getIdentity()
	 * @see #getCloudProvider()
	 * @generated
	 */
	EAttribute getCloudProvider_Identity();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.planning.cloudprofile.CloudProvider#getCredential <em>Credential</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Credential</em>'.
	 * @see org.iobserve.planning.cloudprofile.CloudProvider#getCredential()
	 * @see #getCloudProvider()
	 * @generated
	 */
	EAttribute getCloudProvider_Credential();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.cloudprofile.CloudResourceType <em>Cloud Resource Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cloud Resource Type</em>'.
	 * @see org.iobserve.planning.cloudprofile.CloudResourceType
	 * @generated
	 */
	EClass getCloudResourceType();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.planning.cloudprofile.CloudResourceType#getPricePerHour <em>Price Per Hour</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Per Hour</em>'.
	 * @see org.iobserve.planning.cloudprofile.CloudResourceType#getPricePerHour()
	 * @see #getCloudResourceType()
	 * @generated
	 */
	EAttribute getCloudResourceType_PricePerHour();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.planning.cloudprofile.CloudResourceType#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location</em>'.
	 * @see org.iobserve.planning.cloudprofile.CloudResourceType#getLocation()
	 * @see #getCloudResourceType()
	 * @generated
	 */
	EAttribute getCloudResourceType_Location();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.planning.cloudprofile.CloudResourceType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.iobserve.planning.cloudprofile.CloudResourceType#getName()
	 * @see #getCloudResourceType()
	 * @generated
	 */
	EAttribute getCloudResourceType_Name();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.planning.cloudprofile.CloudResourceType#getProvider <em>Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Provider</em>'.
	 * @see org.iobserve.planning.cloudprofile.CloudResourceType#getProvider()
	 * @see #getCloudResourceType()
	 * @generated
	 */
	EReference getCloudResourceType_Provider();

	/**
	 * Returns the meta object for class '{@link org.iobserve.planning.cloudprofile.VMType <em>VM Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>VM Type</em>'.
	 * @see org.iobserve.planning.cloudprofile.VMType
	 * @generated
	 */
	EClass getVMType();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.planning.cloudprofile.VMType#getMaxCores <em>Max Cores</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Cores</em>'.
	 * @see org.iobserve.planning.cloudprofile.VMType#getMaxCores()
	 * @see #getVMType()
	 * @generated
	 */
	EAttribute getVMType_MaxCores();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.planning.cloudprofile.VMType#getMinCores <em>Min Cores</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Cores</em>'.
	 * @see org.iobserve.planning.cloudprofile.VMType#getMinCores()
	 * @see #getVMType()
	 * @generated
	 */
	EAttribute getVMType_MinCores();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.planning.cloudprofile.VMType#getMinRAM <em>Min RAM</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min RAM</em>'.
	 * @see org.iobserve.planning.cloudprofile.VMType#getMinRAM()
	 * @see #getVMType()
	 * @generated
	 */
	EAttribute getVMType_MinRAM();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.planning.cloudprofile.VMType#getMaxRAM <em>Max RAM</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max RAM</em>'.
	 * @see org.iobserve.planning.cloudprofile.VMType#getMaxRAM()
	 * @see #getVMType()
	 * @generated
	 */
	EAttribute getVMType_MaxRAM();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.planning.cloudprofile.VMType#getMinProcessingRate <em>Min Processing Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Processing Rate</em>'.
	 * @see org.iobserve.planning.cloudprofile.VMType#getMinProcessingRate()
	 * @see #getVMType()
	 * @generated
	 */
	EAttribute getVMType_MinProcessingRate();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.planning.cloudprofile.VMType#getMaxProcessingRate <em>Max Processing Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Processing Rate</em>'.
	 * @see org.iobserve.planning.cloudprofile.VMType#getMaxProcessingRate()
	 * @see #getVMType()
	 * @generated
	 */
	EAttribute getVMType_MaxProcessingRate();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	cloudprofileFactory getcloudprofileFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.iobserve.planning.cloudprofile.impl.CloudProfileImpl <em>Cloud Profile</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.cloudprofile.impl.CloudProfileImpl
		 * @see org.iobserve.planning.cloudprofile.impl.cloudprofilePackageImpl#getCloudProfile()
		 * @generated
		 */
		EClass CLOUD_PROFILE = eINSTANCE.getCloudProfile();

		/**
		 * The meta object literal for the '<em><b>Cloud Providers</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLOUD_PROFILE__CLOUD_PROVIDERS = eINSTANCE.getCloudProfile_CloudProviders();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLOUD_PROFILE__NAME = eINSTANCE.getCloudProfile_Name();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.cloudprofile.impl.CloudProviderImpl <em>Cloud Provider</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.cloudprofile.impl.CloudProviderImpl
		 * @see org.iobserve.planning.cloudprofile.impl.cloudprofilePackageImpl#getCloudProvider()
		 * @generated
		 */
		EClass CLOUD_PROVIDER = eINSTANCE.getCloudProvider();

		/**
		 * The meta object literal for the '<em><b>Cloud Resources</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLOUD_PROVIDER__CLOUD_RESOURCES = eINSTANCE.getCloudProvider_CloudResources();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLOUD_PROVIDER__NAME = eINSTANCE.getCloudProvider_Name();

		/**
		 * The meta object literal for the '<em><b>Identity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLOUD_PROVIDER__IDENTITY = eINSTANCE.getCloudProvider_Identity();

		/**
		 * The meta object literal for the '<em><b>Credential</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLOUD_PROVIDER__CREDENTIAL = eINSTANCE.getCloudProvider_Credential();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.cloudprofile.impl.CloudResourceTypeImpl <em>Cloud Resource Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.cloudprofile.impl.CloudResourceTypeImpl
		 * @see org.iobserve.planning.cloudprofile.impl.cloudprofilePackageImpl#getCloudResourceType()
		 * @generated
		 */
		EClass CLOUD_RESOURCE_TYPE = eINSTANCE.getCloudResourceType();

		/**
		 * The meta object literal for the '<em><b>Price Per Hour</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLOUD_RESOURCE_TYPE__PRICE_PER_HOUR = eINSTANCE.getCloudResourceType_PricePerHour();

		/**
		 * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLOUD_RESOURCE_TYPE__LOCATION = eINSTANCE.getCloudResourceType_Location();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CLOUD_RESOURCE_TYPE__NAME = eINSTANCE.getCloudResourceType_Name();

		/**
		 * The meta object literal for the '<em><b>Provider</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLOUD_RESOURCE_TYPE__PROVIDER = eINSTANCE.getCloudResourceType_Provider();

		/**
		 * The meta object literal for the '{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl <em>VM Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.planning.cloudprofile.impl.VMTypeImpl
		 * @see org.iobserve.planning.cloudprofile.impl.cloudprofilePackageImpl#getVMType()
		 * @generated
		 */
		EClass VM_TYPE = eINSTANCE.getVMType();

		/**
		 * The meta object literal for the '<em><b>Max Cores</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VM_TYPE__MAX_CORES = eINSTANCE.getVMType_MaxCores();

		/**
		 * The meta object literal for the '<em><b>Min Cores</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VM_TYPE__MIN_CORES = eINSTANCE.getVMType_MinCores();

		/**
		 * The meta object literal for the '<em><b>Min RAM</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VM_TYPE__MIN_RAM = eINSTANCE.getVMType_MinRAM();

		/**
		 * The meta object literal for the '<em><b>Max RAM</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VM_TYPE__MAX_RAM = eINSTANCE.getVMType_MaxRAM();

		/**
		 * The meta object literal for the '<em><b>Min Processing Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VM_TYPE__MIN_PROCESSING_RATE = eINSTANCE.getVMType_MinProcessingRate();

		/**
		 * The meta object literal for the '<em><b>Max Processing Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VM_TYPE__MAX_PROCESSING_RATE = eINSTANCE.getVMType_MaxProcessingRate();

	}

} //cloudprofilePackage
