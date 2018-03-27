/**
 */
package org.iobserve.adaptation.executionplan;

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deploy Component Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.DeployComponentAction#getTargetResourceContainer <em>Target Resource Container</em>}</li>
 * </ul>
 *
 * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getDeployComponentAction()
 * @model
 * @generated
 */
public interface DeployComponentAction extends AssemblyContextAction {
    /**
     * Returns the value of the '<em><b>Target Resource Container</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target Resource Container</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Resource Container</em>' reference.
     * @see #setTargetResourceContainer(ResourceContainer)
     * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getDeployComponentAction_TargetResourceContainer()
     * @model
     * @generated
     */
    ResourceContainer getTargetResourceContainer();

    /**
     * Sets the value of the '{@link org.iobserve.adaptation.executionplan.DeployComponentAction#getTargetResourceContainer <em>Target Resource Container</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target Resource Container</em>' reference.
     * @see #getTargetResourceContainer()
     * @generated
     */
    void setTargetResourceContainer(ResourceContainer value);

} // DeployComponentAction
