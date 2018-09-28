/**
 */
package org.iobserve.model.test.storage.two;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.model.test.storage.two.TwoPackage
 * @generated
 */
public interface TwoFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    TwoFactory eINSTANCE = org.iobserve.model.test.storage.two.impl.TwoFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Two</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Two</em>'.
     * @generated
     */
    Two createTwo();

    /**
     * Returns a new object of class '<em>Link</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Link</em>'.
     * @generated
     */
    Link createLink();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    TwoPackage getTwoPackage();

} //TwoFactory
