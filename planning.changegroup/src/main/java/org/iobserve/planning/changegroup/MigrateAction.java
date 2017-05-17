/**
 */
package org.iobserve.planning.changegroup;

import org.palladiosimulator.pcm.cloud.pcmcloud.resourceenvironmentcloud.ResourceContainerCloud;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Migrate Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.changegroup.MigrateAction#getMigrationTargetContainer <em>Migration Target Container</em>}</li>
 * </ul>
 *
 * @see org.iobserve.planning.changegroup.ChangegroupPackage#getMigrateAction()
 * @model
 * @generated
 */
public interface MigrateAction extends Action {
	/**
	 * Returns the value of the '<em><b>Migration Target Container</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Migration Target Container</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Migration Target Container</em>' reference.
	 * @see #setMigrationTargetContainer(ResourceContainerCloud)
	 * @see org.iobserve.planning.changegroup.ChangegroupPackage#getMigrateAction_MigrationTargetContainer()
	 * @model required="true"
	 * @generated
	 */
	ResourceContainerCloud getMigrationTargetContainer();

	/**
	 * Sets the value of the '{@link org.iobserve.planning.changegroup.MigrateAction#getMigrationTargetContainer <em>Migration Target Container</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Migration Target Container</em>' reference.
	 * @see #getMigrationTargetContainer()
	 * @generated
	 */
	void setMigrationTargetContainer(ResourceContainerCloud value);

} // MigrateAction
