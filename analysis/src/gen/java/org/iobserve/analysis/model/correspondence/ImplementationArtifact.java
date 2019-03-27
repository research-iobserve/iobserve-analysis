/**
 */
package org.iobserve.analysis.model.correspondence;

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
 *   <li>{@link org.iobserve.analysis.model.correspondence.ImplementationArtifact#getArtifactId <em>Artifact Id</em>}</li>
 * </ul>
 *
 * @see org.iobserve.analysis.model.correspondence.CorrespondencePackage#getImplementationArtifact()
 * @model
 * @generated
 */
public interface ImplementationArtifact extends EObject, Entity {
	/**
	 * Returns the value of the '<em><b>Artifact Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Artifact Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Artifact Id</em>' attribute.
	 * @see #setArtifactId(String)
	 * @see org.iobserve.analysis.model.correspondence.CorrespondencePackage#getImplementationArtifact_ArtifactId()
	 * @model required="true"
	 * @generated
	 */
	String getArtifactId();

	/**
	 * Sets the value of the '{@link org.iobserve.analysis.model.correspondence.ImplementationArtifact#getArtifactId <em>Artifact Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Artifact Id</em>' attribute.
	 * @see #getArtifactId()
	 * @generated
	 */
	void setArtifactId(String value);

} // ImplementationArtifact
