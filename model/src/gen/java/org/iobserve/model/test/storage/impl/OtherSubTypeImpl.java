/**
 */
package org.iobserve.model.test.storage.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.iobserve.model.test.storage.Other;
import org.iobserve.model.test.storage.OtherSubType;
import org.iobserve.model.test.storage.StoragePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Other Sub Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.test.storage.impl.OtherSubTypeImpl#isLabel <em>Label</em>}</li>
 *   <li>{@link org.iobserve.model.test.storage.impl.OtherSubTypeImpl#getOther <em>Other</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OtherSubTypeImpl extends OtherImpl implements OtherSubType {
    /**
     * The default value of the '{@link #isLabel() <em>Label</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLabel()
     * @generated
     * @ordered
     */
    protected static final boolean LABEL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isLabel() <em>Label</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLabel()
     * @generated
     * @ordered
     */
    protected boolean label = LABEL_EDEFAULT;

    /**
     * The cached value of the '{@link #getOther() <em>Other</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOther()
     * @generated
     * @ordered
     */
    protected Other other;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OtherSubTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return StoragePackage.Literals.OTHER_SUB_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isLabel() {
        return label;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLabel(boolean newLabel) {
        boolean oldLabel = label;
        label = newLabel;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, StoragePackage.OTHER_SUB_TYPE__LABEL, oldLabel, label));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Other getOther() {
        return other;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOther(Other newOther, NotificationChain msgs) {
        Other oldOther = other;
        other = newOther;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StoragePackage.OTHER_SUB_TYPE__OTHER, oldOther, newOther);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOther(Other newOther) {
        if (newOther != other) {
            NotificationChain msgs = null;
            if (other != null)
                msgs = ((InternalEObject)other).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StoragePackage.OTHER_SUB_TYPE__OTHER, null, msgs);
            if (newOther != null)
                msgs = ((InternalEObject)newOther).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StoragePackage.OTHER_SUB_TYPE__OTHER, null, msgs);
            msgs = basicSetOther(newOther, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, StoragePackage.OTHER_SUB_TYPE__OTHER, newOther, newOther));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case StoragePackage.OTHER_SUB_TYPE__OTHER:
                return basicSetOther(null, msgs);
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
            case StoragePackage.OTHER_SUB_TYPE__LABEL:
                return isLabel();
            case StoragePackage.OTHER_SUB_TYPE__OTHER:
                return getOther();
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
            case StoragePackage.OTHER_SUB_TYPE__LABEL:
                setLabel((Boolean)newValue);
                return;
            case StoragePackage.OTHER_SUB_TYPE__OTHER:
                setOther((Other)newValue);
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
            case StoragePackage.OTHER_SUB_TYPE__LABEL:
                setLabel(LABEL_EDEFAULT);
                return;
            case StoragePackage.OTHER_SUB_TYPE__OTHER:
                setOther((Other)null);
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
            case StoragePackage.OTHER_SUB_TYPE__LABEL:
                return label != LABEL_EDEFAULT;
            case StoragePackage.OTHER_SUB_TYPE__OTHER:
                return other != null;
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
        result.append(" (label: ");
        result.append(label);
        result.append(')');
        return result.toString();
    }

} //OtherSubTypeImpl
