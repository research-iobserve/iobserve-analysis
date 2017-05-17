/**
 */
package org.iobserve.planning.changegroup.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.changegroup.ChangegroupPackage;
import org.iobserve.planning.changegroup.ReplicateAction;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Replicate Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.changegroup.impl.ReplicateActionImpl#getNewCount <em>New Count</em>}</li>
 *   <li>{@link org.iobserve.planning.changegroup.impl.ReplicateActionImpl#getOldCount <em>Old Count</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReplicateActionImpl extends ActionImpl implements ReplicateAction {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ReplicateActionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangegroupPackage.Literals.REPLICATE_ACTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<Integer> getNewCount() {
		return (EList<Integer>)eGet(ChangegroupPackage.Literals.REPLICATE_ACTION__NEW_COUNT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<Integer> getOldCount() {
		return (EList<Integer>)eGet(ChangegroupPackage.Literals.REPLICATE_ACTION__OLD_COUNT, true);
	}

} //ReplicateActionImpl
