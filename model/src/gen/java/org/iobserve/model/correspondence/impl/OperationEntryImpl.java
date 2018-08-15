/**
 */
package org.iobserve.model.correspondence.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.OperationEntry;
import org.palladiosimulator.pcm.repository.OperationSignature;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.impl.OperationEntryImpl#getOperation <em>Operation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperationEntryImpl extends AbstractEntryImpl implements OperationEntry {
    /**
     * The cached value of the '{@link #getOperation() <em>Operation</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOperation()
     * @generated
     * @ordered
     */
    protected OperationSignature operation;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OperationEntryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return CorrespondencePackage.Literals.OPERATION_ENTRY;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationSignature getOperation() {
        if (operation != null && ((EObject)operation).eIsProxy()) {
            InternalEObject oldOperation = (InternalEObject)operation;
            operation = (OperationSignature)eResolveProxy(oldOperation);
            if (operation != oldOperation) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, CorrespondencePackage.OPERATION_ENTRY__OPERATION, oldOperation, operation));
            }
        }
        return operation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationSignature basicGetOperation() {
        return operation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOperation(OperationSignature newOperation) {
        OperationSignature oldOperation = operation;
        operation = newOperation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CorrespondencePackage.OPERATION_ENTRY__OPERATION, oldOperation, operation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case CorrespondencePackage.OPERATION_ENTRY__OPERATION:
                if (resolve) return getOperation();
                return basicGetOperation();
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
            case CorrespondencePackage.OPERATION_ENTRY__OPERATION:
                setOperation((OperationSignature)newValue);
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
            case CorrespondencePackage.OPERATION_ENTRY__OPERATION:
                setOperation((OperationSignature)null);
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
            case CorrespondencePackage.OPERATION_ENTRY__OPERATION:
                return operation != null;
        }
        return super.eIsSet(featureID);
    }

} //OperationEntryImpl
