/**
 */
package org.iobserve.analysis.model.correspondence;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Architectural Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.analysis.model.correspondence.ArchitecturalModel#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @see org.iobserve.analysis.model.correspondence.CorrespondencePackage#getArchitecturalModel()
 * @model
 * @generated
 */
public interface ArchitecturalModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.iobserve.analysis.model.correspondence.ArchitecturalModelElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' containment reference list.
	 * @see org.iobserve.analysis.model.correspondence.CorrespondencePackage#getArchitecturalModel_Elements()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<ArchitecturalModelElement> getElements();

} // ArchitecturalModel