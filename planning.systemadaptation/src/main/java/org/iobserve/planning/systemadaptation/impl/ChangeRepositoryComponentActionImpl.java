/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.systemadaptationPackage;

import org.palladiosimulator.pcm.repository.RepositoryComponent;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Repository Component Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl#getNewRepositoryComponent <em>New Repository Component</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeRepositoryComponentActionImpl extends AssemblyContextActionImpl implements ChangeRepositoryComponentAction {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangeRepositoryComponentActionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return systemadaptationPackage.Literals.CHANGE_REPOSITORY_COMPONENT_ACTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RepositoryComponent getNewRepositoryComponent() {
		return (RepositoryComponent)eGet(systemadaptationPackage.Literals.CHANGE_REPOSITORY_COMPONENT_ACTION__NEW_REPOSITORY_COMPONENT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewRepositoryComponent(RepositoryComponent newNewRepositoryComponent) {
		eSet(systemadaptationPackage.Literals.CHANGE_REPOSITORY_COMPONENT_ACTION__NEW_REPOSITORY_COMPONENT, newNewRepositoryComponent);
	}

} //ChangeRepositoryComponentActionImpl
