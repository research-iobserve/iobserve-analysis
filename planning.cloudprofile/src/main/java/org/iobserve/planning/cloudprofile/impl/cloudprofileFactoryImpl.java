/**
 */
package org.iobserve.planning.cloudprofile.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.iobserve.planning.cloudprofile.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class cloudprofileFactoryImpl extends EFactoryImpl implements cloudprofileFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static cloudprofileFactory init() {
		try {
			cloudprofileFactory thecloudprofileFactory = (cloudprofileFactory)EPackage.Registry.INSTANCE.getEFactory(cloudprofilePackage.eNS_URI);
			if (thecloudprofileFactory != null) {
				return thecloudprofileFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new cloudprofileFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public cloudprofileFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case cloudprofilePackage.CLOUD_PROFILE: return createCloudProfile();
			case cloudprofilePackage.CLOUD_PROVIDER: return createCloudProvider();
			case cloudprofilePackage.VM_TYPE: return createVMType();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CloudProfile createCloudProfile() {
		CloudProfileImpl cloudProfile = new CloudProfileImpl();
		return cloudProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CloudProvider createCloudProvider() {
		CloudProviderImpl cloudProvider = new CloudProviderImpl();
		return cloudProvider;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VMType createVMType() {
		VMTypeImpl vmType = new VMTypeImpl();
		return vmType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public cloudprofilePackage getcloudprofilePackage() {
		return (cloudprofilePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static cloudprofilePackage getPackage() {
		return cloudprofilePackage.eINSTANCE;
	}

} //cloudprofileFactoryImpl
