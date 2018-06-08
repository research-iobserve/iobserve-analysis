/**
 */
package org.iobserve.model.privacy;

import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Encapsulated Data Source</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.privacy.EncapsulatedDataSource#isDataSource <em>Data Source</em>}</li>
 *   <li>{@link org.iobserve.model.privacy.EncapsulatedDataSource#getComponent <em>Component</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.privacy.PrivacyPackage#getEncapsulatedDataSource()
 * @model
 * @generated
 */
public interface EncapsulatedDataSource extends EObject {
    /**
     * Returns the value of the '<em><b>Data Source</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Data Source</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Data Source</em>' attribute.
     * @see #setDataSource(boolean)
     * @see org.iobserve.model.privacy.PrivacyPackage#getEncapsulatedDataSource_DataSource()
     * @model
     * @generated
     */
    boolean isDataSource();

    /**
     * Sets the value of the '{@link org.iobserve.model.privacy.EncapsulatedDataSource#isDataSource <em>Data Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Source</em>' attribute.
     * @see #isDataSource()
     * @generated
     */
    void setDataSource(boolean value);

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
     * @see org.iobserve.model.privacy.PrivacyPackage#getEncapsulatedDataSource_Component()
     * @model required="true"
     * @generated
     */
    RepositoryComponent getComponent();

    /**
     * Sets the value of the '{@link org.iobserve.model.privacy.EncapsulatedDataSource#getComponent <em>Component</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Component</em>' reference.
     * @see #getComponent()
     * @generated
     */
    void setComponent(RepositoryComponent value);

} // EncapsulatedDataSource
