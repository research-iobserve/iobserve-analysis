/**
 */
package org.iobserve.analysis.model.correspondence;

import org.eclipse.emf.ecore.EObject;

import org.palladiosimulator.pcm.core.entity.Entity;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Correspondence</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.analysis.model.correspondence.Correspondence#getFrom <em>From</em>}</li>
 *   <li>{@link org.iobserve.analysis.model.correspondence.Correspondence#getTo <em>To</em>}</li>
 * </ul>
 *
 * @see org.iobserve.analysis.model.correspondence.CorrespondencePackage#getCorrespondence()
 * @model
 * @generated
 */
public interface Correspondence extends EObject, Entity {
	/**
	 * Returns the value of the '<em><b>From</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>From</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From</em>' reference.
	 * @see #setFrom(ArchitecturalModelElement)
	 * @see org.iobserve.analysis.model.correspondence.CorrespondencePackage#getCorrespondence_From()
	 * @model required="true"
	 * @generated
	 */
	ArchitecturalModelElement getFrom();

	/**
	 * Sets the value of the '{@link org.iobserve.analysis.model.correspondence.Correspondence#getFrom <em>From</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From</em>' reference.
	 * @see #getFrom()
	 * @generated
	 */
	void setFrom(ArchitecturalModelElement value);

	/**
	 * Returns the value of the '<em><b>To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>To</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To</em>' reference.
	 * @see #setTo(ImplementationArtifact)
	 * @see org.iobserve.analysis.model.correspondence.CorrespondencePackage#getCorrespondence_To()
	 * @model required="true"
	 * @generated
	 */
	ImplementationArtifact getTo();

	/**
	 * Sets the value of the '{@link org.iobserve.analysis.model.correspondence.Correspondence#getTo <em>To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To</em>' reference.
	 * @see #getTo()
	 * @generated
	 */
	void setTo(ImplementationArtifact value);

} // Correspondence
