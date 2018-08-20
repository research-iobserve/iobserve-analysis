/**
 */
package org.iobserve.model.correspondence.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.iobserve.model.correspondence.AbstractEntry;
import org.iobserve.model.correspondence.CorrespondencePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.impl.AbstractEntryImpl#getImplementationId <em>Implementation Id</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractEntryImpl extends MinimalEObjectImpl.Container implements AbstractEntry {
    /**
     * The default value of the '{@link #getImplementationId() <em>Implementation Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImplementationId()
     * @generated
     * @ordered
     */
    protected static final String IMPLEMENTATION_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getImplementationId() <em>Implementation Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImplementationId()
     * @generated
     * @ordered
     */
    protected String implementationId = IMPLEMENTATION_ID_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractEntryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return CorrespondencePackage.Literals.ABSTRACT_ENTRY;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getImplementationId() {
        return implementationId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setImplementationId(String newImplementationId) {
        String oldImplementationId = implementationId;
        implementationId = newImplementationId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CorrespondencePackage.ABSTRACT_ENTRY__IMPLEMENTATION_ID, oldImplementationId, implementationId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case CorrespondencePackage.ABSTRACT_ENTRY__IMPLEMENTATION_ID:
                return getImplementationId();
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
            case CorrespondencePackage.ABSTRACT_ENTRY__IMPLEMENTATION_ID:
                setImplementationId((String)newValue);
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
            case CorrespondencePackage.ABSTRACT_ENTRY__IMPLEMENTATION_ID:
                setImplementationId(IMPLEMENTATION_ID_EDEFAULT);
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
            case CorrespondencePackage.ABSTRACT_ENTRY__IMPLEMENTATION_ID:
                return IMPLEMENTATION_ID_EDEFAULT == null ? implementationId != null : !IMPLEMENTATION_ID_EDEFAULT.equals(implementationId);
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
        result.append(" (implementationId: ");
        result.append(implementationId);
        result.append(')');
        return result.toString();
    }

} //AbstractEntryImpl
