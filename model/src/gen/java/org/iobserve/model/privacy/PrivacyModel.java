/**
 */
package org.iobserve.model.privacy;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.privacy.PrivacyModel#getResourceContainerLocations <em>Resource Container Locations</em>}</li>
 *   <li>{@link org.iobserve.model.privacy.PrivacyModel#getPrivacyLevels <em>Privacy Levels</em>}</li>
 *   <li>{@link org.iobserve.model.privacy.PrivacyModel#getEncapsulatedDataSources <em>Encapsulated Data Sources</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.privacy.PrivacyPackage#getPrivacyModel()
 * @model
 * @generated
 */
public interface PrivacyModel extends EObject {
    /**
     * Returns the value of the '<em><b>Resource Container Locations</b></em>' containment reference list.
     * The list contents are of type {@link org.iobserve.model.privacy.GeoLocation}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resource Container Locations</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resource Container Locations</em>' containment reference list.
     * @see org.iobserve.model.privacy.PrivacyPackage#getPrivacyModel_ResourceContainerLocations()
     * @model containment="true"
     * @generated
     */
    EList<GeoLocation> getResourceContainerLocations();

    /**
     * Returns the value of the '<em><b>Privacy Levels</b></em>' containment reference list.
     * The list contents are of type {@link org.iobserve.model.privacy.IPrivacyAnnotation}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Privacy Levels</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Privacy Levels</em>' containment reference list.
     * @see org.iobserve.model.privacy.PrivacyPackage#getPrivacyModel_PrivacyLevels()
     * @model containment="true"
     * @generated
     */
    EList<IPrivacyAnnotation> getPrivacyLevels();

    /**
     * Returns the value of the '<em><b>Encapsulated Data Sources</b></em>' containment reference list.
     * The list contents are of type {@link org.iobserve.model.privacy.EncapsulatedDataSource}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Encapsulated Data Sources</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Encapsulated Data Sources</em>' containment reference list.
     * @see org.iobserve.model.privacy.PrivacyPackage#getPrivacyModel_EncapsulatedDataSources()
     * @model containment="true"
     * @generated
     */
    EList<EncapsulatedDataSource> getEncapsulatedDataSources();

} // PrivacyModel
