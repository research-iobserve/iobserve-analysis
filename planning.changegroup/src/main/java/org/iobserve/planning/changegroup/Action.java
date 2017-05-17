/**
 */
package org.iobserve.planning.changegroup;

import org.eclipse.emf.ecore.EObject;

import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.changegroup.Action#getResourceContainer <em>Resource Container</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.changegroup.ChangegroupPackage#getAction()
 * @model
 * @generated
 */
public interface Action extends EObject {
	/**
	 * Returns the value of the '<em><b>Resource Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource Container</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Container</em>' reference.
	 * @see #setResourceContainer(ResourceContainerCloud)
	 * @see org.iobserve.planning.changegroup.ChangegroupPackage#getAction_ResourceContainer()
	 * @model required="true"
	 * @generated
	 */
	ResourceContainerCloud getResourceContainer();

	/**
	 * Sets the value of the '{@link org.iobserve.planning.changegroup.Action#getResourceContainer <em>Resource Container</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource Container</em>' reference.
	 * @see #getResourceContainer()
	 * @generated
	 */
	void setResourceContainer(ResourceContainerCloud value);

} // Action
