/**
 */
package org.iobserve.planning.cloudprofile.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.iobserve.planning.cloudprofile.CloudProfile;
import org.iobserve.planning.cloudprofile.CloudProvider;
import org.iobserve.planning.cloudprofile.cloudprofilePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cloud Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.cloudprofile.impl.CloudProfileImpl#getCloudProviders <em>Cloud Providers</em>}</li>
 *   <li>{@link org.iobserve.planning.cloudprofile.impl.CloudProfileImpl#getName <em>Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CloudProfileImpl extends MinimalEObjectImpl.Container implements CloudProfile {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CloudProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return cloudprofilePackage.Literals.CLOUD_PROFILE;
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
	@SuppressWarnings("unchecked")
	public EList<CloudProvider> getCloudProviders() {
		return (EList<CloudProvider>)eGet(cloudprofilePackage.Literals.CLOUD_PROFILE__CLOUD_PROVIDERS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return (String)eGet(cloudprofilePackage.Literals.CLOUD_PROFILE__NAME, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		eSet(cloudprofilePackage.Literals.CLOUD_PROFILE__NAME, newName);
	}

} //CloudProfileImpl
