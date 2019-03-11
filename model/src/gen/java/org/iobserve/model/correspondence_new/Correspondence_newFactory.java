/**
 */
package org.iobserve.model.correspondence_new;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.model.correspondence_new.Correspondence_newPackage
 * @generated
 */
public interface Correspondence_newFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Correspondence_newFactory eINSTANCE = org.iobserve.model.correspondence_new.impl.Correspondence_newFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Correspondence Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Correspondence Model</em>'.
	 * @generated
	 */
	CorrespondenceModel createCorrespondenceModel();

	/**
	 * Returns a new object of class '<em>Correspondence</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Correspondence</em>'.
	 * @generated
	 */
	Correspondence createCorrespondence();

	/**
	 * Returns a new object of class '<em>Architectural Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Architectural Model</em>'.
	 * @generated
	 */
	ArchitecturalModel createArchitecturalModel();

	/**
	 * Returns a new object of class '<em>Architectural Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Architectural Model Element</em>'.
	 * @generated
	 */
	ArchitecturalModelElement createArchitecturalModelElement();

	/**
	 * Returns a new object of class '<em>Implementation Artifacts</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Implementation Artifacts</em>'.
	 * @generated
	 */
	ImplementationArtifacts createImplementationArtifacts();

	/**
	 * Returns a new object of class '<em>Implementation Artifact</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Implementation Artifact</em>'.
	 * @generated
	 */
	ImplementationArtifact createImplementationArtifact();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	Correspondence_newPackage getCorrespondence_newPackage();

} //Correspondence_newFactory
