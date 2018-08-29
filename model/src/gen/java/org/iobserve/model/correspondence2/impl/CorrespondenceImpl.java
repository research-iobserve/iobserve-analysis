/**
 */
package org.iobserve.model.correspondence2.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;

import org.iobserve.model.correspondence2.Correspondence;
import org.iobserve.model.correspondence2.Correspondence2Package;
import org.iobserve.model.correspondence2.CorrespondenceModel;
import org.iobserve.model.correspondence2.HighLevelModelElement;
import org.iobserve.model.correspondence2.LowLevelModelElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Correspondence</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence2.impl.CorrespondenceImpl#getFrom <em>From</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence2.impl.CorrespondenceImpl#getTo <em>To</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence2.impl.CorrespondenceImpl#getCorrespondenceModel <em>Correspondence Model</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence2.impl.CorrespondenceImpl#getDebugStr <em>Debug Str</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CorrespondenceImpl extends MinimalEObjectImpl.Container implements Correspondence {
	/**
	 * The cached value of the '{@link #getFrom() <em>From</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFrom()
	 * @generated
	 * @ordered
	 */
	protected HighLevelModelElement from;

	/**
	 * The cached value of the '{@link #getTo() <em>To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTo()
	 * @generated
	 * @ordered
	 */
	protected LowLevelModelElement to;

	/**
	 * The default value of the '{@link #getDebugStr() <em>Debug Str</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDebugStr()
	 * @generated
	 * @ordered
	 */
	protected static final String DEBUG_STR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDebugStr() <em>Debug Str</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDebugStr()
	 * @generated
	 * @ordered
	 */
	protected String debugStr = DEBUG_STR_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CorrespondenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Correspondence2Package.Literals.CORRESPONDENCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HighLevelModelElement getFrom() {
		if (from != null && from.eIsProxy()) {
			InternalEObject oldFrom = (InternalEObject)from;
			from = (HighLevelModelElement)eResolveProxy(oldFrom);
			if (from != oldFrom) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, Correspondence2Package.CORRESPONDENCE__FROM, oldFrom, from));
			}
		}
		return from;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HighLevelModelElement basicGetFrom() {
		return from;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFrom(HighLevelModelElement newFrom) {
		HighLevelModelElement oldFrom = from;
		from = newFrom;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Correspondence2Package.CORRESPONDENCE__FROM, oldFrom, from));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LowLevelModelElement getTo() {
		if (to != null && to.eIsProxy()) {
			InternalEObject oldTo = (InternalEObject)to;
			to = (LowLevelModelElement)eResolveProxy(oldTo);
			if (to != oldTo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, Correspondence2Package.CORRESPONDENCE__TO, oldTo, to));
			}
		}
		return to;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LowLevelModelElement basicGetTo() {
		return to;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTo(LowLevelModelElement newTo) {
		LowLevelModelElement oldTo = to;
		to = newTo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Correspondence2Package.CORRESPONDENCE__TO, oldTo, to));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CorrespondenceModel getCorrespondenceModel() {
		if (eContainerFeatureID() != Correspondence2Package.CORRESPONDENCE__CORRESPONDENCE_MODEL) return null;
		return (CorrespondenceModel)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCorrespondenceModel(CorrespondenceModel newCorrespondenceModel, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newCorrespondenceModel, Correspondence2Package.CORRESPONDENCE__CORRESPONDENCE_MODEL, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCorrespondenceModel(CorrespondenceModel newCorrespondenceModel) {
		if (newCorrespondenceModel != eInternalContainer() || (eContainerFeatureID() != Correspondence2Package.CORRESPONDENCE__CORRESPONDENCE_MODEL && newCorrespondenceModel != null)) {
			if (EcoreUtil.isAncestor(this, newCorrespondenceModel))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newCorrespondenceModel != null)
				msgs = ((InternalEObject)newCorrespondenceModel).eInverseAdd(this, Correspondence2Package.CORRESPONDENCE_MODEL__CORRESPONDENCES, CorrespondenceModel.class, msgs);
			msgs = basicSetCorrespondenceModel(newCorrespondenceModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Correspondence2Package.CORRESPONDENCE__CORRESPONDENCE_MODEL, newCorrespondenceModel, newCorrespondenceModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDebugStr() {
		return debugStr;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDebugStr(String newDebugStr) {
		String oldDebugStr = debugStr;
		debugStr = newDebugStr;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Correspondence2Package.CORRESPONDENCE__DEBUG_STR, oldDebugStr, debugStr));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Correspondence2Package.CORRESPONDENCE__CORRESPONDENCE_MODEL:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetCorrespondenceModel((CorrespondenceModel)otherEnd, msgs);
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
			case Correspondence2Package.CORRESPONDENCE__CORRESPONDENCE_MODEL:
				return basicSetCorrespondenceModel(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case Correspondence2Package.CORRESPONDENCE__CORRESPONDENCE_MODEL:
				return eInternalContainer().eInverseRemove(this, Correspondence2Package.CORRESPONDENCE_MODEL__CORRESPONDENCES, CorrespondenceModel.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Correspondence2Package.CORRESPONDENCE__FROM:
				if (resolve) return getFrom();
				return basicGetFrom();
			case Correspondence2Package.CORRESPONDENCE__TO:
				if (resolve) return getTo();
				return basicGetTo();
			case Correspondence2Package.CORRESPONDENCE__CORRESPONDENCE_MODEL:
				return getCorrespondenceModel();
			case Correspondence2Package.CORRESPONDENCE__DEBUG_STR:
				return getDebugStr();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Correspondence2Package.CORRESPONDENCE__FROM:
				setFrom((HighLevelModelElement)newValue);
				return;
			case Correspondence2Package.CORRESPONDENCE__TO:
				setTo((LowLevelModelElement)newValue);
				return;
			case Correspondence2Package.CORRESPONDENCE__CORRESPONDENCE_MODEL:
				setCorrespondenceModel((CorrespondenceModel)newValue);
				return;
			case Correspondence2Package.CORRESPONDENCE__DEBUG_STR:
				setDebugStr((String)newValue);
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
			case Correspondence2Package.CORRESPONDENCE__FROM:
				setFrom((HighLevelModelElement)null);
				return;
			case Correspondence2Package.CORRESPONDENCE__TO:
				setTo((LowLevelModelElement)null);
				return;
			case Correspondence2Package.CORRESPONDENCE__CORRESPONDENCE_MODEL:
				setCorrespondenceModel((CorrespondenceModel)null);
				return;
			case Correspondence2Package.CORRESPONDENCE__DEBUG_STR:
				setDebugStr(DEBUG_STR_EDEFAULT);
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
			case Correspondence2Package.CORRESPONDENCE__FROM:
				return from != null;
			case Correspondence2Package.CORRESPONDENCE__TO:
				return to != null;
			case Correspondence2Package.CORRESPONDENCE__CORRESPONDENCE_MODEL:
				return getCorrespondenceModel() != null;
			case Correspondence2Package.CORRESPONDENCE__DEBUG_STR:
				return DEBUG_STR_EDEFAULT == null ? debugStr != null : !DEBUG_STR_EDEFAULT.equals(debugStr);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (debugStr: ");
		result.append(debugStr);
		result.append(')');
		return result.toString();
	}

} //CorrespondenceImpl
