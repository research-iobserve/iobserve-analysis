/**
 */
package org.iobserve.planning.changegroup.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.iobserve.planning.changegroup.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ChangegroupFactoryImpl extends EFactoryImpl implements ChangegroupFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ChangegroupFactory init() {
		try {
			ChangegroupFactory theChangegroupFactory = (ChangegroupFactory)EPackage.Registry.INSTANCE.getEFactory(ChangegroupPackage.eNS_URI);
			if (theChangegroupFactory != null) {
				return theChangegroupFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ChangegroupFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangegroupFactoryImpl() {
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
			case ChangegroupPackage.CHANGE_GROUP: return createChangeGroup();
			case ChangegroupPackage.ACTION: return createAction();
			case ChangegroupPackage.REPLICATE_ACTION: return createReplicateAction();
			case ChangegroupPackage.MIGRATE_ACTION: return createMigrateAction();
			case ChangegroupPackage.CHANGE_GROUP_REPOSITORY: return createChangeGroupRepository();
			case ChangegroupPackage.ALLOCATE_ACTION: return createAllocateAction();
			case ChangegroupPackage.CONFIGURE_ACTION: return createConfigureAction();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeGroup createChangeGroup() {
		ChangeGroupImpl changeGroup = new ChangeGroupImpl();
		return changeGroup;
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
	public ReplicateAction createReplicateAction() {
		ReplicateActionImpl replicateAction = new ReplicateActionImpl();
		return replicateAction;
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
	public ChangeGroupRepository createChangeGroupRepository() {
		ChangeGroupRepositoryImpl changeGroupRepository = new ChangeGroupRepositoryImpl();
		return changeGroupRepository;
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
	public ConfigureAction createConfigureAction() {
		ConfigureActionImpl configureAction = new ConfigureActionImpl();
		return configureAction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangegroupPackage getChangegroupPackage() {
		return (ChangegroupPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ChangegroupPackage getPackage() {
		return ChangegroupPackage.eINSTANCE;
	}

} //ChangegroupFactoryImpl
