/**
 */
package org.iobserve.model.correspondence.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.iobserve.model.correspondence.AbstractEntry;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.Part;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Part</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.impl.PartImpl#getModelType <em>Model Type</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence.impl.PartImpl#getEntries <em>Entries</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PartImpl extends MinimalEObjectImpl.Container implements Part {
    /**
     * The cached value of the '{@link #getModelType() <em>Model Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getModelType()
     * @generated
     * @ordered
     */
    protected EObject modelType;

    /**
     * The cached value of the '{@link #getEntries() <em>Entries</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEntries()
     * @generated
     * @ordered
     */
    protected EList<AbstractEntry> entries;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PartImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return CorrespondencePackage.Literals.PART;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getModelType() {
        if (modelType != null && modelType.eIsProxy()) {
            InternalEObject oldModelType = (InternalEObject)modelType;
            modelType = eResolveProxy(oldModelType);
            if (modelType != oldModelType) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, CorrespondencePackage.PART__MODEL_TYPE, oldModelType, modelType));
            }
        }
        return modelType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject basicGetModelType() {
        return modelType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setModelType(EObject newModelType) {
        EObject oldModelType = modelType;
        modelType = newModelType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CorrespondencePackage.PART__MODEL_TYPE, oldModelType, modelType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractEntry> getEntries() {
        if (entries == null) {
            entries = new EObjectContainmentEList<AbstractEntry>(AbstractEntry.class, this, CorrespondencePackage.PART__ENTRIES);
        }
        return entries;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case CorrespondencePackage.PART__ENTRIES:
                return ((InternalEList<?>)getEntries()).basicRemove(otherEnd, msgs);
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
            case CorrespondencePackage.PART__MODEL_TYPE:
                if (resolve) return getModelType();
                return basicGetModelType();
            case CorrespondencePackage.PART__ENTRIES:
                return getEntries();
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
            case CorrespondencePackage.PART__MODEL_TYPE:
                setModelType((EObject)newValue);
                return;
            case CorrespondencePackage.PART__ENTRIES:
                getEntries().clear();
                getEntries().addAll((Collection<? extends AbstractEntry>)newValue);
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
            case CorrespondencePackage.PART__MODEL_TYPE:
                setModelType((EObject)null);
                return;
            case CorrespondencePackage.PART__ENTRIES:
                getEntries().clear();
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
            case CorrespondencePackage.PART__MODEL_TYPE:
                return modelType != null;
            case CorrespondencePackage.PART__ENTRIES:
                return entries != null && !entries.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //PartImpl
