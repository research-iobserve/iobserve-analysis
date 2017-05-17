/**
 */
package org.iobserve.planning.changegroup;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.planning.changegroup.ChangegroupPackage
 * @generated
 */
public interface ChangegroupFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ChangegroupFactory eINSTANCE = org.iobserve.planning.changegroup.impl.ChangegroupFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Change Group</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Change Group</em>'.
	 * @generated
	 */
	ChangeGroup createChangeGroup();

	/**
	 * Returns a new object of class '<em>Action</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Action</em>'.
	 * @generated
	 */
	Action createAction();

	/**
	 * Returns a new object of class '<em>Replicate Action</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Replicate Action</em>'.
	 * @generated
	 */
	ReplicateAction createReplicateAction();

	/**
	 * Returns a new object of class '<em>Migrate Action</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Migrate Action</em>'.
	 * @generated
	 */
	MigrateAction createMigrateAction();

	/**
	 * Returns a new object of class '<em>Change Group Repository</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Change Group Repository</em>'.
	 * @generated
	 */
	ChangeGroupRepository createChangeGroupRepository();

	/**
	 * Returns a new object of class '<em>Allocate Action</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Allocate Action</em>'.
	 * @generated
	 */
	AllocateAction createAllocateAction();

	/**
	 * Returns a new object of class '<em>Configure Action</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Configure Action</em>'.
	 * @generated
	 */
	ConfigureAction createConfigureAction();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ChangegroupPackage getChangegroupPackage();

} //ChangegroupFactory
