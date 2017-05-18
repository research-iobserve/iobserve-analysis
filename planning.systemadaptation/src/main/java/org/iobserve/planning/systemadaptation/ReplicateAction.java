/**
 */
package org.iobserve.planning.systemadaptation;

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Replicate Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.ReplicateAction#getNewResourceContainer <em>New Resource Container</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getReplicateAction()
 * @model
 * @generated
 */
public interface ReplicateAction extends ResourceContainerAction {
	/**
	 * Returns the value of the '<em><b>New Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Resource Container</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Resource Container</em>' reference.
	 * @see #setNewResourceContainer(ResourceContainer)
	 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getReplicateAction_NewResourceContainer()
	 * @model required="true"
	 * @generated
	 */
	ResourceContainer getNewResourceContainer();

	/**
	 * Sets the value of the '{@link org.iobserve.planning.systemadaptation.ReplicateAction#getNewResourceContainer <em>New Resource Container</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Resource Container</em>' reference.
	 * @see #getNewResourceContainer()
	 * @generated
	 */
	void setNewResourceContainer(ResourceContainer value);

} // ReplicateAction
