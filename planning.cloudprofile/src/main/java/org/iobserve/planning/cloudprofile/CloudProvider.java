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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Cloud Provider</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.iobserve.planning.cloudprofile.CloudProvider#getCloudResources <em>Cloud
 * Resources</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.CloudProvider#getName <em>Name</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.CloudProvider#getIdentity <em>Identity</em>}</li>
 * <li>{@link org.iobserve.planning.cloudprofile.CloudProvider#getCredential
 * <em>Credential</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudProvider()
 * @model
 * @generated
 */
public interface CloudProvider extends EObject {
    /**
     * Returns the value of the '<em><b>Cloud Resources</b></em>' containment reference list. The
     * list contents are of type {@link org.iobserve.planning.cloudprofile.CloudResourceType}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cloud Resources</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Cloud Resources</em>' containment reference list.
     * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudProvider_CloudResources()
     * @model containment="true"
     * @generated
     */
    EList<CloudResourceType> getCloudResources();

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
     * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudProvider_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.iobserve.planning.cloudprofile.CloudProvider#getName
     * <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Identity</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Identity</em>' attribute isn't clear, there really should be more
     * of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Identity</em>' attribute.
     * @see #setIdentity(String)
     * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudProvider_Identity()
     * @model
     * @generated
     */
    String getIdentity();

    /**
     * Sets the value of the '{@link org.iobserve.planning.cloudprofile.CloudProvider#getIdentity
     * <em>Identity</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Identity</em>' attribute.
     * @see #getIdentity()
     * @generated
     */
    void setIdentity(String value);

    /**
     * Returns the value of the '<em><b>Credential</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Credential</em>' attribute isn't clear, there really should be
     * more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Credential</em>' attribute.
     * @see #setCredential(String)
     * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudProvider_Credential()
     * @model
     * @generated
     */
    String getCredential();

    /**
     * Sets the value of the '{@link org.iobserve.planning.cloudprofile.CloudProvider#getCredential
     * <em>Credential</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Credential</em>' attribute.
     * @see #getCredential()
     * @generated
     */
    void setCredential(String value);

} // CloudProvider
