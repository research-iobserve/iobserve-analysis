/**
 */
package org.iobserve.model.correspondence;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * @see org.iobserve.model.correspondence.CorrespondenceFactory
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
    String eNS_URI = "https://www.iobserve-devops.net/model/0.0.3/correspondence";

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
    CorrespondencePackage eINSTANCE = org.iobserve.model.correspondence.impl.CorrespondencePackageImpl.init();

    /**
     * The meta object id for the '{@link org.iobserve.model.correspondence.impl.CorrespondenceModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.correspondence.impl.CorrespondenceModelImpl
     * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getCorrespondenceModel()
     * @generated
     */
    int CORRESPONDENCE_MODEL = 0;

    /**
     * The feature id for the '<em><b>Parts</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CORRESPONDENCE_MODEL__PARTS = 0;

    /**
     * The number of structural features of the '<em>Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CORRESPONDENCE_MODEL_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CORRESPONDENCE_MODEL_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.correspondence.impl.PartImpl <em>Part</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.correspondence.impl.PartImpl
     * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getPart()
     * @generated
     */
    int PART = 2;

    /**
     * The meta object id for the '{@link org.iobserve.model.correspondence.impl.AbstractEntryImpl <em>Abstract Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.correspondence.impl.AbstractEntryImpl
     * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getAbstractEntry()
     * @generated
     */
    int ABSTRACT_ENTRY = 3;

    /**
     * The feature id for the '<em><b>Implementation Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_ENTRY__IMPLEMENTATION_ID = 0;

    /**
     * The number of structural features of the '<em>Abstract Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_ENTRY_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Abstract Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ABSTRACT_ENTRY_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.correspondence.impl.DataTypeEntryImpl <em>Data Type Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.correspondence.impl.DataTypeEntryImpl
     * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getDataTypeEntry()
     * @generated
     */
    int DATA_TYPE_ENTRY = 1;

    /**
     * The feature id for the '<em><b>Implementation Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_TYPE_ENTRY__IMPLEMENTATION_ID = ABSTRACT_ENTRY__IMPLEMENTATION_ID;

    /**
     * The feature id for the '<em><b>Data Type Entry</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_TYPE_ENTRY__DATA_TYPE_ENTRY = ABSTRACT_ENTRY_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Data Type Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_TYPE_ENTRY_FEATURE_COUNT = ABSTRACT_ENTRY_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Data Type Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DATA_TYPE_ENTRY_OPERATION_COUNT = ABSTRACT_ENTRY_OPERATION_COUNT + 0;

    /**
     * The feature id for the '<em><b>Model Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PART__MODEL_TYPE = 0;

    /**
     * The feature id for the '<em><b>Entries</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PART__ENTRIES = 1;

    /**
     * The number of structural features of the '<em>Part</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PART_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Part</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PART_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.correspondence.impl.ComponentEntryImpl <em>Component Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.correspondence.impl.ComponentEntryImpl
     * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getComponentEntry()
     * @generated
     */
    int COMPONENT_ENTRY = 4;

    /**
     * The feature id for the '<em><b>Implementation Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_ENTRY__IMPLEMENTATION_ID = ABSTRACT_ENTRY__IMPLEMENTATION_ID;

    /**
     * The feature id for the '<em><b>Component</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_ENTRY__COMPONENT = ABSTRACT_ENTRY_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Component Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_ENTRY_FEATURE_COUNT = ABSTRACT_ENTRY_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Component Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_ENTRY_OPERATION_COUNT = ABSTRACT_ENTRY_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.correspondence.impl.AllocationEntryImpl <em>Allocation Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.correspondence.impl.AllocationEntryImpl
     * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getAllocationEntry()
     * @generated
     */
    int ALLOCATION_ENTRY = 5;

    /**
     * The feature id for the '<em><b>Implementation Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOCATION_ENTRY__IMPLEMENTATION_ID = ABSTRACT_ENTRY__IMPLEMENTATION_ID;

    /**
     * The feature id for the '<em><b>Allocation</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOCATION_ENTRY__ALLOCATION = ABSTRACT_ENTRY_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Allocation Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOCATION_ENTRY_FEATURE_COUNT = ABSTRACT_ENTRY_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Allocation Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ALLOCATION_ENTRY_OPERATION_COUNT = ABSTRACT_ENTRY_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.correspondence.impl.AssemblyEntryImpl <em>Assembly Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.correspondence.impl.AssemblyEntryImpl
     * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getAssemblyEntry()
     * @generated
     */
    int ASSEMBLY_ENTRY = 6;

    /**
     * The feature id for the '<em><b>Implementation Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSEMBLY_ENTRY__IMPLEMENTATION_ID = ABSTRACT_ENTRY__IMPLEMENTATION_ID;

    /**
     * The feature id for the '<em><b>Assembly</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSEMBLY_ENTRY__ASSEMBLY = ABSTRACT_ENTRY_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Assembly Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSEMBLY_ENTRY_FEATURE_COUNT = ABSTRACT_ENTRY_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Assembly Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ASSEMBLY_ENTRY_OPERATION_COUNT = ABSTRACT_ENTRY_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.correspondence.impl.OperationEntryImpl <em>Operation Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.correspondence.impl.OperationEntryImpl
     * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getOperationEntry()
     * @generated
     */
    int OPERATION_ENTRY = 7;

    /**
     * The feature id for the '<em><b>Implementation Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_ENTRY__IMPLEMENTATION_ID = ABSTRACT_ENTRY__IMPLEMENTATION_ID;

    /**
     * The feature id for the '<em><b>Operation</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_ENTRY__OPERATION = ABSTRACT_ENTRY_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Operation Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_ENTRY_FEATURE_COUNT = ABSTRACT_ENTRY_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Operation Entry</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_ENTRY_OPERATION_COUNT = ABSTRACT_ENTRY_OPERATION_COUNT + 0;


    /**
     * Returns the meta object for class '{@link org.iobserve.model.correspondence.CorrespondenceModel <em>Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model</em>'.
     * @see org.iobserve.model.correspondence.CorrespondenceModel
     * @generated
     */
    EClass getCorrespondenceModel();

    /**
     * Returns the meta object for the containment reference list '{@link org.iobserve.model.correspondence.CorrespondenceModel#getParts <em>Parts</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Parts</em>'.
     * @see org.iobserve.model.correspondence.CorrespondenceModel#getParts()
     * @see #getCorrespondenceModel()
     * @generated
     */
    EReference getCorrespondenceModel_Parts();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.correspondence.DataTypeEntry <em>Data Type Entry</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Data Type Entry</em>'.
     * @see org.iobserve.model.correspondence.DataTypeEntry
     * @generated
     */
    EClass getDataTypeEntry();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.correspondence.DataTypeEntry#getDataTypeEntry <em>Data Type Entry</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Data Type Entry</em>'.
     * @see org.iobserve.model.correspondence.DataTypeEntry#getDataTypeEntry()
     * @see #getDataTypeEntry()
     * @generated
     */
    EReference getDataTypeEntry_DataTypeEntry();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.correspondence.Part <em>Part</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Part</em>'.
     * @see org.iobserve.model.correspondence.Part
     * @generated
     */
    EClass getPart();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.correspondence.Part#getModelType <em>Model Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Model Type</em>'.
     * @see org.iobserve.model.correspondence.Part#getModelType()
     * @see #getPart()
     * @generated
     */
    EReference getPart_ModelType();

    /**
     * Returns the meta object for the containment reference list '{@link org.iobserve.model.correspondence.Part#getEntries <em>Entries</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Entries</em>'.
     * @see org.iobserve.model.correspondence.Part#getEntries()
     * @see #getPart()
     * @generated
     */
    EReference getPart_Entries();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.correspondence.AbstractEntry <em>Abstract Entry</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Abstract Entry</em>'.
     * @see org.iobserve.model.correspondence.AbstractEntry
     * @generated
     */
    EClass getAbstractEntry();

    /**
     * Returns the meta object for the attribute '{@link org.iobserve.model.correspondence.AbstractEntry#getImplementationId <em>Implementation Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Implementation Id</em>'.
     * @see org.iobserve.model.correspondence.AbstractEntry#getImplementationId()
     * @see #getAbstractEntry()
     * @generated
     */
    EAttribute getAbstractEntry_ImplementationId();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.correspondence.ComponentEntry <em>Component Entry</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Component Entry</em>'.
     * @see org.iobserve.model.correspondence.ComponentEntry
     * @generated
     */
    EClass getComponentEntry();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.correspondence.ComponentEntry#getComponent <em>Component</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Component</em>'.
     * @see org.iobserve.model.correspondence.ComponentEntry#getComponent()
     * @see #getComponentEntry()
     * @generated
     */
    EReference getComponentEntry_Component();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.correspondence.AllocationEntry <em>Allocation Entry</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Allocation Entry</em>'.
     * @see org.iobserve.model.correspondence.AllocationEntry
     * @generated
     */
    EClass getAllocationEntry();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.correspondence.AllocationEntry#getAllocation <em>Allocation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Allocation</em>'.
     * @see org.iobserve.model.correspondence.AllocationEntry#getAllocation()
     * @see #getAllocationEntry()
     * @generated
     */
    EReference getAllocationEntry_Allocation();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.correspondence.AssemblyEntry <em>Assembly Entry</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Assembly Entry</em>'.
     * @see org.iobserve.model.correspondence.AssemblyEntry
     * @generated
     */
    EClass getAssemblyEntry();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.correspondence.AssemblyEntry#getAssembly <em>Assembly</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Assembly</em>'.
     * @see org.iobserve.model.correspondence.AssemblyEntry#getAssembly()
     * @see #getAssemblyEntry()
     * @generated
     */
    EReference getAssemblyEntry_Assembly();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.correspondence.OperationEntry <em>Operation Entry</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Operation Entry</em>'.
     * @see org.iobserve.model.correspondence.OperationEntry
     * @generated
     */
    EClass getOperationEntry();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.correspondence.OperationEntry#getOperation <em>Operation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Operation</em>'.
     * @see org.iobserve.model.correspondence.OperationEntry#getOperation()
     * @see #getOperationEntry()
     * @generated
     */
    EReference getOperationEntry_Operation();

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
     *   <li>each operation of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link org.iobserve.model.correspondence.impl.CorrespondenceModelImpl <em>Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.correspondence.impl.CorrespondenceModelImpl
         * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getCorrespondenceModel()
         * @generated
         */
        EClass CORRESPONDENCE_MODEL = eINSTANCE.getCorrespondenceModel();

        /**
         * The meta object literal for the '<em><b>Parts</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CORRESPONDENCE_MODEL__PARTS = eINSTANCE.getCorrespondenceModel_Parts();

        /**
         * The meta object literal for the '{@link org.iobserve.model.correspondence.impl.DataTypeEntryImpl <em>Data Type Entry</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.correspondence.impl.DataTypeEntryImpl
         * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getDataTypeEntry()
         * @generated
         */
        EClass DATA_TYPE_ENTRY = eINSTANCE.getDataTypeEntry();

        /**
         * The meta object literal for the '<em><b>Data Type Entry</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DATA_TYPE_ENTRY__DATA_TYPE_ENTRY = eINSTANCE.getDataTypeEntry_DataTypeEntry();

        /**
         * The meta object literal for the '{@link org.iobserve.model.correspondence.impl.PartImpl <em>Part</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.correspondence.impl.PartImpl
         * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getPart()
         * @generated
         */
        EClass PART = eINSTANCE.getPart();

        /**
         * The meta object literal for the '<em><b>Model Type</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PART__MODEL_TYPE = eINSTANCE.getPart_ModelType();

        /**
         * The meta object literal for the '<em><b>Entries</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PART__ENTRIES = eINSTANCE.getPart_Entries();

        /**
         * The meta object literal for the '{@link org.iobserve.model.correspondence.impl.AbstractEntryImpl <em>Abstract Entry</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.correspondence.impl.AbstractEntryImpl
         * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getAbstractEntry()
         * @generated
         */
        EClass ABSTRACT_ENTRY = eINSTANCE.getAbstractEntry();

        /**
         * The meta object literal for the '<em><b>Implementation Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ABSTRACT_ENTRY__IMPLEMENTATION_ID = eINSTANCE.getAbstractEntry_ImplementationId();

        /**
         * The meta object literal for the '{@link org.iobserve.model.correspondence.impl.ComponentEntryImpl <em>Component Entry</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.correspondence.impl.ComponentEntryImpl
         * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getComponentEntry()
         * @generated
         */
        EClass COMPONENT_ENTRY = eINSTANCE.getComponentEntry();

        /**
         * The meta object literal for the '<em><b>Component</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_ENTRY__COMPONENT = eINSTANCE.getComponentEntry_Component();

        /**
         * The meta object literal for the '{@link org.iobserve.model.correspondence.impl.AllocationEntryImpl <em>Allocation Entry</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.correspondence.impl.AllocationEntryImpl
         * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getAllocationEntry()
         * @generated
         */
        EClass ALLOCATION_ENTRY = eINSTANCE.getAllocationEntry();

        /**
         * The meta object literal for the '<em><b>Allocation</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ALLOCATION_ENTRY__ALLOCATION = eINSTANCE.getAllocationEntry_Allocation();

        /**
         * The meta object literal for the '{@link org.iobserve.model.correspondence.impl.AssemblyEntryImpl <em>Assembly Entry</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.correspondence.impl.AssemblyEntryImpl
         * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getAssemblyEntry()
         * @generated
         */
        EClass ASSEMBLY_ENTRY = eINSTANCE.getAssemblyEntry();

        /**
         * The meta object literal for the '<em><b>Assembly</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ASSEMBLY_ENTRY__ASSEMBLY = eINSTANCE.getAssemblyEntry_Assembly();

        /**
         * The meta object literal for the '{@link org.iobserve.model.correspondence.impl.OperationEntryImpl <em>Operation Entry</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.correspondence.impl.OperationEntryImpl
         * @see org.iobserve.model.correspondence.impl.CorrespondencePackageImpl#getOperationEntry()
         * @generated
         */
        EClass OPERATION_ENTRY = eINSTANCE.getOperationEntry();

        /**
         * The meta object literal for the '<em><b>Operation</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_ENTRY__OPERATION = eINSTANCE.getOperationEntry_Operation();

    }

} //CorrespondencePackage
