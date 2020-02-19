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

import org.iobserve.model.privacy.DataProtectionModel;
import org.iobserve.model.privacy.EncapsulatedDataSource;
import org.iobserve.model.privacy.GeoLocation;
import org.iobserve.model.privacy.IDataProtectionAnnotation;
import org.iobserve.model.privacy.PrivacyPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Protection Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.privacy.impl.DataProtectionModelImpl#getResourceContainerLocations <em>Resource Container Locations</em>}</li>
 *   <li>{@link org.iobserve.model.privacy.impl.DataProtectionModelImpl#getPrivacyLevels <em>Privacy Levels</em>}</li>
 *   <li>{@link org.iobserve.model.privacy.impl.DataProtectionModelImpl#getEncapsulatedDataSources <em>Encapsulated Data Sources</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DataProtectionModelImpl extends MinimalEObjectImpl.Container implements DataProtectionModel {
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
     * The cached value of the '{@link #getDataProectionLevels() <em>Privacy Levels</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDataProectionLevels()
     * @generated
     * @ordered
     */
    protected EList<IDataProtectionAnnotation> privacyLevels;

    /**
     * The cached value of the '{@link #getEncapsulatedDataSources() <em>Encapsulated Data Sources</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEncapsulatedDataSources()
     * @generated
     * @ordered
     */
    protected EList<EncapsulatedDataSource> encapsulatedDataSources;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DataProtectionModelImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PrivacyPackage.Literals.DATA_PROTECTION_MODEL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<GeoLocation> getResourceContainerLocations() {
        if (resourceContainerLocations == null) {
            resourceContainerLocations = new EObjectContainmentEList<GeoLocation>(GeoLocation.class, this, PrivacyPackage.DATA_PROTECTION_MODEL__RESOURCE_CONTAINER_LOCATIONS);
        }
        return resourceContainerLocations;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<IDataProtectionAnnotation> getDataProectionLevels() {
        if (privacyLevels == null) {
            privacyLevels = new EObjectContainmentEList<IDataProtectionAnnotation>(IDataProtectionAnnotation.class, this, PrivacyPackage.DATA_PROTECTION_MODEL__PRIVACY_LEVELS);
        }
        return privacyLevels;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<EncapsulatedDataSource> getEncapsulatedDataSources() {
        if (encapsulatedDataSources == null) {
            encapsulatedDataSources = new EObjectContainmentEList<EncapsulatedDataSource>(EncapsulatedDataSource.class, this, PrivacyPackage.DATA_PROTECTION_MODEL__ENCAPSULATED_DATA_SOURCES);
        }
        return encapsulatedDataSources;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case PrivacyPackage.DATA_PROTECTION_MODEL__RESOURCE_CONTAINER_LOCATIONS:
                return ((InternalEList<?>)getResourceContainerLocations()).basicRemove(otherEnd, msgs);
            case PrivacyPackage.DATA_PROTECTION_MODEL__PRIVACY_LEVELS:
                return ((InternalEList<?>)getDataProectionLevels()).basicRemove(otherEnd, msgs);
            case PrivacyPackage.DATA_PROTECTION_MODEL__ENCAPSULATED_DATA_SOURCES:
                return ((InternalEList<?>)getEncapsulatedDataSources()).basicRemove(otherEnd, msgs);
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
            case PrivacyPackage.DATA_PROTECTION_MODEL__RESOURCE_CONTAINER_LOCATIONS:
                return getResourceContainerLocations();
            case PrivacyPackage.DATA_PROTECTION_MODEL__PRIVACY_LEVELS:
                return getDataProectionLevels();
            case PrivacyPackage.DATA_PROTECTION_MODEL__ENCAPSULATED_DATA_SOURCES:
                return getEncapsulatedDataSources();
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
            case PrivacyPackage.DATA_PROTECTION_MODEL__RESOURCE_CONTAINER_LOCATIONS:
                getResourceContainerLocations().clear();
                getResourceContainerLocations().addAll((Collection<? extends GeoLocation>)newValue);
                return;
            case PrivacyPackage.DATA_PROTECTION_MODEL__PRIVACY_LEVELS:
                getDataProectionLevels().clear();
                getDataProectionLevels().addAll((Collection<? extends IDataProtectionAnnotation>)newValue);
                return;
            case PrivacyPackage.DATA_PROTECTION_MODEL__ENCAPSULATED_DATA_SOURCES:
                getEncapsulatedDataSources().clear();
                getEncapsulatedDataSources().addAll((Collection<? extends EncapsulatedDataSource>)newValue);
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
            case PrivacyPackage.DATA_PROTECTION_MODEL__RESOURCE_CONTAINER_LOCATIONS:
                getResourceContainerLocations().clear();
                return;
            case PrivacyPackage.DATA_PROTECTION_MODEL__PRIVACY_LEVELS:
                getDataProectionLevels().clear();
                return;
            case PrivacyPackage.DATA_PROTECTION_MODEL__ENCAPSULATED_DATA_SOURCES:
                getEncapsulatedDataSources().clear();
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
            case PrivacyPackage.DATA_PROTECTION_MODEL__RESOURCE_CONTAINER_LOCATIONS:
                return resourceContainerLocations != null && !resourceContainerLocations.isEmpty();
            case PrivacyPackage.DATA_PROTECTION_MODEL__PRIVACY_LEVELS:
                return privacyLevels != null && !privacyLevels.isEmpty();
            case PrivacyPackage.DATA_PROTECTION_MODEL__ENCAPSULATED_DATA_SOURCES:
                return encapsulatedDataSources != null && !encapsulatedDataSources.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //DataProtectionModelImpl
