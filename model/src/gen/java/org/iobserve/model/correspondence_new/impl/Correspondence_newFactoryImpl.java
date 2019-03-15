/**
 */
package org.iobserve.model.correspondence_new.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.iobserve.model.correspondence_new.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Correspondence_newFactoryImpl extends EFactoryImpl implements Correspondence_newFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Correspondence_newFactory init() {
		try {
			Correspondence_newFactory theCorrespondence_newFactory = (Correspondence_newFactory)EPackage.Registry.INSTANCE.getEFactory(Correspondence_newPackage.eNS_URI);
			if (theCorrespondence_newFactory != null) {
				return theCorrespondence_newFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Correspondence_newFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Correspondence_newFactoryImpl() {
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
			case Correspondence_newPackage.CORRESPONDENCE_MODEL: return createCorrespondenceModel();
			case Correspondence_newPackage.CORRESPONDENCE: return createCorrespondence();
			case Correspondence_newPackage.ARCHITECTURAL_MODEL: return createArchitecturalModel();
			case Correspondence_newPackage.ARCHITECTURAL_MODEL_ELEMENT: return createArchitecturalModelElement();
			case Correspondence_newPackage.IMPLEMENTATION_ARTIFACT_SET: return createImplementationArtifactSet();
			case Correspondence_newPackage.IMPLEMENTATION_ARTIFACT: return createImplementationArtifact();
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
	public Correspondence_newPackage getCorrespondence_newPackage() {
		return (Correspondence_newPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static Correspondence_newPackage getPackage() {
		return Correspondence_newPackage.eINSTANCE;
	}

} //Correspondence_newFactoryImpl
