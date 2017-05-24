/**
 */
package org.iobserve.planning.cloudprofile.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.iobserve.planning.cloudprofile.CloudProvider;
import org.iobserve.planning.cloudprofile.CloudResourceType;
import org.iobserve.planning.cloudprofile.cloudprofilePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cloud Resource Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.cloudprofile.impl.CloudResourceTypeImpl#getPricePerHour <em>Price Per Hour</em>}</li>
 *   <li>{@link org.iobserve.planning.cloudprofile.impl.CloudResourceTypeImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.iobserve.planning.cloudprofile.impl.CloudResourceTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.iobserve.planning.cloudprofile.impl.CloudResourceTypeImpl#getProvider <em>Provider</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class CloudResourceTypeImpl extends MinimalEObjectImpl.Container implements CloudResourceType {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CloudResourceTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected int eStaticFeatureCount() {
		return 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getPricePerHour() {
		return (Double)eGet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__PRICE_PER_HOUR, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPricePerHour(double newPricePerHour) {
		eSet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__PRICE_PER_HOUR, newPricePerHour);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLocation() {
		return (String)eGet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__LOCATION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocation(String newLocation) {
		eSet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__LOCATION, newLocation);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return (String)eGet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__NAME, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		eSet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__NAME, newName);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CloudProvider getProvider() {
		return (CloudProvider)eGet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__PROVIDER, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProvider(CloudProvider newProvider) {
		eSet(cloudprofilePackage.Literals.CLOUD_RESOURCE_TYPE__PROVIDER, newProvider);
	}

} //CloudResourceTypeImpl
