/**
 */
package org.iobserve.analysis.model.correspondence.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.iobserve.analysis.model.correspondence.CorrespondencePackage;
import org.iobserve.analysis.model.correspondence.ImplementationArtifact;

import org.palladiosimulator.pcm.core.entity.impl.EntityImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Implementation Artifact</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.analysis.model.correspondence.impl.ImplementationArtifactImpl#getArtifactId <em>Artifact Id</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ImplementationArtifactImpl extends EntityImpl implements ImplementationArtifact {
	/**
	 * The default value of the '{@link #getArtifactId() <em>Artifact Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArtifactId()
	 * @generated
	 * @ordered
	 */
	protected static final String ARTIFACT_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getArtifactId() <em>Artifact Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArtifactId()
	 * @generated
	 * @ordered
	 */
	protected String artifactId = ARTIFACT_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ImplementationArtifactImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CorrespondencePackage.Literals.IMPLEMENTATION_ARTIFACT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getArtifactId() {
		return artifactId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setArtifactId(String newArtifactId) {
		String oldArtifactId = artifactId;
		artifactId = newArtifactId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CorrespondencePackage.IMPLEMENTATION_ARTIFACT__ARTIFACT_ID, oldArtifactId, artifactId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CorrespondencePackage.IMPLEMENTATION_ARTIFACT__ARTIFACT_ID:
				return getArtifactId();
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
			case CorrespondencePackage.IMPLEMENTATION_ARTIFACT__ARTIFACT_ID:
				setArtifactId((String)newValue);
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
			case CorrespondencePackage.IMPLEMENTATION_ARTIFACT__ARTIFACT_ID:
				setArtifactId(ARTIFACT_ID_EDEFAULT);
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
			case CorrespondencePackage.IMPLEMENTATION_ARTIFACT__ARTIFACT_ID:
				return ARTIFACT_ID_EDEFAULT == null ? artifactId != null : !ARTIFACT_ID_EDEFAULT.equals(artifactId);
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
		result.append(" (artifactId: ");
		result.append(artifactId);
		result.append(')');
		return result.toString();
	}

} //ImplementationArtifactImpl
