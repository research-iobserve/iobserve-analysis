/**
 */
package org.iobserve.model.privacy;

import org.eclipse.emf.ecore.EObject;

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Geo Location</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.privacy.GeoLocation#getIsocode <em>Isocode</em>}</li>
 *   <li>{@link org.iobserve.model.privacy.GeoLocation#getResourceContainer <em>Resource Container</em>}</li>
 * </ul>
 *
 * @see org.iobserve.model.privacy.PrivacyPackage#getGeoLocation()
 * @model
 * @generated
 */
public interface GeoLocation extends EObject {
    /**
     * Returns the value of the '<em><b>Isocode</b></em>' attribute.
     * The literals are from the enumeration {@link org.iobserve.model.privacy.EISOCode}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Isocode</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Isocode</em>' attribute.
     * @see org.iobserve.model.privacy.EISOCode
     * @see #setIsocode(EISOCode)
     * @see org.iobserve.model.privacy.PrivacyPackage#getGeoLocation_Isocode()
     * @model
     * @generated
     */
    EISOCode getIsocode();

    /**
     * Sets the value of the '{@link org.iobserve.model.privacy.GeoLocation#getIsocode <em>Isocode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Isocode</em>' attribute.
     * @see org.iobserve.model.privacy.EISOCode
     * @see #getIsocode()
     * @generated
     */
    void setIsocode(EISOCode value);

    /**
     * Returns the value of the '<em><b>Resource Container</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resource Container</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resource Container</em>' reference.
     * @see #setResourceContainer(ResourceContainer)
     * @see org.iobserve.model.privacy.PrivacyPackage#getGeoLocation_ResourceContainer()
     * @model required="true" derived="true"
     * @generated
     */
    ResourceContainer getResourceContainer();

    /**
     * Sets the value of the '{@link org.iobserve.model.privacy.GeoLocation#getResourceContainer <em>Resource Container</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Resource Container</em>' reference.
     * @see #getResourceContainer()
     * @generated
     */
    void setResourceContainer(ResourceContainer value);

} // GeoLocation
