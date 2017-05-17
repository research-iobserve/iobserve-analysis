/**
 */
package org.iobserve.planning.changegroup;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.changegroup.ChangeGroup#getActions <em>Actions</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.changegroup.ChangegroupPackage#getChangeGroup()
 * @model
 * @generated
 */
public interface ChangeGroup extends EObject {
	/**
	 * Returns the value of the '<em><b>Actions</b></em>' containment reference list.
	 * The list contents are of type {@link org.iobserve.planning.changegroup.Action}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Actions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Actions</em>' containment reference list.
	 * @see org.iobserve.planning.changegroup.ChangegroupPackage#getChangeGroup_Actions()
	 * @model containment="true"
	 * @generated
	 */
	EList<Action> getActions();

} // ChangeGroup
