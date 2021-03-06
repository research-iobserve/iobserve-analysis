/**
 */
package org.iobserve.adaptation.executionplan;

import org.eclipse.emf.common.util.EList;

import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Disconnect Node Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.DisconnectNodeAction#getTargetConnectors <em>Target Connectors</em>}</li>
 * </ul>
 *
 * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getDisconnectNodeAction()
 * @model
 * @generated
 */
public interface DisconnectNodeAction extends ResourceContainerAction {
    /**
     * Returns the value of the '<em><b>Target Connectors</b></em>' reference list.
     * The list contents are of type {@link org.palladiosimulator.pcm.resourceenvironment.LinkingResource}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Connectors</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Connectors</em>' reference list.
     * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getDisconnectNodeAction_TargetConnectors()
     * @model
     * @generated
     */
    EList<LinkingResource> getTargetConnectors();

} // DisconnectNodeAction
