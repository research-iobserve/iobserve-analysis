/**
 */
package org.iobserve.model.correspondence_new;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Architectural Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence_new.ArchitecturalModel#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence_new.Correspondence_newPackage#getArchitecturalModel()
 * @model
 * @generated
 */
public interface ArchitecturalModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.iobserve.model.correspondence_new.ArchitecturalModelElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' containment reference list.
	 * @see org.iobserve.model.correspondence_new.Correspondence_newPackage#getArchitecturalModel_Elements()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<ArchitecturalModelElement> getElements();

} // ArchitecturalModel
