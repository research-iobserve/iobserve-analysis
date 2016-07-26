package org.iobserve.analysis.userbehavior;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.userbehavior.data.ClusteringResults;
import org.iobserve.analysis.userbehavior.data.UserSessionAsTransitionMatrix;

import weka.core.Instances;

/**
 * Entry Point of the user group detection. It clusters the entryCallSequenceModel´s user sessions to detect different user groups. 
 * The numberOfUserGroupsFromInputUsageModel serves as the input for the number of clusters. The result is a list of
 * entryCallSequenceModels, for each user group one entryCallSequenceModel, that can be retrieved via the getter method after 
 * performing the extractUserGroups method. Each entryCallSequenceModel contains the user group´s assigned user sessions,
 * the user group´s likelihood and the user group´s specific workload intensity. The clustering similarity bases on 
 * the transition model that represents each user session as a matrix of transitions. It states the number of transitions
 * from each distinct operation signature occurring within the considered user sessions to each other. 
 *  
 * @param entryCallSequenceModel contains the user sessions that are used to detect user groups via similar behavior
 * @param numberOfUserGroupsFromInputUsageModel states the number of user groups in the latest created usage model. It serves
 * as input for the number of clusters, i.e., for the number of user groups to detect
 * @author David Peter, Robert Heinrich
 */
public class UserGroupExtraction {
	
	private final EntryCallSequenceModel entryCallSequenceModel;
	private final int numberOfUserGroupsFromInputUsageModel;
	private final int varianceOfUserGroups;
	private List<EntryCallSequenceModel> entryCallSequenceModelsOfUserGroups = null;
	ClusteringResults clusteringResults = null;
	
	/**
	 * @param entryCallSequenceModel contains the user sessions that are used to detect user groups via similar behavior
	 * @param numberOfUserGroupsFromInputUsageModel states the number of user groups in the latest created usage model. It serves
	 * as input for the number of clusters, i.e., for the number of user groups to detect
	 */
	public UserGroupExtraction(EntryCallSequenceModel entryCallSequenceModel, int numberOfUserGroupsFromInputUsageModel, int varianceOfUserGroups) {
		this.entryCallSequenceModel = entryCallSequenceModel;
		this.numberOfUserGroupsFromInputUsageModel = numberOfUserGroupsFromInputUsageModel;
		this.varianceOfUserGroups = varianceOfUserGroups;
	}
	
	public void extractUserGroups() {
		
		ClusteringPrePostProcessing clusteringProcessing = new ClusteringPrePostProcessing();
		XMeansClustering xMeansClustering = new XMeansClustering();
		ClusteringResults xMeansClusteringResults;
		
		/**
		 * 1. Extraction of distinct system operations
		 * Creates a list of the distinct operation signatures occurring within the entryCallSequenceModel.
		 * It is required to transform each user session to transition matrix.
		 */
		List<String> listOfDistinctOperationSignatures = clusteringProcessing.getListOfDistinctOperationSignatures(entryCallSequenceModel.getUserSessions());
	
		/**
		 * 2. Transformation to the absolute transition model
		 * Transforms the call sequences of the user sessions to transition matrices that state the number of transitions
		 * between the obtained operation signatures.
		 */
		List<UserSessionAsTransitionMatrix> absoluteTransitionModel = clusteringProcessing.getTransitionModel(entryCallSequenceModel.getUserSessions(), listOfDistinctOperationSignatures);
	
		/**
		 * 3. Clustering of user sessions
		 * Clustering of the transition matrices to obtain user groups 
		 */
		Instances instances = xMeansClustering.createInstances(absoluteTransitionModel, listOfDistinctOperationSignatures);
		for(int i=0;i<5;i++) {
			xMeansClusteringResults = xMeansClustering.clusterSessionsWithXMeans(instances, numberOfUserGroupsFromInputUsageModel, varianceOfUserGroups, i);
			if(this.clusteringResults==null)
				this.clusteringResults = xMeansClusteringResults;
			else if(xMeansClusteringResults.getClusteringMetrics().getSumOfSquaredErrors()<this.clusteringResults.getClusteringMetrics().getSumOfSquaredErrors())
				this.clusteringResults = xMeansClusteringResults;
		}
		
		/**
		 * 4. Obtaining the user groups´ call sequence models 
		 * Creates for each cluster resp. user group its own entry call sequence model that exclusively contains its assigned
		 * user sessions
		 */
		List<EntryCallSequenceModel> entryCallSequenceModelsOfXMeansClustering = clusteringProcessing.getForEachUserGroupAnEntryCallSequenceModel(this.clusteringResults, entryCallSequenceModel);
		
		/**
		 * 5. Obtaining the user groups´ workload intensity 
		 * Calculates and sets for each user group its specific workload intensity parameters
		 */
		clusteringProcessing.setTheWorkloadIntensityForTheEntryCallSequenceModels(entryCallSequenceModelsOfXMeansClustering);
		
		/**
		 * Sets the resulting entryCallSequenceModels that can be retrieved via the getter method
		 */
		this.entryCallSequenceModelsOfUserGroups = entryCallSequenceModelsOfXMeansClustering;
	}
	
	/**
	 * Returns the obtained entryCallSequenceModels. For each detected user group there is one entryCallSequenceModel.
	 * The extractUserGroups method has to be performed before. 
	 * 
	 * @return the entryCallSequenceModels that are created by the extractUserGroups method
	 */
	public List<EntryCallSequenceModel> getEntryCallSequenceModelsOfUserGroups() {
		return this.entryCallSequenceModelsOfUserGroups;
	}
	 

}
