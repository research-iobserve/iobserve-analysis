package org.iobserve.analysis.usage.modelprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.OpenWorkload;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.UsagemodelFactory;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

public final class UsageModelProvider {

	private final UsagemodelFactory usageModelfactory = UsagemodelFactory.eINSTANCE;

	// private final Stack<AbstractAction> stack = new Stack<AbstractAction>();

	private Repository repository;
	private List<OperationInterface> operInterfaces;
	private List<BasicComponent> basicComponents;

	private UsageModel model;
	private ScenarioBehaviour scenarioBehaviour;
	private UsageScenario usageScenario;

	private EntryLevelSystemCall lastAction;
	private Start start;
	private Stop stop;

	// ********************************************************************
	// * INITIALIZATION
	// ********************************************************************

	public UsageModelProvider(final URI uriUsageModel, final URI uriRepository) {
		this.loadRepository(uriRepository);
		this.loadUsageModelInstance(uriUsageModel);
		this.resetUsageModel();
	}

	private void loadUsageModelInstance(final URI uriInstance) {
		// Initialize the model
		UsagemodelPackage.eINSTANCE.eClass();

		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> map = reg.getExtensionToFactoryMap();
		map.put("*", new XMIResourceFactoryImpl());

		final ResourceSet resSet = new ResourceSetImpl();
		resSet.setResourceFactoryRegistry(reg);

		final Resource resource = resSet.getResource(uriInstance, true);

		this.model = (UsageModel) resource.getContents().get(0);

		this.usageScenario = this.model.getUsageScenario_UsageModel().get(0);
		this.scenarioBehaviour = this.usageScenario.getScenarioBehaviour_UsageScenario();

		// TODO what about the rest of the usage scenarios?
		// for(UsageScenario nextUsgSce:this.model.getUsageScenario_UsageModel()){
		// this.scenarioBehaviour = nextUsgSce.getScenarioBehaviour_UsageScenario();
		// }
	}

	private void loadRepository(final URI repositoryUri) {
		// Initialize the model
		RepositoryPackage.eINSTANCE.eClass();

		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> map = reg.getExtensionToFactoryMap();
		map.put("*", new XMIResourceFactoryImpl());

		final ResourceSet resSet = new ResourceSetImpl();
		resSet.setResourceFactoryRegistry(reg);

		final Resource resource = resSet.getResource(repositoryUri, true);

		final Object result = resource.getContents().get(0);
		if (result instanceof Repository) {
			this.repository = (Repository) result;
			this.basicComponents = new ArrayList<BasicComponent>();
			this.operInterfaces = new ArrayList<OperationInterface>();

			// get basic components
			for (final RepositoryComponent nextRepoCmp : this.repository.getComponents__Repository()) {
				if (nextRepoCmp instanceof BasicComponent) {
					final BasicComponent basicCmp = (BasicComponent) nextRepoCmp;
					this.basicComponents.add(basicCmp);
				}
			}

			// get operation interfaces
			for (final Interface nextInterface : this.repository.getInterfaces__Repository()) {
				if (nextInterface instanceof OperationInterface) {
					final OperationInterface opInf = (OperationInterface) nextInterface;
					this.operInterfaces.add(opInf);
				}
			}
		}
	}

	private void resetUsageModel() {
		this.scenarioBehaviour.getActions_ScenarioBehaviour().clear();
	}

	// ********************************************************************
	// * GETTER / SETTER
	// ********************************************************************

	public UsageModel getModel() {
		return this.model;
	}

	// ********************************************************************
	// * BUSINESS METHODS
	// ********************************************************************

	private OperationSignature getOperationSignature(final String operationSig) {
		for (final OperationInterface nextOpInter : this.operInterfaces) {
			for (final OperationSignature nextOpSig : nextOpInter
					.getSignatures__OperationInterface()) {
				if (nextOpSig.getEntityName().equalsIgnoreCase(operationSig)) {
					return nextOpSig;
				}
			}
		}
		return null;
	}

	private BasicComponent getBasicComponent(final String operationSig) {
		for (final BasicComponent nextBasicCmp : this.basicComponents) {
			for (final ServiceEffectSpecification nextSerEffSpec : nextBasicCmp.getServiceEffectSpecifications__BasicComponent()) {
				if (nextSerEffSpec instanceof ResourceDemandingSEFF) {
					final ResourceDemandingSEFF rdSeff = (ResourceDemandingSEFF) nextSerEffSpec;
					if (operationSig.equalsIgnoreCase(rdSeff.getId())) {
						return nextBasicCmp;
					}
				}
			}
		}
		return null;
	}

	public void addAction(final String operationSig) {
		final OperationSignature opSig = this.getOperationSignature(operationSig);
		if (opSig != null) {
			final EntryLevelSystemCall eSysCall = this.usageModelfactory.createEntryLevelSystemCall();
			eSysCall.setEntityName(opSig.getEntityName());
			eSysCall.setOperationSignature__EntryLevelSystemCall(opSig);
			final OperationInterface opInf = opSig.getInterface__OperationSignature();
			final BasicComponent bCmp = this.getBasicComponent(operationSig);
			for (final ProvidedRole nextProvidedRole : bCmp.getProvidedRoles_InterfaceProvidingEntity()) {
				// TODO why is this here?
				// final Object obj = nextProvidedRole.eGet(nextProvidedRole.eClass()
				// .getEStructuralFeature("providedInterface__OperationProvidedRole"), true);
				if (nextProvidedRole instanceof OperationProvidedRole) {
					final OperationProvidedRole operationProvidedRole = (OperationProvidedRole) nextProvidedRole;
					final String idProvidedRole = operationProvidedRole.getProvidedInterface__OperationProvidedRole().getId();
					final String idOpIf = opInf.getId();
					if (idOpIf.equals(idProvidedRole)) {
						eSysCall.setProvidedRole_EntryLevelSystemCall((OperationProvidedRole) nextProvidedRole);
						break;
					}
				}

			}

			if (this.lastAction != null) {
				eSysCall.setPredecessor(this.lastAction);
				this.lastAction.setSuccessor(eSysCall);
			} else {
				// connect to start
				eSysCall.setPredecessor(this.start);
				this.start.setSuccessor(eSysCall);
			}
			this.scenarioBehaviour.getActions_ScenarioBehaviour().add(eSysCall);
			this.lastAction = eSysCall;
		} else {
			// TODO warning?
		}
	}

	public void createOpenWorkload(final long avgInterarrivalTime) {
		final OpenWorkload openWorkload = this.usageModelfactory.createOpenWorkload();
		final PCMRandomVariable pcmInterarrivalTime = CoreFactory.eINSTANCE.createPCMRandomVariable();
		pcmInterarrivalTime.setSpecification(String.valueOf(avgInterarrivalTime));
		pcmInterarrivalTime.setOpenWorkload_PCMRandomVariable(openWorkload);
		openWorkload.setInterArrivalTime_OpenWorkload(pcmInterarrivalTime);
		this.scenarioBehaviour.getUsageScenario_SenarioBehaviour()
				.setWorkload_UsageScenario(openWorkload);
	}

	public void createStart() {
		this.start = this.usageModelfactory.createStart();
		this.scenarioBehaviour.getActions_ScenarioBehaviour().add(this.start);
	}

	public void createStop() {
		this.stop = this.usageModelfactory.createStop();
		if (this.lastAction != null) {
			this.lastAction.setSuccessor(this.stop);
			this.stop.setPredecessor(this.lastAction);
			this.scenarioBehaviour.getActions_ScenarioBehaviour().add(this.stop);
		} else if (this.start != null) {
			this.start.setSuccessor(this.stop);
			this.stop.setPredecessor(this.start);
			this.scenarioBehaviour.getActions_ScenarioBehaviour().add(this.stop);
		} else {
			// TODO warning?
		}
	}

}
