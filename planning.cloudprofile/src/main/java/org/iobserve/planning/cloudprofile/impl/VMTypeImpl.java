/**
 */
package org.iobserve.planning.cloudprofile.impl;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.cloudprofile.VMType;
import org.iobserve.planning.cloudprofile.cloudprofilePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>VM Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl#getMaxCores <em>Max Cores</em>}</li>
 *   <li>{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl#getMinCores <em>Min Cores</em>}</li>
 *   <li>{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl#getMinRAM <em>Min RAM</em>}</li>
 *   <li>{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl#getMaxRAM <em>Max RAM</em>}</li>
 *   <li>{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl#getMinProcessingRate <em>Min Processing Rate</em>}</li>
 *   <li>{@link org.iobserve.planning.cloudprofile.impl.VMTypeImpl#getMaxProcessingRate <em>Max Processing Rate</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VMTypeImpl extends CloudResourceTypeImpl implements VMType {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VMTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return cloudprofilePackage.Literals.VM_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxCores() {
		return (Integer)eGet(cloudprofilePackage.Literals.VM_TYPE__MAX_CORES, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxCores(int newMaxCores) {
		eSet(cloudprofilePackage.Literals.VM_TYPE__MAX_CORES, newMaxCores);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinCores() {
		return (Integer)eGet(cloudprofilePackage.Literals.VM_TYPE__MIN_CORES, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinCores(int newMinCores) {
		eSet(cloudprofilePackage.Literals.VM_TYPE__MIN_CORES, newMinCores);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getMinRAM() {
		return (Float)eGet(cloudprofilePackage.Literals.VM_TYPE__MIN_RAM, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinRAM(float newMinRAM) {
		eSet(cloudprofilePackage.Literals.VM_TYPE__MIN_RAM, newMinRAM);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getMaxRAM() {
		return (Float)eGet(cloudprofilePackage.Literals.VM_TYPE__MAX_RAM, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxRAM(float newMaxRAM) {
		eSet(cloudprofilePackage.Literals.VM_TYPE__MAX_RAM, newMaxRAM);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getMinProcessingRate() {
		return (Float)eGet(cloudprofilePackage.Literals.VM_TYPE__MIN_PROCESSING_RATE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinProcessingRate(float newMinProcessingRate) {
		eSet(cloudprofilePackage.Literals.VM_TYPE__MIN_PROCESSING_RATE, newMinProcessingRate);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getMaxProcessingRate() {
		return (Float)eGet(cloudprofilePackage.Literals.VM_TYPE__MAX_PROCESSING_RATE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxProcessingRate(float newMaxProcessingRate) {
		eSet(cloudprofilePackage.Literals.VM_TYPE__MAX_PROCESSING_RATE, newMaxProcessingRate);
	}

} //VMTypeImpl
