/**
 */
package org.iobserve.analysis.model.correspondence.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.iobserve.analysis.model.correspondence.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CorrespondenceFactoryImpl extends EFactoryImpl implements CorrespondenceFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CorrespondenceFactory init() {
		try {
			CorrespondenceFactory theCorrespondenceFactory = (CorrespondenceFactory)EPackage.Registry.INSTANCE.getEFactory(CorrespondencePackage.eNS_URI);
			if (theCorrespondenceFactory != null) {
				return theCorrespondenceFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CorrespondenceFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CorrespondenceFactoryImpl() {
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
			case CorrespondencePackage.CORRESPONDENCE_MODEL: return createCorrespondenceModel();
			case CorrespondencePackage.CORRESPONDENCE: return createCorrespondence();
			case CorrespondencePackage.ARCHITECTURAL_MODEL: return createArchitecturalModel();
			case CorrespondencePackage.ARCHITECTURAL_MODEL_ELEMENT: return createArchitecturalModelElement();
			case CorrespondencePackage.IMPLEMENTATION_ARTIFACT_SET: return createImplementationArtifactSet();
			case CorrespondencePackage.IMPLEMENTATION_ARTIFACT: return createImplementationArtifact();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CorrespondenceModel createCorrespondenceModel() {
		CorrespondenceModelImpl correspondenceModel = new CorrespondenceModelImpl();
		return correspondenceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Correspondence createCorrespondence() {
		CorrespondenceImpl correspondence = new CorrespondenceImpl();
		return correspondence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ArchitecturalModel createArchitecturalModel() {
		ArchitecturalModelImpl architecturalModel = new ArchitecturalModelImpl();
		return architecturalModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ArchitecturalModelElement createArchitecturalModelElement() {
		ArchitecturalModelElementImpl architecturalModelElement = new ArchitecturalModelElementImpl();
		return architecturalModelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImplementationArtifactSet createImplementationArtifactSet() {
		ImplementationArtifactSetImpl implementationArtifactSet = new ImplementationArtifactSetImpl();
		return implementationArtifactSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImplementationArtifact createImplementationArtifact() {
		ImplementationArtifactImpl implementationArtifact = new ImplementationArtifactImpl();
		return implementationArtifact;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CorrespondencePackage getCorrespondencePackage() {
		return (CorrespondencePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CorrespondencePackage getPackage() {
		return CorrespondencePackage.eINSTANCE;
	}

} //CorrespondenceFactoryImpl
