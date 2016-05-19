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
import org.palladiosimulator.pcm.usagemodel.UserData;

/**
 * UsageModelBuilder is able to build a {@link UsageModel}.
 * @author Robert Heinrich
 * @author Alessandro
 *
 */
public class UsageModelBuilder extends ModelBuilder<UsageModelProvider, UsageModel> {

	/**
	 * Create a usage model builder.
	 * @param modelProvider model provider
	 */
	public UsageModelBuilder(final UsageModelProvider modelProvider) {
		super(modelProvider);
	}
	
	// *****************************************************************
	//
	// *****************************************************************
	
	/**
	 * Load the model from file.
	 * @return builder to pipeline more commands
	 */
	public UsageModelBuilder loadModel() {
		this.modelProvider.loadModel();
		return this;
	}
	
	/**
	 * Reset the model. This will delete all {@link UsageScenario} and {@link UserData} instances.
	 * @return builder to pipeline more commands
	 */
	public UsageModelBuilder resetModel() {
		final UsageModel model = this.modelProvider.getModel();
		model.getUsageScenario_UsageModel().clear();
		model.getUserData_UsageModel().clear();
		return this;
	}
	
	/**
	 * Create an {@link UsageScenario}. The scenario is added to the model.
	 * @return created usage scenario
	 */
	public UsageScenario createUsageScenario() {
		final UsageModel model = this.modelProvider.getModel();
		final UsageScenario usageScenario =  UsagemodelFactory.eINSTANCE.createUsageScenario();
		model.getUsageScenario_UsageModel().add(usageScenario);
		return usageScenario;
	}
	
	/**
	 * Create a {@link ScenarioBehaviour}. The behavior is just created not added to the model.
	 * @return the behavior
	 */
	public ScenarioBehaviour createScenarioBehaviour() {
		final ScenarioBehaviour scenarioBehaviour = UsagemodelFactory.eINSTANCE.createScenarioBehaviour();
		return scenarioBehaviour;
	}
	
	/**
	 * Create a {@link ScenarioBehaviour} and add it to the given {@link UsageScenario}.
	 * @param usageScenario usage scenario
	 * @return the scenario behavior
	 */
	public ScenarioBehaviour createScenarioBehaviour(final UsageScenario usageScenario) {
		final ScenarioBehaviour scenarioBehaviour = UsagemodelFactory.eINSTANCE.createScenarioBehaviour();
		usageScenario.setScenarioBehaviour_UsageScenario(scenarioBehaviour);
		return scenarioBehaviour;
	}
	
	/**
	 * Create an {@link OpenWorkload}. It will just be created with the given interarrival time.
	 * It is not been added anywhere.
	 * @param avgInterarrivalTime the interarrival time
	 * @return brand new instance of {@link OpenWorkload}
	 */
	public OpenWorkload createOpenWorkload(final long avgInterarrivalTime) {
		final OpenWorkload openWorkload = UsagemodelFactory.eINSTANCE.createOpenWorkload();
		final PCMRandomVariable pcmInterarrivalTime = 
				CoreFactory.eINSTANCE.createPCMRandomVariable();
		pcmInterarrivalTime.setSpecification(String.valueOf(avgInterarrivalTime));
		pcmInterarrivalTime.setOpenWorkload_PCMRandomVariable(openWorkload);
		openWorkload.setInterArrivalTime_OpenWorkload(pcmInterarrivalTime);
		return openWorkload;
	}
	
	/**
	 * Create an {@link OpenWorkload} and add it to the given {@link UsageScenario}.
	 * @param avgInterarrivalTime the interarrival time
	 * @param usageScenario usage scenario the workload should be added to
	 * @return brand new instance of {@link OpenWorkload}
	 */
	public OpenWorkload createOpenWorkload(final long avgInterarrivalTime,
			final UsageScenario usageScenario) {
		final OpenWorkload openWorkload = UsagemodelFactory.eINSTANCE.createOpenWorkload();
		final PCMRandomVariable pcmInterarrivalTime = 
				CoreFactory.eINSTANCE.createPCMRandomVariable();
		pcmInterarrivalTime.setSpecification(String.valueOf(avgInterarrivalTime));
		pcmInterarrivalTime.setOpenWorkload_PCMRandomVariable(openWorkload);
		openWorkload.setInterArrivalTime_OpenWorkload(pcmInterarrivalTime);
		usageScenario.setWorkload_UsageScenario(openWorkload);
		return openWorkload;
	}
	
	
	public Start createStart() {
		final Start start = UsagemodelFactory.eINSTANCE.createStart();
		return start;
	}
	
	public Stop createStop() {
		final Stop stop = UsagemodelFactory.eINSTANCE.createStop();
		return stop;
	}
	
	/**
	 * Create an EntryLevelSystemCall with the given operation signature.
	 * @param operationSignature operation signature of the EntryLevelSystemCall
	 * @return null, if the creation failed, the instance if not.
	 */
	public EntryLevelSystemCall createEntryLevelSystemCall(final String operationSignature) {
		final RepositoryModelProvider repositoryModelProvider = 
				this.modelProvider.getPlatform().getRepositoryModelProvider();
		
		final OperationSignature opSig = repositoryModelProvider.getOperationSignature(operationSignature);
		final EntryLevelSystemCall eSysCall;
		if (opSig != null) {
			eSysCall = UsagemodelFactory.eINSTANCE.createEntryLevelSystemCall();
			eSysCall.setEntityName(opSig.getEntityName());
			eSysCall.setOperationSignature__EntryLevelSystemCall(opSig);
			final OperationInterface opInf = opSig.getInterface__OperationSignature();
			final BasicComponent bCmp = repositoryModelProvider.getBasicComponent(operationSignature);
			final OperationProvidedRole providedRole = repositoryModelProvider.getOperationProvidedRole(opInf, bCmp);
			
			//TODO null checks for all this modelProvider calls?
			eSysCall.setProvidedRole_EntryLevelSystemCall(providedRole);
		} else {
			eSysCall = null;
			System.err.printf("%s operation signature was null?", operationSignature);
		}
		return eSysCall;
	}
	
	// *****************************************************************
	//
	// *****************************************************************
	
	/**
	 * Connect actions.
	 * @param predecessor predecessor of successor
	 * @param successor successor of predecessor
	 */
	public void connect(final AbstractUserAction predecessor, final AbstractUserAction successor) {
		successor.setPredecessor(predecessor);
		predecessor.setSuccessor(successor);
	}
	
}
