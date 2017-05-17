/**
 */
package org.iobserve.planning.changegroup.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.iobserve.planning.changegroup.ChangeGroup;
import org.iobserve.planning.changegroup.ChangeGroupRepository;
import org.iobserve.planning.changegroup.ChangegroupPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Group Repository</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.changegroup.impl.ChangeGroupRepositoryImpl#getChangeGroups <em>Change Groups</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeGroupRepositoryImpl extends MinimalEObjectImpl.Container implements ChangeGroupRepository {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangeGroupRepositoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangegroupPackage.Literals.CHANGE_GROUP_REPOSITORY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected int eStaticFeatureCount() {
		return 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<ChangeGroup> getChangeGroups() {
		return (EList<ChangeGroup>)eGet(ChangegroupPackage.Literals.CHANGE_GROUP_REPOSITORY__CHANGE_GROUPS, true);
	}

} //ChangeGroupRepositoryImpl
