/**
 */
package org.iobserve.model.correspondence2.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.iobserve.model.correspondence2.Correspondence;
import org.iobserve.model.correspondence2.Correspondence2Package;
import org.iobserve.model.correspondence2.CorrespondenceModel;
import org.iobserve.model.correspondence2.HighLevelModel;
import org.iobserve.model.correspondence2.LowLevelModel;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Correspondence Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence2.impl.CorrespondenceModelImpl#getHighLevelModel <em>High Level Model</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence2.impl.CorrespondenceModelImpl#getLowLevelModel <em>Low Level Model</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence2.impl.CorrespondenceModelImpl#getCorrespondences <em>Correspondences</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CorrespondenceModelImpl extends MinimalEObjectImpl.Container implements CorrespondenceModel {
	/**
	 * The cached value of the '{@link #getHighLevelModel() <em>High Level Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHighLevelModel()
	 * @generated
	 * @ordered
	 */
	protected HighLevelModel highLevelModel;

	/**
	 * The cached value of the '{@link #getLowLevelModel() <em>Low Level Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLowLevelModel()
	 * @generated
	 * @ordered
	 */
	protected LowLevelModel lowLevelModel;

	/**
	 * The cached value of the '{@link #getCorrespondences() <em>Correspondences</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCorrespondences()
	 * @generated
	 * @ordered
	 */
	protected EList<Correspondence> correspondences;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CorrespondenceModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Correspondence2Package.Literals.CORRESPONDENCE_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HighLevelModel getHighLevelModel() {
		if (highLevelModel != null && highLevelModel.eIsProxy()) {
			InternalEObject oldHighLevelModel = (InternalEObject)highLevelModel;
			highLevelModel = (HighLevelModel)eResolveProxy(oldHighLevelModel);
			if (highLevelModel != oldHighLevelModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, Correspondence2Package.CORRESPONDENCE_MODEL__HIGH_LEVEL_MODEL, oldHighLevelModel, highLevelModel));
			}
		}
		return highLevelModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HighLevelModel basicGetHighLevelModel() {
		return highLevelModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHighLevelModel(HighLevelModel newHighLevelModel) {
		HighLevelModel oldHighLevelModel = highLevelModel;
		highLevelModel = newHighLevelModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Correspondence2Package.CORRESPONDENCE_MODEL__HIGH_LEVEL_MODEL, oldHighLevelModel, highLevelModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LowLevelModel getLowLevelModel() {
		if (lowLevelModel != null && lowLevelModel.eIsProxy()) {
			InternalEObject oldLowLevelModel = (InternalEObject)lowLevelModel;
			lowLevelModel = (LowLevelModel)eResolveProxy(oldLowLevelModel);
			if (lowLevelModel != oldLowLevelModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, Correspondence2Package.CORRESPONDENCE_MODEL__LOW_LEVEL_MODEL, oldLowLevelModel, lowLevelModel));
			}
		}
		return lowLevelModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LowLevelModel basicGetLowLevelModel() {
		return lowLevelModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLowLevelModel(LowLevelModel newLowLevelModel) {
		LowLevelModel oldLowLevelModel = lowLevelModel;
		lowLevelModel = newLowLevelModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Correspondence2Package.CORRESPONDENCE_MODEL__LOW_LEVEL_MODEL, oldLowLevelModel, lowLevelModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Correspondence> getCorrespondences() {
		if (correspondences == null) {
			correspondences = new EObjectContainmentWithInverseEList<Correspondence>(Correspondence.class, this, Correspondence2Package.CORRESPONDENCE_MODEL__CORRESPONDENCES, Correspondence2Package.CORRESPONDENCE__CORRESPONDENCE_MODEL);
		}
		return correspondences;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Correspondence2Package.CORRESPONDENCE_MODEL__CORRESPONDENCES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getCorrespondences()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Correspondence2Package.CORRESPONDENCE_MODEL__CORRESPONDENCES:
				return ((InternalEList<?>)getCorrespondences()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Correspondence2Package.CORRESPONDENCE_MODEL__HIGH_LEVEL_MODEL:
				if (resolve) return getHighLevelModel();
				return basicGetHighLevelModel();
			case Correspondence2Package.CORRESPONDENCE_MODEL__LOW_LEVEL_MODEL:
				if (resolve) return getLowLevelModel();
				return basicGetLowLevelModel();
			case Correspondence2Package.CORRESPONDENCE_MODEL__CORRESPONDENCES:
				return getCorrespondences();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Correspondence2Package.CORRESPONDENCE_MODEL__HIGH_LEVEL_MODEL:
				setHighLevelModel((HighLevelModel)newValue);
				return;
			case Correspondence2Package.CORRESPONDENCE_MODEL__LOW_LEVEL_MODEL:
				setLowLevelModel((LowLevelModel)newValue);
				return;
			case Correspondence2Package.CORRESPONDENCE_MODEL__CORRESPONDENCES:
				getCorrespondences().clear();
				getCorrespondences().addAll((Collection<? extends Correspondence>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case Correspondence2Package.CORRESPONDENCE_MODEL__HIGH_LEVEL_MODEL:
				setHighLevelModel((HighLevelModel)null);
				return;
			case Correspondence2Package.CORRESPONDENCE_MODEL__LOW_LEVEL_MODEL:
				setLowLevelModel((LowLevelModel)null);
				return;
			case Correspondence2Package.CORRESPONDENCE_MODEL__CORRESPONDENCES:
				getCorrespondences().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Correspondence2Package.CORRESPONDENCE_MODEL__HIGH_LEVEL_MODEL:
				return highLevelModel != null;
			case Correspondence2Package.CORRESPONDENCE_MODEL__LOW_LEVEL_MODEL:
				return lowLevelModel != null;
			case Correspondence2Package.CORRESPONDENCE_MODEL__CORRESPONDENCES:
				return correspondences != null && !correspondences.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //CorrespondenceModelImpl
