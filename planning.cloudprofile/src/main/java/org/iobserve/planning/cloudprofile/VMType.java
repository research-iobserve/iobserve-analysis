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
package org.iobserve.planning.cloudprofile;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>VM Type</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.cloudprofile.VMType#getMaxCores <em>Max Cores</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.VMType#getMinCores <em>Min Cores</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.VMType#getMinRAM <em>Min RAM</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.VMType#getMaxRAM <em>Max RAM</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.VMType#getMinProcessingRate <em>Min Processing
 * Rate</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.VMType#getMaxProcessingRate <em>Max Processing
 * Rate</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getVMType()
 * @model
 * @generated
 */
public interface VMType extends CloudResourceType {
    /**
     * Returns the value of the '<em><b>Max Cores</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Max Cores</em>' attribute isn't clear, there really should be more
     * of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Max Cores</em>' attribute.
     * @see #setMaxCores(int)
     * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getVMType_MaxCores()
     * @model
     * @generated
     */
    int getMaxCores();

    /**
     * Sets the value of the '{@link org.iobserve.planning.cloudprofile.VMType#getMaxCores <em>Max
     * Cores</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Max Cores</em>' attribute.
     * @see #getMaxCores()
     * @generated
     */
    void setMaxCores(int value);

    /**
     * Returns the value of the '<em><b>Min Cores</b></em>' attribute. The default value is
     * <code>"-1"</code>. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Min Cores</em>' attribute isn't clear, there really should be more
     * of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Min Cores</em>' attribute.
     * @see #setMinCores(int)
     * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getVMType_MinCores()
     * @model default="-1"
     * @generated
     */
    int getMinCores();

    /**
     * Sets the value of the '{@link org.iobserve.planning.cloudprofile.VMType#getMinCores <em>Min
     * Cores</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Min Cores</em>' attribute.
     * @see #getMinCores()
     * @generated
     */
    void setMinCores(int value);

    /**
     * Returns the value of the '<em><b>Min RAM</b></em>' attribute. The default value is
     * <code>"-1"</code>. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Min RAM</em>' attribute isn't clear, there really should be more
     * of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Min RAM</em>' attribute.
     * @see #setMinRAM(float)
     * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getVMType_MinRAM()
     * @model default="-1"
     * @generated
     */
    float getMinRAM();

    /**
     * Sets the value of the '{@link org.iobserve.planning.cloudprofile.VMType#getMinRAM <em>Min
     * RAM</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Min RAM</em>' attribute.
     * @see #getMinRAM()
     * @generated
     */
    void setMinRAM(float value);

    /**
     * Returns the value of the '<em><b>Max RAM</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Max RAM</em>' attribute isn't clear, there really should be more
     * of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Max RAM</em>' attribute.
     * @see #setMaxRAM(float)
     * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getVMType_MaxRAM()
     * @model
     * @generated
     */
    float getMaxRAM();

    /**
     * Sets the value of the '{@link org.iobserve.planning.cloudprofile.VMType#getMaxRAM <em>Max
     * RAM</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Max RAM</em>' attribute.
     * @see #getMaxRAM()
     * @generated
     */
    void setMaxRAM(float value);

    /**
     * Returns the value of the '<em><b>Min Processing Rate</b></em>' attribute. The default value
     * is <code>"0.0"</code>. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Min Processing Rate</em>' attribute isn't clear, there really
     * should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Min Processing Rate</em>' attribute.
     * @see #setMinProcessingRate(float)
     * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getVMType_MinProcessingRate()
     * @model default="0.0"
     * @generated
     */
    float getMinProcessingRate();

    /**
     * Sets the value of the '{@link org.iobserve.planning.cloudprofile.VMType#getMinProcessingRate
     * <em>Min Processing Rate</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Min Processing Rate</em>' attribute.
     * @see #getMinProcessingRate()
     * @generated
     */
    void setMinProcessingRate(float value);

    /**
     * Returns the value of the '<em><b>Max Processing Rate</b></em>' attribute. The default value
     * is <code>"0.0"</code>. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Max Processing Rate</em>' attribute isn't clear, there really
     * should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Max Processing Rate</em>' attribute.
     * @see #setMaxProcessingRate(float)
     * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getVMType_MaxProcessingRate()
     * @model default="0.0"
     * @generated
     */
    float getMaxProcessingRate();

    /**
     * Sets the value of the '{@link org.iobserve.planning.cloudprofile.VMType#getMaxProcessingRate
     * <em>Max Processing Rate</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Max Processing Rate</em>' attribute.
     * @see #getMaxProcessingRate()
     * @generated
     */
    void setMaxProcessingRate(float value);

} // VMType
