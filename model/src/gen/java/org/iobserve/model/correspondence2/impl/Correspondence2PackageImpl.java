/**
 */
package org.iobserve.model.correspondence2.impl;

import de.uka.ipd.sdq.identifier.IdentifierPackage;
import de.uka.ipd.sdq.probfunction.ProbfunctionPackage;
import de.uka.ipd.sdq.stoex.StoexPackage;
import de.uka.ipd.sdq.units.UnitsPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.iobserve.model.correspondence2.Correspondence;
import org.iobserve.model.correspondence2.Correspondence2Factory;
import org.iobserve.model.correspondence2.Correspondence2Package;
import org.iobserve.model.correspondence2.CorrespondenceModel;
import org.iobserve.model.correspondence2.HighLevelModel;
import org.iobserve.model.correspondence2.HighLevelModelElement;
import org.iobserve.model.correspondence2.LowLevelModel;
import org.iobserve.model.correspondence2.LowLevelModelElement;
import org.iobserve.model.correspondence2.ModelElement;
import org.palladiosimulator.pcm.PcmPackage;
import org.palladiosimulator.pcm.core.entity.EntityPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Correspondence2PackageImpl extends EPackageImpl implements Correspondence2Package {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass highLevelModelElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lowLevelModelElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass highLevelModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass lowLevelModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass correspondenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass correspondenceModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modelElementEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.iobserve.model.correspondence2.Correspondence2Package#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private Correspondence2PackageImpl() {
		super(eNS_URI, Correspondence2Factory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link Correspondence2Package#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static Correspondence2Package init() {
		if (isInited) return (Correspondence2Package)EPackage.Registry.INSTANCE.getEPackage(Correspondence2Package.eNS_URI);

		// Obtain or create and register package
		Correspondence2PackageImpl theCorrespondence2Package = (Correspondence2PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Correspondence2PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Correspondence2PackageImpl());

		isInited = true;

		// Initialize simple dependencies
		IdentifierPackage.eINSTANCE.eClass();
		PcmPackage.eINSTANCE.eClass();
		ProbfunctionPackage.eINSTANCE.eClass();
		StoexPackage.eINSTANCE.eClass();
		UnitsPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theCorrespondence2Package.createPackageContents();

		// Initialize created meta-data
		theCorrespondence2Package.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theCorrespondence2Package.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(Correspondence2Package.eNS_URI, theCorrespondence2Package);
		return theCorrespondence2Package;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getHighLevelModelElement() {
		return highLevelModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getHighLevelModelElement_Model() {
		return (EReference)highLevelModelElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getHighLevelModelElement_Parent() {
		return (EReference)highLevelModelElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLowLevelModelElement() {
		return lowLevelModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLowLevelModelElement_Model() {
		return (EReference)lowLevelModelElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLowLevelModelElement_Parent() {
		return (EReference)lowLevelModelElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getHighLevelModel() {
		return highLevelModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getHighLevelModel_Elements() {
		return (EReference)highLevelModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLowLevelModel() {
		return lowLevelModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLowLevelModel_Elements() {
		return (EReference)lowLevelModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCorrespondence() {
		return correspondenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCorrespondence_From() {
		return (EReference)correspondenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCorrespondence_To() {
		return (EReference)correspondenceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCorrespondence_CorrespondenceModel() {
		return (EReference)correspondenceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCorrespondence_DebugStr() {
		return (EAttribute)correspondenceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCorrespondenceModel() {
		return correspondenceModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCorrespondenceModel_HighLevelModel() {
		return (EReference)correspondenceModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCorrespondenceModel_LowLevelModel() {
		return (EReference)correspondenceModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCorrespondenceModel_Correspondences() {
		return (EReference)correspondenceModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getModelElement() {
		return modelElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Correspondence2Factory getCorrespondence2Factory() {
		return (Correspondence2Factory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		highLevelModelElementEClass = createEClass(HIGH_LEVEL_MODEL_ELEMENT);
		createEReference(highLevelModelElementEClass, HIGH_LEVEL_MODEL_ELEMENT__MODEL);
		createEReference(highLevelModelElementEClass, HIGH_LEVEL_MODEL_ELEMENT__PARENT);

		lowLevelModelElementEClass = createEClass(LOW_LEVEL_MODEL_ELEMENT);
		createEReference(lowLevelModelElementEClass, LOW_LEVEL_MODEL_ELEMENT__MODEL);
		createEReference(lowLevelModelElementEClass, LOW_LEVEL_MODEL_ELEMENT__PARENT);

		highLevelModelEClass = createEClass(HIGH_LEVEL_MODEL);
		createEReference(highLevelModelEClass, HIGH_LEVEL_MODEL__ELEMENTS);

		lowLevelModelEClass = createEClass(LOW_LEVEL_MODEL);
		createEReference(lowLevelModelEClass, LOW_LEVEL_MODEL__ELEMENTS);

		correspondenceEClass = createEClass(CORRESPONDENCE);
		createEReference(correspondenceEClass, CORRESPONDENCE__FROM);
		createEReference(correspondenceEClass, CORRESPONDENCE__TO);
		createEReference(correspondenceEClass, CORRESPONDENCE__CORRESPONDENCE_MODEL);
		createEAttribute(correspondenceEClass, CORRESPONDENCE__DEBUG_STR);

		correspondenceModelEClass = createEClass(CORRESPONDENCE_MODEL);
		createEReference(correspondenceModelEClass, CORRESPONDENCE_MODEL__HIGH_LEVEL_MODEL);
		createEReference(correspondenceModelEClass, CORRESPONDENCE_MODEL__LOW_LEVEL_MODEL);
		createEReference(correspondenceModelEClass, CORRESPONDENCE_MODEL__CORRESPONDENCES);

		modelElementEClass = createEClass(MODEL_ELEMENT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		EntityPackage theEntityPackage = (EntityPackage)EPackage.Registry.INSTANCE.getEPackage(EntityPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		highLevelModelElementEClass.getESuperTypes().add(this.getModelElement());
		lowLevelModelElementEClass.getESuperTypes().add(this.getModelElement());
		modelElementEClass.getESuperTypes().add(theEntityPackage.getEntity());

		// Initialize classes and features; add operations and parameters
		initEClass(highLevelModelElementEClass, HighLevelModelElement.class, "HighLevelModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getHighLevelModelElement_Model(), this.getHighLevelModel(), this.getHighLevelModel_Elements(), "model", null, 0, 1, HighLevelModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getHighLevelModelElement_Parent(), this.getHighLevelModelElement(), null, "parent", null, 0, 1, HighLevelModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lowLevelModelElementEClass, LowLevelModelElement.class, "LowLevelModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLowLevelModelElement_Model(), this.getLowLevelModel(), this.getLowLevelModel_Elements(), "model", null, 0, 1, LowLevelModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLowLevelModelElement_Parent(), this.getLowLevelModelElement(), null, "parent", null, 0, 1, LowLevelModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(highLevelModelEClass, HighLevelModel.class, "HighLevelModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getHighLevelModel_Elements(), this.getHighLevelModelElement(), this.getHighLevelModelElement_Model(), "elements", null, 1, -1, HighLevelModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(lowLevelModelEClass, LowLevelModel.class, "LowLevelModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLowLevelModel_Elements(), this.getLowLevelModelElement(), this.getLowLevelModelElement_Model(), "elements", null, 1, -1, LowLevelModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(correspondenceEClass, Correspondence.class, "Correspondence", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCorrespondence_From(), this.getHighLevelModelElement(), null, "from", null, 1, 1, Correspondence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCorrespondence_To(), this.getLowLevelModelElement(), null, "to", null, 1, 1, Correspondence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCorrespondence_CorrespondenceModel(), this.getCorrespondenceModel(), this.getCorrespondenceModel_Correspondences(), "correspondenceModel", null, 0, 1, Correspondence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCorrespondence_DebugStr(), ecorePackage.getEString(), "debugStr", null, 0, 1, Correspondence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(correspondenceModelEClass, CorrespondenceModel.class, "CorrespondenceModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCorrespondenceModel_HighLevelModel(), this.getHighLevelModel(), null, "highLevelModel", null, 1, 1, CorrespondenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCorrespondenceModel_LowLevelModel(), this.getLowLevelModel(), null, "lowLevelModel", null, 1, 1, CorrespondenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCorrespondenceModel_Correspondences(), this.getCorrespondence(), this.getCorrespondence_CorrespondenceModel(), "correspondences", null, 1, -1, CorrespondenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(modelElementEClass, ModelElement.class, "ModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //Correspondence2PackageImpl
