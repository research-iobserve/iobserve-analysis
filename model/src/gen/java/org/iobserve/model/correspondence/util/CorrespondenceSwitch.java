/**
 */
package org.iobserve.model.correspondence.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.iobserve.model.correspondence.*;
import org.iobserve.model.correspondence.AbstractEntry;
import org.iobserve.model.correspondence.AllocationEntry;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.correspondence.ComponentEntry;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.OperationEntry;
import org.iobserve.model.correspondence.Part;

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
 * @see org.iobserve.model.correspondence.CorrespondencePackage
 * @generated
 */
public class CorrespondenceSwitch<T> extends Switch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static CorrespondencePackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CorrespondenceSwitch() {
        if (modelPackage == null) {
            modelPackage = CorrespondencePackage.eINSTANCE;
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
            case CorrespondencePackage.CORRESPONDENCE_MODEL: {
                CorrespondenceModel correspondenceModel = (CorrespondenceModel)theEObject;
                T result = caseCorrespondenceModel(correspondenceModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CorrespondencePackage.DATA_TYPE_ENTRY: {
                DataTypeEntry dataTypeEntry = (DataTypeEntry)theEObject;
                T result = caseDataTypeEntry(dataTypeEntry);
                if (result == null) result = caseAbstractEntry(dataTypeEntry);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CorrespondencePackage.PART: {
                Part part = (Part)theEObject;
                T result = casePart(part);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CorrespondencePackage.ABSTRACT_ENTRY: {
                AbstractEntry abstractEntry = (AbstractEntry)theEObject;
                T result = caseAbstractEntry(abstractEntry);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CorrespondencePackage.COMPONENT_ENTRY: {
                ComponentEntry componentEntry = (ComponentEntry)theEObject;
                T result = caseComponentEntry(componentEntry);
                if (result == null) result = caseAbstractEntry(componentEntry);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CorrespondencePackage.ALLOCATION_ENTRY: {
                AllocationEntry allocationEntry = (AllocationEntry)theEObject;
                T result = caseAllocationEntry(allocationEntry);
                if (result == null) result = caseAbstractEntry(allocationEntry);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CorrespondencePackage.ASSEMBLY_ENTRY: {
                AssemblyEntry assemblyEntry = (AssemblyEntry)theEObject;
                T result = caseAssemblyEntry(assemblyEntry);
                if (result == null) result = caseAbstractEntry(assemblyEntry);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CorrespondencePackage.OPERATION_ENTRY: {
                OperationEntry operationEntry = (OperationEntry)theEObject;
                T result = caseOperationEntry(operationEntry);
                if (result == null) result = caseAbstractEntry(operationEntry);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCorrespondenceModel(CorrespondenceModel object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Type Entry</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Type Entry</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDataTypeEntry(DataTypeEntry object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Part</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Part</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePart(Part object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Entry</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Entry</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAbstractEntry(AbstractEntry object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component Entry</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component Entry</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComponentEntry(ComponentEntry object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Allocation Entry</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Allocation Entry</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAllocationEntry(AllocationEntry object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Assembly Entry</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Assembly Entry</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssemblyEntry(AssemblyEntry object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operation Entry</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operation Entry</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOperationEntry(OperationEntry object) {
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

} //CorrespondenceSwitch
