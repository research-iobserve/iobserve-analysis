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
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.iobserve.planning.cloudprofile.CloudProvider;
import org.iobserve.planning.cloudprofile.CloudResourceType;
import org.iobserve.planning.cloudprofile.cloudprofilePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Cloud Resource
 * Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.CloudResourceTypeImpl#getPricePerHour
 * <em>Price Per Hour</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.CloudResourceTypeImpl#getLocation
 * <em>Location</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.CloudResourceTypeImpl#getName
 * <em>Name</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.CloudResourceTypeImpl#getProvider
 * <em>Provider</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class CloudResourceTypeImpl extends MinimalEObjectImpl.Container implements CloudResourceType {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected CloudResourceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE;
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
    public double getPricePerHour() {
        return (Double) this.eGet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__PRICE_PER_HOUR, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setPricePerHour(final double newPricePerHour) {
        this.eSet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__PRICE_PER_HOUR, newPricePerHour);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getLocation() {
        return (String) this.eGet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__LOCATION, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setLocation(final String newLocation) {
        this.eSet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__LOCATION, newLocation);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getName() {
        return (String) this.eGet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__NAME, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setName(final String newName) {
        this.eSet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__NAME, newName);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CloudProvider getProvider() {
        return (CloudProvider) this.eGet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__PROVIDER, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setProvider(final CloudProvider newProvider) {
        this.eSet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__PROVIDER, newProvider);
    }

} // CloudResourceTypeImpl
