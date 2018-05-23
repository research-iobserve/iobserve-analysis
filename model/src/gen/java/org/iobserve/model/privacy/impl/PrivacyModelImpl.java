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
import org.iobserve.model.privacy.IPrivacyAnnotation;
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
 *   <li>{@link org.iobserve.model.privacy.impl.PrivacyModelImpl#getPrivacyLevels <em>Privacy Levels</em>}</li>
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
     * The cached value of the '{@link #getPrivacyLevels() <em>Privacy Levels</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPrivacyLevels()
     * @generated
     * @ordered
     */
    protected EList<IPrivacyAnnotation> privacyLevels;

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
    public EList<IPrivacyAnnotation> getPrivacyLevels() {
        if (privacyLevels == null) {
            privacyLevels = new EObjectContainmentEList<IPrivacyAnnotation>(IPrivacyAnnotation.class, this, PrivacyPackage.PRIVACY_MODEL__PRIVACY_LEVELS);
        }
        return privacyLevels;
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
            case PrivacyPackage.PRIVACY_MODEL__PRIVACY_LEVELS:
                return ((InternalEList<?>)getPrivacyLevels()).basicRemove(otherEnd, msgs);
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
            case PrivacyPackage.PRIVACY_MODEL__PRIVACY_LEVELS:
                return getPrivacyLevels();
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
            case PrivacyPackage.PRIVACY_MODEL__PRIVACY_LEVELS:
                getPrivacyLevels().clear();
                getPrivacyLevels().addAll((Collection<? extends IPrivacyAnnotation>)newValue);
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
            case PrivacyPackage.PRIVACY_MODEL__PRIVACY_LEVELS:
                getPrivacyLevels().clear();
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
            case PrivacyPackage.PRIVACY_MODEL__PRIVACY_LEVELS:
                return privacyLevels != null && !privacyLevels.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //PrivacyModelImpl
