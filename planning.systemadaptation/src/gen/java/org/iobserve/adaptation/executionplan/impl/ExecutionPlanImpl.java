/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.iobserve.adaptation.executionplan.AtomicAction;
import org.iobserve.adaptation.executionplan.ExecutionPlan;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Execution Plan</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.ExecutionPlanImpl#getActions <em>Actions</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExecutionPlanImpl extends MinimalEObjectImpl.Container implements ExecutionPlan {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ExecutionPlanImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExecutionplanPackage.Literals.EXECUTION_PLAN;
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
    public EList<AtomicAction> getActions() {
        return (EList<AtomicAction>)eGet(ExecutionplanPackage.Literals.EXECUTION_PLAN__ACTIONS, true);
    }

} //ExecutionPlanImpl
