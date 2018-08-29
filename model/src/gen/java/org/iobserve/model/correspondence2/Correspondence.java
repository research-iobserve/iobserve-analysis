/**
 */
package org.iobserve.model.correspondence2;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Correspondence</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence2.Correspondence#getFrom <em>From</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence2.Correspondence#getTo <em>To</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence2.Correspondence#getCorrespondenceModel <em>Correspondence Model</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence2.Correspondence#getDebugStr <em>Debug Str</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence2.Correspondence2Package#getCorrespondence()
 * @model
 * @generated
 */
public interface Correspondence extends EObject {
	/**
	 * Returns the value of the '<em><b>From</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>From</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>From</em>' reference.
	 * @see #setFrom(HighLevelModelElement)
	 * @see org.iobserve.model.correspondence2.Correspondence2Package#getCorrespondence_From()
	 * @model required="true"
	 * @generated
	 */
	HighLevelModelElement getFrom();

	/**
	 * Sets the value of the '{@link org.iobserve.model.correspondence2.Correspondence#getFrom <em>From</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>From</em>' reference.
	 * @see #getFrom()
	 * @generated
	 */
	void setFrom(HighLevelModelElement value);

	/**
	 * Returns the value of the '<em><b>To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>To</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>To</em>' reference.
	 * @see #setTo(LowLevelModelElement)
	 * @see org.iobserve.model.correspondence2.Correspondence2Package#getCorrespondence_To()
	 * @model required="true"
	 * @generated
	 */
	LowLevelModelElement getTo();

	/**
	 * Sets the value of the '{@link org.iobserve.model.correspondence2.Correspondence#getTo <em>To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To</em>' reference.
	 * @see #getTo()
	 * @generated
	 */
	void setTo(LowLevelModelElement value);

	/**
	 * Returns the value of the '<em><b>Correspondence Model</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.iobserve.model.correspondence2.CorrespondenceModel#getCorrespondences <em>Correspondences</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Correspondence Model</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Correspondence Model</em>' container reference.
	 * @see #setCorrespondenceModel(CorrespondenceModel)
	 * @see org.iobserve.model.correspondence2.Correspondence2Package#getCorrespondence_CorrespondenceModel()
	 * @see org.iobserve.model.correspondence2.CorrespondenceModel#getCorrespondences
	 * @model opposite="correspondences" transient="false"
	 * @generated
	 */
	CorrespondenceModel getCorrespondenceModel();

	/**
	 * Sets the value of the '{@link org.iobserve.model.correspondence2.Correspondence#getCorrespondenceModel <em>Correspondence Model</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Correspondence Model</em>' container reference.
	 * @see #getCorrespondenceModel()
	 * @generated
	 */
	void setCorrespondenceModel(CorrespondenceModel value);

	/**
	 * Returns the value of the '<em><b>Debug Str</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Debug Str</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Debug Str</em>' attribute.
	 * @see #setDebugStr(String)
	 * @see org.iobserve.model.correspondence2.Correspondence2Package#getCorrespondence_DebugStr()
	 * @model
	 * @generated
	 */
	String getDebugStr();

	/**
	 * Sets the value of the '{@link org.iobserve.model.correspondence2.Correspondence#getDebugStr <em>Debug Str</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Debug Str</em>' attribute.
	 * @see #getDebugStr()
	 * @generated
	 */
	void setDebugStr(String value);

} // Correspondence
