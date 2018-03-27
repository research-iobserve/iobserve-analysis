/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.iobserve.adaptation.executionplan.ExecutionplanPackage;
import org.iobserve.adaptation.executionplan.UndeployComponentAction;

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Undeploy Component Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.UndeployComponentActionImpl#getSourceResourceContainer <em>Source Resource Container</em>}</li>
 * </ul>
 *
 * @generated
 */
public class UndeployComponentActionImpl extends AssemblyContextActionImpl implements UndeployComponentAction {
    /**
     * The cached value of the '{@link #getSourceResourceContainer() <em>Source Resource Container</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceResourceContainer()
     * @generated
     * @ordered
     */
    protected ResourceContainer sourceResourceContainer;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected UndeployComponentActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExecutionplanPackage.Literals.UNDEPLOY_COMPONENT_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResourceContainer getSourceResourceContainer() {
        if (sourceResourceContainer != null && ((EObject)sourceResourceContainer).eIsProxy()) {
            InternalEObject oldSourceResourceContainer = (InternalEObject)sourceResourceContainer;
            sourceResourceContainer = (ResourceContainer)eResolveProxy(oldSourceResourceContainer);
            if (sourceResourceContainer != oldSourceResourceContainer) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionplanPackage.UNDEPLOY_COMPONENT_ACTION__SOURCE_RESOURCE_CONTAINER, oldSourceResourceContainer, sourceResourceContainer));
            }
        }
        return sourceResourceContainer;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResourceContainer basicGetSourceResourceContainer() {
        return sourceResourceContainer;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSourceResourceContainer(ResourceContainer newSourceResourceContainer) {
        ResourceContainer oldSourceResourceContainer = sourceResourceContainer;
        sourceResourceContainer = newSourceResourceContainer;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExecutionplanPackage.UNDEPLOY_COMPONENT_ACTION__SOURCE_RESOURCE_CONTAINER, oldSourceResourceContainer, sourceResourceContainer));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case ExecutionplanPackage.UNDEPLOY_COMPONENT_ACTION__SOURCE_RESOURCE_CONTAINER:
                if (resolve) return getSourceResourceContainer();
                return basicGetSourceResourceContainer();
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
            case ExecutionplanPackage.UNDEPLOY_COMPONENT_ACTION__SOURCE_RESOURCE_CONTAINER:
                setSourceResourceContainer((ResourceContainer)newValue);
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
            case ExecutionplanPackage.UNDEPLOY_COMPONENT_ACTION__SOURCE_RESOURCE_CONTAINER:
                setSourceResourceContainer((ResourceContainer)null);
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
            case ExecutionplanPackage.UNDEPLOY_COMPONENT_ACTION__SOURCE_RESOURCE_CONTAINER:
                return sourceResourceContainer != null;
        }
        return super.eIsSet(featureID);
    }

} //UndeployComponentActionImpl
