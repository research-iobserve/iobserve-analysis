package org.iobserve.analysis.userbehavior;

import java.util.List;

import org.iobserve.analysis.userbehavior.data.ClusteringMetrics;
import org.iobserve.analysis.userbehavior.data.ClusteringResults;
import org.iobserve.analysis.userbehavior.data.UserSessionAsTransitionMatrix;

import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.Ranker;
import weka.clusterers.XMeans;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.NormalizableDistance;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;


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
	public ClusteringResults clusterSessionsWithXMeans(Instances instances, int numberOfUserGroupsFromInputUsageModel, int varianceOfUserGroups, int seed){
		
		ClusteringResults xMeansClusteringResults = null;
		
		try {
							
			XMeans xmeans = new XMeans();
			xmeans.setSeed(seed);

			NormalizableDistance manhattenDistance = new ManhattanDistance();
			manhattenDistance.setDontNormalize(false);
			manhattenDistance.setInstances(instances);
			xmeans.setDistanceF(manhattenDistance);
			
			int[] clustersize = null;
			int[] assignments = new int[instances.numInstances()];
			
			int numberOfClustersMin = numberOfUserGroupsFromInputUsageModel - varianceOfUserGroups;
			int numberOfClustersMax = numberOfUserGroupsFromInputUsageModel + varianceOfUserGroups;
			if(numberOfClustersMin<2)
				numberOfClustersMin = 1;
			if(numberOfClustersMax<2)
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
