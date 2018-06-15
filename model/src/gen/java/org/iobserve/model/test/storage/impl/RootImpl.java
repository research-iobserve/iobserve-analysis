/**
 */
package org.iobserve.model.test.storage.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.iobserve.model.test.storage.EnumValueExample;
import org.iobserve.model.test.storage.Other;
import org.iobserve.model.test.storage.OtherInterface;
import org.iobserve.model.test.storage.Root;
import org.iobserve.model.test.storage.StoragePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.test.storage.impl.RootImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.iobserve.model.test.storage.impl.RootImpl#getLabels <em>Labels</em>}</li>
 *   <li>{@link org.iobserve.model.test.storage.impl.RootImpl#getFixed <em>Fixed</em>}</li>
 *   <li>{@link org.iobserve.model.test.storage.impl.RootImpl#getEnumerate <em>Enumerate</em>}</li>
 *   <li>{@link org.iobserve.model.test.storage.impl.RootImpl#getOthers <em>Others</em>}</li>
 *   <li>{@link org.iobserve.model.test.storage.impl.RootImpl#getIfaceOthers <em>Iface Others</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RootImpl extends MinimalEObjectImpl.Container implements Root {
    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The cached value of the '{@link #getLabels() <em>Labels</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLabels()
     * @generated
     * @ordered
     */
    protected EList<String> labels;

    /**
     * The default value of the '{@link #getFixed() <em>Fixed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFixed()
     * @generated
     * @ordered
     */
    protected static final String FIXED_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFixed() <em>Fixed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFixed()
     * @generated
     * @ordered
     */
    protected String fixed = FIXED_EDEFAULT;

    /**
     * The default value of the '{@link #getEnumerate() <em>Enumerate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEnumerate()
     * @generated
     * @ordered
     */
    protected static final EnumValueExample ENUMERATE_EDEFAULT = EnumValueExample.A;

    /**
     * The cached value of the '{@link #getEnumerate() <em>Enumerate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEnumerate()
     * @generated
     * @ordered
     */
    protected EnumValueExample enumerate = ENUMERATE_EDEFAULT;

    /**
     * The cached value of the '{@link #getOthers() <em>Others</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOthers()
     * @generated
     * @ordered
     */
    protected EList<Other> others;

    /**
     * The cached value of the '{@link #getIfaceOthers() <em>Iface Others</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIfaceOthers()
     * @generated
     * @ordered
     */
    protected EList<OtherInterface> ifaceOthers;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RootImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return StoragePackage.Literals.ROOT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, StoragePackage.ROOT__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getLabels() {
        if (labels == null) {
            labels = new EDataTypeUniqueEList<String>(String.class, this, StoragePackage.ROOT__LABELS);
        }
        return labels;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFixed() {
        return fixed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFixed(String newFixed) {
        String oldFixed = fixed;
        fixed = newFixed;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, StoragePackage.ROOT__FIXED, oldFixed, fixed));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EnumValueExample getEnumerate() {
        return enumerate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEnumerate(EnumValueExample newEnumerate) {
        EnumValueExample oldEnumerate = enumerate;
        enumerate = newEnumerate == null ? ENUMERATE_EDEFAULT : newEnumerate;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, StoragePackage.ROOT__ENUMERATE, oldEnumerate, enumerate));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Other> getOthers() {
        if (others == null) {
            others = new EObjectContainmentEList<Other>(Other.class, this, StoragePackage.ROOT__OTHERS);
        }
        return others;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<OtherInterface> getIfaceOthers() {
        if (ifaceOthers == null) {
            ifaceOthers = new EObjectContainmentEList<OtherInterface>(OtherInterface.class, this, StoragePackage.ROOT__IFACE_OTHERS);
        }
        return ifaceOthers;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case StoragePackage.ROOT__OTHERS:
                return ((InternalEList<?>)getOthers()).basicRemove(otherEnd, msgs);
            case StoragePackage.ROOT__IFACE_OTHERS:
                return ((InternalEList<?>)getIfaceOthers()).basicRemove(otherEnd, msgs);
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
            case StoragePackage.ROOT__NAME:
                return getName();
            case StoragePackage.ROOT__LABELS:
                return getLabels();
            case StoragePackage.ROOT__FIXED:
                return getFixed();
            case StoragePackage.ROOT__ENUMERATE:
                return getEnumerate();
            case StoragePackage.ROOT__OTHERS:
                return getOthers();
            case StoragePackage.ROOT__IFACE_OTHERS:
                return getIfaceOthers();
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
            case StoragePackage.ROOT__NAME:
                setName((String)newValue);
                return;
            case StoragePackage.ROOT__LABELS:
                getLabels().clear();
                getLabels().addAll((Collection<? extends String>)newValue);
                return;
            case StoragePackage.ROOT__FIXED:
                setFixed((String)newValue);
                return;
            case StoragePackage.ROOT__ENUMERATE:
                setEnumerate((EnumValueExample)newValue);
                return;
            case StoragePackage.ROOT__OTHERS:
                getOthers().clear();
                getOthers().addAll((Collection<? extends Other>)newValue);
                return;
            case StoragePackage.ROOT__IFACE_OTHERS:
                getIfaceOthers().clear();
                getIfaceOthers().addAll((Collection<? extends OtherInterface>)newValue);
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
            case StoragePackage.ROOT__NAME:
                setName(NAME_EDEFAULT);
                return;
            case StoragePackage.ROOT__LABELS:
                getLabels().clear();
                return;
            case StoragePackage.ROOT__FIXED:
                setFixed(FIXED_EDEFAULT);
                return;
            case StoragePackage.ROOT__ENUMERATE:
                setEnumerate(ENUMERATE_EDEFAULT);
                return;
            case StoragePackage.ROOT__OTHERS:
                getOthers().clear();
                return;
            case StoragePackage.ROOT__IFACE_OTHERS:
                getIfaceOthers().clear();
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
            case StoragePackage.ROOT__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case StoragePackage.ROOT__LABELS:
                return labels != null && !labels.isEmpty();
            case StoragePackage.ROOT__FIXED:
                return FIXED_EDEFAULT == null ? fixed != null : !FIXED_EDEFAULT.equals(fixed);
            case StoragePackage.ROOT__ENUMERATE:
                return enumerate != ENUMERATE_EDEFAULT;
            case StoragePackage.ROOT__OTHERS:
                return others != null && !others.isEmpty();
            case StoragePackage.ROOT__IFACE_OTHERS:
                return ifaceOthers != null && !ifaceOthers.isEmpty();
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
        result.append(" (name: ");
        result.append(name);
        result.append(", labels: ");
        result.append(labels);
        result.append(", fixed: ");
        result.append(fixed);
        result.append(", enumerate: ");
        result.append(enumerate);
        result.append(')');
        return result.toString();
    }

} //RootImpl
