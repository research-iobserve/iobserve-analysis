/**
 */
package org.iobserve.model.correspondence2;

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
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.iobserve.model.correspondence2.Correspondence2Factory
 * @model kind="package"
 * @generated
 */
public interface Correspondence2Package extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "correspondence2";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "https://www.iobserve-devops.net/model/0.0.1/correspondence2";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "correspondence2";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Correspondence2Package eINSTANCE = org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl.init();

	/**
	 * The meta object id for the '{@link org.iobserve.model.correspondence2.impl.ModelElementImpl <em>Model Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.model.correspondence2.impl.ModelElementImpl
	 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getModelElement()
	 * @generated
	 */
	int MODEL_ELEMENT = 6;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT__ID = EntityPackage.ENTITY__ID;

	/**
	 * The feature id for the '<em><b>Entity Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT__ENTITY_NAME = EntityPackage.ENTITY__ENTITY_NAME;

	/**
	 * The number of structural features of the '<em>Model Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODEL_ELEMENT_FEATURE_COUNT = EntityPackage.ENTITY_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.iobserve.model.correspondence2.impl.HighLevelModelElementImpl <em>High Level Model Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.model.correspondence2.impl.HighLevelModelElementImpl
	 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getHighLevelModelElement()
	 * @generated
	 */
	int HIGH_LEVEL_MODEL_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_LEVEL_MODEL_ELEMENT__ID = MODEL_ELEMENT__ID;

	/**
	 * The feature id for the '<em><b>Entity Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_LEVEL_MODEL_ELEMENT__ENTITY_NAME = MODEL_ELEMENT__ENTITY_NAME;

	/**
	 * The feature id for the '<em><b>Model</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_LEVEL_MODEL_ELEMENT__MODEL = MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_LEVEL_MODEL_ELEMENT__PARENT = MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>High Level Model Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_LEVEL_MODEL_ELEMENT_FEATURE_COUNT = MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.iobserve.model.correspondence2.impl.LowLevelModelElementImpl <em>Low Level Model Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.model.correspondence2.impl.LowLevelModelElementImpl
	 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getLowLevelModelElement()
	 * @generated
	 */
	int LOW_LEVEL_MODEL_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOW_LEVEL_MODEL_ELEMENT__ID = MODEL_ELEMENT__ID;

	/**
	 * The feature id for the '<em><b>Entity Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOW_LEVEL_MODEL_ELEMENT__ENTITY_NAME = MODEL_ELEMENT__ENTITY_NAME;

	/**
	 * The feature id for the '<em><b>Model</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOW_LEVEL_MODEL_ELEMENT__MODEL = MODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOW_LEVEL_MODEL_ELEMENT__PARENT = MODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Low Level Model Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOW_LEVEL_MODEL_ELEMENT_FEATURE_COUNT = MODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.iobserve.model.correspondence2.impl.HighLevelModelImpl <em>High Level Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.model.correspondence2.impl.HighLevelModelImpl
	 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getHighLevelModel()
	 * @generated
	 */
	int HIGH_LEVEL_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_LEVEL_MODEL__ELEMENTS = 0;

	/**
	 * The number of structural features of the '<em>High Level Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_LEVEL_MODEL_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.iobserve.model.correspondence2.impl.LowLevelModelImpl <em>Low Level Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.model.correspondence2.impl.LowLevelModelImpl
	 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getLowLevelModel()
	 * @generated
	 */
	int LOW_LEVEL_MODEL = 3;

	/**
	 * The feature id for the '<em><b>Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOW_LEVEL_MODEL__ELEMENTS = 0;

	/**
	 * The number of structural features of the '<em>Low Level Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOW_LEVEL_MODEL_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.iobserve.model.correspondence2.impl.CorrespondenceImpl <em>Correspondence</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.model.correspondence2.impl.CorrespondenceImpl
	 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getCorrespondence()
	 * @generated
	 */
	int CORRESPONDENCE = 4;

	/**
	 * The feature id for the '<em><b>From</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE__FROM = 0;

	/**
	 * The feature id for the '<em><b>To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE__TO = 1;

	/**
	 * The feature id for the '<em><b>Correspondence Model</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE__CORRESPONDENCE_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Debug Str</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE__DEBUG_STR = 3;

	/**
	 * The number of structural features of the '<em>Correspondence</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.iobserve.model.correspondence2.impl.CorrespondenceModelImpl <em>Correspondence Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.iobserve.model.correspondence2.impl.CorrespondenceModelImpl
	 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getCorrespondenceModel()
	 * @generated
	 */
	int CORRESPONDENCE_MODEL = 5;

	/**
	 * The feature id for the '<em><b>High Level Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE_MODEL__HIGH_LEVEL_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Low Level Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE_MODEL__LOW_LEVEL_MODEL = 1;

	/**
	 * The feature id for the '<em><b>Correspondences</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE_MODEL__CORRESPONDENCES = 2;

	/**
	 * The number of structural features of the '<em>Correspondence Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CORRESPONDENCE_MODEL_FEATURE_COUNT = 3;

	/**
	 * Returns the meta object for class '{@link org.iobserve.model.correspondence2.HighLevelModelElement <em>High Level Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>High Level Model Element</em>'.
	 * @see org.iobserve.model.correspondence2.HighLevelModelElement
	 * @generated
	 */
	EClass getHighLevelModelElement();

	/**
	 * Returns the meta object for the container reference '{@link org.iobserve.model.correspondence2.HighLevelModelElement#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Model</em>'.
	 * @see org.iobserve.model.correspondence2.HighLevelModelElement#getModel()
	 * @see #getHighLevelModelElement()
	 * @generated
	 */
	EReference getHighLevelModelElement_Model();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.model.correspondence2.HighLevelModelElement#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parent</em>'.
	 * @see org.iobserve.model.correspondence2.HighLevelModelElement#getParent()
	 * @see #getHighLevelModelElement()
	 * @generated
	 */
	EReference getHighLevelModelElement_Parent();

	/**
	 * Returns the meta object for class '{@link org.iobserve.model.correspondence2.LowLevelModelElement <em>Low Level Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Low Level Model Element</em>'.
	 * @see org.iobserve.model.correspondence2.LowLevelModelElement
	 * @generated
	 */
	EClass getLowLevelModelElement();

	/**
	 * Returns the meta object for the container reference '{@link org.iobserve.model.correspondence2.LowLevelModelElement#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Model</em>'.
	 * @see org.iobserve.model.correspondence2.LowLevelModelElement#getModel()
	 * @see #getLowLevelModelElement()
	 * @generated
	 */
	EReference getLowLevelModelElement_Model();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.model.correspondence2.LowLevelModelElement#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parent</em>'.
	 * @see org.iobserve.model.correspondence2.LowLevelModelElement#getParent()
	 * @see #getLowLevelModelElement()
	 * @generated
	 */
	EReference getLowLevelModelElement_Parent();

	/**
	 * Returns the meta object for class '{@link org.iobserve.model.correspondence2.HighLevelModel <em>High Level Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>High Level Model</em>'.
	 * @see org.iobserve.model.correspondence2.HighLevelModel
	 * @generated
	 */
	EClass getHighLevelModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.iobserve.model.correspondence2.HighLevelModel#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Elements</em>'.
	 * @see org.iobserve.model.correspondence2.HighLevelModel#getElements()
	 * @see #getHighLevelModel()
	 * @generated
	 */
	EReference getHighLevelModel_Elements();

	/**
	 * Returns the meta object for class '{@link org.iobserve.model.correspondence2.LowLevelModel <em>Low Level Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Low Level Model</em>'.
	 * @see org.iobserve.model.correspondence2.LowLevelModel
	 * @generated
	 */
	EClass getLowLevelModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.iobserve.model.correspondence2.LowLevelModel#getElements <em>Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Elements</em>'.
	 * @see org.iobserve.model.correspondence2.LowLevelModel#getElements()
	 * @see #getLowLevelModel()
	 * @generated
	 */
	EReference getLowLevelModel_Elements();

	/**
	 * Returns the meta object for class '{@link org.iobserve.model.correspondence2.Correspondence <em>Correspondence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Correspondence</em>'.
	 * @see org.iobserve.model.correspondence2.Correspondence
	 * @generated
	 */
	EClass getCorrespondence();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.model.correspondence2.Correspondence#getFrom <em>From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>From</em>'.
	 * @see org.iobserve.model.correspondence2.Correspondence#getFrom()
	 * @see #getCorrespondence()
	 * @generated
	 */
	EReference getCorrespondence_From();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.model.correspondence2.Correspondence#getTo <em>To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>To</em>'.
	 * @see org.iobserve.model.correspondence2.Correspondence#getTo()
	 * @see #getCorrespondence()
	 * @generated
	 */
	EReference getCorrespondence_To();

	/**
	 * Returns the meta object for the container reference '{@link org.iobserve.model.correspondence2.Correspondence#getCorrespondenceModel <em>Correspondence Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Correspondence Model</em>'.
	 * @see org.iobserve.model.correspondence2.Correspondence#getCorrespondenceModel()
	 * @see #getCorrespondence()
	 * @generated
	 */
	EReference getCorrespondence_CorrespondenceModel();

	/**
	 * Returns the meta object for the attribute '{@link org.iobserve.model.correspondence2.Correspondence#getDebugStr <em>Debug Str</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Debug Str</em>'.
	 * @see org.iobserve.model.correspondence2.Correspondence#getDebugStr()
	 * @see #getCorrespondence()
	 * @generated
	 */
	EAttribute getCorrespondence_DebugStr();

	/**
	 * Returns the meta object for class '{@link org.iobserve.model.correspondence2.CorrespondenceModel <em>Correspondence Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Correspondence Model</em>'.
	 * @see org.iobserve.model.correspondence2.CorrespondenceModel
	 * @generated
	 */
	EClass getCorrespondenceModel();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.model.correspondence2.CorrespondenceModel#getHighLevelModel <em>High Level Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>High Level Model</em>'.
	 * @see org.iobserve.model.correspondence2.CorrespondenceModel#getHighLevelModel()
	 * @see #getCorrespondenceModel()
	 * @generated
	 */
	EReference getCorrespondenceModel_HighLevelModel();

	/**
	 * Returns the meta object for the reference '{@link org.iobserve.model.correspondence2.CorrespondenceModel#getLowLevelModel <em>Low Level Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Low Level Model</em>'.
	 * @see org.iobserve.model.correspondence2.CorrespondenceModel#getLowLevelModel()
	 * @see #getCorrespondenceModel()
	 * @generated
	 */
	EReference getCorrespondenceModel_LowLevelModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.iobserve.model.correspondence2.CorrespondenceModel#getCorrespondences <em>Correspondences</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Correspondences</em>'.
	 * @see org.iobserve.model.correspondence2.CorrespondenceModel#getCorrespondences()
	 * @see #getCorrespondenceModel()
	 * @generated
	 */
	EReference getCorrespondenceModel_Correspondences();

	/**
	 * Returns the meta object for class '{@link org.iobserve.model.correspondence2.ModelElement <em>Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model Element</em>'.
	 * @see org.iobserve.model.correspondence2.ModelElement
	 * @generated
	 */
	EClass getModelElement();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	Correspondence2Factory getCorrespondence2Factory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.iobserve.model.correspondence2.impl.HighLevelModelElementImpl <em>High Level Model Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.model.correspondence2.impl.HighLevelModelElementImpl
		 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getHighLevelModelElement()
		 * @generated
		 */
		EClass HIGH_LEVEL_MODEL_ELEMENT = eINSTANCE.getHighLevelModelElement();

		/**
		 * The meta object literal for the '<em><b>Model</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HIGH_LEVEL_MODEL_ELEMENT__MODEL = eINSTANCE.getHighLevelModelElement_Model();

		/**
		 * The meta object literal for the '<em><b>Parent</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HIGH_LEVEL_MODEL_ELEMENT__PARENT = eINSTANCE.getHighLevelModelElement_Parent();

		/**
		 * The meta object literal for the '{@link org.iobserve.model.correspondence2.impl.LowLevelModelElementImpl <em>Low Level Model Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.model.correspondence2.impl.LowLevelModelElementImpl
		 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getLowLevelModelElement()
		 * @generated
		 */
		EClass LOW_LEVEL_MODEL_ELEMENT = eINSTANCE.getLowLevelModelElement();

		/**
		 * The meta object literal for the '<em><b>Model</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LOW_LEVEL_MODEL_ELEMENT__MODEL = eINSTANCE.getLowLevelModelElement_Model();

		/**
		 * The meta object literal for the '<em><b>Parent</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LOW_LEVEL_MODEL_ELEMENT__PARENT = eINSTANCE.getLowLevelModelElement_Parent();

		/**
		 * The meta object literal for the '{@link org.iobserve.model.correspondence2.impl.HighLevelModelImpl <em>High Level Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.model.correspondence2.impl.HighLevelModelImpl
		 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getHighLevelModel()
		 * @generated
		 */
		EClass HIGH_LEVEL_MODEL = eINSTANCE.getHighLevelModel();

		/**
		 * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HIGH_LEVEL_MODEL__ELEMENTS = eINSTANCE.getHighLevelModel_Elements();

		/**
		 * The meta object literal for the '{@link org.iobserve.model.correspondence2.impl.LowLevelModelImpl <em>Low Level Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.model.correspondence2.impl.LowLevelModelImpl
		 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getLowLevelModel()
		 * @generated
		 */
		EClass LOW_LEVEL_MODEL = eINSTANCE.getLowLevelModel();

		/**
		 * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LOW_LEVEL_MODEL__ELEMENTS = eINSTANCE.getLowLevelModel_Elements();

		/**
		 * The meta object literal for the '{@link org.iobserve.model.correspondence2.impl.CorrespondenceImpl <em>Correspondence</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.model.correspondence2.impl.CorrespondenceImpl
		 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getCorrespondence()
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
		 * The meta object literal for the '<em><b>Correspondence Model</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CORRESPONDENCE__CORRESPONDENCE_MODEL = eINSTANCE.getCorrespondence_CorrespondenceModel();

		/**
		 * The meta object literal for the '<em><b>Debug Str</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CORRESPONDENCE__DEBUG_STR = eINSTANCE.getCorrespondence_DebugStr();

		/**
		 * The meta object literal for the '{@link org.iobserve.model.correspondence2.impl.CorrespondenceModelImpl <em>Correspondence Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.model.correspondence2.impl.CorrespondenceModelImpl
		 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getCorrespondenceModel()
		 * @generated
		 */
		EClass CORRESPONDENCE_MODEL = eINSTANCE.getCorrespondenceModel();

		/**
		 * The meta object literal for the '<em><b>High Level Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CORRESPONDENCE_MODEL__HIGH_LEVEL_MODEL = eINSTANCE.getCorrespondenceModel_HighLevelModel();

		/**
		 * The meta object literal for the '<em><b>Low Level Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CORRESPONDENCE_MODEL__LOW_LEVEL_MODEL = eINSTANCE.getCorrespondenceModel_LowLevelModel();

		/**
		 * The meta object literal for the '<em><b>Correspondences</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CORRESPONDENCE_MODEL__CORRESPONDENCES = eINSTANCE.getCorrespondenceModel_Correspondences();

		/**
		 * The meta object literal for the '{@link org.iobserve.model.correspondence2.impl.ModelElementImpl <em>Model Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.iobserve.model.correspondence2.impl.ModelElementImpl
		 * @see org.iobserve.model.correspondence2.impl.Correspondence2PackageImpl#getModelElement()
		 * @generated
		 */
		EClass MODEL_ELEMENT = eINSTANCE.getModelElement();

	}

} //Correspondence2Package
