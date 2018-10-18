/**
 */
package org.iobserve.model.test.storage.one;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see org.iobserve.model.test.storage.one.OneFactory
 * @model kind="package"
 * @generated
 */
public interface OnePackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "one";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://local/storage/one";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "one";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    OnePackage eINSTANCE = org.iobserve.model.test.storage.one.impl.OnePackageImpl.init();

    /**
     * The meta object id for the '{@link org.iobserve.model.test.storage.one.impl.RootImpl <em>Root</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.test.storage.one.impl.RootImpl
     * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getRoot()
     * @generated
     */
    int ROOT = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROOT__NAME = 0;

    /**
     * The feature id for the '<em><b>Labels</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROOT__LABELS = 1;

    /**
     * The feature id for the '<em><b>Fixed</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROOT__FIXED = 2;

    /**
     * The feature id for the '<em><b>Enumerate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROOT__ENUMERATE = 3;

    /**
     * The feature id for the '<em><b>Others</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROOT__OTHERS = 4;

    /**
     * The feature id for the '<em><b>Iface Others</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROOT__IFACE_OTHERS = 5;

    /**
     * The number of structural features of the '<em>Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROOT_FEATURE_COUNT = 6;

    /**
     * The number of operations of the '<em>Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ROOT_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.test.storage.one.impl.OtherImpl <em>Other</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.test.storage.one.impl.OtherImpl
     * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getOther()
     * @generated
     */
    int OTHER = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER__NAME = 0;

    /**
     * The number of structural features of the '<em>Other</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Other</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.test.storage.one.impl.OtherSubTypeImpl <em>Other Sub Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.test.storage.one.impl.OtherSubTypeImpl
     * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getOtherSubType()
     * @generated
     */
    int OTHER_SUB_TYPE = 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_SUB_TYPE__NAME = OTHER__NAME;

    /**
     * The feature id for the '<em><b>Label</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_SUB_TYPE__LABEL = OTHER_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Other</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_SUB_TYPE__OTHER = OTHER_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>Other Sub Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_SUB_TYPE_FEATURE_COUNT = OTHER_FEATURE_COUNT + 2;

    /**
     * The number of operations of the '<em>Other Sub Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_SUB_TYPE_OPERATION_COUNT = OTHER_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.test.storage.one.OtherInterface <em>Other Interface</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.test.storage.one.OtherInterface
     * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getOtherInterface()
     * @generated
     */
    int OTHER_INTERFACE = 3;

    /**
     * The feature id for the '<em><b>Next</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_INTERFACE__NEXT = 0;

    /**
     * The number of structural features of the '<em>Other Interface</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_INTERFACE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Other Interface</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OTHER_INTERFACE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.test.storage.one.impl.SpecialAImpl <em>Special A</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.test.storage.one.impl.SpecialAImpl
     * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getSpecialA()
     * @generated
     */
    int SPECIAL_A = 4;

    /**
     * The feature id for the '<em><b>Next</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIAL_A__NEXT = OTHER_INTERFACE__NEXT;

    /**
     * The feature id for the '<em><b>Relate</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIAL_A__RELATE = OTHER_INTERFACE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Special A</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIAL_A_FEATURE_COUNT = OTHER_INTERFACE_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Special A</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIAL_A_OPERATION_COUNT = OTHER_INTERFACE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.test.storage.one.impl.SpecialBImpl <em>Special B</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.test.storage.one.impl.SpecialBImpl
     * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getSpecialB()
     * @generated
     */
    int SPECIAL_B = 5;

    /**
     * The feature id for the '<em><b>Next</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIAL_B__NEXT = OTHER_INTERFACE__NEXT;

    /**
     * The number of structural features of the '<em>Special B</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIAL_B_FEATURE_COUNT = OTHER_INTERFACE_FEATURE_COUNT + 0;

    /**
     * The number of operations of the '<em>Special B</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SPECIAL_B_OPERATION_COUNT = OTHER_INTERFACE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.test.storage.one.EnumValueExample <em>Enum Value Example</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.test.storage.one.EnumValueExample
     * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getEnumValueExample()
     * @generated
     */
    int ENUM_VALUE_EXAMPLE = 6;


    /**
     * Returns the meta object for class '{@link org.iobserve.model.test.storage.one.Root <em>Root</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Root</em>'.
     * @see org.iobserve.model.test.storage.one.Root
     * @generated
     */
    EClass getRoot();

    /**
     * Returns the meta object for the attribute '{@link org.iobserve.model.test.storage.one.Root#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.iobserve.model.test.storage.one.Root#getName()
     * @see #getRoot()
     * @generated
     */
    EAttribute getRoot_Name();

    /**
     * Returns the meta object for the attribute list '{@link org.iobserve.model.test.storage.one.Root#getLabels <em>Labels</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Labels</em>'.
     * @see org.iobserve.model.test.storage.one.Root#getLabels()
     * @see #getRoot()
     * @generated
     */
    EAttribute getRoot_Labels();

    /**
     * Returns the meta object for the attribute '{@link org.iobserve.model.test.storage.one.Root#getFixed <em>Fixed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fixed</em>'.
     * @see org.iobserve.model.test.storage.one.Root#getFixed()
     * @see #getRoot()
     * @generated
     */
    EAttribute getRoot_Fixed();

    /**
     * Returns the meta object for the attribute '{@link org.iobserve.model.test.storage.one.Root#getEnumerate <em>Enumerate</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Enumerate</em>'.
     * @see org.iobserve.model.test.storage.one.Root#getEnumerate()
     * @see #getRoot()
     * @generated
     */
    EAttribute getRoot_Enumerate();

    /**
     * Returns the meta object for the containment reference list '{@link org.iobserve.model.test.storage.one.Root#getOthers <em>Others</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Others</em>'.
     * @see org.iobserve.model.test.storage.one.Root#getOthers()
     * @see #getRoot()
     * @generated
     */
    EReference getRoot_Others();

    /**
     * Returns the meta object for the containment reference list '{@link org.iobserve.model.test.storage.one.Root#getIfaceOthers <em>Iface Others</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Iface Others</em>'.
     * @see org.iobserve.model.test.storage.one.Root#getIfaceOthers()
     * @see #getRoot()
     * @generated
     */
    EReference getRoot_IfaceOthers();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.test.storage.one.Other <em>Other</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Other</em>'.
     * @see org.iobserve.model.test.storage.one.Other
     * @generated
     */
    EClass getOther();

    /**
     * Returns the meta object for the attribute '{@link org.iobserve.model.test.storage.one.Other#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.iobserve.model.test.storage.one.Other#getName()
     * @see #getOther()
     * @generated
     */
    EAttribute getOther_Name();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.test.storage.one.OtherSubType <em>Other Sub Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Other Sub Type</em>'.
     * @see org.iobserve.model.test.storage.one.OtherSubType
     * @generated
     */
    EClass getOtherSubType();

    /**
     * Returns the meta object for the attribute '{@link org.iobserve.model.test.storage.one.OtherSubType#isLabel <em>Label</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Label</em>'.
     * @see org.iobserve.model.test.storage.one.OtherSubType#isLabel()
     * @see #getOtherSubType()
     * @generated
     */
    EAttribute getOtherSubType_Label();

    /**
     * Returns the meta object for the containment reference '{@link org.iobserve.model.test.storage.one.OtherSubType#getOther <em>Other</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Other</em>'.
     * @see org.iobserve.model.test.storage.one.OtherSubType#getOther()
     * @see #getOtherSubType()
     * @generated
     */
    EReference getOtherSubType_Other();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.test.storage.one.OtherInterface <em>Other Interface</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Other Interface</em>'.
     * @see org.iobserve.model.test.storage.one.OtherInterface
     * @generated
     */
    EClass getOtherInterface();

    /**
     * Returns the meta object for the containment reference '{@link org.iobserve.model.test.storage.one.OtherInterface#getNext <em>Next</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Next</em>'.
     * @see org.iobserve.model.test.storage.one.OtherInterface#getNext()
     * @see #getOtherInterface()
     * @generated
     */
    EReference getOtherInterface_Next();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.test.storage.one.SpecialA <em>Special A</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Special A</em>'.
     * @see org.iobserve.model.test.storage.one.SpecialA
     * @generated
     */
    EClass getSpecialA();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.test.storage.one.SpecialA#getRelate <em>Relate</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Relate</em>'.
     * @see org.iobserve.model.test.storage.one.SpecialA#getRelate()
     * @see #getSpecialA()
     * @generated
     */
    EReference getSpecialA_Relate();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.test.storage.one.SpecialB <em>Special B</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Special B</em>'.
     * @see org.iobserve.model.test.storage.one.SpecialB
     * @generated
     */
    EClass getSpecialB();

    /**
     * Returns the meta object for enum '{@link org.iobserve.model.test.storage.one.EnumValueExample <em>Enum Value Example</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Enum Value Example</em>'.
     * @see org.iobserve.model.test.storage.one.EnumValueExample
     * @generated
     */
    EEnum getEnumValueExample();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    OneFactory getOneFactory();

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
         * The meta object literal for the '{@link org.iobserve.model.test.storage.one.impl.RootImpl <em>Root</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.test.storage.one.impl.RootImpl
         * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getRoot()
         * @generated
         */
        EClass ROOT = eINSTANCE.getRoot();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ROOT__NAME = eINSTANCE.getRoot_Name();

        /**
         * The meta object literal for the '<em><b>Labels</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ROOT__LABELS = eINSTANCE.getRoot_Labels();

        /**
         * The meta object literal for the '<em><b>Fixed</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ROOT__FIXED = eINSTANCE.getRoot_Fixed();

        /**
         * The meta object literal for the '<em><b>Enumerate</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ROOT__ENUMERATE = eINSTANCE.getRoot_Enumerate();

        /**
         * The meta object literal for the '<em><b>Others</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ROOT__OTHERS = eINSTANCE.getRoot_Others();

        /**
         * The meta object literal for the '<em><b>Iface Others</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference ROOT__IFACE_OTHERS = eINSTANCE.getRoot_IfaceOthers();

        /**
         * The meta object literal for the '{@link org.iobserve.model.test.storage.one.impl.OtherImpl <em>Other</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.test.storage.one.impl.OtherImpl
         * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getOther()
         * @generated
         */
        EClass OTHER = eINSTANCE.getOther();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OTHER__NAME = eINSTANCE.getOther_Name();

        /**
         * The meta object literal for the '{@link org.iobserve.model.test.storage.one.impl.OtherSubTypeImpl <em>Other Sub Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.test.storage.one.impl.OtherSubTypeImpl
         * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getOtherSubType()
         * @generated
         */
        EClass OTHER_SUB_TYPE = eINSTANCE.getOtherSubType();

        /**
         * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OTHER_SUB_TYPE__LABEL = eINSTANCE.getOtherSubType_Label();

        /**
         * The meta object literal for the '<em><b>Other</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OTHER_SUB_TYPE__OTHER = eINSTANCE.getOtherSubType_Other();

        /**
         * The meta object literal for the '{@link org.iobserve.model.test.storage.one.OtherInterface <em>Other Interface</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.test.storage.one.OtherInterface
         * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getOtherInterface()
         * @generated
         */
        EClass OTHER_INTERFACE = eINSTANCE.getOtherInterface();

        /**
         * The meta object literal for the '<em><b>Next</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OTHER_INTERFACE__NEXT = eINSTANCE.getOtherInterface_Next();

        /**
         * The meta object literal for the '{@link org.iobserve.model.test.storage.one.impl.SpecialAImpl <em>Special A</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.test.storage.one.impl.SpecialAImpl
         * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getSpecialA()
         * @generated
         */
        EClass SPECIAL_A = eINSTANCE.getSpecialA();

        /**
         * The meta object literal for the '<em><b>Relate</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SPECIAL_A__RELATE = eINSTANCE.getSpecialA_Relate();

        /**
         * The meta object literal for the '{@link org.iobserve.model.test.storage.one.impl.SpecialBImpl <em>Special B</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.test.storage.one.impl.SpecialBImpl
         * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getSpecialB()
         * @generated
         */
        EClass SPECIAL_B = eINSTANCE.getSpecialB();

        /**
         * The meta object literal for the '{@link org.iobserve.model.test.storage.one.EnumValueExample <em>Enum Value Example</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.test.storage.one.EnumValueExample
         * @see org.iobserve.model.test.storage.one.impl.OnePackageImpl#getEnumValueExample()
         * @generated
         */
        EEnum ENUM_VALUE_EXAMPLE = eINSTANCE.getEnumValueExample();

    }

} //OnePackage
