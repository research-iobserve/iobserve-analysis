/**
 */
package org.iobserve.model.privacy;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.model.privacy.PrivacyPackage
 * @generated
 */
public interface PrivacyFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    PrivacyFactory eINSTANCE = org.iobserve.model.privacy.impl.PrivacyFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Model</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Model</em>'.
     * @generated
     */
    PrivacyModel createPrivacyModel();

    /**
     * Returns a new object of class '<em>Return Type Privacy</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Return Type Privacy</em>'.
     * @generated
     */
    ReturnTypePrivacy createReturnTypePrivacy();

    /**
     * Returns a new object of class '<em>Geo Location</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Geo Location</em>'.
     * @generated
     */
    GeoLocation createGeoLocation();

    /**
     * Returns a new object of class '<em>Parameter Privacy</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Parameter Privacy</em>'.
     * @generated
     */
    ParameterPrivacy createParameterPrivacy();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    PrivacyPackage getPrivacyPackage();

} //PrivacyFactory
