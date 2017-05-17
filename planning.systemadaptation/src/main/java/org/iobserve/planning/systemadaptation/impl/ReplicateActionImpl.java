/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.systemadaptationPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Replicate Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl#getNewCount <em>New Count</em>}</li>
 *   <li>{@link org.iobserve.planning.systemadaptation.impl.ReplicateActionImpl#getOldCount <em>Old Count</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReplicateActionImpl extends ResourceContainerActionImpl implements ReplicateAction {
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
		return systemadaptationPackage.Literals.REPLICATE_ACTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<Integer> getNewCount() {
		return (EList<Integer>)eGet(systemadaptationPackage.Literals.REPLICATE_ACTION__NEW_COUNT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<Integer> getOldCount() {
		return (EList<Integer>)eGet(systemadaptationPackage.Literals.REPLICATE_ACTION__OLD_COUNT, true);
	}

} //ReplicateActionImpl
