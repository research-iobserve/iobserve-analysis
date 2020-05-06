/**
 */
package org.iobserve.analysis.model.correspondence;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.palladiosimulator.pcm.core.entity.EntityPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.iobserve.analysis.model.correspondence.CorrespondenceFactory
 * @model kind="package"
 * @generated
 */
public interface CorrespondencePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "correspondence";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "https://www.iobserve-devops.net/model/0.0.4/correspondence";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "correspondence";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CorrespondencePackage eINSTANCE = org.iobserve.analysis.model.correspondence.impl.CorrespondencePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.iobserve.analysis.model.correspondence.impl.CorrespondenceModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondenceModelImpl
	 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondencePackageImpl#getCorrespondenceModel()
	 * @generated
	 */
	int CORRESPONDENCE_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Correspondences</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE_MODEL__CORRESPONDENCES = 0;

	/**
	 * The feature id for the '<em><b>Implementation Artifacts</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE_MODEL__IMPLEMENTATION_ARTIFACTS = 1;

	/**
	 * The feature id for the '<em><b>Architectural Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE_MODEL__ARCHITECTURAL_MODEL = 2;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE_MODEL_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.iobserve.analysis.model.correspondence.impl.CorrespondenceImpl <em>Correspondence</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondenceImpl
	 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondencePackageImpl#getCorrespondence()
	 * @generated
	 */
	int CORRESPONDENCE = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE__ID = EntityPackage.ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Entity Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE__ENTITY_NAME = EntityPackage.ENTITY__ENTITY_NAME;

	/**
	 * The feature id for the '<em><b>From</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE__FROM = EntityPackage.ENTITY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE__TO = EntityPackage.ENTITY_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Correspondence</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE_FEATURE_COUNT = EntityPackage.ENTITY_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.iobserve.analysis.model.correspondence.impl.ArchitecturalModelImpl <em>Architectural Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.analysis.model.correspondence.impl.ArchitecturalModelImpl
	 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondencePackageImpl#getArchitecturalModel()
	 * @generated
	 */
	int ARCHITECTURAL_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARCHITECTURAL_MODEL__ELEMENTS = 0;

	/**
	 * The number of structural features of the '<em>Architectural Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARCHITECTURAL_MODEL_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.iobserve.analysis.model.correspondence.impl.ArchitecturalModelElementImpl <em>Architectural Model Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.analysis.model.correspondence.impl.ArchitecturalModelElementImpl
	 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondencePackageImpl#getArchitecturalModelElement()
	 * @generated
	 */
	int ARCHITECTURAL_MODEL_ELEMENT = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARCHITECTURAL_MODEL_ELEMENT__ID = EntityPackage.ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Entity Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARCHITECTURAL_MODEL_ELEMENT__ENTITY_NAME = EntityPackage.ENTITY__ENTITY_NAME;

	/**
	 * The feature id for the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARCHITECTURAL_MODEL_ELEMENT__ELEMENT = EntityPackage.ENTITY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Architectural Model Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARCHITECTURAL_MODEL_ELEMENT_FEATURE_COUNT = EntityPackage.ENTITY_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.iobserve.analysis.model.correspondence.impl.ImplementationArtifactSetImpl <em>Implementation Artifact Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.analysis.model.correspondence.impl.ImplementationArtifactSetImpl
	 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondencePackageImpl#getImplementationArtifactSet()
	 * @generated
	 */
	int IMPLEMENTATION_ARTIFACT_SET = 4;

	/**
	 * The feature id for the '<em><b>Artifacts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPLEMENTATION_ARTIFACT_SET__ARTIFACTS = 0;

	/**
	 * The number of structural features of the '<em>Implementation Artifact Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPLEMENTATION_ARTIFACT_SET_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.iobserve.analysis.model.correspondence.impl.ImplementationArtifactImpl <em>Implementation Artifact</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.analysis.model.correspondence.impl.ImplementationArtifactImpl
	 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondencePackageImpl#getImplementationArtifact()
	 * @generated
	 */
	int IMPLEMENTATION_ARTIFACT = 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPLEMENTATION_ARTIFACT__ID = EntityPackage.ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Entity Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPLEMENTATION_ARTIFACT__ENTITY_NAME = EntityPackage.ENTITY__ENTITY_NAME;

	/**
	 * The feature id for the '<em><b>Artifact Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPLEMENTATION_ARTIFACT__ARTIFACT_ID = EntityPackage.ENTITY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Implementation Artifact</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPLEMENTATION_ARTIFACT_FEATURE_COUNT = EntityPackage.ENTITY_FEATURE_COUNT + 1;


	/**
	 * Returns the meta object for class '{@link org.iobserve.analysis.model.correspondence.CorrespondenceModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see org.iobserve.analysis.model.correspondence.CorrespondenceModel
	 * @generated
	 */
	EClass getCorrespondenceModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.iobserve.analysis.model.correspondence.CorrespondenceModel#getCorrespondences <em>Correspondences</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Correspondences</em>'.
	 * @see org.iobserve.analysis.model.correspondence.CorrespondenceModel#getCorrespondences()
	 * @see #getCorrespondenceModel()
	 * @generated
	 */
	EReference getCorrespondenceModel_Correspondences();

	/**
	 * Returns the meta object for the containment reference '{@link org.iobserve.analysis.model.correspondence.CorrespondenceModel#getImplementationArtifacts <em>Implementation Artifacts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Implementation Artifacts</em>'.
	 * @see org.iobserve.analysis.model.correspondence.CorrespondenceModel#getImplementationArtifacts()
	 * @see #getCorrespondenceModel()
	 * @generated
	 */
	EReference getCorrespondenceModel_ImplementationArtifacts();

	/**
	 * Returns the meta object for the containment reference '{@link org.iobserve.analysis.model.correspondence.CorrespondenceModel#getArchitecturalModel <em>Architectural Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Architectural Model</em>'.
	 * @see org.iobserve.analysis.model.correspondence.CorrespondenceModel#getArchitecturalModel()
	 * @see #getCorrespondenceModel()
	 * @generated
	 */
	EReference getCorrespondenceModel_ArchitecturalModel();

	/**
	 * Returns the meta object for class '{@link org.iobserve.analysis.model.correspondence.Correspondence <em>Correspondence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Correspondence</em>'.
	 * @see org.iobserve.analysis.model.correspondence.Correspondence
	 * @generated
	 */
	EClass getCorrespondence();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.analysis.model.correspondence.Correspondence#getFrom <em>From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>From</em>'.
	 * @see org.iobserve.analysis.model.correspondence.Correspondence#getFrom()
	 * @see #getCorrespondence()
	 * @generated
	 */
	EReference getCorrespondence_From();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.analysis.model.correspondence.Correspondence#getTo <em>To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>To</em>'.
	 * @see org.iobserve.analysis.model.correspondence.Correspondence#getTo()
	 * @see #getCorrespondence()
	 * @generated
	 */
	EReference getCorrespondence_To();

	/**
	 * Returns the meta object for class '{@link org.iobserve.analysis.model.correspondence.ArchitecturalModel <em>Architectural Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Architectural Model</em>'.
	 * @see org.iobserve.analysis.model.correspondence.ArchitecturalModel
	 * @generated
	 */
	EClass getArchitecturalModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.iobserve.analysis.model.correspondence.ArchitecturalModel#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Elements</em>'.
	 * @see org.iobserve.analysis.model.correspondence.ArchitecturalModel#getElements()
	 * @see #getArchitecturalModel()
	 * @generated
	 */
	EReference getArchitecturalModel_Elements();

	/**
	 * Returns the meta object for class '{@link org.iobserve.analysis.model.correspondence.ArchitecturalModelElement <em>Architectural Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Architectural Model Element</em>'.
	 * @see org.iobserve.analysis.model.correspondence.ArchitecturalModelElement
	 * @generated
	 */
	EClass getArchitecturalModelElement();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.analysis.model.correspondence.ArchitecturalModelElement#getElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Element</em>'.
	 * @see org.iobserve.analysis.model.correspondence.ArchitecturalModelElement#getElement()
	 * @see #getArchitecturalModelElement()
	 * @generated
	 */
	EReference getArchitecturalModelElement_Element();

	/**
	 * Returns the meta object for class '{@link org.iobserve.analysis.model.correspondence.ImplementationArtifactSet <em>Implementation Artifact Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Implementation Artifact Set</em>'.
	 * @see org.iobserve.analysis.model.correspondence.ImplementationArtifactSet
	 * @generated
	 */
	EClass getImplementationArtifactSet();

	/**
	 * Returns the meta object for the containment reference list '{@link org.iobserve.analysis.model.correspondence.ImplementationArtifactSet#getArtifacts <em>Artifacts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Artifacts</em>'.
	 * @see org.iobserve.analysis.model.correspondence.ImplementationArtifactSet#getArtifacts()
	 * @see #getImplementationArtifactSet()
	 * @generated
	 */
	EReference getImplementationArtifactSet_Artifacts();

	/**
	 * Returns the meta object for class '{@link org.iobserve.analysis.model.correspondence.ImplementationArtifact <em>Implementation Artifact</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Implementation Artifact</em>'.
	 * @see org.iobserve.analysis.model.correspondence.ImplementationArtifact
	 * @generated
	 */
	EClass getImplementationArtifact();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.analysis.model.correspondence.ImplementationArtifact#getArtifactId <em>Artifact Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Artifact Id</em>'.
	 * @see org.iobserve.analysis.model.correspondence.ImplementationArtifact#getArtifactId()
	 * @see #getImplementationArtifact()
	 * @generated
	 */
	EAttribute getImplementationArtifact_ArtifactId();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CorrespondenceFactory getCorrespondenceFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.iobserve.analysis.model.correspondence.impl.CorrespondenceModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondenceModelImpl
		 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondencePackageImpl#getCorrespondenceModel()
		 * @generated
		 */
		EClass CORRESPONDENCE_MODEL = eINSTANCE.getCorrespondenceModel();

		/**
		 * The meta object literal for the '<em><b>Correspondences</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CORRESPONDENCE_MODEL__CORRESPONDENCES = eINSTANCE.getCorrespondenceModel_Correspondences();

		/**
		 * The meta object literal for the '<em><b>Implementation Artifacts</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CORRESPONDENCE_MODEL__IMPLEMENTATION_ARTIFACTS = eINSTANCE.getCorrespondenceModel_ImplementationArtifacts();

		/**
		 * The meta object literal for the '<em><b>Architectural Model</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CORRESPONDENCE_MODEL__ARCHITECTURAL_MODEL = eINSTANCE.getCorrespondenceModel_ArchitecturalModel();

		/**
		 * The meta object literal for the '{@link org.iobserve.analysis.model.correspondence.impl.CorrespondenceImpl <em>Correspondence</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondenceImpl
		 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondencePackageImpl#getCorrespondence()
		 * @generated
		 */
		EClass CORRESPONDENCE = eINSTANCE.getCorrespondence();

		/**
		 * The meta object literal for the '<em><b>From</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CORRESPONDENCE__FROM = eINSTANCE.getCorrespondence_From();

		/**
		 * The meta object literal for the '<em><b>To</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CORRESPONDENCE__TO = eINSTANCE.getCorrespondence_To();

		/**
		 * The meta object literal for the '{@link org.iobserve.analysis.model.correspondence.impl.ArchitecturalModelImpl <em>Architectural Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.analysis.model.correspondence.impl.ArchitecturalModelImpl
		 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondencePackageImpl#getArchitecturalModel()
		 * @generated
		 */
		EClass ARCHITECTURAL_MODEL = eINSTANCE.getArchitecturalModel();

		/**
		 * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ARCHITECTURAL_MODEL__ELEMENTS = eINSTANCE.getArchitecturalModel_Elements();

		/**
		 * The meta object literal for the '{@link org.iobserve.analysis.model.correspondence.impl.ArchitecturalModelElementImpl <em>Architectural Model Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.analysis.model.correspondence.impl.ArchitecturalModelElementImpl
		 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondencePackageImpl#getArchitecturalModelElement()
		 * @generated
		 */
		EClass ARCHITECTURAL_MODEL_ELEMENT = eINSTANCE.getArchitecturalModelElement();

		/**
		 * The meta object literal for the '<em><b>Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ARCHITECTURAL_MODEL_ELEMENT__ELEMENT = eINSTANCE.getArchitecturalModelElement_Element();

		/**
		 * The meta object literal for the '{@link org.iobserve.analysis.model.correspondence.impl.ImplementationArtifactSetImpl <em>Implementation Artifact Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.analysis.model.correspondence.impl.ImplementationArtifactSetImpl
		 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondencePackageImpl#getImplementationArtifactSet()
		 * @generated
		 */
		EClass IMPLEMENTATION_ARTIFACT_SET = eINSTANCE.getImplementationArtifactSet();

		/**
		 * The meta object literal for the '<em><b>Artifacts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMPLEMENTATION_ARTIFACT_SET__ARTIFACTS = eINSTANCE.getImplementationArtifactSet_Artifacts();

		/**
		 * The meta object literal for the '{@link org.iobserve.analysis.model.correspondence.impl.ImplementationArtifactImpl <em>Implementation Artifact</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.analysis.model.correspondence.impl.ImplementationArtifactImpl
		 * @see org.iobserve.analysis.model.correspondence.impl.CorrespondencePackageImpl#getImplementationArtifact()
		 * @generated
		 */
		EClass IMPLEMENTATION_ARTIFACT = eINSTANCE.getImplementationArtifact();

		/**
		 * The meta object literal for the '<em><b>Artifact Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMPLEMENTATION_ARTIFACT__ARTIFACT_ID = eINSTANCE.getImplementationArtifact_ArtifactId();

	}

} //CorrespondencePackage