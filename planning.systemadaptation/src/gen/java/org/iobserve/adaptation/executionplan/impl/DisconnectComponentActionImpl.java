/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.iobserve.adaptation.executionplan.DisconnectComponentAction;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;

import org.palladiosimulator.pcm.core.composition.AssemblyConnector;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Disconnect Component Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.DisconnectComponentActionImpl#getTargetConnectors <em>Target Connectors</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DisconnectComponentActionImpl extends AssemblyContextActionImpl implements DisconnectComponentAction {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DisconnectComponentActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExecutionplanPackage.Literals.DISCONNECT_COMPONENT_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<AssemblyConnector> getTargetConnectors() {
        return (EList<AssemblyConnector>)eGet(ExecutionplanPackage.Literals.DISCONNECT_COMPONENT_ACTION__TARGET_CONNECTORS, true);
    }

} //DisconnectComponentActionImpl
