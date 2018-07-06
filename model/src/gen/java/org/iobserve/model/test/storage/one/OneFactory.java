/**
 */
package org.iobserve.model.test.storage.one;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.model.test.storage.one.OnePackage
 * @generated
 */
public interface OneFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    OneFactory eINSTANCE = org.iobserve.model.test.storage.one.impl.OneFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Root</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Root</em>'.
     * @generated
     */
    Root createRoot();

    /**
     * Returns a new object of class '<em>Other</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Other</em>'.
     * @generated
     */
    Other createOther();

    /**
     * Returns a new object of class '<em>Other Sub Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Other Sub Type</em>'.
     * @generated
     */
    OtherSubType createOtherSubType();

    /**
     * Returns a new object of class '<em>Special A</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Special A</em>'.
     * @generated
     */
    SpecialA createSpecialA();

    /**
     * Returns a new object of class '<em>Special B</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Special B</em>'.
     * @generated
     */
    SpecialB createSpecialB();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    OnePackage getOnePackage();

} //OneFactory
