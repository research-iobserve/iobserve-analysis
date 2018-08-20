/**
 */
package org.iobserve.model.correspondence;

import org.palladiosimulator.pcm.repository.RepositoryComponent;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence.ComponentEntry#getComponent <em>Component</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.correspondence.CorrespondencePackage#getComponentEntry()
 * @model
 * @generated
 */
public interface ComponentEntry extends AbstractEntry {
    /**
     * Returns the value of the '<em><b>Component</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Component</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Component</em>' reference.
     * @see #setComponent(RepositoryComponent)
     * @see org.iobserve.model.correspondence.CorrespondencePackage#getComponentEntry_Component()
     * @model required="true"
     * @generated
     */
    RepositoryComponent getComponent();

    /**
     * Sets the value of the '{@link org.iobserve.model.correspondence.ComponentEntry#getComponent <em>Component</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Component</em>' reference.
     * @see #getComponent()
     * @generated
     */
    void setComponent(RepositoryComponent value);

} // ComponentEntry
