/**
 */
package org.iobserve.model.correspondence.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.iobserve.model.correspondence.AllocationEntry;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.EServiceTechnology;
import org.palladiosimulator.pcm.allocation.AllocationContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Allocation Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.impl.AllocationEntryImpl#getAllocation <em>Allocation</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence.impl.AllocationEntryImpl#getTechnology <em>Technology</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AllocationEntryImpl extends AbstractEntryImpl implements AllocationEntry {
    /**
     * The cached value of the '{@link #getAllocation() <em>Allocation</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAllocation()
     * @generated
     * @ordered
     */
    protected AllocationContext allocation;

    /**
     * The default value of the '{@link #getTechnology() <em>Technology</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTechnology()
     * @generated
     * @ordered
     */
    protected static final EServiceTechnology TECHNOLOGY_EDEFAULT = EServiceTechnology.SERVLET;
    /**
     * The cached value of the '{@link #getTechnology() <em>Technology</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTechnology()
     * @generated
     * @ordered
     */
    protected EServiceTechnology technology = TECHNOLOGY_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AllocationEntryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return CorrespondencePackage.Literals.ALLOCATION_ENTRY;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllocationContext getAllocation() {
        if (allocation != null && ((EObject)allocation).eIsProxy()) {
            InternalEObject oldAllocation = (InternalEObject)allocation;
            allocation = (AllocationContext)eResolveProxy(oldAllocation);
            if (allocation != oldAllocation) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, CorrespondencePackage.ALLOCATION_ENTRY__ALLOCATION, oldAllocation, allocation));
            }
        }
        return allocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllocationContext basicGetAllocation() {
        return allocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAllocation(AllocationContext newAllocation) {
        AllocationContext oldAllocation = allocation;
        allocation = newAllocation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CorrespondencePackage.ALLOCATION_ENTRY__ALLOCATION, oldAllocation, allocation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EServiceTechnology getTechnology() {
        return technology;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTechnology(EServiceTechnology newTechnology) {
        EServiceTechnology oldTechnology = technology;
        technology = newTechnology == null ? TECHNOLOGY_EDEFAULT : newTechnology;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CorrespondencePackage.ALLOCATION_ENTRY__TECHNOLOGY, oldTechnology, technology));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case CorrespondencePackage.ALLOCATION_ENTRY__ALLOCATION:
                if (resolve) return getAllocation();
                return basicGetAllocation();
            case CorrespondencePackage.ALLOCATION_ENTRY__TECHNOLOGY:
                return getTechnology();
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
            case CorrespondencePackage.ALLOCATION_ENTRY__ALLOCATION:
                setAllocation((AllocationContext)newValue);
                return;
            case CorrespondencePackage.ALLOCATION_ENTRY__TECHNOLOGY:
                setTechnology((EServiceTechnology)newValue);
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
            case CorrespondencePackage.ALLOCATION_ENTRY__ALLOCATION:
                setAllocation((AllocationContext)null);
                return;
            case CorrespondencePackage.ALLOCATION_ENTRY__TECHNOLOGY:
                setTechnology(TECHNOLOGY_EDEFAULT);
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
            case CorrespondencePackage.ALLOCATION_ENTRY__ALLOCATION:
                return allocation != null;
            case CorrespondencePackage.ALLOCATION_ENTRY__TECHNOLOGY:
                return technology != TECHNOLOGY_EDEFAULT;
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
        result.append(" (technology: ");
        result.append(technology);
        result.append(')');
        return result.toString();
    }

} //AllocationEntryImpl
