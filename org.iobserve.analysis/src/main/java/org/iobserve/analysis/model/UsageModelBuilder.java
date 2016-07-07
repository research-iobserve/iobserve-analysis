/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.model;

import org.iobserve.analysis.correspondence.Correspondent;
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
	 * @param name of start node
	 * @return start node
	 */
	public Start createStart(final String name) {
		final Start start = UsagemodelFactory.eINSTANCE.createStart();
		start.setEntityName(name);
		return start;
	}
	
	/**
	 * Create a start node without name. (Recommended to use {@link #createStart(String)}).
	 * @return start node
	 */
	public Start createStart() {
		return this.createStart("start-with-no-name");
	}
	
	/**
	 * Create a stop node.
	 * @param name name of stop node
	 * @return stop node.
	 */
	public Stop createStop(final String name) {
		final Stop stop = UsagemodelFactory.eINSTANCE.createStop();
		stop.setEntityName(name);
		return stop;
	}
	
	/**
	 * Create a stop node without name. (Recommended to use {@link #createStop(String)}).
	 * @return stop node.
	 */
	public Stop createStop() {
		return this.createStop("stop-with-no-name");
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
		final BasicComponent bCmp = repositoryModelProvider.getBasicComponent(operationSignature);
		final EntryLevelSystemCall eSysCall;
		if (opSig != null && bCmp != null) {
			eSysCall = UsagemodelFactory.eINSTANCE.createEntryLevelSystemCall();
			eSysCall.setEntityName(opSig.getEntityName());
			eSysCall.setOperationSignature__EntryLevelSystemCall(opSig);
			
			final OperationInterface opInf = opSig.getInterface__OperationSignature();
			final OperationProvidedRole providedRole = repositoryModelProvider.getOperationProvidedRole(opInf, bCmp);
			eSysCall.setProvidedRole_EntryLevelSystemCall(providedRole);
		} else {
			eSysCall = null;
			System.err.printf("%s caused Nullpointer since OperationSignature=% or BasicComponent=%s is null?!",
					operationSignature,String.valueOf(opSig), String.valueOf(bCmp));
		}
		return eSysCall;
	}
	
	/**
	 * Create an EntryLevelSystemCall with the given correspondent.
	 * @param correspondent correspondent containing operation signature
	 * @return null, if the creation failed, the instance if not.
	 */
	public EntryLevelSystemCall createEntryLevelSystemCall(final Correspondent correspondent) {
		return this.createEntryLevelSystemCall(correspondent.getPcmOperationName());
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
	 * Add the given actions to the given {@link ScenarioBehaviour}.
	 * @param parent parent
	 * @param actions actions to add
	 */
	public void addUserAction(final ScenarioBehaviour parent, final AbstractUserAction...actions) {
		for (final AbstractUserAction nextUserAction : actions) {
			parent.getActions_ScenarioBehaviour().add(nextUserAction);
		}
	}
	
	/**
	 * Add the given actions to the given {@link UsageScenario}.
	 * @param parent parent
	 * @param actions actions to add
	 */
	public void addUserAction(final UsageScenario parent, final AbstractUserAction...actions) {
		this.addUserAction(parent.getScenarioBehaviour_UsageScenario(), actions);
	}
	
	/**
	 * Add the given actions to the given {@link BranchTransition}.
	 * @param parent parent
	 * @param actions actions to add
	 */
	public void addUserAction(final BranchTransition parent, final AbstractUserAction...actions) {
		this.addUserAction(parent.getBranchedBehaviour_BranchTransition(), actions);
	}
	
	/**
	 * Add the given actions to the given {@link Loop}.
	 * @param parent parent
	 * @param actions actions to add
	 */
	public void addUserAction(final Loop parent, final AbstractUserAction...actions) {
		this.addUserAction(parent.getBodyBehaviour_Loop(), actions);
	}
	
}
