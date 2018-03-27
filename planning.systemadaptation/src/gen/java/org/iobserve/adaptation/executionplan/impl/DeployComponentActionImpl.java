/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.iobserve.adaptation.executionplan.DeployComponentAction;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deploy Component Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.DeployComponentActionImpl#getTargetResourceContainer <em>Target Resource Container</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployComponentActionImpl extends AssemblyContextActionImpl implements DeployComponentAction {
    /**
     * The cached value of the '{@link #getTargetResourceContainer() <em>Target Resource Container</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetResourceContainer()
     * @generated
     * @ordered
     */
    protected ResourceContainer targetResourceContainer;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DeployComponentActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExecutionplanPackage.Literals.DEPLOY_COMPONENT_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResourceContainer getTargetResourceContainer() {
        if (targetResourceContainer != null && ((EObject)targetResourceContainer).eIsProxy()) {
            InternalEObject oldTargetResourceContainer = (InternalEObject)targetResourceContainer;
            targetResourceContainer = (ResourceContainer)eResolveProxy(oldTargetResourceContainer);
            if (targetResourceContainer != oldTargetResourceContainer) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionplanPackage.DEPLOY_COMPONENT_ACTION__TARGET_RESOURCE_CONTAINER, oldTargetResourceContainer, targetResourceContainer));
            }
        }
        return targetResourceContainer;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResourceContainer basicGetTargetResourceContainer() {
        return targetResourceContainer;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTargetResourceContainer(ResourceContainer newTargetResourceContainer) {
        ResourceContainer oldTargetResourceContainer = targetResourceContainer;
        targetResourceContainer = newTargetResourceContainer;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExecutionplanPackage.DEPLOY_COMPONENT_ACTION__TARGET_RESOURCE_CONTAINER, oldTargetResourceContainer, targetResourceContainer));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case ExecutionplanPackage.DEPLOY_COMPONENT_ACTION__TARGET_RESOURCE_CONTAINER:
                if (resolve) return getTargetResourceContainer();
                return basicGetTargetResourceContainer();
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
            case ExecutionplanPackage.DEPLOY_COMPONENT_ACTION__TARGET_RESOURCE_CONTAINER:
                setTargetResourceContainer((ResourceContainer)newValue);
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
            case ExecutionplanPackage.DEPLOY_COMPONENT_ACTION__TARGET_RESOURCE_CONTAINER:
                setTargetResourceContainer((ResourceContainer)null);
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
            case ExecutionplanPackage.DEPLOY_COMPONENT_ACTION__TARGET_RESOURCE_CONTAINER:
                return targetResourceContainer != null;
        }
        return super.eIsSet(featureID);
    }

} //DeployComponentActionImpl
