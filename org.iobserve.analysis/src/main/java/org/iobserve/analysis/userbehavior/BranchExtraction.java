package org.iobserve.analysis.userbehavior;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.userbehavior.data.CallBranchModel;


/**
 * Entry Point of the aggregation of the single entryCall sequences to a coherent model. At that the single sequences are 
 * aggregated to a tree-like structure: Equal sequences are summarized to one sequence, alternative sequences are 
 * represented via branches. The likelihood of each branch depends on the occurrence frequency of its sequence. 
 * The result is a CallBranchModel for each EntryCallSequenceModel(user group). 
 * 
 * @param entryCallSequenceModels are the entryCallSequenceModels of the extracted user groups(For each user group one entryCallSequenceModel)
 * @author David Peter, Robert Heinrich
 */
public class BranchExtraction {
	
	private final List<EntryCallSequenceModel> entryCallSequenceModels;
	private List<CallBranchModel> branchOperationModels = null;
	
	/**
	 * @param entryCallSequenceModels are the entryCallSequenceModels of the extracted user groups(For each user group one entryCallSequenceModel)
	 */
	public BranchExtraction(List<EntryCallSequenceModel> entryCallSequenceModels) {
		this.entryCallSequenceModels = entryCallSequenceModels;
	}
	
	public void createCallBranchModels() {
		
		CallBranchModelCreator modelCreator = new CallBranchModelCreator();
		branchOperationModels = new ArrayList<CallBranchModel>();
		
		for(final EntryCallSequenceModel entryCallSequenceModel:entryCallSequenceModels) {
			/**
			 * 1. Aggregates the single EntryCall sequences to a CallBranchModel 
			 */
			CallBranchModel branchOperationModel = modelCreator.createCallBranchModel(entryCallSequenceModel);
			
			/**
			 * 2. Calculates the likelihoods of the branches of the obtained CallBranchModel 
			 */
			modelCreator.calculateLikelihoodsOfBranches(branchOperationModel);
			
			branchOperationModels.add(branchOperationModel);
		}

	}

	public List<CallBranchModel> getBranchOperationModels() {
		return branchOperationModels;
	}

}
