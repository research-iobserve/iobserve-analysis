/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.iobserve.adaptation.executionplan.AssemblyContextAction;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Assembly Context Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.AssemblyContextActionImpl#getSourceAssemblyContext <em>Source Assembly Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyContextActionImpl extends ActionImpl implements AssemblyContextAction {
    /**
     * The cached value of the '{@link #getSourceAssemblyContext() <em>Source Assembly Context</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceAssemblyContext()
     * @generated
     * @ordered
     */
    protected AssemblyContext sourceAssemblyContext;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AssemblyContextActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExecutionplanPackage.Literals.ASSEMBLY_CONTEXT_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AssemblyContext getSourceAssemblyContext() {
        if (sourceAssemblyContext != null && ((EObject)sourceAssemblyContext).eIsProxy()) {
            InternalEObject oldSourceAssemblyContext = (InternalEObject)sourceAssemblyContext;
            sourceAssemblyContext = (AssemblyContext)eResolveProxy(oldSourceAssemblyContext);
            if (sourceAssemblyContext != oldSourceAssemblyContext) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionplanPackage.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT, oldSourceAssemblyContext, sourceAssemblyContext));
            }
        }
        return sourceAssemblyContext;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AssemblyContext basicGetSourceAssemblyContext() {
        return sourceAssemblyContext;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSourceAssemblyContext(AssemblyContext newSourceAssemblyContext) {
        AssemblyContext oldSourceAssemblyContext = sourceAssemblyContext;
        sourceAssemblyContext = newSourceAssemblyContext;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExecutionplanPackage.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT, oldSourceAssemblyContext, sourceAssemblyContext));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case ExecutionplanPackage.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT:
                if (resolve) return getSourceAssemblyContext();
                return basicGetSourceAssemblyContext();
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
            case ExecutionplanPackage.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT:
                setSourceAssemblyContext((AssemblyContext)newValue);
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
            case ExecutionplanPackage.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT:
                setSourceAssemblyContext((AssemblyContext)null);
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
            case ExecutionplanPackage.ASSEMBLY_CONTEXT_ACTION__SOURCE_ASSEMBLY_CONTEXT:
                return sourceAssemblyContext != null;
        }
        return super.eIsSet(featureID);
    }

} //AssemblyContextActionImpl
