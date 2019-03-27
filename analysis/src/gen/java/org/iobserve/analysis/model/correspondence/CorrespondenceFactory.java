/**
 */
package org.iobserve.analysis.model.correspondence;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.analysis.model.correspondence.CorrespondencePackage
 * @generated
 */
public interface CorrespondenceFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CorrespondenceFactory eINSTANCE = org.iobserve.analysis.model.correspondence.impl.CorrespondenceFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
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
	 * Returns a new object of class '<em>Implementation Artifact Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Implementation Artifact Set</em>'.
	 * @generated
	 */
	ImplementationArtifactSet createImplementationArtifactSet();

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
	CorrespondencePackage getCorrespondencePackage();

} //CorrespondenceFactory
