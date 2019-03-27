/**
 */
package org.iobserve.analysis.model.correspondence;

import org.eclipse.emf.ecore.EObject;

import org.palladiosimulator.pcm.core.entity.Entity;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Architectural Model Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.analysis.model.correspondence.ArchitecturalModelElement#getElement <em>Element</em>}</li>
 * </ul>
 *
 * @see org.iobserve.analysis.model.correspondence.CorrespondencePackage#getArchitecturalModelElement()
 * @model
 * @generated
 */
public interface ArchitecturalModelElement extends EObject, Entity {
	/**
	 * Returns the value of the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element</em>' reference.
	 * @see #setElement(Entity)
	 * @see org.iobserve.analysis.model.correspondence.CorrespondencePackage#getArchitecturalModelElement_Element()
	 * @model required="true"
	 * @generated
	 */
	Entity getElement();

	/**
	 * Sets the value of the '{@link org.iobserve.analysis.model.correspondence.ArchitecturalModelElement#getElement <em>Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element</em>' reference.
	 * @see #getElement()
	 * @generated
	 */
	void setElement(Entity value);

} // ArchitecturalModelElement
