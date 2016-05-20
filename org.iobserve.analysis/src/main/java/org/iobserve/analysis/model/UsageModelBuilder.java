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
import org.palladiosimulator.pcm.usagemodel.ClosedWorkload;
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
	 * Load the model from file using the model provider.
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
	 * Create an {@link UsageScenario} and create a body {@link ScenarioBehaviour} for it.
	 * Get the {@link ScenarioBehaviour} by {@link UsageScenario#getScenarioBehaviour_UsageScenario()}
	 * @return created usage scenario
	 */
	public UsageScenario createUsageScenario(final String name) {
		// create the usage scenario
		final UsageModel model = this.modelProvider.getModel();
		final UsageScenario usageScenario =  UsagemodelFactory.eINSTANCE.createUsageScenario();
		usageScenario.setEntityName(name);
		usageScenario.setUsageModel_UsageScenario(model);
		model.getUsageScenario_UsageModel().add(usageScenario);
		
		// create a scenario behavior
		final ScenarioBehaviour scenarioBehaviour = this.createScenarioBehaviour();
		usageScenario.setScenarioBehaviour_UsageScenario(scenarioBehaviour);
		return usageScenario;
	}
	
	/**
	 * Create a {@link ScenarioBehaviour}. The behavior is just created not added any model part.
	 * @return the behavior
	 */
	public ScenarioBehaviour createScenarioBehaviour() {
		final ScenarioBehaviour scenarioBehaviour = UsagemodelFactory.eINSTANCE.createScenarioBehaviour();
		return scenarioBehaviour;
	}
	
	/**
	 * Create an {@link OpenWorkload} and add it to the given {@link UsageScenario}.
	 * @param avgInterarrivalTime the interarrival time
	 * @param parent usage scenario the workload should be added to
	 * @return brand new instance of {@link OpenWorkload}
	 */
	public OpenWorkload createOpenWorkload(final long avgInterarrivalTime,
			final UsageScenario parent) {
		final OpenWorkload openWorkload = UsagemodelFactory.eINSTANCE.createOpenWorkload();
		parent.setWorkload_UsageScenario(openWorkload);
		
		// create varaibles
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
	 * @param parent usage scenario the workload should be added to
	 * @return brand new instance of {@link OpenWorkload}
	 */
	public ClosedWorkload createClosedWorkload(final int population, final double thinkTime,
			final UsageScenario parent) {
		final ClosedWorkload closedWorkload = UsagemodelFactory.eINSTANCE.createClosedWorkload();
		parent.setWorkload_UsageScenario(closedWorkload);
		
		// create variables
		final PCMRandomVariable pcmThinkTime = 
				CoreFactory.eINSTANCE.createPCMRandomVariable();
		pcmThinkTime.setSpecification(String.valueOf(thinkTime));
		pcmThinkTime.setClosedWorkload_PCMRandomVariable(closedWorkload);
		closedWorkload.setPopulation(population);
		closedWorkload.setThinkTime_ClosedWorkload(pcmThinkTime);
		return closedWorkload;
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
	 * Create branch with the given name and add it to the given scenario behavior.
	 * @param name name of the branch
	 * @param parent parent to add branch to
	 * @return created branch instance
	 */
	public Branch createBranch(final String name, final ScenarioBehaviour parent) {
		final Branch branch = UsagemodelFactory.eINSTANCE.createBranch();
		branch.setEntityName(name);
		branch.setScenarioBehaviour_AbstractUserAction(parent);
		parent.getActions_ScenarioBehaviour().add(branch);
		return branch;
	}
	
	/**
	 * Create branch with the given name and add it to the given loop.
	 * @param name name of the branch
	 * @param parent parent to add branch to
	 * @return created branch instance
	 */
	public Branch createBranch(final String name, final Loop parent) {
		return this.createBranch(name, parent.getBodyBehaviour_Loop());
	}
	
	/**
	 * Create branch with the given name and add it to the given branch transition.
	 * @param name name of the branch
	 * @param parent parent to add branch to
	 * @return created branch instance
	 */
	public Branch createBranch(final String name, final BranchTransition parent) {
		return this.createBranch(name, parent.getBranchedBehaviour_BranchTransition());
	}
	
	/**
	 * Create empty {@link BranchTransition} with the given parent. A
	 * {@link ScenarioBehaviour} is added to the body of the branch transition,
	 * in order to make it possible adding further model elements.
	 * 
	 * @param parent
	 *            branch to add transition to
	 * @return created branch transition
	 */
	public BranchTransition createBranchTransition(final Branch parent) {
		// create branch transition
		final BranchTransition branchTransition = UsagemodelFactory.eINSTANCE
				.createBranchTransition();
		branchTransition.setBranch_BranchTransition(parent);
		parent.getBranchTransitions_Branch().add(branchTransition);
		
		// create body of branch transition
		final ScenarioBehaviour bodyScenarioBehaviour = this.createScenarioBehaviour();
		branchTransition.setBranchedBehaviour_BranchTransition(bodyScenarioBehaviour);
		return branchTransition;
	}
	
	/**
	 * Create loop in given parent {@link ScenarioBehaviour}. A
	 * {@link ScenarioBehaviour} is added to the body of the loop,
	 * in order to make it possible adding further model elements.
	 * 
	 * @param name
	 *            name of loop
	 * @param parent
	 *            parent
	 * @return created loop
	 */
	public Loop createLoop(final String name, final ScenarioBehaviour parent) {
		// create the loop
		final Loop loop = UsagemodelFactory.eINSTANCE.createLoop();
		loop.setEntityName(name);
		parent.getActions_ScenarioBehaviour().add(loop);
		
		// create the body scenario behavior
		final ScenarioBehaviour bodyScenarioBehaviour = this.createScenarioBehaviour();
		loop.setBodyBehaviour_Loop(bodyScenarioBehaviour);
		return loop;
	}
	
	/**
	 * Create a loop within the given loop.
	 * @param name name of the loop
	 * @param parent parent loop
	 * @return created loop
	 */
	public Loop createLoop(final String name, final Loop parent) {
		return this.createLoop(name, parent.getBodyBehaviour_Loop());
	}
	
	/**
	 * Create a loop within the given branch transition.
	 * @param name name of loop
	 * @param parent parent branch transition
	 * @return created loop
	 */
	public Loop createLoop(final String name, final BranchTransition parent) {
		return this.createLoop(name, parent.getBranchedBehaviour_BranchTransition());
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
