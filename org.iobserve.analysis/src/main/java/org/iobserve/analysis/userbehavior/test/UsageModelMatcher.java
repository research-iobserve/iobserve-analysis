package org.iobserve.analysis.userbehavior.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.impl.BranchImpl;
import org.palladiosimulator.pcm.usagemodel.impl.EntryLevelSystemCallImpl;
import org.palladiosimulator.pcm.usagemodel.impl.LoopImpl;
import org.palladiosimulator.pcm.usagemodel.impl.StartImpl;
import org.palladiosimulator.pcm.usagemodel.impl.StopImpl;

/**
 * It matches two usage models related to their model elements as well as the ordering of their elements
 * The objective is to determine whether the two usage models correspond to each other
 * 
 * @author David
 *
 */
public class UsageModelMatcher {
	
	int countOfMatchingElements = 0;
	
	/**
	 * Executes the matching of the two passed usage models
	 * @param usageModel1 is matched against usage model 2
	 * @param usageModel2 is matched against usage model 1
	 * @throws IOException 
	 */
	public AccuracyResults matchUsageModels(final UsageModel usageModel1, final UsageModel usageModel2) throws IOException {
		AccuracyResults accuracyResults = new AccuracyResults();
		UsageScenario usageScenarioOfUsageModel1 = usageModel1.getUsageScenario_UsageModel().get(0);
		UsageScenario usageScenarioOfUsageModel2 = usageModel2.getUsageScenario_UsageModel().get(0);
		ScenarioBehaviour scenarioBehaviourOfUsageModel1 = usageScenarioOfUsageModel1.getScenarioBehaviour_UsageScenario();
		ScenarioBehaviour scenarioBehaviourOfUsageModel2 = usageScenarioOfUsageModel2.getScenarioBehaviour_UsageScenario();
		List<ModelElement> modelElementsOfUsageModel1 = new ArrayList<ModelElement>();
		List<ModelElement> modelElementsOfUsageModel2 = new ArrayList<ModelElement>();
		getModelElements(scenarioBehaviourOfUsageModel1,modelElementsOfUsageModel1);
		getModelElements(scenarioBehaviourOfUsageModel2,modelElementsOfUsageModel2);
		double jc = calculateJaccardCoefficient(modelElementsOfUsageModel1,modelElementsOfUsageModel2);
		double srcc = calculateSpearmansCoefficient(modelElementsOfUsageModel1,modelElementsOfUsageModel2);
		accuracyResults.setJc(jc);
		accuracyResults.setSrcc(srcc);
		return accuracyResults;
	}
	
	/**
	 * Calculates the JaccardCoefficient of the two lists that contain the model elements of the usage models 
	 * that are matched against each other
	 * 
	 * @param modelElementsOfUsageModel1 contains the model elements of usage model 1
	 * @param modelElementsOfUsageModel2 contains the model elements of usage model 2
	 * @return
	 */
	private double calculateJaccardCoefficient(List<ModelElement> modelElementsOfUsageModel1,List<ModelElement> modelElementsOfUsageModel2) {
		List<ModelElement> modelElements1 = new ArrayList<ModelElement>(modelElementsOfUsageModel1);
		List<ModelElement> modelElements2 = new ArrayList<ModelElement>(modelElementsOfUsageModel2);
		double jc = 0;
		double numberOfMatchingElements = 0;
		for(int i=0;i<modelElements1.size();i++) {
			for(int j=0;j<modelElements2.size();j++) {
				if(modelElements1.get(i).equals(modelElements2.get(j))){
					modelElements1.remove(i);
					i--;
					modelElements2.remove(j);
					numberOfMatchingElements++;
					break;
				}
			}
		}
		jc = numberOfMatchingElements / (modelElements1.size()*1.0 + modelElements2.size()*1.0 + numberOfMatchingElements);
		return jc;
	}
	
	/**
	 * Calculates the Spearman’s Rank Correlation Coefficient of the two lists that contain the model elements of the usage models
	 * that are matched against each other
	 * 
	 * @param modelElementsOfUsageModel1 contains the model elements of usage model 1
	 * @param modelElementsOfUsageModel2 contains the model elements of usage model 2
	 * @return
	 */
	private double calculateSpearmansCoefficient(List<ModelElement> modelElementsOfUsageModel1,List<ModelElement> modelElementsOfUsageModel2) {
		double srcc = 0;
		long squaredRank = 0;
		
		for(int i=0;i<modelElementsOfUsageModel1.size();i++) {
			for(int j=0;j<modelElementsOfUsageModel2.size();j++) {
				if(modelElementsOfUsageModel2.get(j)==null)
					continue;
				if(modelElementsOfUsageModel1.get(i).equals(modelElementsOfUsageModel2.get(j))){
					squaredRank += (i-j) * (i-j);
					modelElementsOfUsageModel2.set(j, null);
					break;
				}
			}
		}
		srcc = 1 - (6.0*squaredRank)/(1L * modelElementsOfUsageModel1.size() * (modelElementsOfUsageModel1.size()*modelElementsOfUsageModel1.size() -1));
		return srcc;
	}
	
	/**
	 * Extracts for a scenario behavior the model elements
	 * 
	 * @param scenarioBehaviour whose model elements are extracted
	 * @param modelElements the list to that the model elements are added
	 */
	private void getModelElements(ScenarioBehaviour scenarioBehaviour,List<ModelElement>modelElements) {
		
		List<AbstractUserAction> actionsOfScenarioBehaviour = scenarioBehaviour.getActions_ScenarioBehaviour();
		AbstractUserAction nextActionOfScenarioBehaviour = actionsOfScenarioBehaviour.get(0);
		
		while(nextActionOfScenarioBehaviour!=null) {
			
			if(nextActionOfScenarioBehaviour.getClass().equals(StartImpl.class)) {
				ModelElement modelElement = new ModelElement(true,false,false,false,false,"","",0); 
				modelElements.add(modelElement);
			}	
			else if(nextActionOfScenarioBehaviour.getClass().equals(StopImpl.class)) {
				ModelElement modelElement = new ModelElement(false,true,false,false,false,"","",0); 
				modelElements.add(modelElement);
			}
			else if(nextActionOfScenarioBehaviour.getClass().equals(EntryLevelSystemCallImpl.class)) {
				EntryLevelSystemCall call = (EntryLevelSystemCall)nextActionOfScenarioBehaviour;
				ModelElement modelElement = new ModelElement(false,false,true,false,false,call.getEntityName(),"",0); 
				modelElements.add(modelElement);
			}	
			else if(nextActionOfScenarioBehaviour.getClass().equals(LoopImpl.class)) {
				Loop loop = (Loop)nextActionOfScenarioBehaviour;
				ModelElement modelElement = new ModelElement(false,false,false,false,true,"",loop.getLoopIteration_Loop().getSpecification(),0); 
				modelElements.add(modelElement);
				getModelElements(loop.getBodyBehaviour_Loop(),modelElements);
			}	
			else if(nextActionOfScenarioBehaviour.getClass().equals(BranchImpl.class)) {
				Branch branch = (Branch)nextActionOfScenarioBehaviour;
				// Because the branch transitions of each usage model are not always in the same order
				// the branch transitions are ordered by the entity name of their first EntryLevelSystemCall
				// In doing so, the model elements of each usage model are added in the same order independently of the ordering of the usage model
				List<BranchTransition> branchTransitionsSorted = sortBranchTransitions(branch.getBranchTransitions_Branch());
				
				for(int i=0;i<branchTransitionsSorted.size();i++) {
					ModelElement modelElement = new ModelElement(false,false,false,true,false,"","",branchTransitionsSorted.get(i).getBranchProbability()); 
					modelElements.add(modelElement);
					getModelElements(branchTransitionsSorted.get(i).getBranchedBehaviour_BranchTransition(),modelElements);
				}
			}
					
			nextActionOfScenarioBehaviour = nextActionOfScenarioBehaviour.getSuccessor();
		}
		
	}
	
	/**
	 * It sorts a list of branch transitions by their first EntryLevelSystemCall
	 * 
	 * @param branchTransitions that are sorted
	 * @return sorted list of branch transitions
	 */
	private List<BranchTransition> sortBranchTransitions(List<BranchTransition> branchTransitions) {
		List<String> entityNames = new ArrayList<String>();
		List<BranchTransition> branchTransitionsSorted = new ArrayList<BranchTransition>();
		for(BranchTransition branchTransition : branchTransitions) {
			EntryLevelSystemCall call = (EntryLevelSystemCall)branchTransition.getBranchedBehaviour_BranchTransition().getActions_ScenarioBehaviour().get(0).getSuccessor();
			entityNames.add(call.getEntityName());
		}
		java.util.Collections.sort(entityNames);
		for(String entityName : entityNames) {
			for(BranchTransition branchTransition : branchTransitions) {
				EntryLevelSystemCall call = (EntryLevelSystemCall)branchTransition.getBranchedBehaviour_BranchTransition().getActions_ScenarioBehaviour().get(0).getSuccessor();
				if(call.getEntityName().equals(entityName))
					branchTransitionsSorted.add(branchTransition);
			}
		}
		
		return branchTransitionsSorted;
	}

}
