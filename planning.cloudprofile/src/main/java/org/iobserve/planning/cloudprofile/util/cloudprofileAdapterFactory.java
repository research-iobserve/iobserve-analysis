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
package org.iobserve.planning.cloudprofile.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.iobserve.planning.cloudprofile.CloudProfile;
import org.iobserve.planning.cloudprofile.CloudProvider;
import org.iobserve.planning.cloudprofile.CloudResourceType;
import org.iobserve.planning.cloudprofile.VMType;
import org.iobserve.planning.cloudprofile.cloudprofilePackage;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter
 * <code>createXXX</code> method for each class of the model. <!-- end-user-doc -->
 * 
 * @see org.iobserve.planning.cloudprofile.cloudprofilePackage
 * @generated
 */
public class cloudprofileAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected static cloudprofilePackage modelPackage;

    /**
     * Creates an instance of the adapter factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public cloudprofileAdapterFactory() {
        if (cloudprofileAdapterFactory.modelPackage == null) {
            cloudprofileAdapterFactory.modelPackage = cloudprofilePackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object. <!-- begin-user-doc
     * --> This implementation returns <code>true</code> if the object is either the model's package
     * or is an instance object of the model. <!-- end-user-doc -->
     * 
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(final Object object) {
        if (object == cloudprofileAdapterFactory.modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject) object).eClass().getEPackage() == cloudprofileAdapterFactory.modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    protected cloudprofileSwitch<Adapter> modelSwitch = new cloudprofileSwitch<Adapter>() {
        @Override
        public Adapter caseCloudProfile(final CloudProfile object) {
            return cloudprofileAdapterFactory.this.createCloudProfileAdapter();
        }

        @Override
        public Adapter caseCloudProvider(final CloudProvider object) {
            return cloudprofileAdapterFactory.this.createCloudProviderAdapter();
        }

        @Override
        public Adapter caseCloudResourceType(final CloudResourceType object) {
            return cloudprofileAdapterFactory.this.createCloudResourceTypeAdapter();
        }

        @Override
        public Adapter caseVMType(final VMType object) {
            return cloudprofileAdapterFactory.this.createVMTypeAdapter();
        }

        @Override
        public Adapter defaultCase(final EObject object) {
            return cloudprofileAdapterFactory.this.createEObjectAdapter();
        }
    };

    /**
     * Creates an adapter for the <code>target</code>. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param target
     *            the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(final Notifier target) {
        return this.modelSwitch.doSwitch((EObject) target);
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.cloudprofile.CloudProfile <em>Cloud Profile</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.cloudprofile.CloudProfile
     * @generated
     */
    public Adapter createCloudProfileAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.cloudprofile.CloudProvider <em>Cloud Provider</em>}'. <!--
     * begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.cloudprofile.CloudProvider
     * @generated
     */
    public Adapter createCloudProviderAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.cloudprofile.CloudResourceType <em>Cloud Resource Type</em>}'.
     * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
     * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!--
     * end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.cloudprofile.CloudResourceType
     * @generated
     */
    public Adapter createCloudResourceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class
     * '{@link org.iobserve.planning.cloudprofile.VMType <em>VM Type</em>}'. <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases; it's useful to
     * ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @see org.iobserve.planning.cloudprofile.VMType
     * @generated
     */
    public Adapter createVMTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case. <!-- begin-user-doc --> This default
     * implementation returns null. <!-- end-user-doc -->
     * 
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} // cloudprofileAdapterFactory
