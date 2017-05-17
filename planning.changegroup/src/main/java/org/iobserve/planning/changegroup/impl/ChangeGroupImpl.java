/**
 */
package org.iobserve.planning.changegroup.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.iobserve.planning.changegroup.Action;
import org.iobserve.planning.changegroup.ChangeGroup;
import org.iobserve.planning.changegroup.ChangegroupPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Change Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.changegroup.impl.ChangeGroupImpl#getActions <em>Actions</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ChangeGroupImpl extends MinimalEObjectImpl.Container implements ChangeGroup {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangeGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangegroupPackage.Literals.CHANGE_GROUP;
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
	public EList<Action> getActions() {
		return (EList<Action>)eGet(ChangegroupPackage.Literals.CHANGE_GROUP__ACTIONS, true);
	}

} //ChangeGroupImpl
