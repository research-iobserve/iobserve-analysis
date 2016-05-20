package org.iobserve.analysis.model;

import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
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
	
	/**
	 * Create a start node.
	 * @return start node
	 */
	public Start createStart() {
		final Start start = UsagemodelFactory.eINSTANCE.createStart();
		return start;
	}
	
	/**
	 * Create a stop node.
	 * @return stop node.
	 */
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
	// BRANCHING
	// *****************************************************************
	
	/**
	 * Create a branch. The branch is not added to any other model part.
	 * @return created branch instance
	 */
	public Branch createBranch() {
		final Branch branch = UsagemodelFactory.eINSTANCE.createBranch();
		return branch;
	}
	
	/**
	 * Create a branch with the given name. The branch is not added to any other model part.
	 * @param name name for the branch
	 * @return created branch instance
	 */
	public Branch createBranch(final String name) {
		final Branch branch = UsagemodelFactory.eINSTANCE.createBranch();
		branch.setEntityName(name);
		return branch;
	}
	
	/**
	 * Create a branch and add it to the given scenario behavior.
	 * @param name name of the branch
	 * @param scenariobehavior scenario behavior instance
	 * @return created branch instance
	 */
	public Branch createBranch(final String name, final ScenarioBehaviour scenarioBehaviour) {
		final Branch branch = UsagemodelFactory.eINSTANCE.createBranch();
		branch.setEntityName(name);
		scenarioBehaviour.getActions_ScenarioBehaviour().add(branch);
		return branch;
	}
	
	/**
	 * Create a branch and add it to the given loop.
	 * @param name name of the branch
	 * @param loop loop to add branch to
	 * @return created branch instance
	 */
	public Branch createBranch(final String name, final Loop loop) {
		final Branch branch = UsagemodelFactory.eINSTANCE.createBranch();
		branch.setEntityName(name);
		loop.getBodyBehaviour_Loop().getActions_ScenarioBehaviour().add(branch);
		return branch;
	}
	
	/**
	 * Create branch and add it to the branch transition.
	 * @param name name of the branch
	 * @param branchTransition branch transition
	 * @return created branch instance
	 */
	public Branch createBranch(final String name, final BranchTransition branchTransition) {
		final Branch branch = UsagemodelFactory.eINSTANCE.createBranch();
		branch.setEntityName(name);
		branchTransition.getBranchedBehaviour_BranchTransition()
			.getActions_ScenarioBehaviour().add(branch);
		return branch;
	}
	
	/**
	 * Create bare branch transition
	 * @return created branch transition
	 */
	public BranchTransition createBranchTransition() {
		final BranchTransition bt = UsagemodelFactory.eINSTANCE.createBranchTransition();
		return bt;
	}
	
	/**
	 * Create bare branch transition and set the probability
	 * @param probability probability
	 * @return created branch transition
	 */
	public BranchTransition createBranchTransition(final double probability) {
		final BranchTransition bt = UsagemodelFactory.eINSTANCE.createBranchTransition();
		bt.setBranchProbability(probability);
		return bt;
	}
	
	/**
	 * Create bare branch transition and set the probability
	 * @param branch branch to add transition to
	 * @return created branch transition
	 */
	public BranchTransition createBranchTransition(final Branch branch) {
		final BranchTransition bt = UsagemodelFactory.eINSTANCE.createBranchTransition();
		branch.getBranchTransitions_Branch().add(bt);
		return bt;
	}
	
	// *****************************************************************
	// LOOPING
	// *****************************************************************
	
	/**
	 * Create a loop.
	 * @return created loop instance
	 */
	public Loop createLoop() {
		final Loop loop = UsagemodelFactory.eINSTANCE.createLoop();
		return loop;
	}
	
	
	/**
	 * Create a loop.
	 * @param name name of the loop
	 * @return created loop instance
	 */
	public Loop createLoop(final String name) {
		final Loop loop = UsagemodelFactory.eINSTANCE.createLoop();
		loop.setEntityName(name);
		return loop;
	}
	
	/**
	 * Create a loop and add it to the parent loop.
	 * @param name name of the loop
	 * @param parentLoop parent loop
	 * @return created loop instance
	 */
	public Loop createLoop(final String name, final Loop parentLoop) {
		final Loop loop = UsagemodelFactory.eINSTANCE.createLoop();
		loop.setEntityName(name);
		parentLoop.getBodyBehaviour_Loop().getActions_ScenarioBehaviour().add(loop);
		return loop;
	}
	
	/**
	 * Create a loop and add it to the branch transition.
	 * @param name name of the loop
	 * @param branchTransition branchTransition
	 * @return created loop instance
	 */
	public Loop createLoop(final String name, final BranchTransition branchTransition) {
		final Loop loop = UsagemodelFactory.eINSTANCE.createLoop();
		loop.setEntityName(name);
		branchTransition.getBranchedBehaviour_BranchTransition()
			.getActions_ScenarioBehaviour().add(loop);
		return loop;
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
	
	/**
	 * Add the given action to the given scenario behaviour.
	 * @param scenarioBehaviour scenarioBehaviour
	 * @param action action
	 */
	public void addAbstractUserAction(final ScenarioBehaviour scenarioBehaviour,
			final AbstractUserAction action) {
		scenarioBehaviour.getActions_ScenarioBehaviour().add(action);
	}
	
}
