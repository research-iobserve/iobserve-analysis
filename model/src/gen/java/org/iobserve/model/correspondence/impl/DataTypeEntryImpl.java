/**
 */
package org.iobserve.model.correspondence.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.DataTypeEntry;

import org.palladiosimulator.pcm.repository.DataType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Type Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.impl.DataTypeEntryImpl#getDataTypeEntry <em>Data Type Entry</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DataTypeEntryImpl extends AbstractEntryImpl implements DataTypeEntry {
    /**
     * The cached value of the '{@link #getDataTypeEntry() <em>Data Type Entry</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDataTypeEntry()
     * @generated
     * @ordered
     */
    protected DataType dataTypeEntry;
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DataTypeEntryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return CorrespondencePackage.Literals.DATA_TYPE_ENTRY;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataType getDataTypeEntry() {
        if (dataTypeEntry != null && ((EObject)dataTypeEntry).eIsProxy()) {
            InternalEObject oldDataTypeEntry = (InternalEObject)dataTypeEntry;
            dataTypeEntry = (DataType)eResolveProxy(oldDataTypeEntry);
            if (dataTypeEntry != oldDataTypeEntry) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, CorrespondencePackage.DATA_TYPE_ENTRY__DATA_TYPE_ENTRY, oldDataTypeEntry, dataTypeEntry));
            }
        }
        return dataTypeEntry;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataType basicGetDataTypeEntry() {
        return dataTypeEntry;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDataTypeEntry(DataType newDataTypeEntry) {
        DataType oldDataTypeEntry = dataTypeEntry;
        dataTypeEntry = newDataTypeEntry;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CorrespondencePackage.DATA_TYPE_ENTRY__DATA_TYPE_ENTRY, oldDataTypeEntry, dataTypeEntry));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case CorrespondencePackage.DATA_TYPE_ENTRY__DATA_TYPE_ENTRY:
                if (resolve) return getDataTypeEntry();
                return basicGetDataTypeEntry();
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
            case CorrespondencePackage.DATA_TYPE_ENTRY__DATA_TYPE_ENTRY:
                setDataTypeEntry((DataType)newValue);
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
            case CorrespondencePackage.DATA_TYPE_ENTRY__DATA_TYPE_ENTRY:
                setDataTypeEntry((DataType)null);
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
            case CorrespondencePackage.DATA_TYPE_ENTRY__DATA_TYPE_ENTRY:
                return dataTypeEntry != null;
        }
        return super.eIsSet(featureID);
    }

} //DataTypeEntryImpl
