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
import org.iobserve.planning.cloudprofile.VMType;
import org.iobserve.planning.cloudprofile.cloudprofilePackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>VM Type</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl#getMaxCores <em>Max
 * Cores</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl#getMinCores <em>Min
 * Cores</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl#getMinRAM <em>Min RAM</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl#getMaxRAM <em>Max RAM</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl#getMinProcessingRate <em>Min
 * Processing Rate</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl#getMaxProcessingRate <em>Max
 * Processing Rate</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VMTypeImpl extends CloudResourceTypeImpl implements VMType {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected VMTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return cloudprofilePackage.Literals.VM_TYPE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public int getMaxCores() {
        return (Integer) this.eGet(cloudprofilePackage.Literals.VM_TYPE__MAX_CORES, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setMaxCores(final int newMaxCores) {
        this.eSet(cloudprofilePackage.Literals.VM_TYPE__MAX_CORES, newMaxCores);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public int getMinCores() {
        return (Integer) this.eGet(cloudprofilePackage.Literals.VM_TYPE__MIN_CORES, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setMinCores(final int newMinCores) {
        this.eSet(cloudprofilePackage.Literals.VM_TYPE__MIN_CORES, newMinCores);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public float getMinRAM() {
        return (Float) this.eGet(cloudprofilePackage.Literals.VM_TYPE__MIN_RAM, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setMinRAM(final float newMinRAM) {
        this.eSet(cloudprofilePackage.Literals.VM_TYPE__MIN_RAM, newMinRAM);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public float getMaxRAM() {
        return (Float) this.eGet(cloudprofilePackage.Literals.VM_TYPE__MAX_RAM, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setMaxRAM(final float newMaxRAM) {
        this.eSet(cloudprofilePackage.Literals.VM_TYPE__MAX_RAM, newMaxRAM);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public float getMinProcessingRate() {
        return (Float) this.eGet(cloudprofilePackage.Literals.VM_TYPE__MIN_PROCESSING_RATE, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setMinProcessingRate(final float newMinProcessingRate) {
        this.eSet(cloudprofilePackage.Literals.VM_TYPE__MIN_PROCESSING_RATE, newMinProcessingRate);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public float getMaxProcessingRate() {
        return (Float) this.eGet(cloudprofilePackage.Literals.VM_TYPE__MAX_PROCESSING_RATE, true);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setMaxProcessingRate(final float newMaxProcessingRate) {
        this.eSet(cloudprofilePackage.Literals.VM_TYPE__MAX_PROCESSING_RATE, newMaxProcessingRate);
    }

} // VMTypeImpl
