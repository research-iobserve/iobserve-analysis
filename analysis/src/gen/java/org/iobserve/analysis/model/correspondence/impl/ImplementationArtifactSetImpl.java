/**
 */
package org.iobserve.analysis.model.correspondence.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.iobserve.analysis.model.correspondence.CorrespondencePackage;
import org.iobserve.analysis.model.correspondence.ImplementationArtifact;
import org.iobserve.analysis.model.correspondence.ImplementationArtifactSet;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Implementation Artifact Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.analysis.model.correspondence.impl.ImplementationArtifactSetImpl#getArtifacts <em>Artifacts</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ImplementationArtifactSetImpl extends MinimalEObjectImpl.Container implements ImplementationArtifactSet {
	/**
	 * The cached value of the '{@link #getArtifacts() <em>Artifacts</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArtifacts()
	 * @generated
	 * @ordered
	 */
	protected EList<ImplementationArtifact> artifacts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ImplementationArtifactSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CorrespondencePackage.Literals.IMPLEMENTATION_ARTIFACT_SET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ImplementationArtifact> getArtifacts() {
		if (artifacts == null) {
			artifacts = new EObjectContainmentEList<ImplementationArtifact>(ImplementationArtifact.class, this, CorrespondencePackage.IMPLEMENTATION_ARTIFACT_SET__ARTIFACTS);
		}
		return artifacts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CorrespondencePackage.IMPLEMENTATION_ARTIFACT_SET__ARTIFACTS:
				return ((InternalEList<?>)getArtifacts()).basicRemove(otherEnd, msgs);
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
			case CorrespondencePackage.IMPLEMENTATION_ARTIFACT_SET__ARTIFACTS:
				return getArtifacts();
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
			case CorrespondencePackage.IMPLEMENTATION_ARTIFACT_SET__ARTIFACTS:
				getArtifacts().clear();
				getArtifacts().addAll((Collection<? extends ImplementationArtifact>)newValue);
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
			case CorrespondencePackage.IMPLEMENTATION_ARTIFACT_SET__ARTIFACTS:
				getArtifacts().clear();
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
			case CorrespondencePackage.IMPLEMENTATION_ARTIFACT_SET__ARTIFACTS:
				return artifacts != null && !artifacts.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ImplementationArtifactSetImpl
