/**
 */
package org.iobserve.model.correspondence_new;

import org.eclipse.emf.ecore.EObject;

import org.palladiosimulator.pcm.core.entity.Entity;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Implementation Artifact</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence_new.ImplementationArtifact#getArtifactId <em>Artifact Id</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence_new.Correspondence_newPackage#getImplementationArtifact()
 * @model
 * @generated
 */
public interface ImplementationArtifact extends EObject, Entity {
	/**
	 * Returns the value of the '<em><b>Artifact Id</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Artifact Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Artifact Id</em>' attribute.
	 * @see #setArtifactId(String)
	 * @see org.iobserve.model.correspondence_new.Correspondence_newPackage#getImplementationArtifact_ArtifactId()
	 * @model default="" required="true"
	 * @generated
	 */
	String getArtifactId();

	/**
	 * Sets the value of the '{@link org.iobserve.model.correspondence_new.ImplementationArtifact#getArtifactId <em>Artifact Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Artifact Id</em>' attribute.
	 * @see #getArtifactId()
	 * @generated
	 */
	void setArtifactId(String value);

} // ImplementationArtifact
