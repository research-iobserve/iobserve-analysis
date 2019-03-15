/**
 */
package org.iobserve.model.correspondence_new;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Implementation Artifact Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence_new.ImplementationArtifactSet#getArtifacts <em>Artifacts</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence_new.Correspondence_newPackage#getImplementationArtifactSet()
 * @model
 * @generated
 */
public interface ImplementationArtifactSet extends EObject {
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
	 * @see org.iobserve.model.correspondence_new.Correspondence_newPackage#getImplementationArtifactSet_Artifacts()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<ImplementationArtifact> getArtifacts();

} // ImplementationArtifactSet
