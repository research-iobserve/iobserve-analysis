/**
 */
package org.iobserve.model.correspondence2.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.iobserve.model.correspondence2.Correspondence2Package;
import org.iobserve.model.correspondence2.LowLevelModel;
import org.iobserve.model.correspondence2.LowLevelModelElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Low Level Model Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence2.impl.LowLevelModelElementImpl#getModel <em>Model</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence2.impl.LowLevelModelElementImpl#getParent <em>Parent</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LowLevelModelElementImpl extends ModelElementImpl implements LowLevelModelElement {
	/**
	 * The cached value of the '{@link #getParent() <em>Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParent()
	 * @generated
	 * @ordered
	 */
	protected LowLevelModelElement parent;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LowLevelModelElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Correspondence2Package.Literals.LOW_LEVEL_MODEL_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LowLevelModel getModel() {
		if (eContainerFeatureID() != Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__MODEL) return null;
		return (LowLevelModel)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetModel(LowLevelModel newModel, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newModel, Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__MODEL, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModel(LowLevelModel newModel) {
		if (newModel != eInternalContainer() || (eContainerFeatureID() != Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__MODEL && newModel != null)) {
			if (EcoreUtil.isAncestor(this, newModel))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newModel != null)
				msgs = ((InternalEObject)newModel).eInverseAdd(this, Correspondence2Package.LOW_LEVEL_MODEL__ELEMENTS, LowLevelModel.class, msgs);
			msgs = basicSetModel(newModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__MODEL, newModel, newModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LowLevelModelElement getParent() {
		if (parent != null && parent.eIsProxy()) {
			InternalEObject oldParent = (InternalEObject)parent;
			parent = (LowLevelModelElement)eResolveProxy(oldParent);
			if (parent != oldParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__PARENT, oldParent, parent));
			}
		}
		return parent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LowLevelModelElement basicGetParent() {
		return parent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParent(LowLevelModelElement newParent) {
		LowLevelModelElement oldParent = parent;
		parent = newParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__PARENT, oldParent, parent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__MODEL:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetModel((LowLevelModel)otherEnd, msgs);
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
			case Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__MODEL:
				return basicSetModel(null, msgs);
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
			case Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__MODEL:
				return eInternalContainer().eInverseRemove(this, Correspondence2Package.LOW_LEVEL_MODEL__ELEMENTS, LowLevelModel.class, msgs);
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
			case Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__MODEL:
				return getModel();
			case Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__PARENT:
				if (resolve) return getParent();
				return basicGetParent();
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
			case Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__MODEL:
				setModel((LowLevelModel)newValue);
				return;
			case Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__PARENT:
				setParent((LowLevelModelElement)newValue);
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
			case Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__MODEL:
				setModel((LowLevelModel)null);
				return;
			case Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__PARENT:
				setParent((LowLevelModelElement)null);
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
			case Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__MODEL:
				return getModel() != null;
			case Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT__PARENT:
				return parent != null;
		}
		return super.eIsSet(featureID);
	}

} //LowLevelModelElementImpl
