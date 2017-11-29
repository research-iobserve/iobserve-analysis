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
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class cloudprofilePackageImpl extends EPackageImpl implements cloudprofilePackage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass cloudProfileEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass cloudProviderEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass cloudResourceTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private EClass vmTypeEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI
     * value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init
     * init()}, which also performs initialization of the package, or returns the registered
     * package, if one already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#eNS_URI
     * @see #init()
     * @generated
     */
    private cloudprofilePackageImpl() {
        super(cloudprofilePackage.eNS_URI, cloudprofileFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others
     * upon which it depends.
     * 
     * <p>
     * This method is used to initialize {@link cloudprofilePackage#eINSTANCE} when that field is
     * accessed. Clients should not invoke it directly. Instead, they should simply access that
     * field to obtain the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static cloudprofilePackage init() {
        if (cloudprofilePackageImpl.isInited) {
            return (cloudprofilePackage) EPackage.Registry.INSTANCE.getEPackage(cloudprofilePackage.eNS_URI);
        }

        // Obtain or create and register package
        final cloudprofilePackageImpl thecloudprofilePackage = (cloudprofilePackageImpl) (EPackage.Registry.INSTANCE
                .get(cloudprofilePackage.eNS_URI) instanceof cloudprofilePackageImpl
                        ? EPackage.Registry.INSTANCE.get(cloudprofilePackage.eNS_URI)
                        : new cloudprofilePackageImpl());

        cloudprofilePackageImpl.isInited = true;

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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getCloudProfile() {
        return this.cloudProfileEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getCloudProfile_CloudProviders() {
        return (EReference) this.cloudProfileEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getCloudProfile_Name() {
        return (EAttribute) this.cloudProfileEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getCloudProvider() {
        return this.cloudProviderEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getCloudProvider_CloudResources() {
        return (EReference) this.cloudProviderEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getCloudProvider_Name() {
        return (EAttribute) this.cloudProviderEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getCloudProvider_Identity() {
        return (EAttribute) this.cloudProviderEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getCloudProvider_Credential() {
        return (EAttribute) this.cloudProviderEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getCloudResourceType() {
        return this.cloudResourceTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getCloudResourceType_PricePerHour() {
        return (EAttribute) this.cloudResourceTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getCloudResourceType_Location() {
        return (EAttribute) this.cloudResourceTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getCloudResourceType_Name() {
        return (EAttribute) this.cloudResourceTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EReference getCloudResourceType_Provider() {
        return (EReference) this.cloudResourceTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EClass getVMType() {
        return this.vmTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getVMType_MaxCores() {
        return (EAttribute) this.vmTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getVMType_MinCores() {
        return (EAttribute) this.vmTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getVMType_MinRAM() {
        return (EAttribute) this.vmTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getVMType_MaxRAM() {
        return (EAttribute) this.vmTypeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getVMType_MinProcessingRate() {
        return (EAttribute) this.vmTypeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EAttribute getVMType_MaxProcessingRate() {
        return (EAttribute) this.vmTypeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public cloudprofileFactory getcloudprofileFactory() {
        return (cloudprofileFactory) this.getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package. This method is guarded to have no affect on
     * any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void createPackageContents() {
        if (this.isCreated) {
            return;
        }
        this.isCreated = true;

        // Create classes and their features
        this.cloudProfileEClass = this.createEClass(cloudprofilePackage.CLOUD_PROFILE);
        this.createEReference(this.cloudProfileEClass, cloudprofilePackage.CLOUD_PROFILE__CLOUD_PROVIDERS);
        this.createEAttribute(this.cloudProfileEClass, cloudprofilePackage.CLOUD_PROFILE__NAME);

        this.cloudProviderEClass = this.createEClass(cloudprofilePackage.CLOUD_PROVIDER);
        this.createEReference(this.cloudProviderEClass, cloudprofilePackage.CLOUD_PROVIDER__CLOUD_RESOURCES);
        this.createEAttribute(this.cloudProviderEClass, cloudprofilePackage.CLOUD_PROVIDER__NAME);
        this.createEAttribute(this.cloudProviderEClass, cloudprofilePackage.CLOUD_PROVIDER__IDENTITY);
        this.createEAttribute(this.cloudProviderEClass, cloudprofilePackage.CLOUD_PROVIDER__CREDENTIAL);

        this.cloudResourceTypeEClass = this.createEClass(cloudprofilePackage.CLOUD_RESOURCE_TYPE);
        this.createEAttribute(this.cloudResourceTypeEClass, cloudprofilePackage.CLOUD_RESOURCE_TYPE__PRICE_PER_HOUR);
        this.createEAttribute(this.cloudResourceTypeEClass, cloudprofilePackage.CLOUD_RESOURCE_TYPE__LOCATION);
        this.createEAttribute(this.cloudResourceTypeEClass, cloudprofilePackage.CLOUD_RESOURCE_TYPE__NAME);
        this.createEReference(this.cloudResourceTypeEClass, cloudprofilePackage.CLOUD_RESOURCE_TYPE__PROVIDER);

        this.vmTypeEClass = this.createEClass(cloudprofilePackage.VM_TYPE);
        this.createEAttribute(this.vmTypeEClass, cloudprofilePackage.VM_TYPE__MAX_CORES);
        this.createEAttribute(this.vmTypeEClass, cloudprofilePackage.VM_TYPE__MIN_CORES);
        this.createEAttribute(this.vmTypeEClass, cloudprofilePackage.VM_TYPE__MIN_RAM);
        this.createEAttribute(this.vmTypeEClass, cloudprofilePackage.VM_TYPE__MAX_RAM);
        this.createEAttribute(this.vmTypeEClass, cloudprofilePackage.VM_TYPE__MIN_PROCESSING_RATE);
        this.createEAttribute(this.vmTypeEClass, cloudprofilePackage.VM_TYPE__MAX_PROCESSING_RATE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model. This method is guarded to have
     * no affect on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void initializePackageContents() {
        if (this.isInitialized) {
            return;
        }
        this.isInitialized = true;

        // Initialize package
        this.setName(cloudprofilePackage.eNAME);
        this.setNsPrefix(cloudprofilePackage.eNS_PREFIX);
        this.setNsURI(cloudprofilePackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        this.vmTypeEClass.getESuperTypes().add(this.getCloudResourceType());

        // Initialize classes and features; add operations and parameters
        this.initEClass(this.cloudProfileEClass, CloudProfile.class, "CloudProfile", !EPackageImpl.IS_ABSTRACT,
                !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getCloudProfile_CloudProviders(), this.getCloudProvider(), null, "cloudProviders",
                null, 0, -1, CloudProfile.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, EPackageImpl.IS_COMPOSITE, !EPackageImpl.IS_RESOLVE_PROXIES,
                !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);
        this.initEAttribute(this.getCloudProfile_Name(), this.ecorePackage.getEString(), "name", null, 0, 1,
                CloudProfile.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE,
                !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED,
                EPackageImpl.IS_ORDERED);

        this.initEClass(this.cloudProviderEClass, CloudProvider.class, "CloudProvider", !EPackageImpl.IS_ABSTRACT,
                !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getCloudProvider_CloudResources(), this.getCloudResourceType(), null, "cloudResources",
                null, 0, -1, CloudProvider.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, EPackageImpl.IS_COMPOSITE, !EPackageImpl.IS_RESOLVE_PROXIES,
                !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);
        this.initEAttribute(this.getCloudProvider_Name(), this.ecorePackage.getEString(), "name", null, 0, 1,
                CloudProvider.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE,
                !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED,
                EPackageImpl.IS_ORDERED);
        this.initEAttribute(this.getCloudProvider_Identity(), this.ecorePackage.getEString(), "identity", null, 0, 1,
                CloudProvider.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE,
                !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED,
                EPackageImpl.IS_ORDERED);
        this.initEAttribute(this.getCloudProvider_Credential(), this.ecorePackage.getEString(), "credential", null, 0,
                1, CloudProvider.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE,
                !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.cloudResourceTypeEClass, CloudResourceType.class, "CloudResourceType",
                EPackageImpl.IS_ABSTRACT, !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getCloudResourceType_PricePerHour(), this.ecorePackage.getEDouble(), "pricePerHour",
                null, 0, 1, CloudResourceType.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE,
                !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);
        this.initEAttribute(this.getCloudResourceType_Location(), this.ecorePackage.getEString(), "location", "-1", 0,
                1, CloudResourceType.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE,
                !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);
        this.initEAttribute(this.getCloudResourceType_Name(), this.ecorePackage.getEString(), "name", null, 0, 1,
                CloudResourceType.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE,
                !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);
        this.initEReference(this.getCloudResourceType_Provider(), this.getCloudProvider(), null, "provider", null, 1, 1,
                CloudResourceType.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_COMPOSITE, EPackageImpl.IS_RESOLVE_PROXIES,
                !EPackageImpl.IS_UNSETTABLE, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        this.initEClass(this.vmTypeEClass, VMType.class, "VMType", !EPackageImpl.IS_ABSTRACT,
                !EPackageImpl.IS_INTERFACE, EPackageImpl.IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getVMType_MaxCores(), this.ecorePackage.getEInt(), "maxCores", null, 0, 1,
                VMType.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE,
                !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED,
                EPackageImpl.IS_ORDERED);
        this.initEAttribute(this.getVMType_MinCores(), this.ecorePackage.getEInt(), "minCores", "-1", 0, 1,
                VMType.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE,
                !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED,
                EPackageImpl.IS_ORDERED);
        this.initEAttribute(this.getVMType_MinRAM(), this.ecorePackage.getEFloat(), "minRAM", "-1", 0, 1, VMType.class,
                !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE,
                !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED,
                EPackageImpl.IS_ORDERED);
        this.initEAttribute(this.getVMType_MaxRAM(), this.ecorePackage.getEFloat(), "maxRAM", null, 0, 1, VMType.class,
                !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE, EPackageImpl.IS_CHANGEABLE,
                !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE, !EPackageImpl.IS_DERIVED,
                EPackageImpl.IS_ORDERED);
        this.initEAttribute(this.getVMType_MinProcessingRate(), this.ecorePackage.getEFloat(), "minProcessingRate",
                "0.0", 0, 1, VMType.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE,
                !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);
        this.initEAttribute(this.getVMType_MaxProcessingRate(), this.ecorePackage.getEFloat(), "maxProcessingRate",
                "0.0", 0, 1, VMType.class, !EPackageImpl.IS_TRANSIENT, !EPackageImpl.IS_VOLATILE,
                EPackageImpl.IS_CHANGEABLE, !EPackageImpl.IS_UNSETTABLE, !EPackageImpl.IS_ID, EPackageImpl.IS_UNIQUE,
                !EPackageImpl.IS_DERIVED, EPackageImpl.IS_ORDERED);

        // Create resource
        this.createResource(cloudprofilePackage.eNS_URI);
    }

} // cloudprofilePackageImpl
