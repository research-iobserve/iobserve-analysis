/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.clustering.birch;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.clustering.birch.model.ICFComparisonStrategy;
import org.iobserve.analysis.clustering.filter.TBehaviorModelCreation;
import org.iobserve.analysis.clustering.filter.models.BehaviorModel;
import org.iobserve.analysis.clustering.filter.models.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.session.data.UserSession;


/** This class handles the classification process with the
 * birch algorithm. Transforms user sessions to behavior
 * models.
 * @author Melf Lorenzen
 *
 */
public class BirchClassificaton extends CompositeStage {
   
    private final InputPort<UserSession> sessionInputPort;
    private final InputPort<Long> timerInputPort;
    
    private final OutputPort<BehaviorModel> outputPort;

    /**
     * constructor for the BirchClassificaton composite stage.
     * @param keepTime the time interval to keep user sessions
     * @param minCollectionSize  minimal number of collected user session
     * @param representativeStrategy representative strategy for behavior model table generation
     * @param keepEmptyTransitions allows behavior model table generation to keep empty transitions
	 * @param leafThresholdValue the merge threshold for the underlying cf tree
	 * @param maxLeafSize the maximum number of entries in a leaf
	 * @param maxNodeSize the maximum number of entries in a node
	 * @param maxLeafEntries the maximum number of leaf entries in the underlying cf tree
	 * @param expectedNumberOfClusters the expected number of clusters in the data
	 * @param useClusterNumberMetric whether to use the expected number or 
	 * number calculated by the cluster number metric
	 * @param clusterComparisonStrategy the cluster comparison strategy 
	 * @param evalStrategy the strategy for the l-method evaluation graph
     */
    public BirchClassificaton(final long keepTime, final int minCollectionSize, 
    		final IRepresentativeStrategy representativeStrategy, final boolean keepEmptyTransitions,
    		final double leafThresholdValue, final int maxLeafSize, final int maxNodeSize,
    		final int maxLeafEntries, final int expectedNumberOfClusters, final boolean useClusterNumberMetric, 
    		final ICFComparisonStrategy clusterComparisonStrategy,
    		final ILMethodEvalStrategy evalStrategy) {

        final SessionsToInstances sessionsToInstances = new SessionsToInstances(keepTime, minCollectionSize, 
        		representativeStrategy, keepEmptyTransitions);
        final BirchClustering birchClustering = new BirchClustering(leafThresholdValue, maxLeafSize,
        		maxNodeSize, maxLeafEntries, expectedNumberOfClusters, useClusterNumberMetric, 
        		clusterComparisonStrategy, evalStrategy);
        final TBehaviorModelCreation tBehaviorModelCreation = new TBehaviorModelCreation("birch-");   
        this.sessionInputPort = sessionsToInstances.getSessionInputPort();
        this.timerInputPort = sessionsToInstances.getTimerInputPort();
        this.outputPort = tBehaviorModelCreation.getOutputPort();
        
        this.connectPorts(sessionsToInstances.getOutputPort(), birchClustering.getInputPort());
        this.connectPorts(birchClustering.getOutputPort(), tBehaviorModelCreation.getInputPort());
    }

    /**
     * get matching input port.
     *
     * @return input port
     */

    public InputPort<UserSession> getSessionInputPort() {
        return this.sessionInputPort;
    }

    public InputPort<Long> getTimerInputPort() {
        return this.timerInputPort;
    }
    
    /**
     * get suitable output port.
     *
     * @return outputPort
     */
    public OutputPort<BehaviorModel> getOutputPort() {
        return this.outputPort;
    }

}

