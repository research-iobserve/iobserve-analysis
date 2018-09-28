/**
 */
package org.iobserve.model.correspondence.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Assembly Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.impl.AssemblyEntryImpl#getAssembly <em>Assembly</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyEntryImpl extends AbstractEntryImpl implements AssemblyEntry {
    /**
     * The cached value of the '{@link #getAssembly() <em>Assembly</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAssembly()
     * @generated
     * @ordered
     */
    protected AssemblyContext assembly;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AssemblyEntryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return CorrespondencePackage.Literals.ASSEMBLY_ENTRY;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AssemblyContext getAssembly() {
        if (assembly != null && ((EObject)assembly).eIsProxy()) {
            InternalEObject oldAssembly = (InternalEObject)assembly;
            assembly = (AssemblyContext)eResolveProxy(oldAssembly);
            if (assembly != oldAssembly) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, CorrespondencePackage.ASSEMBLY_ENTRY__ASSEMBLY, oldAssembly, assembly));
            }
        }
        return assembly;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AssemblyContext basicGetAssembly() {
        return assembly;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAssembly(AssemblyContext newAssembly) {
        AssemblyContext oldAssembly = assembly;
        assembly = newAssembly;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CorrespondencePackage.ASSEMBLY_ENTRY__ASSEMBLY, oldAssembly, assembly));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case CorrespondencePackage.ASSEMBLY_ENTRY__ASSEMBLY:
                if (resolve) return getAssembly();
                return basicGetAssembly();
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
            case CorrespondencePackage.ASSEMBLY_ENTRY__ASSEMBLY:
                setAssembly((AssemblyContext)newValue);
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
            case CorrespondencePackage.ASSEMBLY_ENTRY__ASSEMBLY:
                setAssembly((AssemblyContext)null);
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
            case CorrespondencePackage.ASSEMBLY_ENTRY__ASSEMBLY:
                return assembly != null;
        }
        return super.eIsSet(featureID);
    }

} //AssemblyEntryImpl
