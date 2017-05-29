/**
 */
package org.iobserve.planning.cloudprofile;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cloud Profile</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.cloudprofile.CloudProfile#getCloudProviders <em>Cloud Providers</em>}</li>
 *   <li>{@link org.iobserve.planning.cloudprofile.CloudProfile#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudProfile()
 * @model
 * @generated
 */
public interface CloudProfile extends EObject {
	/**
	 * Returns the value of the '<em><b>Cloud Providers</b></em>' containment reference list.
	 * The list contents are of type {@link org.iobserve.planning.cloudprofile.CloudProvider}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cloud Providers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cloud Providers</em>' containment reference list.
	 * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudProfile_CloudProviders()
	 * @model containment="true"
	 * @generated
	 */
	EList<CloudProvider> getCloudProviders();

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
	 * @see org.iobserve.planning.cloudprofile.cloudprofilePackage#getCloudProfile_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.iobserve.planning.cloudprofile.CloudProfile#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // CloudProfile
