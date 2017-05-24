/**
 */
package org.iobserve.planning.cloudprofile;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cloud Resource Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.cloudprofile.CloudResourceType#getPricePerHour <em>Price Per Hour</em>}</li>
 *   <li>{@link org.iobserve.planning.cloudprofile.CloudResourceType#getLocation <em>Location</em>}</li>
 *   <li>{@link org.iobserve.planning.cloudprofile.CloudResourceType#getName <em>Name</em>}</li>
 *   <li>{@link org.iobserve.planning.cloudprofile.CloudResourceType#getProvider <em>Provider</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudResourceType()
 * @model abstract="true"
 * @generated
 */
public interface CloudResourceType extends EObject {
	/**
	 * Returns the value of the '<em><b>Price Per Hour</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price Per Hour</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Per Hour</em>' attribute.
	 * @see #setPricePerHour(double)
	 * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudResourceType_PricePerHour()
	 * @model
	 * @generated
	 */
	double getPricePerHour();

	/**
	 * Sets the value of the '{@link org.iobserve.planning.cloudprofile.CloudResourceType#getPricePerHour <em>Price Per Hour</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Per Hour</em>' attribute.
	 * @see #getPricePerHour()
	 * @generated
	 */
	void setPricePerHour(double value);

	/**
	 * Returns the value of the '<em><b>Location</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Location</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location</em>' attribute.
	 * @see #setLocation(String)
	 * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudResourceType_Location()
	 * @model default="-1"
	 * @generated
	 */
	String getLocation();

	/**
	 * Sets the value of the '{@link org.iobserve.planning.cloudprofile.CloudResourceType#getLocation <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' attribute.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudResourceType_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.iobserve.planning.cloudprofile.CloudResourceType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Provider</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Provider</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provider</em>' reference.
	 * @see #setProvider(CloudProvider)
	 * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudResourceType_Provider()
	 * @model required="true"
	 * @generated
	 */
	CloudProvider getProvider();

	/**
	 * Sets the value of the '{@link org.iobserve.planning.cloudprofile.CloudResourceType#getProvider <em>Provider</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Provider</em>' reference.
	 * @see #getProvider()
	 * @generated
	 */
	void setProvider(CloudProvider value);

} // CloudResourceType
