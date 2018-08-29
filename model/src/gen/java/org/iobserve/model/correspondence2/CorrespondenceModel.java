/**
 */
package org.iobserve.model.correspondence2;

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
 *   <li>{@link org.iobserve.model.correspondence2.CorrespondenceModel#getHighLevelModel <em>High Level Model</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence2.CorrespondenceModel#getLowLevelModel <em>Low Level Model</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence2.CorrespondenceModel#getCorrespondences <em>Correspondences</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence2.Correspondence2Package#getCorrespondenceModel()
 * @model
 * @generated
 */
public interface CorrespondenceModel extends EObject {
	/**
	 * Returns the value of the '<em><b>High Level Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>High Level Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>High Level Model</em>' reference.
	 * @see #setHighLevelModel(HighLevelModel)
	 * @see org.iobserve.model.correspondence2.Correspondence2Package#getCorrespondenceModel_HighLevelModel()
	 * @model required="true"
	 * @generated
	 */
	HighLevelModel getHighLevelModel();

	/**
	 * Sets the value of the '{@link org.iobserve.model.correspondence2.CorrespondenceModel#getHighLevelModel <em>High Level Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>High Level Model</em>' reference.
	 * @see #getHighLevelModel()
	 * @generated
	 */
	void setHighLevelModel(HighLevelModel value);

	/**
	 * Returns the value of the '<em><b>Low Level Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Low Level Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Low Level Model</em>' reference.
	 * @see #setLowLevelModel(LowLevelModel)
	 * @see org.iobserve.model.correspondence2.Correspondence2Package#getCorrespondenceModel_LowLevelModel()
	 * @model required="true"
	 * @generated
	 */
	LowLevelModel getLowLevelModel();

	/**
	 * Sets the value of the '{@link org.iobserve.model.correspondence2.CorrespondenceModel#getLowLevelModel <em>Low Level Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Low Level Model</em>' reference.
	 * @see #getLowLevelModel()
	 * @generated
	 */
	void setLowLevelModel(LowLevelModel value);

	/**
	 * Returns the value of the '<em><b>Correspondences</b></em>' containment reference list.
	 * The list contents are of type {@link org.iobserve.model.correspondence2.Correspondence}.
	 * It is bidirectional and its opposite is '{@link org.iobserve.model.correspondence2.Correspondence#getCorrespondenceModel <em>Correspondence Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Correspondences</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Correspondences</em>' containment reference list.
	 * @see org.iobserve.model.correspondence2.Correspondence2Package#getCorrespondenceModel_Correspondences()
	 * @see org.iobserve.model.correspondence2.Correspondence#getCorrespondenceModel
	 * @model opposite="correspondenceModel" containment="true" required="true"
	 * @generated
	 */
	EList<Correspondence> getCorrespondences();

} // CorrespondenceModel
