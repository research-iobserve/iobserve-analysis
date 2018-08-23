/**
 */
package org.iobserve.model.privacy.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.iobserve.model.privacy.EDataPrivacyLevel;
import org.iobserve.model.privacy.PrivacyPackage;
import org.iobserve.model.privacy.ReturnTypePrivacy;
import org.palladiosimulator.pcm.repository.OperationSignature;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Return Type Privacy</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.privacy.impl.ReturnTypePrivacyImpl#getLevel <em>Level</em>}</li>
 *   <li>{@link org.iobserve.model.privacy.impl.ReturnTypePrivacyImpl#getOperationSignature <em>Operation Signature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReturnTypePrivacyImpl extends MinimalEObjectImpl.Container implements ReturnTypePrivacy {
    /**
     * The default value of the '{@link #getLevel() <em>Level</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLevel()
     * @generated
     * @ordered
     */
    protected static final EDataPrivacyLevel LEVEL_EDEFAULT = EDataPrivacyLevel.ANONYMOUS;

    /**
     * The cached value of the '{@link #getLevel() <em>Level</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLevel()
     * @generated
     * @ordered
     */
    protected EDataPrivacyLevel level = LEVEL_EDEFAULT;

    /**
     * The cached value of the '{@link #getOperationSignature() <em>Operation Signature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOperationSignature()
     * @generated
     * @ordered
     */
    protected OperationSignature operationSignature;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ReturnTypePrivacyImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PrivacyPackage.Literals.RETURN_TYPE_PRIVACY;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataPrivacyLevel getLevel() {
        return level;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLevel(EDataPrivacyLevel newLevel) {
        EDataPrivacyLevel oldLevel = level;
        level = newLevel == null ? LEVEL_EDEFAULT : newLevel;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PrivacyPackage.RETURN_TYPE_PRIVACY__LEVEL, oldLevel, level));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationSignature getOperationSignature() {
        if (operationSignature != null && ((EObject)operationSignature).eIsProxy()) {
            InternalEObject oldOperationSignature = (InternalEObject)operationSignature;
            operationSignature = (OperationSignature)eResolveProxy(oldOperationSignature);
            if (operationSignature != oldOperationSignature) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, PrivacyPackage.RETURN_TYPE_PRIVACY__OPERATION_SIGNATURE, oldOperationSignature, operationSignature));
            }
        }
        return operationSignature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationSignature basicGetOperationSignature() {
        return operationSignature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOperationSignature(OperationSignature newOperationSignature) {
        OperationSignature oldOperationSignature = operationSignature;
        operationSignature = newOperationSignature;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, PrivacyPackage.RETURN_TYPE_PRIVACY__OPERATION_SIGNATURE, oldOperationSignature, operationSignature));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case PrivacyPackage.RETURN_TYPE_PRIVACY__LEVEL:
                return getLevel();
            case PrivacyPackage.RETURN_TYPE_PRIVACY__OPERATION_SIGNATURE:
                if (resolve) return getOperationSignature();
                return basicGetOperationSignature();
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
            case PrivacyPackage.RETURN_TYPE_PRIVACY__LEVEL:
                setLevel((EDataPrivacyLevel)newValue);
                return;
            case PrivacyPackage.RETURN_TYPE_PRIVACY__OPERATION_SIGNATURE:
                setOperationSignature((OperationSignature)newValue);
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
            case PrivacyPackage.RETURN_TYPE_PRIVACY__LEVEL:
                setLevel(LEVEL_EDEFAULT);
                return;
            case PrivacyPackage.RETURN_TYPE_PRIVACY__OPERATION_SIGNATURE:
                setOperationSignature((OperationSignature)null);
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
            case PrivacyPackage.RETURN_TYPE_PRIVACY__LEVEL:
                return level != LEVEL_EDEFAULT;
            case PrivacyPackage.RETURN_TYPE_PRIVACY__OPERATION_SIGNATURE:
                return operationSignature != null;
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
        result.append(" (level: ");
        result.append(level);
        result.append(')');
        return result.toString();
    }

} //ReturnTypePrivacyImpl
