/**
 */
package org.iobserve.model.privacy.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.iobserve.model.privacy.EISOCode;
import org.iobserve.model.privacy.GeoLocation;
import org.iobserve.model.privacy.PrivacyPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Geo Location</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.privacy.impl.GeoLocationImpl#getIsocode <em>Isocode</em>}</li>
 *   <li>{@link org.iobserve.model.privacy.impl.GeoLocationImpl#getResourceContainer <em>Resource Container</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GeoLocationImpl extends MinimalEObjectImpl.Container implements GeoLocation {
    /**
     * The default value of the '{@link #getIsocode() <em>Isocode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIsocode()
     * @generated
     * @ordered
     */
    protected static final EISOCode ISOCODE_EDEFAULT = EISOCode.USA;

    /**
     * The cached value of the '{@link #getIsocode() <em>Isocode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIsocode()
     * @generated
     * @ordered
     */
    protected EISOCode isocode = ISOCODE_EDEFAULT;

    /**
     * The cached value of the '{@link #getResourceContainer() <em>Resource Container</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceContainer()
     * @generated
     * @ordered
     */
    protected ResourceContainer resourceContainer;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GeoLocationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PrivacyPackage.Literals.GEO_LOCATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EISOCode getIsocode() {
        return isocode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIsocode(EISOCode newIsocode) {
        EISOCode oldIsocode = isocode;
        isocode = newIsocode == null ? ISOCODE_EDEFAULT : newIsocode;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PrivacyPackage.GEO_LOCATION__ISOCODE, oldIsocode, isocode));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResourceContainer getResourceContainer() {
        if (resourceContainer != null && ((EObject)resourceContainer).eIsProxy()) {
            InternalEObject oldResourceContainer = (InternalEObject)resourceContainer;
            resourceContainer = (ResourceContainer)eResolveProxy(oldResourceContainer);
            if (resourceContainer != oldResourceContainer) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, PrivacyPackage.GEO_LOCATION__RESOURCE_CONTAINER, oldResourceContainer, resourceContainer));
            }
        }
        return resourceContainer;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResourceContainer basicGetResourceContainer() {
        return resourceContainer;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResourceContainer(ResourceContainer newResourceContainer) {
        ResourceContainer oldResourceContainer = resourceContainer;
        resourceContainer = newResourceContainer;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PrivacyPackage.GEO_LOCATION__RESOURCE_CONTAINER, oldResourceContainer, resourceContainer));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case PrivacyPackage.GEO_LOCATION__ISOCODE:
                return getIsocode();
            case PrivacyPackage.GEO_LOCATION__RESOURCE_CONTAINER:
                if (resolve) return getResourceContainer();
                return basicGetResourceContainer();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case PrivacyPackage.GEO_LOCATION__ISOCODE:
                setIsocode((EISOCode)newValue);
                return;
            case PrivacyPackage.GEO_LOCATION__RESOURCE_CONTAINER:
                setResourceContainer((ResourceContainer)newValue);
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
            case PrivacyPackage.GEO_LOCATION__ISOCODE:
                setIsocode(ISOCODE_EDEFAULT);
                return;
            case PrivacyPackage.GEO_LOCATION__RESOURCE_CONTAINER:
                setResourceContainer((ResourceContainer)null);
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
            case PrivacyPackage.GEO_LOCATION__ISOCODE:
                return isocode != ISOCODE_EDEFAULT;
            case PrivacyPackage.GEO_LOCATION__RESOURCE_CONTAINER:
                return resourceContainer != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (isocode: ");
        result.append(isocode);
        result.append(')');
        return result.toString();
    }

} //GeoLocationImpl
