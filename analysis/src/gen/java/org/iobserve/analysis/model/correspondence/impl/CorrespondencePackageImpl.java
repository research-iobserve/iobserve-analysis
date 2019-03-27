/**
 */
package org.iobserve.analysis.model.correspondence.impl;

import de.uka.ipd.sdq.identifier.IdentifierPackage;

import de.uka.ipd.sdq.probfunction.ProbfunctionPackage;

import de.uka.ipd.sdq.stoex.StoexPackage;

import de.uka.ipd.sdq.units.UnitsPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.iobserve.analysis.model.correspondence.ArchitecturalModel;
import org.iobserve.analysis.model.correspondence.ArchitecturalModelElement;
import org.iobserve.analysis.model.correspondence.Correspondence;
import org.iobserve.analysis.model.correspondence.CorrespondenceFactory;
import org.iobserve.analysis.model.correspondence.CorrespondenceModel;
import org.iobserve.analysis.model.correspondence.CorrespondencePackage;
import org.iobserve.analysis.model.correspondence.ImplementationArtifact;
import org.iobserve.analysis.model.correspondence.ImplementationArtifactSet;

import org.palladiosimulator.pcm.PcmPackage;

import org.palladiosimulator.pcm.core.entity.EntityPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CorrespondencePackageImpl extends EPackageImpl implements CorrespondencePackage {
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
	private EClass correspondenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass architecturalModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass architecturalModelElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass implementationArtifactSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass implementationArtifactEClass = null;

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
	 * @see org.iobserve.analysis.model.correspondence.CorrespondencePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private CorrespondencePackageImpl() {
		super(eNS_URI, CorrespondenceFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link CorrespondencePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static CorrespondencePackage init() {
		if (isInited) return (CorrespondencePackage)EPackage.Registry.INSTANCE.getEPackage(CorrespondencePackage.eNS_URI);

		// Obtain or create and register package
		CorrespondencePackageImpl theCorrespondencePackage = (CorrespondencePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof CorrespondencePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new CorrespondencePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		IdentifierPackage.eINSTANCE.eClass();
		PcmPackage.eINSTANCE.eClass();
		ProbfunctionPackage.eINSTANCE.eClass();
		StoexPackage.eINSTANCE.eClass();
		UnitsPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theCorrespondencePackage.createPackageContents();

		// Initialize created meta-data
		theCorrespondencePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theCorrespondencePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(CorrespondencePackage.eNS_URI, theCorrespondencePackage);
		return theCorrespondencePackage;
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
	public EReference getCorrespondenceModel_Correspondences() {
		return (EReference)correspondenceModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCorrespondenceModel_ImplementationArtifacts() {
		return (EReference)correspondenceModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCorrespondenceModel_ArchitecturalModel() {
		return (EReference)correspondenceModelEClass.getEStructuralFeatures().get(2);
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
	public EClass getArchitecturalModel() {
		return architecturalModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getArchitecturalModel_Elements() {
		return (EReference)architecturalModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getArchitecturalModelElement() {
		return architecturalModelElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getArchitecturalModelElement_Element() {
		return (EReference)architecturalModelElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getImplementationArtifactSet() {
		return implementationArtifactSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getImplementationArtifactSet_Artifacts() {
		return (EReference)implementationArtifactSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getImplementationArtifact() {
		return implementationArtifactEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getImplementationArtifact_ArtifactId() {
		return (EAttribute)implementationArtifactEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CorrespondenceFactory getCorrespondenceFactory() {
		return (CorrespondenceFactory)getEFactoryInstance();
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
		correspondenceModelEClass = createEClass(CORRESPONDENCE_MODEL);
		createEReference(correspondenceModelEClass, CORRESPONDENCE_MODEL__CORRESPONDENCES);
		createEReference(correspondenceModelEClass, CORRESPONDENCE_MODEL__IMPLEMENTATION_ARTIFACTS);
		createEReference(correspondenceModelEClass, CORRESPONDENCE_MODEL__ARCHITECTURAL_MODEL);

		correspondenceEClass = createEClass(CORRESPONDENCE);
		createEReference(correspondenceEClass, CORRESPONDENCE__FROM);
		createEReference(correspondenceEClass, CORRESPONDENCE__TO);

		architecturalModelEClass = createEClass(ARCHITECTURAL_MODEL);
		createEReference(architecturalModelEClass, ARCHITECTURAL_MODEL__ELEMENTS);

		architecturalModelElementEClass = createEClass(ARCHITECTURAL_MODEL_ELEMENT);
		createEReference(architecturalModelElementEClass, ARCHITECTURAL_MODEL_ELEMENT__ELEMENT);

		implementationArtifactSetEClass = createEClass(IMPLEMENTATION_ARTIFACT_SET);
		createEReference(implementationArtifactSetEClass, IMPLEMENTATION_ARTIFACT_SET__ARTIFACTS);

		implementationArtifactEClass = createEClass(IMPLEMENTATION_ARTIFACT);
		createEAttribute(implementationArtifactEClass, IMPLEMENTATION_ARTIFACT__ARTIFACT_ID);
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
		correspondenceEClass.getESuperTypes().add(theEntityPackage.getEntity());
		architecturalModelElementEClass.getESuperTypes().add(theEntityPackage.getEntity());
		implementationArtifactEClass.getESuperTypes().add(theEntityPackage.getEntity());

		// Initialize classes and features; add operations and parameters
		initEClass(correspondenceModelEClass, CorrespondenceModel.class, "CorrespondenceModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCorrespondenceModel_Correspondences(), this.getCorrespondence(), null, "correspondences", null, 1, -1, CorrespondenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCorrespondenceModel_ImplementationArtifacts(), this.getImplementationArtifactSet(), null, "implementationArtifacts", null, 1, 1, CorrespondenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCorrespondenceModel_ArchitecturalModel(), this.getArchitecturalModel(), null, "architecturalModel", null, 1, 1, CorrespondenceModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(correspondenceEClass, Correspondence.class, "Correspondence", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCorrespondence_From(), this.getArchitecturalModelElement(), null, "from", null, 1, 1, Correspondence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCorrespondence_To(), this.getImplementationArtifact(), null, "to", null, 1, 1, Correspondence.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(architecturalModelEClass, ArchitecturalModel.class, "ArchitecturalModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getArchitecturalModel_Elements(), this.getArchitecturalModelElement(), null, "elements", null, 1, -1, ArchitecturalModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(architecturalModelElementEClass, ArchitecturalModelElement.class, "ArchitecturalModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getArchitecturalModelElement_Element(), theEntityPackage.getEntity(), null, "element", null, 1, 1, ArchitecturalModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(implementationArtifactSetEClass, ImplementationArtifactSet.class, "ImplementationArtifactSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getImplementationArtifactSet_Artifacts(), this.getImplementationArtifact(), null, "artifacts", null, 1, -1, ImplementationArtifactSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(implementationArtifactEClass, ImplementationArtifact.class, "ImplementationArtifact", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getImplementationArtifact_ArtifactId(), ecorePackage.getEString(), "artifactId", null, 1, 1, ImplementationArtifact.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //CorrespondencePackageImpl
