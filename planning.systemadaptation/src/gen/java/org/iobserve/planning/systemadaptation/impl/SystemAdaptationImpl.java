/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.iobserve.planning.systemadaptation.ComposedAction;
import org.iobserve.planning.systemadaptation.SystemAdaptation;
import org.iobserve.planning.systemadaptation.SystemadaptationPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>System Adaptation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.SystemAdaptationImpl#getActions <em>Actions</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SystemAdaptationImpl extends MinimalEObjectImpl.Container implements SystemAdaptation {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SystemAdaptationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SystemadaptationPackage.Literals.SYSTEM_ADAPTATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected int eStaticFeatureCount() {
        return 0;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<ComposedAction> getActions() {
        return (EList<ComposedAction>)eGet(SystemadaptationPackage.Literals.SYSTEM_ADAPTATION__ACTIONS, true);
    }

} //SystemAdaptationImpl
