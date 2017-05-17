/**
 */
package org.iobserve.planning.systemadaptation;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Replicate Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.ReplicateAction#getNewCount <em>New Count</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.ReplicateAction#getOldCount <em>Old Count</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getReplicateAction()
 * @model
 * @generated
 */
public interface ReplicateAction extends ResourceContainerAction {
	/**
	 * Returns the value of the '<em><b>New Count</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Integer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Count</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Count</em>' attribute list.
	 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getReplicateAction_NewCount()
	 * @model default="0"
	 * @generated
	 */
	EList<Integer> getNewCount();

	/**
	 * Returns the value of the '<em><b>Old Count</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Integer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Count</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Old Count</em>' attribute list.
	 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage#getReplicateAction_OldCount()
	 * @model default="0"
	 * @generated
	 */
	EList<Integer> getOldCount();

} // ReplicateAction
