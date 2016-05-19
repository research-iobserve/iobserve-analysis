package org.iobserve.analysis.model;

import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.OpenWorkload;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.UsagemodelFactory;
import org.palladiosimulator.pcm.usagemodel.Workload;

public class UsageModelBuilder extends ModelBuilder<UsageModelProvider, UsageModel> {
	
	private ScenarioBehaviour scenarioBehaviour;
	private UsageScenario usageScenario;
	private Workload workload;
	private AbstractUserAction action;

	public UsageModelBuilder(final UsageModelProvider modelProvider) {
		super(modelProvider);
	}
	
	private void connectAbstractUserAction(final AbstractUserAction newAction) {
		if (this.action != null) {
			this.action.setSuccessor(newAction);
			newAction.setPredecessor(this.action);
		}
	}
	
	// *****************************************************************
	//
	// *****************************************************************
	
	public UsageModelBuilder loadModel() {
		this.modelProvider.loadModel();
		return this;
	}
	
	public UsageModelBuilder resetModel() {
		final UsageModel model = this.modelProvider.getModel();
		model.getUsageScenario_UsageModel().clear();
		model.getUserData_UsageModel().clear();
		return this;
	}
	
	public UsageModelBuilder setUsageScenario(final int index) {
		final UsageModel model = this.modelProvider.getModel();
		this.usageScenario = model.getUsageScenario_UsageModel().get(index);
		this.workload = this.usageScenario.getWorkload_UsageScenario();
		this.scenarioBehaviour = this.usageScenario.getScenarioBehaviour_UsageScenario();
		return this;
	}
	
	public UsageModelBuilder createUsageScenario() {
		final UsageModel model = this.modelProvider.getModel();
		this.usageScenario =  UsagemodelFactory.eINSTANCE.createUsageScenario();
		model.getUsageScenario_UsageModel().add(this.usageScenario);
		this.scenarioBehaviour = UsagemodelFactory.eINSTANCE.createScenarioBehaviour();
		this.usageScenario.setScenarioBehaviour_UsageScenario(this.scenarioBehaviour);
		return this;
	}
	
	public UsageModelBuilder createOpenWorkload(final long avgInterarrivalTime) {
		final OpenWorkload openWorkload = UsagemodelFactory.eINSTANCE.createOpenWorkload();
		final PCMRandomVariable pcmInterarrivalTime = 
				CoreFactory.eINSTANCE.createPCMRandomVariable();
		pcmInterarrivalTime.setSpecification(String.valueOf(avgInterarrivalTime));
		pcmInterarrivalTime.setOpenWorkload_PCMRandomVariable(openWorkload);
		openWorkload.setInterArrivalTime_OpenWorkload(pcmInterarrivalTime);
		this.scenarioBehaviour.getUsageScenario_SenarioBehaviour()
			.setWorkload_UsageScenario(openWorkload);
		return this;
	}
	
	public UsageModelBuilder createStart() {
		final Start start = UsagemodelFactory.eINSTANCE.createStart();
		this.scenarioBehaviour.getActions_ScenarioBehaviour().add(start);
		this.connectAbstractUserAction(start);
		this.action = start;
		return this;
	}
	
	public UsageModelBuilder createStop() {
		final Stop stop = UsagemodelFactory.eINSTANCE.createStop();
		this.scenarioBehaviour.getActions_ScenarioBehaviour().add(stop);
		this.connectAbstractUserAction(stop);
		this.action = stop;
		return this;
	}
	
	public UsageModelBuilder createAction(final String operationSignature) {
		final RepositoryModelProvider repositoryModelProvider = 
				this.modelProvider.getPlatform().getRepositoryModelProvider();
		
		final OperationSignature opSig = repositoryModelProvider.getOperationSignature(operationSignature);
		if (opSig != null) {
			final EntryLevelSystemCall eSysCall = UsagemodelFactory.eINSTANCE
					.createEntryLevelSystemCall();
			eSysCall.setEntityName(opSig.getEntityName());
			eSysCall.setOperationSignature__EntryLevelSystemCall(opSig);
			final OperationInterface opInf = opSig.getInterface__OperationSignature();
			final BasicComponent bCmp = repositoryModelProvider.getBasicComponent(operationSignature);
			final OperationProvidedRole providedRole = repositoryModelProvider.getOperationProvidedRole(opInf, bCmp);
			
			//TODO null checks for all this modelProvider calls?
			eSysCall.setProvidedRole_EntryLevelSystemCall(providedRole);
			this.scenarioBehaviour.getActions_ScenarioBehaviour().add(eSysCall);
			this.connectAbstractUserAction(eSysCall);
			this.action = eSysCall;
		} else {
			System.err.printf("%s operation signature was null?", operationSignature);
		}
		return this;
	}
	

}
