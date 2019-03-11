/**
 */
package org.iobserve.model.correspondence_new;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Correspondence Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence_new.CorrespondenceModel#getCorrespondences <em>Correspondences</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence_new.CorrespondenceModel#getArchitecturalModel <em>Architectural Model</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence_new.CorrespondenceModel#getImplementationArtifacts <em>Implementation Artifacts</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence_new.Correspondence_newPackage#getCorrespondenceModel()
 * @model
 * @generated
 */
public interface CorrespondenceModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Correspondences</b></em>' containment reference list.
	 * The list contents are of type {@link org.iobserve.model.correspondence_new.Correspondence}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Correspondences</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Correspondences</em>' containment reference list.
	 * @see org.iobserve.model.correspondence_new.Correspondence_newPackage#getCorrespondenceModel_Correspondences()
	 * @model containment="true"
	 * @generated
	 */
	EList<Correspondence> getCorrespondences();

	/**
	 * Returns the value of the '<em><b>Architectural Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Architectural Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Architectural Model</em>' containment reference.
	 * @see #setArchitecturalModel(ArchitecturalModel)
	 * @see org.iobserve.model.correspondence_new.Correspondence_newPackage#getCorrespondenceModel_ArchitecturalModel()
	 * @model containment="true" required="true"
	 * @generated
	 */
	ArchitecturalModel getArchitecturalModel();

	/**
	 * Sets the value of the '{@link org.iobserve.model.correspondence_new.CorrespondenceModel#getArchitecturalModel <em>Architectural Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Architectural Model</em>' containment reference.
	 * @see #getArchitecturalModel()
	 * @generated
	 */
	void setArchitecturalModel(ArchitecturalModel value);

	/**
	 * Returns the value of the '<em><b>Implementation Artifacts</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Implementation Artifacts</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Implementation Artifacts</em>' containment reference.
	 * @see #setImplementationArtifacts(ImplementationArtifacts)
	 * @see org.iobserve.model.correspondence_new.Correspondence_newPackage#getCorrespondenceModel_ImplementationArtifacts()
	 * @model containment="true" required="true"
	 * @generated
	 */
	ImplementationArtifacts getImplementationArtifacts();

	/**
	 * Sets the value of the '{@link org.iobserve.model.correspondence_new.CorrespondenceModel#getImplementationArtifacts <em>Implementation Artifacts</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Implementation Artifacts</em>' containment reference.
	 * @see #getImplementationArtifacts()
	 * @generated
	 */
	void setImplementationArtifacts(ImplementationArtifacts value);

} // CorrespondenceModel
