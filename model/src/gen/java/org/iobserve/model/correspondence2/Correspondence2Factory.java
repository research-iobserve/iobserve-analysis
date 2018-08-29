/**
 */
package org.iobserve.model.correspondence2;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.model.correspondence2.Correspondence2Package
 * @generated
 */
public interface Correspondence2Factory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Correspondence2Factory eINSTANCE = org.iobserve.model.correspondence2.impl.Correspondence2FactoryImpl.init();

	/**
	 * Returns a new object of class '<em>High Level Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>High Level Model Element</em>'.
	 * @generated
	 */
	HighLevelModelElement createHighLevelModelElement();

	/**
	 * Returns a new object of class '<em>Low Level Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Low Level Model Element</em>'.
	 * @generated
	 */
	LowLevelModelElement createLowLevelModelElement();

	/**
	 * Returns a new object of class '<em>High Level Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>High Level Model</em>'.
	 * @generated
	 */
	HighLevelModel createHighLevelModel();

	/**
	 * Returns a new object of class '<em>Low Level Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Low Level Model</em>'.
	 * @generated
	 */
	LowLevelModel createLowLevelModel();

	/**
	 * Returns a new object of class '<em>Correspondence</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Correspondence</em>'.
	 * @generated
	 */
	Correspondence createCorrespondence();

	/**
	 * Returns a new object of class '<em>Correspondence Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Correspondence Model</em>'.
	 * @generated
	 */
	CorrespondenceModel createCorrespondenceModel();

	/**
	 * Returns a new object of class '<em>Model Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model Element</em>'.
	 * @generated
	 */
	ModelElement createModelElement();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	Correspondence2Package getCorrespondence2Package();

} //Correspondence2Factory
