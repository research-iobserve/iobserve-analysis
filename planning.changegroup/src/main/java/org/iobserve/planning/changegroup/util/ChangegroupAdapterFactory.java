/**
 */
package org.iobserve.planning.changegroup.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.iobserve.planning.changegroup.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.planning.changegroup.ChangegroupPackage
 * @generated
 */
public class ChangegroupAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ChangegroupPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangegroupAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ChangegroupPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChangegroupSwitch<Adapter> modelSwitch =
		new ChangegroupSwitch<Adapter>() {
			@Override
			public Adapter caseChangeGroup(ChangeGroup object) {
				return createChangeGroupAdapter();
			}
			@Override
			public Adapter caseAction(Action object) {
				return createActionAdapter();
			}
			@Override
			public Adapter caseReplicateAction(ReplicateAction object) {
				return createReplicateActionAdapter();
			}
			@Override
			public Adapter caseMigrateAction(MigrateAction object) {
				return createMigrateActionAdapter();
			}
			@Override
			public Adapter caseChangeGroupRepository(ChangeGroupRepository object) {
				return createChangeGroupRepositoryAdapter();
			}
			@Override
			public Adapter caseAllocateAction(AllocateAction object) {
				return createAllocateActionAdapter();
			}
			@Override
			public Adapter caseConfigureAction(ConfigureAction object) {
				return createConfigureActionAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.changegroup.ChangeGroup <em>Change Group</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.changegroup.ChangeGroup
	 * @generated
	 */
	public Adapter createChangeGroupAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.changegroup.Action <em>Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.changegroup.Action
	 * @generated
	 */
	public Adapter createActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.changegroup.ReplicateAction <em>Replicate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.changegroup.ReplicateAction
	 * @generated
	 */
	public Adapter createReplicateActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.changegroup.MigrateAction <em>Migrate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.changegroup.MigrateAction
	 * @generated
	 */
	public Adapter createMigrateActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.changegroup.ChangeGroupRepository <em>Change Group Repository</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.changegroup.ChangeGroupRepository
	 * @generated
	 */
	public Adapter createChangeGroupRepositoryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.changegroup.AllocateAction <em>Allocate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.changegroup.AllocateAction
	 * @generated
	 */
	public Adapter createAllocateActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.changegroup.ConfigureAction <em>Configure Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.changegroup.ConfigureAction
	 * @generated
	 */
	public Adapter createConfigureActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //ChangegroupAdapterFactory
