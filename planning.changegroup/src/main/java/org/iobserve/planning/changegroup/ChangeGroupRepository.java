/**
 */
package org.iobserve.planning.changegroup;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Group Repository</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.changegroup.ChangeGroupRepository#getChangeGroups <em>Change Groups</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.changegroup.ChangegroupPackage#getChangeGroupRepository()
 * @model
 * @generated
 */
public interface ChangeGroupRepository extends EObject {
	/**
	 * Returns the value of the '<em><b>Change Groups</b></em>' reference list.
	 * The list contents are of type {@link org.iobserve.planning.changegroup.ChangeGroup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Groups</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change Groups</em>' reference list.
	 * @see org.iobserve.planning.changegroup.ChangegroupPackage#getChangeGroupRepository_ChangeGroups()
	 * @model
	 * @generated
	 */
	EList<ChangeGroup> getChangeGroups();

} // ChangeGroupRepository
