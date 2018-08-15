/**
 */
package org.iobserve.model.correspondence;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.model.correspondence.CorrespondencePackage
 * @generated
 */
public interface CorrespondenceFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    CorrespondenceFactory eINSTANCE = org.iobserve.model.correspondence.impl.CorrespondenceFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Model</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Model</em>'.
     * @generated
     */
    CorrespondenceModel createCorrespondenceModel();

    /**
     * Returns a new object of class '<em>Data Type Entry</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Data Type Entry</em>'.
     * @generated
     */
    DataTypeEntry createDataTypeEntry();

    /**
     * Returns a new object of class '<em>Part</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Part</em>'.
     * @generated
     */
    Part createPart();

    /**
     * Returns a new object of class '<em>Component Entry</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Component Entry</em>'.
     * @generated
     */
    ComponentEntry createComponentEntry();

    /**
     * Returns a new object of class '<em>Allocation Entry</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Allocation Entry</em>'.
     * @generated
     */
    AllocationEntry createAllocationEntry();

    /**
     * Returns a new object of class '<em>Assembly Entry</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Assembly Entry</em>'.
     * @generated
     */
    AssemblyEntry createAssemblyEntry();

    /**
     * Returns a new object of class '<em>Operation Entry</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Operation Entry</em>'.
     * @generated
     */
    OperationEntry createOperationEntry();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    CorrespondencePackage getCorrespondencePackage();

} //CorrespondenceFactory
