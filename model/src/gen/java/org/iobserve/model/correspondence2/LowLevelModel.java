/**
 */
package org.iobserve.model.correspondence2;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Low Level Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence2.LowLevelModel#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence2.Correspondence2Package#getLowLevelModel()
 * @model
 * @generated
 */
public interface LowLevelModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.iobserve.model.correspondence2.LowLevelModelElement}.
	 * It is bidirectional and its opposite is '{@link org.iobserve.model.correspondence2.LowLevelModelElement#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' containment reference list.
	 * @see org.iobserve.model.correspondence2.Correspondence2Package#getLowLevelModel_Elements()
	 * @see org.iobserve.model.correspondence2.LowLevelModelElement#getModel
	 * @model opposite="model" containment="true" required="true"
	 * @generated
	 */
	EList<LowLevelModelElement> getElements();

} // LowLevelModel
