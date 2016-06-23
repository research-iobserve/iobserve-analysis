package org.iobserve.analysis.userbehavior;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.userbehavior.data.CallBranchModel;

/**
 * Entry point of the LoopDetection process that detects iterated behavior within the passed CallBranchModels. At that, 
 * single callElements or sequences of callElements that are iterated in a row within the branch sequences
 * are detected and summarized to loops containing the number of loops as the count of each loop. If several loops overlap 
 * (More than one loop uses the same callElement) the loop is used that replaces the most callElements.
 * The result is one callBranchModel for each user group that additionally contains loops for iterated entryCalls. 
 * 
 * 
 * @param callBranchModels are the CallBranchModels that are created by the BranchExtraction process
 * @author David Peter, Robert Heinrich
 */

public class LoopDetection {
	
	private final List<CallBranchModel> callBranchModels;
	private List<CallBranchModel> callLoopBranchModels;
	
	/**
	 * 
	 * @param callBranchModels are the CallBranchModels that are created by the BranchExtraction process
	 */
	public LoopDetection(List<CallBranchModel> callBranchModels) {
		this.callBranchModels = callBranchModels;
	}
	
	public void createCallLoopBranchModels() {
		
		CallLoopBranchModelCreator modelCreator = new CallLoopBranchModelCreator();
		callLoopBranchModels = new ArrayList<CallBranchModel>();
		
		for(CallBranchModel callBranchModel : callBranchModels) {
			/**
			 * Detection of iterated behavior 
			 */
			modelCreator.detectLoopsInCallBranchModel(callBranchModel);
			callLoopBranchModels.add(callBranchModel);
		}
		
	}

	public List<CallBranchModel> getCallLoopBranchModels() {
		return callLoopBranchModels;
	}
	


}
