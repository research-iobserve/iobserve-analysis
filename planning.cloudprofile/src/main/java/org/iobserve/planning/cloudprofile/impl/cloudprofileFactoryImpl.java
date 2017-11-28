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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.iobserve.planning.cloudprofile.CloudProfile;
import org.iobserve.planning.cloudprofile.CloudProvider;
import org.iobserve.planning.cloudprofile.VMType;
import org.iobserve.planning.cloudprofile.cloudprofileFactory;
import org.iobserve.planning.cloudprofile.cloudprofilePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class cloudprofileFactoryImpl extends EFactoryImpl implements cloudprofileFactory {
    /**
     * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public static cloudprofileFactory init() {
        try {
            final cloudprofileFactory thecloudprofileFactory = (cloudprofileFactory) EPackage.Registry.INSTANCE
                    .getEFactory(cloudprofilePackage.eNS_URI);
            if (thecloudprofileFactory != null) {
                return thecloudprofileFactory;
            }
        } catch (final Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new cloudprofileFactoryImpl();
    }

    /**
     * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public cloudprofileFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EObject create(final EClass eClass) {
        switch (eClass.getClassifierID()) {
        case cloudprofilePackage.CLOUD_PROFILE:
            return this.createCloudProfile();
        case cloudprofilePackage.CLOUD_PROVIDER:
            return this.createCloudProvider();
        case cloudprofilePackage.VM_TYPE:
            return this.createVMType();
        default:
            throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public CloudProfile createCloudProfile() {
        final CloudProfileImpl cloudProfile = new CloudProfileImpl();
        return cloudProfile;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public CloudProvider createCloudProvider() {
        final CloudProviderImpl cloudProvider = new CloudProviderImpl();
        return cloudProvider;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public VMType createVMType() {
        final VMTypeImpl vmType = new VMTypeImpl();
        return vmType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public cloudprofilePackage getcloudprofilePackage() {
        return (cloudprofilePackage) this.getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @deprecated
     * @generated
     */
    @Deprecated
    public static cloudprofilePackage getPackage() {
        return cloudprofilePackage.eINSTANCE;
    }

} // cloudprofileFactoryImpl
