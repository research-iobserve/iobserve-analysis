/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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

package org.iobserve.analysis.userbehavior;

import java.util.Collection;
import java.util.List;

import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.userbehavior.data.ClusteringResults;
import org.iobserve.analysis.userbehavior.data.UserSessionAsCountsOfCalls;
import org.iobserve.analysis.utils.ExecutionTimeLogger;

import weka.core.Instances;

/**
 * Entry Point of the user group extraction process. It clusters the entryCallSequenceModel's user
 * sessions to detect different user groups. The numberOfUserGroupsFromInputUsageModel and the
 * varianceOfUserGroups serve as the input for the number of clusters. The result is a list of
 * entryCallSequenceModels, for each user group one entryCallSequenceModel, that can be retrieved
 * via the getter method after performing the extractUserGroups method. Each entryCallSequenceModel
 * contains the user group's assigned user sessions, the user group's likelihood and the user
 * group's specific workload intensity. The clustering similarity bases on the transition model that
 * represents each user session as counts of called operation signatures. It states the number of
 * calls of each distinct operation signature occurring within the user sessions
 *
 * @author David Peter, Robert Heinrich
 */
public class UserGroupExtraction {

    private final EntryCallSequenceModel entryCallSequenceModel;
    private final int numberOfUserGroupsFromInputUsageModel;
    private final int varianceOfUserGroups;
    private List<EntryCallSequenceModel> entryCallSequenceModelsOfUserGroups = null;
    private ClusteringResults clusteringResults = null;
    private final boolean isClosedWorkload;
    
    private long timeDistinctSignatures;
    private long timeCallCountModel;
    private long timeXMeans;
    private long timeUserGroupEntryCallSequenceModel;
    private long timeWorkload;

    /**
     *
     * @param entryCallSequenceModel
     *            contains the user sessions that are used to detect user groups via similar
     *            behavior. Therefore, the user sessions are clustered.
     * @param numberOfUserGroupsFromInputUsageModel
     *            states the number of user groups in the latest created usage model. It serves as
     *            input for the number of clusters, i.e., for the number of user groups to detect
     * @param varianceOfUserGroups
     *            enables the creation of a minimum and maximum number of clusters for the X-Means
     *            clustering algorithm
     * @param isClosedWorkload
     *            states whether a closed or open workload specification is requested by the user
     */
    public UserGroupExtraction(final EntryCallSequenceModel entryCallSequenceModel,
            final int numberOfUserGroupsFromInputUsageModel, final int varianceOfUserGroups,
            final boolean isClosedWorkload) {
        this.entryCallSequenceModel = entryCallSequenceModel;
        this.numberOfUserGroupsFromInputUsageModel = numberOfUserGroupsFromInputUsageModel;
        this.varianceOfUserGroups = varianceOfUserGroups;
        this.isClosedWorkload = isClosedWorkload;
    }

    /**
     * Function to extract user groups.
     */
    public void extractUserGroups() {
    	ExecutionTimeLogger.getInstance().startLogging(this);
    	long timeBefore = 0;
    	long timeAfter = 0;

        final ClusteringPrePostProcessing clusteringProcessing = new ClusteringPrePostProcessing();
        final XMeansClustering xMeansClustering = new XMeansClustering();
        ClusteringResults xMeansClusteringResults;

        timeBefore = System.currentTimeMillis();
        /**
         * 1. Extraction of distinct system operations. Creates a list of the distinct operation
         * signatures occurring within the entryCallSequenceModel. It is required to transform each
         * user session to counts of its called operations. The counts are used to determine the
         * similarity between the user sessions
         * 
         * O(n*E(n) + n) n = UserSessions, E(n) = EntryCallEvents von Session
         */
        final List<String> listOfDistinctOperationSignatures = clusteringProcessing
                .getListOfDistinctOperationSignatures(this.entryCallSequenceModel.getUserSessions());
        timeAfter = System.currentTimeMillis();
        this.timeDistinctSignatures = timeAfter - timeBefore;

        timeBefore = System.currentTimeMillis();
        /**
         * 2. Transformation to the call count model. Transforms the call sequences of the user
         * sessions to a list of counts of calls that state the number of calls of each distinct
         * operation signature for each user session
         * 
         * O((n^2)*E(n)) n = UserSessions, E(n) = EntryCallEvents von Session
         */
        final List<UserSessionAsCountsOfCalls> callCountModel = clusteringProcessing
                .getCallCountModel(this.entryCallSequenceModel.getUserSessions(), listOfDistinctOperationSignatures);
        timeAfter = System.currentTimeMillis();
        this.timeCallCountModel = timeAfter - timeBefore;

        
        timeBefore = System.currentTimeMillis();
        /**
         * 3. Clustering of user sessions. Clustering of the user sessions whose behavior is
         * represented as counts of their called operation signatures to obtain user groups
         */
        // O((n+1) * m) m = numberOfDistinctOperationSignatures 
        final Instances instances = xMeansClustering.createInstances(callCountModel, listOfDistinctOperationSignatures.size());
        /*
         * The clustering is performed 5 times and the best result is taken. The quality of a
         * clustering result is determined by the value of the sum of squared error (SSE) of the
         * clustering. The lower the SSE is the better the clustering result.
         */
        // O(5*n*k*m) (5 iterations, n sessions, k clusters, m dimensions for distance calculations)
        // Definition from Wikipedia
        for (int i = 0; i < 5; i++) {
            xMeansClusteringResults = xMeansClustering.clusterSessionsWithXMeans(instances,
                    this.numberOfUserGroupsFromInputUsageModel, this.varianceOfUserGroups, i);
            if (this.clusteringResults == null) {
                this.clusteringResults = xMeansClusteringResults;
            } else if (xMeansClusteringResults.getClusteringMetrics().getSumOfSquaredErrors() < this.clusteringResults
                    .getClusteringMetrics().getSumOfSquaredErrors()) {
                this.clusteringResults = xMeansClusteringResults;
            }
        }
        timeAfter = System.currentTimeMillis();
        this.timeXMeans = timeAfter - timeBefore;

        timeBefore = System.currentTimeMillis();
        /**
         * 4. Obtaining the user groups' call sequence models. Creates for each cluster resp. user
         * group its own entry call sequence model that exclusively contains its assigned user
         * sessions
         * O(n * k) n = UserSessions, k = amount of clusters
         */
        final List<EntryCallSequenceModel> entryCallSequenceModelsOfXMeansClustering = clusteringProcessing
                .getForEachUserGroupAnEntryCallSequenceModel(this.clusteringResults, this.entryCallSequenceModel);
        timeAfter = System.currentTimeMillis();
        this.timeUserGroupEntryCallSequenceModel = timeAfter - timeBefore;

        timeBefore = System.currentTimeMillis();
        /**
         * 5. Obtaining the user groups' workload intensity. Calculates and sets for each user group
         * its specific workload intensity parameters
         * O(k * n)
         */
        clusteringProcessing.setTheWorkloadIntensityForTheEntryCallSequenceModels(
                entryCallSequenceModelsOfXMeansClustering, this.isClosedWorkload);
        timeAfter = System.currentTimeMillis();
        this.timeWorkload = timeAfter - timeBefore;

        /**
         * Sets the resulting entryCallSequenceModels that can be retrieved via the getter method
         */
        this.entryCallSequenceModelsOfUserGroups = entryCallSequenceModelsOfXMeansClustering;
        ExecutionTimeLogger.getInstance().stopLogging(this);
    }

    /**
     * Returns the obtained entryCallSequenceModels. For each detected user group there is one
     * entryCallSequenceModel. The extractUserGroups method has to be performed before.
     *
     * @return the entryCallSequenceModels that are created by the extractUserGroups method
     */
    public List<EntryCallSequenceModel> getEntryCallSequenceModelsOfUserGroups() {
        return this.entryCallSequenceModelsOfUserGroups;
    }

    public ClusteringResults getClusteringResults() {
        return this.clusteringResults;
    }
    
    public long getTimeDistinctSignatures() {
    	return this.timeDistinctSignatures;
    }
    public long getTimeCallCountModel() {
    	return this.timeCallCountModel;
    }
    public long getTimeXMeans() {
    	return this.timeXMeans;
    }
    public long getTimeUserGroupEntryCallSequenceModel() {
    	return this.timeUserGroupEntryCallSequenceModel;
    }
    public long getTimeWorkload() {
    	return this.timeWorkload;
    }

}
