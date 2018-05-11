/**
 */
package org.iobserve.model.privacy.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.iobserve.model.privacy.GeoLocation;
import org.iobserve.model.privacy.ParameterPrivacy;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.privacy.PrivacyPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.privacy.impl.PrivacyModelImpl#getResourceContainerLocations <em>Resource Container Locations</em>}</li>
 *   <li>{@link org.iobserve.model.privacy.impl.PrivacyModelImpl#getParameterPrivacyLevels <em>Parameter Privacy Levels</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PrivacyModelImpl extends MinimalEObjectImpl.Container implements PrivacyModel {
    /**
     * The cached value of the '{@link #getResourceContainerLocations() <em>Resource Container Locations</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceContainerLocations()
     * @generated
     * @ordered
     */
    protected EList<GeoLocation> resourceContainerLocations;

    /**
     * The cached value of the '{@link #getParameterPrivacyLevels() <em>Parameter Privacy Levels</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameterPrivacyLevels()
     * @generated
     * @ordered
     */
    protected EList<ParameterPrivacy> parameterPrivacyLevels;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PrivacyModelImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PrivacyPackage.Literals.PRIVACY_MODEL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<GeoLocation> getResourceContainerLocations() {
        if (resourceContainerLocations == null) {
            resourceContainerLocations = new EObjectContainmentEList<GeoLocation>(GeoLocation.class, this, PrivacyPackage.PRIVACY_MODEL__RESOURCE_CONTAINER_LOCATIONS);
        }
        return resourceContainerLocations;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ParameterPrivacy> getParameterPrivacyLevels() {
        if (parameterPrivacyLevels == null) {
            parameterPrivacyLevels = new EObjectContainmentEList<ParameterPrivacy>(ParameterPrivacy.class, this, PrivacyPackage.PRIVACY_MODEL__PARAMETER_PRIVACY_LEVELS);
        }
        return parameterPrivacyLevels;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case PrivacyPackage.PRIVACY_MODEL__RESOURCE_CONTAINER_LOCATIONS:
                return ((InternalEList<?>)getResourceContainerLocations()).basicRemove(otherEnd, msgs);
            case PrivacyPackage.PRIVACY_MODEL__PARAMETER_PRIVACY_LEVELS:
                return ((InternalEList<?>)getParameterPrivacyLevels()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case PrivacyPackage.PRIVACY_MODEL__RESOURCE_CONTAINER_LOCATIONS:
                return getResourceContainerLocations();
            case PrivacyPackage.PRIVACY_MODEL__PARAMETER_PRIVACY_LEVELS:
                return getParameterPrivacyLevels();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case PrivacyPackage.PRIVACY_MODEL__RESOURCE_CONTAINER_LOCATIONS:
                getResourceContainerLocations().clear();
                getResourceContainerLocations().addAll((Collection<? extends GeoLocation>)newValue);
                return;
            case PrivacyPackage.PRIVACY_MODEL__PARAMETER_PRIVACY_LEVELS:
                getParameterPrivacyLevels().clear();
                getParameterPrivacyLevels().addAll((Collection<? extends ParameterPrivacy>)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case PrivacyPackage.PRIVACY_MODEL__RESOURCE_CONTAINER_LOCATIONS:
                getResourceContainerLocations().clear();
                return;
            case PrivacyPackage.PRIVACY_MODEL__PARAMETER_PRIVACY_LEVELS:
                getParameterPrivacyLevels().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case PrivacyPackage.PRIVACY_MODEL__RESOURCE_CONTAINER_LOCATIONS:
                return resourceContainerLocations != null && !resourceContainerLocations.isEmpty();
            case PrivacyPackage.PRIVACY_MODEL__PARAMETER_PRIVACY_LEVELS:
                return parameterPrivacyLevels != null && !parameterPrivacyLevels.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //PrivacyModelImpl
