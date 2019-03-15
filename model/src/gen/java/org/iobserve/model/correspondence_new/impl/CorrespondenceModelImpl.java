/**
 */
package org.iobserve.model.correspondence_new.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.iobserve.model.correspondence_new.ArchitecturalModel;
import org.iobserve.model.correspondence_new.Correspondence;
import org.iobserve.model.correspondence_new.CorrespondenceModel;
import org.iobserve.model.correspondence_new.Correspondence_newPackage;
import org.iobserve.model.correspondence_new.ImplementationArtifactSet;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Correspondence Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.model.correspondence_new.impl.CorrespondenceModelImpl#getCorrespondences <em>Correspondences</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence_new.impl.CorrespondenceModelImpl#getArchitecturalModel <em>Architectural Model</em>}</li>
 *   <li>{@link org.iobserve.model.correspondence_new.impl.CorrespondenceModelImpl#getImplementationArtifacts <em>Implementation Artifacts</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CorrespondenceModelImpl extends MinimalEObjectImpl.Container implements CorrespondenceModel {
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
	 * The cached value of the '{@link #getArchitecturalModel() <em>Architectural Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArchitecturalModel()
	 * @generated
	 * @ordered
	 */
	protected ArchitecturalModel architecturalModel;

	/**
	 * The cached value of the '{@link #getImplementationArtifacts() <em>Implementation Artifacts</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImplementationArtifacts()
	 * @generated
	 * @ordered
	 */
	protected ImplementationArtifactSet implementationArtifacts;

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
		return Correspondence_newPackage.Literals.CORRESPONDENCE_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Correspondence> getCorrespondences() {
		if (correspondences == null) {
			correspondences = new EObjectContainmentEList<Correspondence>(Correspondence.class, this, Correspondence_newPackage.CORRESPONDENCE_MODEL__CORRESPONDENCES);
		}
		return correspondences;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ArchitecturalModel getArchitecturalModel() {
		return architecturalModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetArchitecturalModel(ArchitecturalModel newArchitecturalModel, NotificationChain msgs) {
		ArchitecturalModel oldArchitecturalModel = architecturalModel;
		architecturalModel = newArchitecturalModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Correspondence_newPackage.CORRESPONDENCE_MODEL__ARCHITECTURAL_MODEL, oldArchitecturalModel, newArchitecturalModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setArchitecturalModel(ArchitecturalModel newArchitecturalModel) {
		if (newArchitecturalModel != architecturalModel) {
			NotificationChain msgs = null;
			if (architecturalModel != null)
				msgs = ((InternalEObject)architecturalModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Correspondence_newPackage.CORRESPONDENCE_MODEL__ARCHITECTURAL_MODEL, null, msgs);
			if (newArchitecturalModel != null)
				msgs = ((InternalEObject)newArchitecturalModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Correspondence_newPackage.CORRESPONDENCE_MODEL__ARCHITECTURAL_MODEL, null, msgs);
			msgs = basicSetArchitecturalModel(newArchitecturalModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Correspondence_newPackage.CORRESPONDENCE_MODEL__ARCHITECTURAL_MODEL, newArchitecturalModel, newArchitecturalModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImplementationArtifactSet getImplementationArtifacts() {
		return implementationArtifacts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetImplementationArtifacts(ImplementationArtifactSet newImplementationArtifacts, NotificationChain msgs) {
		ImplementationArtifactSet oldImplementationArtifacts = implementationArtifacts;
		implementationArtifacts = newImplementationArtifacts;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Correspondence_newPackage.CORRESPONDENCE_MODEL__IMPLEMENTATION_ARTIFACTS, oldImplementationArtifacts, newImplementationArtifacts);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setImplementationArtifacts(ImplementationArtifactSet newImplementationArtifacts) {
		if (newImplementationArtifacts != implementationArtifacts) {
			NotificationChain msgs = null;
			if (implementationArtifacts != null)
				msgs = ((InternalEObject)implementationArtifacts).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Correspondence_newPackage.CORRESPONDENCE_MODEL__IMPLEMENTATION_ARTIFACTS, null, msgs);
			if (newImplementationArtifacts != null)
				msgs = ((InternalEObject)newImplementationArtifacts).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Correspondence_newPackage.CORRESPONDENCE_MODEL__IMPLEMENTATION_ARTIFACTS, null, msgs);
			msgs = basicSetImplementationArtifacts(newImplementationArtifacts, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Correspondence_newPackage.CORRESPONDENCE_MODEL__IMPLEMENTATION_ARTIFACTS, newImplementationArtifacts, newImplementationArtifacts));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__CORRESPONDENCES:
				return ((InternalEList<?>)getCorrespondences()).basicRemove(otherEnd, msgs);
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__ARCHITECTURAL_MODEL:
				return basicSetArchitecturalModel(null, msgs);
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__IMPLEMENTATION_ARTIFACTS:
				return basicSetImplementationArtifacts(null, msgs);
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
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__CORRESPONDENCES:
				return getCorrespondences();
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__ARCHITECTURAL_MODEL:
				return getArchitecturalModel();
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__IMPLEMENTATION_ARTIFACTS:
				return getImplementationArtifacts();
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
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__CORRESPONDENCES:
				getCorrespondences().clear();
				getCorrespondences().addAll((Collection<? extends Correspondence>)newValue);
				return;
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__ARCHITECTURAL_MODEL:
				setArchitecturalModel((ArchitecturalModel)newValue);
				return;
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__IMPLEMENTATION_ARTIFACTS:
				setImplementationArtifacts((ImplementationArtifactSet)newValue);
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
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__CORRESPONDENCES:
				getCorrespondences().clear();
				return;
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__ARCHITECTURAL_MODEL:
				setArchitecturalModel((ArchitecturalModel)null);
				return;
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__IMPLEMENTATION_ARTIFACTS:
				setImplementationArtifacts((ImplementationArtifactSet)null);
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
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__CORRESPONDENCES:
				return correspondences != null && !correspondences.isEmpty();
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__ARCHITECTURAL_MODEL:
				return architecturalModel != null;
			case Correspondence_newPackage.CORRESPONDENCE_MODEL__IMPLEMENTATION_ARTIFACTS:
				return implementationArtifacts != null;
		}
		return super.eIsSet(featureID);
	}

} //CorrespondenceModelImpl
