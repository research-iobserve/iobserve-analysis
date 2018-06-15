/**
 */
package org.iobserve.model.test.storage.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.iobserve.model.test.storage.Other;
import org.iobserve.model.test.storage.OtherInterface;
import org.iobserve.model.test.storage.SpecialA;
import org.iobserve.model.test.storage.StoragePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Special A</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.test.storage.impl.SpecialAImpl#getNext <em>Next</em>}</li>
 *   <li>{@link org.iobserve.model.test.storage.impl.SpecialAImpl#getRelate <em>Relate</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpecialAImpl extends MinimalEObjectImpl.Container implements SpecialA {
    /**
     * The cached value of the '{@link #getNext() <em>Next</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNext()
     * @generated
     * @ordered
     */
    protected OtherInterface next;
    /**
     * The cached value of the '{@link #getRelate() <em>Relate</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRelate()
     * @generated
     * @ordered
     */
    protected Other relate;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SpecialAImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return StoragePackage.Literals.SPECIAL_A;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OtherInterface getNext() {
        return next;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetNext(OtherInterface newNext, NotificationChain msgs) {
        OtherInterface oldNext = next;
        next = newNext;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StoragePackage.SPECIAL_A__NEXT, oldNext, newNext);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNext(OtherInterface newNext) {
        if (newNext != next) {
            NotificationChain msgs = null;
            if (next != null)
                msgs = ((InternalEObject)next).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StoragePackage.SPECIAL_A__NEXT, null, msgs);
            if (newNext != null)
                msgs = ((InternalEObject)newNext).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StoragePackage.SPECIAL_A__NEXT, null, msgs);
            msgs = basicSetNext(newNext, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, StoragePackage.SPECIAL_A__NEXT, newNext, newNext));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Other getRelate() {
        if (relate != null && relate.eIsProxy()) {
            InternalEObject oldRelate = (InternalEObject)relate;
            relate = (Other)eResolveProxy(oldRelate);
            if (relate != oldRelate) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, StoragePackage.SPECIAL_A__RELATE, oldRelate, relate));
            }
        }
        return relate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Other basicGetRelate() {
        return relate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRelate(Other newRelate) {
        Other oldRelate = relate;
        relate = newRelate;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, StoragePackage.SPECIAL_A__RELATE, oldRelate, relate));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case StoragePackage.SPECIAL_A__NEXT:
                return basicSetNext(null, msgs);
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
            case StoragePackage.SPECIAL_A__NEXT:
                return getNext();
            case StoragePackage.SPECIAL_A__RELATE:
                if (resolve) return getRelate();
                return basicGetRelate();
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
            case StoragePackage.SPECIAL_A__NEXT:
                setNext((OtherInterface)newValue);
                return;
            case StoragePackage.SPECIAL_A__RELATE:
                setRelate((Other)newValue);
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
            case StoragePackage.SPECIAL_A__NEXT:
                setNext((OtherInterface)null);
                return;
            case StoragePackage.SPECIAL_A__RELATE:
                setRelate((Other)null);
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
            case StoragePackage.SPECIAL_A__NEXT:
                return next != null;
            case StoragePackage.SPECIAL_A__RELATE:
                return relate != null;
        }
        return super.eIsSet(featureID);
    }

} //SpecialAImpl
