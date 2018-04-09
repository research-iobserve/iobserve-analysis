/**
 */
package org.iobserve.planning.systemadaptation;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.planning.systemadaptation.SystemadaptationPackage
 * @generated
 */
public interface SystemadaptationFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    SystemadaptationFactory eINSTANCE = org.iobserve.planning.systemadaptation.impl.SystemadaptationFactoryImpl.init();

    /**
     * Returns a new object of class '<em>System Adaptation</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>System Adaptation</em>'.
     * @generated
     */
    SystemAdaptation createSystemAdaptation();

    /**
     * Returns a new object of class '<em>Change Repository Component Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Change Repository Component Action</em>'.
     * @generated
     */
    ChangeRepositoryComponentAction createChangeRepositoryComponentAction();

    /**
     * Returns a new object of class '<em>Replicate Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Replicate Action</em>'.
     * @generated
     */
    ReplicateAction createReplicateAction();

    /**
     * Returns a new object of class '<em>Dereplicate Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Dereplicate Action</em>'.
     * @generated
     */
    DereplicateAction createDereplicateAction();

    /**
     * Returns a new object of class '<em>Migrate Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Migrate Action</em>'.
     * @generated
     */
    MigrateAction createMigrateAction();

    /**
     * Returns a new object of class '<em>Allocate Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Allocate Action</em>'.
     * @generated
     */
    AllocateAction createAllocateAction();

    /**
     * Returns a new object of class '<em>Deallocate Action</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Deallocate Action</em>'.
     * @generated
     */
    DeallocateAction createDeallocateAction();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    SystemadaptationPackage getSystemadaptationPackage();

} //SystemadaptationFactory
