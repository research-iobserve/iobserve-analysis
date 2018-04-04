/**
 */
package org.iobserve.adaptation.executionplan.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.iobserve.adaptation.executionplan.ConnectComponentAction;
import org.iobserve.adaptation.executionplan.ExecutionplanPackage;

import org.palladiosimulator.pcm.core.composition.AssemblyConnector;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Connect Component Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.impl.ConnectComponentActionImpl#getTargetConnectors <em>Target Connectors</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConnectComponentActionImpl extends AssemblyContextActionImpl implements ConnectComponentAction {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConnectComponentActionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExecutionplanPackage.Literals.CONNECT_COMPONENT_ACTION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<AssemblyConnector> getTargetConnectors() {
        return (EList<AssemblyConnector>)eGet(ExecutionplanPackage.Literals.CONNECT_COMPONENT_ACTION__TARGET_CONNECTORS, true);
    }

} //ConnectComponentActionImpl
