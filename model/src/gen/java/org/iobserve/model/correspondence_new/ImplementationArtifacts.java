/**
 */
package org.iobserve.model.correspondence_new;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Implementation Artifacts</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence_new.ImplementationArtifacts#getArtifacts <em>Artifacts</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence_new.Correspondence_newPackage#getImplementationArtifacts()
 * @model
 * @generated
 */
public interface ImplementationArtifacts extends EObject {
	/**
	 * Returns the value of the '<em><b>Artifacts</b></em>' containment reference list.
	 * The list contents are of type {@link org.iobserve.model.correspondence_new.ImplementationArtifact}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Artifacts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Artifacts</em>' containment reference list.
	 * @see org.iobserve.model.correspondence_new.Correspondence_newPackage#getImplementationArtifacts_Artifacts()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<ImplementationArtifact> getArtifacts();

} // ImplementationArtifacts
