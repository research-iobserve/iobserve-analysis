package org.iobserve.analysis.userbehavior;

import java.util.List;

import org.iobserve.analysis.userbehavior.data.ClusteringMetrics;
import org.iobserve.analysis.userbehavior.data.ClusteringResults;
import org.iobserve.analysis.userbehavior.data.UserSessionAsTransitionMatrix;

import weka.clusterers.XMeans;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;

/**
 * This class performs the clustering of the user sessions that have been transformed to transition matrices before.
 * 
 * @author David Peter, Robert Heinrich
 */
public class XMeansClustering extends AbstractClustering {
	
	/**
	 * It performs the clustering by means of the Weka plugin. The similarity between user sessions is calculated on the basis
	 * of their transition matrices.
	 * 
	 * @param listOfDistinctOperationSignatures are the extracted distinct operation signatures of the input entryCallSequenceModel
	 * @param transitionModel contains the userSessions in form of transition matrices
	 * @param numberOfUserGroupsFromInputUsageModel is the input number of clusters
	 * @return the clustering results that contain the number of cluster and the assignments
	 */
	public ClusteringResults clusterSessionsWithXMeans(List<String> listOfDistinctOperationSignatures, List<UserSessionAsTransitionMatrix> transitionModel, int numberOfUserGroupsFromInputUsageModel, int varianceOfUserGroups){
		
		ClusteringResults xMeansClusteringResults = null;
		
		try {
				
			Instances instances = createInstances(transitionModel, listOfDistinctOperationSignatures);
			
			XMeans xmeans = new XMeans();
			xmeans.setSeed(5);
			DistanceFunction euclideanDistance = new EuclideanDistance();
			euclideanDistance.setInstances(instances);
			xmeans.setDistanceF(euclideanDistance);
			
			int[] clustersize = null;
			int[] assignments = new int[instances.numInstances()];
			
			int numberOfClustersMin = numberOfUserGroupsFromInputUsageModel - varianceOfUserGroups;
			int numberOfClustersMax = numberOfUserGroupsFromInputUsageModel + varianceOfUserGroups;
			if(numberOfClustersMin<1)
				numberOfClustersMin = 1;
			if(numberOfClustersMax<1)
				numberOfClustersMax = 1;
			
			xmeans.setMinNumClusters(numberOfClustersMin);
			xmeans.setMaxNumClusters(numberOfClustersMax);
			xmeans.buildClusterer(instances);
			
			clustersize = new int[xmeans.getClusterCenters().numInstances()];
			for (int s = 0; s < instances.numInstances(); s++) {
				assignments[s] = xmeans.clusterInstance(instances.instance(s));
				clustersize[xmeans.clusterInstance(instances.instance(s))]++;
			}
			
			ClusteringMetrics clusteringMetrics = new ClusteringMetrics(xmeans.getClusterCenters(), instances, assignments);
			clusteringMetrics.calculateSimilarityMetrics();
			
			xMeansClusteringResults = new ClusteringResults("X-Means", xmeans.getClusterCenters().numInstances(), assignments, clusteringMetrics);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return xMeansClusteringResults;		
	}
}
