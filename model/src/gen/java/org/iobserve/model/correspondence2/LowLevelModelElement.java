/**
 */
package org.iobserve.model.correspondence2;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Low Level Model Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence2.LowLevelModelElement#getModel <em>Model</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence2.LowLevelModelElement#getParent <em>Parent</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence2.Correspondence2Package#getLowLevelModelElement()
 * @model
 * @generated
 */
public interface LowLevelModelElement extends ModelElement {
	/**
	 * Returns the value of the '<em><b>Model</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.iobserve.model.correspondence2.LowLevelModel#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' container reference.
	 * @see #setModel(LowLevelModel)
	 * @see org.iobserve.model.correspondence2.Correspondence2Package#getLowLevelModelElement_Model()
	 * @see org.iobserve.model.correspondence2.LowLevelModel#getElements
	 * @model opposite="elements" transient="false"
	 * @generated
	 */
	LowLevelModel getModel();

	/**
	 * Sets the value of the '{@link org.iobserve.model.correspondence2.LowLevelModelElement#getModel <em>Model</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' container reference.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(LowLevelModel value);

	/**
	 * Returns the value of the '<em><b>Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' reference.
	 * @see #setParent(LowLevelModelElement)
	 * @see org.iobserve.model.correspondence2.Correspondence2Package#getLowLevelModelElement_Parent()
	 * @model
	 * @generated
	 */
	LowLevelModelElement getParent();

	/**
	 * Sets the value of the '{@link org.iobserve.model.correspondence2.LowLevelModelElement#getParent <em>Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(LowLevelModelElement value);

} // LowLevelModelElement
