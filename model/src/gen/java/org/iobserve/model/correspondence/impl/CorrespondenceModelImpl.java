/**
 */
package org.iobserve.model.correspondence.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.Part;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.impl.CorrespondenceModelImpl#getParts <em>Parts</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CorrespondenceModelImpl extends MinimalEObjectImpl.Container implements CorrespondenceModel {
    /**
     * The cached value of the '{@link #getParts() <em>Parts</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParts()
     * @generated
     * @ordered
     */
    protected EList<Part> parts;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CorrespondenceModelImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return CorrespondencePackage.Literals.CORRESPONDENCE_MODEL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Part> getParts() {
        if (parts == null) {
            parts = new EObjectContainmentEList<Part>(Part.class, this, CorrespondencePackage.CORRESPONDENCE_MODEL__PARTS);
        }
        return parts;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case CorrespondencePackage.CORRESPONDENCE_MODEL__PARTS:
                return ((InternalEList<?>)getParts()).basicRemove(otherEnd, msgs);
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
            case CorrespondencePackage.CORRESPONDENCE_MODEL__PARTS:
                return getParts();
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
            case CorrespondencePackage.CORRESPONDENCE_MODEL__PARTS:
                getParts().clear();
                getParts().addAll((Collection<? extends Part>)newValue);
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
            case CorrespondencePackage.CORRESPONDENCE_MODEL__PARTS:
                getParts().clear();
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
            case CorrespondencePackage.CORRESPONDENCE_MODEL__PARTS:
                return parts != null && !parts.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //CorrespondenceModelImpl
