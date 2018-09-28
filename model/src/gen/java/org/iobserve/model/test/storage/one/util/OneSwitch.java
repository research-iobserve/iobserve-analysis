/**
 */
package org.iobserve.model.test.storage.one.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.iobserve.model.test.storage.one.*;

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
 * @see org.iobserve.model.test.storage.one.OnePackage
 * @generated
 */
public class OneSwitch<T> extends Switch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static OnePackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OneSwitch() {
        if (modelPackage == null) {
            modelPackage = OnePackage.eINSTANCE;
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
            case OnePackage.ROOT: {
                Root root = (Root)theEObject;
                T result = caseRoot(root);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case OnePackage.OTHER: {
                Other other = (Other)theEObject;
                T result = caseOther(other);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case OnePackage.OTHER_SUB_TYPE: {
                OtherSubType otherSubType = (OtherSubType)theEObject;
                T result = caseOtherSubType(otherSubType);
                if (result == null) result = caseOther(otherSubType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case OnePackage.OTHER_INTERFACE: {
                OtherInterface otherInterface = (OtherInterface)theEObject;
                T result = caseOtherInterface(otherInterface);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case OnePackage.SPECIAL_A: {
                SpecialA specialA = (SpecialA)theEObject;
                T result = caseSpecialA(specialA);
                if (result == null) result = caseOtherInterface(specialA);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case OnePackage.SPECIAL_B: {
                SpecialB specialB = (SpecialB)theEObject;
                T result = caseSpecialB(specialB);
                if (result == null) result = caseOtherInterface(specialB);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Root</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Root</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRoot(Root object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Other</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Other</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOther(Other object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Other Sub Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Other Sub Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOtherSubType(OtherSubType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Other Interface</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Other Interface</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOtherInterface(OtherInterface object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Special A</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Special A</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSpecialA(SpecialA object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Special B</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Special B</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSpecialB(SpecialB object) {
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

} //OneSwitch
