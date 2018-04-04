/**
 */
package org.iobserve.adaptation.executionplan;

import org.eclipse.emf.common.util.EList;

import org.palladiosimulator.pcm.core.composition.AssemblyConnector;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connect Component Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.ConnectComponentAction#getTargetConnectors <em>Target Connectors</em>}</li>
 * </ul>
 *
 * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getConnectComponentAction()
 * @model
 * @generated
 */
public interface ConnectComponentAction extends AssemblyContextAction {
    /**
     * Returns the value of the '<em><b>Target Connectors</b></em>' reference list.
     * The list contents are of type {@link org.palladiosimulator.pcm.core.composition.AssemblyConnector}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Connectors</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Connectors</em>' reference list.
     * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getConnectComponentAction_TargetConnectors()
     * @model
     * @generated
     */
    EList<AssemblyConnector> getTargetConnectors();

} // ConnectComponentAction
