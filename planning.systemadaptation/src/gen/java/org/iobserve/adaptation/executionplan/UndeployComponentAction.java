/**
 */
package org.iobserve.adaptation.executionplan;

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Undeploy Component Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.adaptation.executionplan.UndeployComponentAction#getSourceResourceContainer <em>Source Resource Container</em>}</li>
 * </ul>
 *
 * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getUndeployComponentAction()
 * @model
 * @generated
 */
public interface UndeployComponentAction extends AssemblyContextAction {
    /**
     * Returns the value of the '<em><b>Source Resource Container</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source Resource Container</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source Resource Container</em>' reference.
     * @see #setSourceResourceContainer(ResourceContainer)
     * @see org.iobserve.adaptation.executionplan.ExecutionplanPackage#getUndeployComponentAction_SourceResourceContainer()
     * @model
     * @generated
     */
    ResourceContainer getSourceResourceContainer();

    /**
     * Sets the value of the '{@link org.iobserve.adaptation.executionplan.UndeployComponentAction#getSourceResourceContainer <em>Source Resource Container</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source Resource Container</em>' reference.
     * @see #getSourceResourceContainer()
     * @generated
     */
    void setSourceResourceContainer(ResourceContainer value);

} // UndeployComponentAction
