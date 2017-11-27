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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.iobserve.planning.cloudprofile.CloudProvider;
import org.iobserve.planning.cloudprofile.CloudResourceType;
import org.iobserve.planning.cloudprofile.cloudprofilePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Cloud Provider</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.CloudProviderImpl#getCloudResources <em>Cloud
 * Resources</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.CloudProviderImpl#getName <em>Name</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.CloudProviderImpl#getIdentity
 * <em>Identity</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.CloudProviderImpl#getCredential
 * <em>Credential</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CloudProviderImpl extends MinimalEObjectImpl.Container implements CloudProvider {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected CloudProviderImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return cloudprofilePackage.Literals.CLOUD_PROVIDER;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected int eStaticFeatureCount() {
        return 0;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    @SuppressWarnings("unchecked")
    public EList<CloudResourceType> getCloudResources() {
        return (EList<CloudResourceType>) this.eGet(cloudprofilePackage.Literals.CLOUD_PROVIDER__CLOUD_RESOURCES, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getName() {
        return (String) this.eGet(cloudprofilePackage.Literals.CLOUD_PROVIDER__NAME, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setName(final String newName) {
        this.eSet(cloudprofilePackage.Literals.CLOUD_PROVIDER__NAME, newName);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getIdentity() {
        return (String) this.eGet(cloudprofilePackage.Literals.CLOUD_PROVIDER__IDENTITY, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setIdentity(final String newIdentity) {
        this.eSet(cloudprofilePackage.Literals.CLOUD_PROVIDER__IDENTITY, newIdentity);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getCredential() {
        return (String) this.eGet(cloudprofilePackage.Literals.CLOUD_PROVIDER__CREDENTIAL, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setCredential(final String newCredential) {
        this.eSet(cloudprofilePackage.Literals.CLOUD_PROVIDER__CREDENTIAL, newCredential);
    }

} // CloudProviderImpl
