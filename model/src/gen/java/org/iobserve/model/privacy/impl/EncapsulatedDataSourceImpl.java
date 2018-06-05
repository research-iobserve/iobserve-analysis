/**
 */
package org.iobserve.model.privacy.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.iobserve.model.privacy.EncapsulatedDataSource;
import org.iobserve.model.privacy.PrivacyPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Encapsulated Data Source</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.privacy.impl.EncapsulatedDataSourceImpl#isDataSource <em>Data Source</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EncapsulatedDataSourceImpl extends MinimalEObjectImpl.Container implements EncapsulatedDataSource {
    /**
     * The default value of the '{@link #isDataSource() <em>Data Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isDataSource()
     * @generated
     * @ordered
     */
    protected static final boolean DATA_SOURCE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isDataSource() <em>Data Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isDataSource()
     * @generated
     * @ordered
     */
    protected boolean dataSource = DATA_SOURCE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EncapsulatedDataSourceImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PrivacyPackage.Literals.ENCAPSULATED_DATA_SOURCE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isDataSource() {
        return dataSource;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDataSource(boolean newDataSource) {
        boolean oldDataSource = dataSource;
        dataSource = newDataSource;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PrivacyPackage.ENCAPSULATED_DATA_SOURCE__DATA_SOURCE, oldDataSource, dataSource));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case PrivacyPackage.ENCAPSULATED_DATA_SOURCE__DATA_SOURCE:
                return isDataSource();
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
            case PrivacyPackage.ENCAPSULATED_DATA_SOURCE__DATA_SOURCE:
                setDataSource((Boolean)newValue);
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
            case PrivacyPackage.ENCAPSULATED_DATA_SOURCE__DATA_SOURCE:
                setDataSource(DATA_SOURCE_EDEFAULT);
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
            case PrivacyPackage.ENCAPSULATED_DATA_SOURCE__DATA_SOURCE:
                return dataSource != DATA_SOURCE_EDEFAULT;
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
        result.append(" (dataSource: ");
        result.append(dataSource);
        result.append(')');
        return result.toString();
    }

} //EncapsulatedDataSourceImpl
