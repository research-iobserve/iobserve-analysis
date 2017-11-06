/**
 */
package org.iobserve.planning.cloudprofile.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.iobserve.planning.cloudprofile.CloudProfile;
import org.iobserve.planning.cloudprofile.CloudProvider;
import org.iobserve.planning.cloudprofile.CloudResourceType;
import org.iobserve.planning.cloudprofile.VMType;
import org.iobserve.planning.cloudprofile.cloudprofileFactory;
import org.iobserve.planning.cloudprofile.cloudprofilePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class cloudprofilePackageImpl extends EPackageImpl implements cloudprofilePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cloudProfileEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cloudProviderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass cloudResourceTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vmTypeEClass = null;

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
	 * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private cloudprofilePackageImpl() {
		super(eNS_URI, cloudprofileFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link cloudprofilePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static cloudprofilePackage init() {
		if (isInited) return (cloudprofilePackage)EPackage.Registry.INSTANCE.getEPackage(cloudprofilePackage.eNS_URI);

		// Obtain or create and register package
		cloudprofilePackageImpl thecloudprofilePackage = (cloudprofilePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof cloudprofilePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new cloudprofilePackageImpl());

		isInited = true;

		// Create package meta-data objects
		thecloudprofilePackage.createPackageContents();

		// Initialize created meta-data
		thecloudprofilePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thecloudprofilePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(cloudprofilePackage.eNS_URI, thecloudprofilePackage);
		return thecloudprofilePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCloudProfile() {
		return cloudProfileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCloudProfile_CloudProviders() {
		return (EReference)cloudProfileEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCloudProfile_Name() {
		return (EAttribute)cloudProfileEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCloudProvider() {
		return cloudProviderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCloudProvider_CloudResources() {
		return (EReference)cloudProviderEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCloudProvider_Name() {
		return (EAttribute)cloudProviderEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCloudProvider_Identity() {
		return (EAttribute)cloudProviderEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCloudProvider_Credential() {
		return (EAttribute)cloudProviderEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCloudResourceType() {
		return cloudResourceTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCloudResourceType_PricePerHour() {
		return (EAttribute)cloudResourceTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCloudResourceType_Location() {
		return (EAttribute)cloudResourceTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCloudResourceType_Name() {
		return (EAttribute)cloudResourceTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCloudResourceType_Provider() {
		return (EReference)cloudResourceTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVMType() {
		return vmTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVMType_MaxCores() {
		return (EAttribute)vmTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVMType_MinCores() {
		return (EAttribute)vmTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVMType_MinRAM() {
		return (EAttribute)vmTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVMType_MaxRAM() {
		return (EAttribute)vmTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVMType_MinProcessingRate() {
		return (EAttribute)vmTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVMType_MaxProcessingRate() {
		return (EAttribute)vmTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public cloudprofileFactory getcloudprofileFactory() {
		return (cloudprofileFactory)getEFactoryInstance();
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
		cloudProfileEClass = createEClass(CLOUD_PROFILE);
		createEReference(cloudProfileEClass, CLOUD_PROFILE__CLOUD_PROVIDERS);
		createEAttribute(cloudProfileEClass, CLOUD_PROFILE__NAME);

		cloudProviderEClass = createEClass(CLOUD_PROVIDER);
		createEReference(cloudProviderEClass, CLOUD_PROVIDER__CLOUD_RESOURCES);
		createEAttribute(cloudProviderEClass, CLOUD_PROVIDER__NAME);
		createEAttribute(cloudProviderEClass, CLOUD_PROVIDER__IDENTITY);
		createEAttribute(cloudProviderEClass, CLOUD_PROVIDER__CREDENTIAL);

		cloudResourceTypeEClass = createEClass(CLOUD_RESOURCE_TYPE);
		createEAttribute(cloudResourceTypeEClass, CLOUD_RESOURCE_TYPE__PRICE_PER_HOUR);
		createEAttribute(cloudResourceTypeEClass, CLOUD_RESOURCE_TYPE__LOCATION);
		createEAttribute(cloudResourceTypeEClass, CLOUD_RESOURCE_TYPE__NAME);
		createEReference(cloudResourceTypeEClass, CLOUD_RESOURCE_TYPE__PROVIDER);

		vmTypeEClass = createEClass(VM_TYPE);
		createEAttribute(vmTypeEClass, VM_TYPE__MAX_CORES);
		createEAttribute(vmTypeEClass, VM_TYPE__MIN_CORES);
		createEAttribute(vmTypeEClass, VM_TYPE__MIN_RAM);
		createEAttribute(vmTypeEClass, VM_TYPE__MAX_RAM);
		createEAttribute(vmTypeEClass, VM_TYPE__MIN_PROCESSING_RATE);
		createEAttribute(vmTypeEClass, VM_TYPE__MAX_PROCESSING_RATE);
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
		vmTypeEClass.getESuperTypes().add(this.getCloudResourceType());

		// Initialize classes and features; add operations and parameters
		initEClass(cloudProfileEClass, CloudProfile.class, "CloudProfile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCloudProfile_CloudProviders(), this.getCloudProvider(), null, "cloudProviders", null, 0, -1, CloudProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCloudProfile_Name(), ecorePackage.getEString(), "name", null, 0, 1, CloudProfile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cloudProviderEClass, CloudProvider.class, "CloudProvider", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCloudProvider_CloudResources(), this.getCloudResourceType(), null, "cloudResources", null, 0, -1, CloudProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCloudProvider_Name(), ecorePackage.getEString(), "name", null, 0, 1, CloudProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCloudProvider_Identity(), ecorePackage.getEString(), "identity", null, 0, 1, CloudProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCloudProvider_Credential(), ecorePackage.getEString(), "credential", null, 0, 1, CloudProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(cloudResourceTypeEClass, CloudResourceType.class, "CloudResourceType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCloudResourceType_PricePerHour(), ecorePackage.getEDouble(), "pricePerHour", null, 0, 1, CloudResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCloudResourceType_Location(), ecorePackage.getEString(), "location", "-1", 0, 1, CloudResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCloudResourceType_Name(), ecorePackage.getEString(), "name", null, 0, 1, CloudResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCloudResourceType_Provider(), this.getCloudProvider(), null, "provider", null, 1, 1, CloudResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vmTypeEClass, VMType.class, "VMType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVMType_MaxCores(), ecorePackage.getEInt(), "maxCores", null, 0, 1, VMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVMType_MinCores(), ecorePackage.getEInt(), "minCores", "-1", 0, 1, VMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVMType_MinRAM(), ecorePackage.getEFloat(), "minRAM", "-1", 0, 1, VMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVMType_MaxRAM(), ecorePackage.getEFloat(), "maxRAM", null, 0, 1, VMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVMType_MinProcessingRate(), ecorePackage.getEFloat(), "minProcessingRate", "0.0", 0, 1, VMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVMType_MaxProcessingRate(), ecorePackage.getEFloat(), "maxProcessingRate", "0.0", 0, 1, VMType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //cloudprofilePackageImpl
