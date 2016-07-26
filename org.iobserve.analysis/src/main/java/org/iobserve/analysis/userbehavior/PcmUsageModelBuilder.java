package org.iobserve.analysis.userbehavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

import com.google.common.base.Optional;

import org.iobserve.analysis.AnalysisMain;
import org.iobserve.analysis.correspondence.Correspondent;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.model.UsageModelBuilder;
import org.iobserve.analysis.userbehavior.data.Branch;
import org.iobserve.analysis.userbehavior.data.BranchElement;
import org.iobserve.analysis.userbehavior.data.BranchTransitionElement;
import org.iobserve.analysis.userbehavior.data.SequenceElement;
import org.iobserve.analysis.userbehavior.data.CallBranchModel;
import org.iobserve.analysis.userbehavior.data.CallElement;
import org.iobserve.analysis.userbehavior.data.LoopBranchElement;
import org.iobserve.analysis.userbehavior.data.LoopElement;

/**
 * This class creates the PCM usage model from the callBranchModels
 * 
 * 
 * @author David Peter, Robert Heinrich
 */


public class PcmUsageModelBuilder {
	
	List<CallBranchModel> callLoopBranchModels;
	boolean isClosedWorkloadRequested;
	double thinkTime;
	private List<HashMap<Integer,ScenarioBehaviour>> branchScenarioBehavioursOfUserGroups;
	private final UsageModelBuilder usageModelBuilder;
	private final ICorrespondence correspondenceModel;
	
	public PcmUsageModelBuilder(List<CallBranchModel> callLoopBranchModels, boolean isClosedWorkloadRequested, double thinkTime) {
		this.callLoopBranchModels = callLoopBranchModels;
		this.isClosedWorkloadRequested = isClosedWorkloadRequested;
		this.thinkTime = thinkTime;
		branchScenarioBehavioursOfUserGroups = new ArrayList<HashMap<Integer,ScenarioBehaviour>>();
		usageModelBuilder = new UsageModelBuilder(AnalysisMain.getInstance().getModelProviderPlatform().getUsageModelProvider());
		this.correspondenceModel = AnalysisMain.getInstance().getModelProviderPlatform().getCorrespondenceModel();
	}
	
	
	public UsageModel createUsageModel() {
		
		UsageModel usageModel = this.usageModelBuilder.createUsageModel();
		
		// Creates for each detected user group its own usage scenario
		for(int i=0;i<this.callLoopBranchModels.size();i++) {
			CallBranchModel callBranchModel = this.callLoopBranchModels.get(i);
			UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("Usage Scneario of user group "+i, usageModel);
			HashMap<Integer,ScenarioBehaviour> branchScenarioBehaviours = new HashMap<Integer,ScenarioBehaviour>();
			this.branchScenarioBehavioursOfUserGroups.add(branchScenarioBehaviours);
			
			if(this.isClosedWorkloadRequested) {
				this.usageModelBuilder.createClosedWorkload(callBranchModel.getWorkloadIntensity().getAvgNumberOfConcurrentUsers(), this.thinkTime, usageScenario);
			} else {
				this.usageModelBuilder.createOpenWorkload(callBranchModel.getWorkloadIntensity().getInterarrivalTimeOfUserSessions(), usageScenario);
			}
			// creates for each Branch its own scenario behavior
			this.createForEachBranchAScenarioBehavior(callBranchModel.getRootBranch(), i);
			
			// The rootBranch contains every succeeding branches and is set as the scenario behavior of the user group´s usage scenario
			usageScenario.setScenarioBehaviour_UsageScenario(branchScenarioBehavioursOfUserGroups.get(i).get(callBranchModel.getRootBranch().getBranchId()));
			usageModel.getUsageScenario_UsageModel().add(usageScenario);
		}
		
		return usageModel;
	}
	
	private void createForEachBranchAScenarioBehavior(Branch branch, int indexOfUserGroup) {		
		for(int i=0;i<branch.getChildBranches().size();i++) {
			createForEachBranchAScenarioBehavior(branch.getChildBranches().get(i), indexOfUserGroup);
		}
		this.modelBranchScenarioBehavior(branch, indexOfUserGroup);
	}
	
	private void modelBranchScenarioBehavior(final Branch branch, int indexOfUserGroup) {		
		final ScenarioBehaviour scenarioBehaviour = this.transformSequenceToScenarioBehavior(indexOfUserGroup, branch.getBranchSequence(), branch);
		branchScenarioBehavioursOfUserGroups.get(indexOfUserGroup).put(branch.getBranchId(), scenarioBehaviour);
	}
	
	/**
	 * David Peter
	 * Creates an scenario behavior corresponding to the given sequence
	 * The sequence can be a branch sequence or a loop sequence
	 * It creates a sequence of entry level system calls including loops within the sequence
	 * It sets the successors and predecessors
	 */
	private ScenarioBehaviour transformSequenceToScenarioBehavior(int indexOfScenario, final List<SequenceElement> sequence, final Branch branch) {
		
		final ScenarioBehaviour scenarioBehaviour = this.usageModelBuilder.createScenarioBehaviour();
		
		Start start = this.usageModelBuilder.createStart("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
		Stop stop = this.usageModelBuilder.createStop("");
		this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
		
		EntryLevelSystemCall lastESysCall = this.usageModelBuilder.createEmptyEntryLevelSystemCall();
		boolean isLastElementACall = false;
		Loop lastLoop =  this.usageModelBuilder.createEmptyLoop();
		boolean isLastElementALoop = false;
		org.palladiosimulator.pcm.usagemodel.Branch lastLoopBranch =  this.usageModelBuilder.createEmptyBranch();
		boolean isLastElementALoopBranch = false;
		org.palladiosimulator.pcm.usagemodel.Branch lastBranch =  this.usageModelBuilder.createEmptyBranch();
		boolean isLastElementABranch = false;
		
		for(SequenceElement branchElement : sequence) {
			
			// Element is a entryLevelSystemCall
			if(branchElement.getClass().equals(CallElement.class)) {
				EntryLevelSystemCall eSysCall = null;
				final Optional<Correspondent> optionCorrespondent = this.correspondenceModel.getCorrespondent(branchElement.getClassSignature(), branchElement.getOperationSignature());
				if (optionCorrespondent.isPresent()) {
					final Correspondent correspondent = optionCorrespondent.get();
					eSysCall = this.usageModelBuilder.createEntryLevelSystemCall(correspondent);
				}
				if(eSysCall!=null) { 
					if(isLastElementACall) {
						this.usageModelBuilder.connect(lastESysCall, eSysCall);
					} else if(isLastElementALoop) {
						this.usageModelBuilder.connect(lastLoop, eSysCall);
					} else if(isLastElementALoopBranch) {
						this.usageModelBuilder.connect(lastLoopBranch, eSysCall);
					} else if(isLastElementABranch) {
						this.usageModelBuilder.connect(lastBranch, eSysCall);
					} else {
						this.usageModelBuilder.connect(start, eSysCall);
					}
					this.usageModelBuilder.addUserAction(scenarioBehaviour, eSysCall);
					lastESysCall = eSysCall;
					isLastElementACall = true;
					isLastElementALoop = false;
					isLastElementALoopBranch = false;
					isLastElementABranch = false;
				}
			}
			// Element is a loop 
			else if(branchElement.getClass().equals(LoopElement.class)) {
				final Loop loop = this.createLoop(scenarioBehaviour, (LoopElement)branchElement); 
				if(isLastElementACall) {
					this.usageModelBuilder.connect(lastESysCall, loop);
				} else if(isLastElementALoop) {
					this.usageModelBuilder.connect(lastLoop, loop);
				} else if(isLastElementALoopBranch) {
					this.usageModelBuilder.connect(lastLoopBranch, loop);
				} else if(isLastElementABranch) {
					this.usageModelBuilder.connect(lastBranch, loop);
				} else {
					this.usageModelBuilder.connect(start, loop);
				}
				lastLoop = loop;
				isLastElementALoop = true;
				isLastElementACall = false;
				isLastElementALoopBranch = false;
				isLastElementABranch = false;
			}
			// Element is a looped Branch
			else if(branchElement.getClass().equals(LoopBranchElement.class)) {
				final org.palladiosimulator.pcm.usagemodel.Branch loopBranch = this.createLoopBranch(scenarioBehaviour, (LoopBranchElement)branchElement);
				if(isLastElementACall) {
					this.usageModelBuilder.connect(lastESysCall, loopBranch);
				} else if(isLastElementALoop) {
					this.usageModelBuilder.connect(lastLoop, loopBranch);
				} else if(isLastElementALoopBranch) {
					this.usageModelBuilder.connect(lastLoopBranch, loopBranch);
				} else if(isLastElementABranch) {
					this.usageModelBuilder.connect(lastBranch, loopBranch);
				} else {
					this.usageModelBuilder.connect(start, loopBranch);
				}
				lastLoopBranch = loopBranch;
				isLastElementALoopBranch = true;
				isLastElementACall = false;
				isLastElementALoop = false;
				isLastElementABranch = false;
			}
			// Element is a Branch
			else if(branchElement.getClass().equals(BranchElement.class)) {
				final org.palladiosimulator.pcm.usagemodel.Branch branchInter = this.createBranch(scenarioBehaviour, (BranchElement)branchElement);
				if(isLastElementACall) {
					this.usageModelBuilder.connect(lastESysCall, branchInter);
				} else if(isLastElementALoop) {
					this.usageModelBuilder.connect(lastLoop, branchInter);
				} else if(isLastElementALoopBranch) {
					this.usageModelBuilder.connect(lastLoopBranch, branchInter);
				} else if(isLastElementABranch) {
					this.usageModelBuilder.connect(lastBranch, branchInter);
				} else {
					this.usageModelBuilder.connect(start, branchInter);
				}
				lastBranch = branchInter;
				isLastElementABranch = true;
				isLastElementALoopBranch = false;
				isLastElementACall = false;
				isLastElementALoop = false;
			} else {
				break;
			}	
		}
		
		if(branch==null) {
			if(isLastElementACall) {
				this.usageModelBuilder.connect(lastESysCall, stop);
			} else if(isLastElementALoop) {
				this.usageModelBuilder.connect(lastLoop, stop);
			} else if(isLastElementALoopBranch) {
				this.usageModelBuilder.connect(lastLoopBranch, stop);
			} else if(isLastElementABranch) {
				this.usageModelBuilder.connect(lastBranch, stop);
			} else {
				this.usageModelBuilder.connect(start, stop);
			}
		} else {
			org.palladiosimulator.pcm.usagemodel.Branch branchUM = createChildBranch(scenarioBehaviour, indexOfScenario, branch);
			if(branchUM!=null) {
				if(isLastElementACall) {
					this.usageModelBuilder.connect(lastESysCall, branchUM);
					this.usageModelBuilder.connect(branchUM, stop);
				} else if (isLastElementALoop) {
					this.usageModelBuilder.connect(lastLoop, branchUM);
					this.usageModelBuilder.connect(branchUM, stop);
				} else if(isLastElementALoopBranch) {
					this.usageModelBuilder.connect(lastLoopBranch, branchUM);
					this.usageModelBuilder.connect(branchUM, stop);
				} else if(isLastElementABranch) {
					this.usageModelBuilder.connect(lastBranch, branchUM);
					this.usageModelBuilder.connect(branchUM, stop);
				} else {
					this.usageModelBuilder.connect(start, branchUM);
					this.usageModelBuilder.connect(branchUM, stop);
				}
			} else {
				if(isLastElementACall) {
					this.usageModelBuilder.connect(lastESysCall, stop);
				} else if (isLastElementALoop) {
					this.usageModelBuilder.connect(lastLoop, stop);
				} else if(isLastElementALoopBranch) {
					this.usageModelBuilder.connect(lastLoopBranch, stop);
				} else if(isLastElementABranch) {
					this.usageModelBuilder.connect(lastBranch, stop);
				} else {
					this.usageModelBuilder.connect(start, stop);
				}
			}		
		}
		
		return scenarioBehaviour;
	}
	
	private org.palladiosimulator.pcm.usagemodel.Branch createBranch(ScenarioBehaviour scenarioBehaviour, BranchElement branchElement) {
		org.palladiosimulator.pcm.usagemodel.Branch branchUM = this.usageModelBuilder.createBranch("", scenarioBehaviour);
		for(BranchTransitionElement transition : branchElement.getBranchTransitions()) {
			BranchTransition branchTransition = this.usageModelBuilder.createBranchTransition(branchUM);
			final ScenarioBehaviour branchScenarioBehaviour = transformSequenceToScenarioBehavior(0, transition.getBranchSequence(), null);
			branchTransition.setBranchedBehaviour_BranchTransition(branchScenarioBehaviour);
			branchTransition.setBranch_BranchTransition(branchUM);
			branchTransition.setBranchProbability(transition.getTransitionLikelihood());
		}
		
		return branchUM;
	}


	private Loop createLoop(final ScenarioBehaviour scenarioBehaviour, LoopElement loopSequence) {
		
		Loop loop = this.usageModelBuilder.createLoop("", scenarioBehaviour);
		final ScenarioBehaviour loopScenarioBehaviour = transformSequenceToScenarioBehavior(0, loopSequence.getLoopSequence(), null);
		loop.setBodyBehaviour_Loop(loopScenarioBehaviour); // Set behavior of the loop
		final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
		pcmLoopIteration.setSpecification(String.valueOf(loopSequence.getLoopCount()));
		loop.setLoopIteration_Loop(pcmLoopIteration); // Set number of loops
		
		return loop;
	}
	
	private org.palladiosimulator.pcm.usagemodel.Branch createChildBranch(final ScenarioBehaviour scenarioBehaviour, final int indexOfScenario, final Branch branch) {
				
		if(branch.getChildBranches().size()>0) {
			org.palladiosimulator.pcm.usagemodel.Branch branchUM = this.usageModelBuilder.createBranch("", scenarioBehaviour);
			for(Branch childBranch : branch.getChildBranches()) {
				BranchTransition branchTransition = this.usageModelBuilder.createBranchTransition(branchUM);
				branchTransition.setBranchedBehaviour_BranchTransition(branchScenarioBehavioursOfUserGroups.get(indexOfScenario).get(childBranch.getBranchId()));
				branchTransition.setBranch_BranchTransition(branchUM);
				branchTransition.setBranchProbability(childBranch.getBranchLikelihood());
			}
			return branchUM;
		}
		
		return null;
	}
	
	private org.palladiosimulator.pcm.usagemodel.Branch createLoopBranch(final ScenarioBehaviour scenarioBehaviour, final LoopBranchElement loopBranch) {
				
		org.palladiosimulator.pcm.usagemodel.Branch branchUM = this.usageModelBuilder.createBranch("", scenarioBehaviour);
		for(Branch branch : loopBranch.getLoopBranches()) {
			BranchTransition branchTransition = this.usageModelBuilder.createBranchTransition(branchUM);
			final ScenarioBehaviour branchScenarioBehaviour = transformSequenceToScenarioBehavior(0, branch.getBranchSequence(), null);
			branchTransition.setBranchedBehaviour_BranchTransition(branchScenarioBehaviour);
			branchTransition.setBranch_BranchTransition(branchUM);
			branchTransition.setBranchProbability(branch.getBranchLikelihood());
		}
		
		return branchUM;
	}

}
