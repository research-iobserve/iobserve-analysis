/**
 */
package org.iobserve.model.correspondence2.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.iobserve.model.correspondence2.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Correspondence2FactoryImpl extends EFactoryImpl implements Correspondence2Factory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Correspondence2Factory init() {
		try {
			Correspondence2Factory theCorrespondence2Factory = (Correspondence2Factory)EPackage.Registry.INSTANCE.getEFactory(Correspondence2Package.eNS_URI);
			if (theCorrespondence2Factory != null) {
				return theCorrespondence2Factory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Correspondence2FactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Correspondence2FactoryImpl() {
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
			case Correspondence2Package.HIGH_LEVEL_MODEL_ELEMENT: return createHighLevelModelElement();
			case Correspondence2Package.LOW_LEVEL_MODEL_ELEMENT: return createLowLevelModelElement();
			case Correspondence2Package.HIGH_LEVEL_MODEL: return createHighLevelModel();
			case Correspondence2Package.LOW_LEVEL_MODEL: return createLowLevelModel();
			case Correspondence2Package.CORRESPONDENCE: return createCorrespondence();
			case Correspondence2Package.CORRESPONDENCE_MODEL: return createCorrespondenceModel();
			case Correspondence2Package.MODEL_ELEMENT: return createModelElement();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HighLevelModelElement createHighLevelModelElement() {
		HighLevelModelElementImpl highLevelModelElement = new HighLevelModelElementImpl();
		return highLevelModelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LowLevelModelElement createLowLevelModelElement() {
		LowLevelModelElementImpl lowLevelModelElement = new LowLevelModelElementImpl();
		return lowLevelModelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HighLevelModel createHighLevelModel() {
		HighLevelModelImpl highLevelModel = new HighLevelModelImpl();
		return highLevelModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LowLevelModel createLowLevelModel() {
		LowLevelModelImpl lowLevelModel = new LowLevelModelImpl();
		return lowLevelModel;
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
	public CorrespondenceModel createCorrespondenceModel() {
		CorrespondenceModelImpl correspondenceModel = new CorrespondenceModelImpl();
		return correspondenceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelElement createModelElement() {
		ModelElementImpl modelElement = new ModelElementImpl();
		return modelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Correspondence2Package getCorrespondence2Package() {
		return (Correspondence2Package)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static Correspondence2Package getPackage() {
		return Correspondence2Package.eINSTANCE;
	}

} //Correspondence2FactoryImpl
