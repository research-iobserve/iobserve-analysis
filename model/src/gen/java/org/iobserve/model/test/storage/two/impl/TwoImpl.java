/**
 */
package org.iobserve.model.test.storage.two.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.iobserve.model.test.storage.two.Link;
import org.iobserve.model.test.storage.two.Two;
import org.iobserve.model.test.storage.two.TwoPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Two</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.test.storage.two.impl.TwoImpl#getLinks <em>Links</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TwoImpl extends MinimalEObjectImpl.Container implements Two {
    /**
     * The cached value of the '{@link #getLinks() <em>Links</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLinks()
     * @generated
     * @ordered
     */
    protected EList<Link> links;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TwoImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return TwoPackage.Literals.TWO;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Link> getLinks() {
        if (links == null) {
            links = new EObjectContainmentEList<Link>(Link.class, this, TwoPackage.TWO__LINKS);
        }
        return links;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case TwoPackage.TWO__LINKS:
                return ((InternalEList<?>)getLinks()).basicRemove(otherEnd, msgs);
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
            case TwoPackage.TWO__LINKS:
                return getLinks();
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
            case TwoPackage.TWO__LINKS:
                getLinks().clear();
                getLinks().addAll((Collection<? extends Link>)newValue);
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
            case TwoPackage.TWO__LINKS:
                getLinks().clear();
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
            case TwoPackage.TWO__LINKS:
                return links != null && !links.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //TwoImpl
