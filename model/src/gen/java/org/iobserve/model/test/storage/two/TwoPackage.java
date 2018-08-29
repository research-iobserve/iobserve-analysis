/**
 */
package org.iobserve.model.test.storage.two;

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
 * @see org.iobserve.model.test.storage.two.TwoFactory
 * @model kind="package"
 * @generated
 */
public interface TwoPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "two";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://local/storage/two";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "two";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    TwoPackage eINSTANCE = org.iobserve.model.test.storage.two.impl.TwoPackageImpl.init();

    /**
     * The meta object id for the '{@link org.iobserve.model.test.storage.two.impl.TwoImpl <em>Two</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.test.storage.two.impl.TwoImpl
     * @see org.iobserve.model.test.storage.two.impl.TwoPackageImpl#getTwo()
     * @generated
     */
    int TWO = 0;

    /**
     * The feature id for the '<em><b>Links</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TWO__LINKS = 0;

    /**
     * The number of structural features of the '<em>Two</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TWO_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Two</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int TWO_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.iobserve.model.test.storage.two.impl.LinkImpl <em>Link</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.iobserve.model.test.storage.two.impl.LinkImpl
     * @see org.iobserve.model.test.storage.two.impl.TwoPackageImpl#getLink()
     * @generated
     */
    int LINK = 1;

    /**
     * The feature id for the '<em><b>Reference</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK__REFERENCE = 0;

    /**
     * The number of structural features of the '<em>Link</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Link</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int LINK_OPERATION_COUNT = 0;


    /**
     * Returns the meta object for class '{@link org.iobserve.model.test.storage.two.Two <em>Two</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Two</em>'.
     * @see org.iobserve.model.test.storage.two.Two
     * @generated
     */
    EClass getTwo();

    /**
     * Returns the meta object for the containment reference list '{@link org.iobserve.model.test.storage.two.Two#getLinks <em>Links</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Links</em>'.
     * @see org.iobserve.model.test.storage.two.Two#getLinks()
     * @see #getTwo()
     * @generated
     */
    EReference getTwo_Links();

    /**
     * Returns the meta object for class '{@link org.iobserve.model.test.storage.two.Link <em>Link</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Link</em>'.
     * @see org.iobserve.model.test.storage.two.Link
     * @generated
     */
    EClass getLink();

    /**
     * Returns the meta object for the reference '{@link org.iobserve.model.test.storage.two.Link#getReference <em>Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Reference</em>'.
     * @see org.iobserve.model.test.storage.two.Link#getReference()
     * @see #getLink()
     * @generated
     */
    EReference getLink_Reference();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    TwoFactory getTwoFactory();

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
         * The meta object literal for the '{@link org.iobserve.model.test.storage.two.impl.TwoImpl <em>Two</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.test.storage.two.impl.TwoImpl
         * @see org.iobserve.model.test.storage.two.impl.TwoPackageImpl#getTwo()
         * @generated
         */
        EClass TWO = eINSTANCE.getTwo();

        /**
         * The meta object literal for the '<em><b>Links</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference TWO__LINKS = eINSTANCE.getTwo_Links();

        /**
         * The meta object literal for the '{@link org.iobserve.model.test.storage.two.impl.LinkImpl <em>Link</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.iobserve.model.test.storage.two.impl.LinkImpl
         * @see org.iobserve.model.test.storage.two.impl.TwoPackageImpl#getLink()
         * @generated
         */
        EClass LINK = eINSTANCE.getLink();

        /**
         * The meta object literal for the '<em><b>Reference</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference LINK__REFERENCE = eINSTANCE.getLink_Reference();

    }

} //TwoPackage
