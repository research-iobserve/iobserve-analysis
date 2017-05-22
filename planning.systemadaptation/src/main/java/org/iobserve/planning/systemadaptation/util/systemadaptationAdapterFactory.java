/**
 */
package org.iobserve.planning.systemadaptation.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.iobserve.planning.systemadaptation.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.iobserve.planning.systemadaptation.systemadaptationPackage
 * @generated
 */
public class systemadaptationAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static systemadaptationPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public systemadaptationAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = systemadaptationPackage.eINSTANCE;
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
	protected systemadaptationSwitch<Adapter> modelSwitch =
		new systemadaptationSwitch<Adapter>() {
			@Override
			public Adapter caseSystemAdaptation(SystemAdaptation object) {
				return createSystemAdaptationAdapter();
			}
			@Override
			public Adapter caseAction(Action object) {
				return createActionAdapter();
			}
			@Override
			public Adapter caseAssemblyContextAction(AssemblyContextAction object) {
				return createAssemblyContextActionAdapter();
			}
			@Override
			public Adapter caseResourceContainerAction(ResourceContainerAction object) {
				return createResourceContainerActionAdapter();
			}
			@Override
			public Adapter caseChangeRepositoryComponentAction(ChangeRepositoryComponentAction object) {
				return createChangeRepositoryComponentActionAdapter();
			}
			@Override
			public Adapter caseAllocateAction(AllocateAction object) {
				return createAllocateActionAdapter();
			}
			@Override
			public Adapter caseDeallocateAction(DeallocateAction object) {
				return createDeallocateActionAdapter();
			}
			@Override
			public Adapter caseMigrateAction(MigrateAction object) {
				return createMigrateActionAdapter();
			}
			@Override
			public Adapter caseAcquireAction(AcquireAction object) {
				return createAcquireActionAdapter();
			}
			@Override
			public Adapter caseTerminateAction(TerminateAction object) {
				return createTerminateActionAdapter();
			}
			@Override
			public Adapter caseReplicateAction(ReplicateAction object) {
				return createReplicateActionAdapter();
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
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.systemadaptation.SystemAdaptation <em>System Adaptation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.systemadaptation.SystemAdaptation
	 * @generated
	 */
	public Adapter createSystemAdaptationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.systemadaptation.Action <em>Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.systemadaptation.Action
	 * @generated
	 */
	public Adapter createActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.systemadaptation.AssemblyContextAction <em>Assembly Context Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.systemadaptation.AssemblyContextAction
	 * @generated
	 */
	public Adapter createAssemblyContextActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.systemadaptation.ResourceContainerAction <em>Resource Container Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.systemadaptation.ResourceContainerAction
	 * @generated
	 */
	public Adapter createResourceContainerActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction <em>Change Repository Component Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction
	 * @generated
	 */
	public Adapter createChangeRepositoryComponentActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.systemadaptation.AllocateAction <em>Allocate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.systemadaptation.AllocateAction
	 * @generated
	 */
	public Adapter createAllocateActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.systemadaptation.DeallocateAction <em>Deallocate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.systemadaptation.DeallocateAction
	 * @generated
	 */
	public Adapter createDeallocateActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.systemadaptation.MigrateAction <em>Migrate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.systemadaptation.MigrateAction
	 * @generated
	 */
	public Adapter createMigrateActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.systemadaptation.AcquireAction <em>Acquire Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.systemadaptation.AcquireAction
	 * @generated
	 */
	public Adapter createAcquireActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.systemadaptation.TerminateAction <em>Terminate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.systemadaptation.TerminateAction
	 * @generated
	 */
	public Adapter createTerminateActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.iobserve.planning.systemadaptation.ReplicateAction <em>Replicate Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.iobserve.planning.systemadaptation.ReplicateAction
	 * @generated
	 */
	public Adapter createReplicateActionAdapter() {
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

} //systemadaptationAdapterFactory
