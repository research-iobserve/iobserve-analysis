/**
 */
package org.iobserve.planning.systemadaptation.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.iobserve.planning.systemadaptation.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class systemadaptationFactoryImpl extends EFactoryImpl implements systemadaptationFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static systemadaptationFactory init() {
		try {
			systemadaptationFactory thesystemadaptationFactory = (systemadaptationFactory)EPackage.Registry.INSTANCE.getEFactory(systemadaptationPackage.eNS_URI);
			if (thesystemadaptationFactory != null) {
				return thesystemadaptationFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new systemadaptationFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public systemadaptationFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case systemadaptationPackage.SYSTEM_ADAPTATION: return createSystemAdaptation();
			case systemadaptationPackage.ACTION: return createAction();
			case systemadaptationPackage.ASSEMBLY_CONTEXT_ACTION: return createAssemblyContextAction();
			case systemadaptationPackage.RESOURCE_CONTAINER_ACTION: return createResourceContainerAction();
			case systemadaptationPackage.CHANGE_REPOSITORY_COMPONENT_ACTION: return createChangeRepositoryComponentAction();
			case systemadaptationPackage.ALLOCATE_ACTION: return createAllocateAction();
			case systemadaptationPackage.DEALLOCATE_ACTION: return createDeallocateAction();
			case systemadaptationPackage.MIGRATE_ACTION: return createMigrateAction();
			case systemadaptationPackage.ACQUIRE_ACTION: return createAcquireAction();
			case systemadaptationPackage.TERMINATE_ACTION: return createTerminateAction();
			case systemadaptationPackage.REPLICATE_ACTION: return createReplicateAction();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SystemAdaptation createSystemAdaptation() {
		SystemAdaptationImpl systemAdaptation = new SystemAdaptationImpl();
		return systemAdaptation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Action createAction() {
		ActionImpl action = new ActionImpl();
		return action;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssemblyContextAction createAssemblyContextAction() {
		AssemblyContextActionImpl assemblyContextAction = new AssemblyContextActionImpl();
		return assemblyContextAction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceContainerAction createResourceContainerAction() {
		ResourceContainerActionImpl resourceContainerAction = new ResourceContainerActionImpl();
		return resourceContainerAction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeRepositoryComponentAction createChangeRepositoryComponentAction() {
		ChangeRepositoryComponentActionImpl changeRepositoryComponentAction = new ChangeRepositoryComponentActionImpl();
		return changeRepositoryComponentAction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AllocateAction createAllocateAction() {
		AllocateActionImpl allocateAction = new AllocateActionImpl();
		return allocateAction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeallocateAction createDeallocateAction() {
		DeallocateActionImpl deallocateAction = new DeallocateActionImpl();
		return deallocateAction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MigrateAction createMigrateAction() {
		MigrateActionImpl migrateAction = new MigrateActionImpl();
		return migrateAction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AcquireAction createAcquireAction() {
		AcquireActionImpl acquireAction = new AcquireActionImpl();
		return acquireAction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReplicateAction createReplicateAction() {
		ReplicateActionImpl replicateAction = new ReplicateActionImpl();
		return replicateAction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TerminateAction createTerminateAction() {
		TerminateActionImpl terminateAction = new TerminateActionImpl();
		return terminateAction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public systemadaptationPackage getsystemadaptationPackage() {
		return (systemadaptationPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static systemadaptationPackage getPackage() {
		return systemadaptationPackage.eINSTANCE;
	}

} //systemadaptationFactoryImpl
