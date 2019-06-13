/**
 */
package org.iobserve.model.privacy.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.iobserve.model.privacy.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.iobserve.model.privacy.PrivacyPackage
 * @generated
 */
public class PrivacySwitch<T> extends Switch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static PrivacyPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PrivacySwitch() {
        if (modelPackage == null) {
            modelPackage = PrivacyPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage) {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case PrivacyPackage.DATA_PROTECTION_MODEL: {
                DataProtectionModel dataProtectionModel = (DataProtectionModel)theEObject;
                T result = caseDataProtectionModel(dataProtectionModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case PrivacyPackage.RETURN_TYPE_DATA_PROTECTION: {
                ReturnTypeDataProtection returnTypeDataProtection = (ReturnTypeDataProtection)theEObject;
                T result = caseReturnTypeDataProtection(returnTypeDataProtection);
                if (result == null) result = caseIDataProtectionAnnotation(returnTypeDataProtection);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case PrivacyPackage.IDATA_PROTECTION_ANNOTATION: {
                IDataProtectionAnnotation iDataProtectionAnnotation = (IDataProtectionAnnotation)theEObject;
                T result = caseIDataProtectionAnnotation(iDataProtectionAnnotation);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case PrivacyPackage.GEO_LOCATION: {
                GeoLocation geoLocation = (GeoLocation)theEObject;
                T result = caseGeoLocation(geoLocation);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case PrivacyPackage.PARAMETER_DATA_PROTECTION: {
                ParameterDataProtection parameterDataProtection = (ParameterDataProtection)theEObject;
                T result = caseParameterDataProtection(parameterDataProtection);
                if (result == null) result = caseIDataProtectionAnnotation(parameterDataProtection);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case PrivacyPackage.ENCAPSULATED_DATA_SOURCE: {
                EncapsulatedDataSource encapsulatedDataSource = (EncapsulatedDataSource)theEObject;
                T result = caseEncapsulatedDataSource(encapsulatedDataSource);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Protection Model</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Protection Model</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDataProtectionModel(DataProtectionModel object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Return Type Data Protection</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Return Type Data Protection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReturnTypeDataProtection(ReturnTypeDataProtection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>IData Protection Annotation</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>IData Protection Annotation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIDataProtectionAnnotation(IDataProtectionAnnotation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Geo Location</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Geo Location</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGeoLocation(GeoLocation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Parameter Data Protection</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Parameter Data Protection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseParameterDataProtection(ParameterDataProtection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Encapsulated Data Source</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Encapsulated Data Source</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEncapsulatedDataSource(EncapsulatedDataSource object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object) {
        return null;
    }

} //PrivacySwitch
