/**
 */
package org.iobserve.planning.systemadaptation;

import org.palladiosimulator.pcm.repository.RepositoryComponent;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Repository Component Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction#getNewRepositoryComponent <em>New Repository Component</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getChangeRepositoryComponentAction()
 * @model
 * @generated
 */
public interface ChangeRepositoryComponentAction extends AssemblyContextAction {
	/**
	 * Returns the value of the '<em><b>New Repository Component</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Repository Component</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Repository Component</em>' reference.
	 * @see #setNewRepositoryComponent(RepositoryComponent)
	 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getChangeRepositoryComponentAction_NewRepositoryComponent()
	 * @model required="true" ordered="false"
	 * @generated
	 */
	RepositoryComponent getNewRepositoryComponent();

	/**
	 * Sets the value of the '{@link org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction#getNewRepositoryComponent <em>New Repository Component</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Repository Component</em>' reference.
	 * @see #getNewRepositoryComponent()
	 * @generated
	 */
	void setNewRepositoryComponent(RepositoryComponent value);

} // ChangeRepositoryComponentAction
