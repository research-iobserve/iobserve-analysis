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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Cloud Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.cloudprofile.CloudProfile#getCloudProviders <em>Cloud
 * Providers</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.CloudProfile#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudProfile()
 * @model
 * @generated
 */
public interface CloudProfile extends EObject {
    /**
     * Returns the value of the '<em><b>Cloud Providers</b></em>' containment reference list. The
     * list contents are of type {@link org.iobserve.planning.cloudprofile.CloudProvider}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cloud Providers</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Cloud Providers</em>' containment reference list.
     * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudProfile_CloudProviders()
     * @model containment="true"
     * @generated
     */
    EList<CloudProvider> getCloudProviders();

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudProfile_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.iobserve.planning.cloudprofile.CloudProfile#getName
     * <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

} // CloudProfile
