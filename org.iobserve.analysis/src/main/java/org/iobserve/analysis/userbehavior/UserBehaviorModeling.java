package org.iobserve.analysis.userbehavior;

/**
 * Copyright 2016 David Peter
 */

import java.io.IOException;
import java.util.List;

import org.palladiosimulator.pcm.usagemodel.UsageModel;

import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.userbehavior.data.CallBranchModel;

/**
 * Entry Point of the user behavior modeling. This class subsequently calls the user behavior modeling processes and stores 
 * the result as the userBeheviorModel that can be retrieved via a getter method. The modelUserBehavior method triggers the 
 * user behavior modeling. The constructor takes the input entryCallSequenceModel that contains the user sessions that are used to 
 * analyze and build the user behavior. The numberOfUserGroupsFromInputUsageModel states the number of user groups in the latest 
 * created usage model. It serves as input for the number of clusters within the user group detection.
 *  
 * @param inputEntryCallSequenceModel contains the user sessions that are used to analyze and build the user behavior
 * @param numberOfUserGroupsFromInputUsageModel states the number of user groups in the latest created usage model. It serves
 * as input for the number of clusters
 * @author David Peter, Robert Heinrich
 */
public class UserBehaviorModeling {
		
	private final EntryCallSequenceModel inputEntryCallSequenceModel;
	private final int numberOfUserGroupsFromInputUsageModel;
	private final int varianceOfUserGroups;
	private final boolean isClosedWorkloadRequested;
	private final double thinkTime;
	private UsageModel pcmUsageModel;
	
	/**
	* @param inputEntryCallSequenceModel contains the user sessions that are used to analyze and build the user behavior
	* @param numberOfUserGroupsFromInputUsageModel states the number of user groups in the latest created usage model. It serves
	* as input for the number of clusters
	*/
	public UserBehaviorModeling (EntryCallSequenceModel inputEntryCallSequenceModel, int numberOfUserGroupsFromInputUsageModel, int varianceOfUserGroups, boolean isClosedWorkloadRequested, double thinkTime) {
		this.inputEntryCallSequenceModel = inputEntryCallSequenceModel;
		this.numberOfUserGroupsFromInputUsageModel = numberOfUserGroupsFromInputUsageModel;
		this.varianceOfUserGroups = varianceOfUserGroups;
		this.isClosedWorkloadRequested = isClosedWorkloadRequested;
		this.thinkTime = thinkTime;
	}	
	
	/**
	 * It triggers the user behavior modeling. At that, it serves as a controller method that subsequently invokes the 
	 * single process steps that comprise
	 * 1. The detection of user groups
	 * 2. The aggregation of the user sessions´ call sequences to a coherent branch model
	 * 3. The detection of iterated behavior in form of loops
	 * 
	 * After performing this method the resulting user behavior model that contains for each user group its specific behavior 
	 * model can be retrieved via the getter method.
	 */ 
	public void modelUserBehavior() throws IOException {
		
		if(this.inputEntryCallSequenceModel.getUserSessions().size()<1)
			return;
				
		/**
		 * 1. The extraction of user groups
		 * It clusters the entry call sequence model to detect different user groups within the user sessions
		 * The result is one entry call sequence model for each detected user group
		 * Each entry call sequence model contains: 
		 * - the user sessions that are assigned to the user group
		 * - the likelihood of its user group
		 * - the parameters for the workload intensity of its user group
		 */
		final UserGroupExtraction extractionOfUserGroups = new UserGroupExtraction(inputEntryCallSequenceModel, numberOfUserGroupsFromInputUsageModel, varianceOfUserGroups);
		extractionOfUserGroups.extractUserGroups();
		final List<EntryCallSequenceModel> entryCallSequenceModels = extractionOfUserGroups.getEntryCallSequenceModelsOfUserGroups();
		
		/**
		 * 2. The aggregation of the call sequences
		 * It aggregates each call sequence model that is created during the user group detection
		 * into a call branch model that consists of a tree like structure: 
		 * The single sequences are aggregated to one coherent model that contains alternative sequences in form of branches.  
		 * It detects branches and the branch likelihoods.
		 * The result is one callBranchModel for each user group.
		 */
		final BranchExtraction branchExtraction = new BranchExtraction(entryCallSequenceModels);
		branchExtraction.createCallBranchModels();
		final List<CallBranchModel> callBranchModels = branchExtraction.getBranchOperationModels();
		
		/**
		 * 3. The detection of iterated behavior
		 * It detects iterated behavior within the created callBranchModels in form of loops. 
		 * Single entryCalls or sequences of entryCalls that are iterated in a row 
		 * are detected and summarized to loops containing the number of loops as the count of each loop. 
		 * The result is one callBranchModel for each user group that additionally contains loops for iterated entryCalls.
		 */
		final LoopDetection loopDetection = new LoopDetection(callBranchModels);
		loopDetection.createCallLoopBranchModels();
		final List<CallBranchModel> callLoopBranchModels = loopDetection.getCallLoopBranchModels();
		
		/**
		 * 4. Modeling of the usage behavior
		 * It creates a PCM usage model corresponding to the callLoopBranchModel
		 * Each detected user group is represented as one usage scenario within the usage model.
		 * It contains loops and branches corresponding to the callLoopBranchModel.
		 * The resulting PCM usage model can be retrieved via the getter method.
		 */
		final PcmUsageModelBuilder usageModelBuilder = new PcmUsageModelBuilder(callLoopBranchModels, this.isClosedWorkloadRequested, this.thinkTime);
		this.pcmUsageModel = usageModelBuilder.createUsageModel();
	}

	/**
	 * Returns the generated PCM Usage Model. The modelUserBehavior method that performs the transformations from
	 * the passed entryCallSequenceModel and generates the PCM usage model has to be invoked before. 
	 * 
	 * @return the generated PCM Usage Model
	 */
	public UsageModel getPcmUsageModel() {
		return pcmUsageModel;
	}
	
	
	


}
