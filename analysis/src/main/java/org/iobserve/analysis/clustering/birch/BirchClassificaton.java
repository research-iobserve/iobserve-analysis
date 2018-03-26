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

import teetime.stage.basic.merger.Merger;
import teetime.stage.basic.merger.strategy.BlockingBusyWaitingRoundRobinMergerStrategy;
import teetime.stage.basic.merger.strategy.IMergerStrategy;

import org.iobserve.analysis.clustering.filter.TBehaviorModelCreation;
import org.iobserve.analysis.clustering.filter.TBehaviorModelPreperation;
import org.iobserve.analysis.clustering.filter.TBehaviorModelTableGeneration;
import org.iobserve.analysis.clustering.filter.TEntryCallSequenceFilter;
import org.iobserve.analysis.clustering.filter.TInstanceTransformations;
import org.iobserve.analysis.clustering.filter.models.BehaviorModel;
import org.iobserve.analysis.clustering.filter.models.configuration.BehaviorModelConfiguration;
import org.iobserve.analysis.clustering.filter.models.configuration.GetLastXSignatureStrategy;
import org.iobserve.analysis.clustering.filter.models.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.analysis.session.CollectUserSessionsFilter;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.analysis.sink.BehaviorModelSink;

import weka.core.Instances;

/**
 *
 * @author Melf Lorenzen
 */

public class BirchClassificaton extends CompositeStage {
   
    private final InputPort<Object> sessionInputPort;
    private final InputPort<Object> timerInputPort;
    
    private final OutputPort<BehaviorModel> outputPort;
       
    /**
     * constructor.
     *
     * @param configuration
     *            model configuration
     */
    public BirchClassificaton(final long keepTime, final int minCollectionSize, 
    		final IRepresentativeStrategy representativeStrategy, final boolean keepEmptyTransitions,
    		final double leafThresholdValue, final int maxLeafSize, final int maxNodeSize,
    		final int maxLeafEntries, int expectedNumberOfClusters, boolean useClusterNumberMetric) {

        SessionsToInstances sessionsToInstances = new SessionsToInstances(keepTime, minCollectionSize, 
        		representativeStrategy, keepEmptyTransitions);
        BirchClustering birchClustering = new BirchClustering(leafThresholdValue, maxLeafSize,
        		maxNodeSize, maxLeafEntries, expectedNumberOfClusters, useClusterNumberMetric);
        TBehaviorModelCreation tBehaviorModelCreation = new TBehaviorModelCreation("birch-");   

        
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

    public InputPort<Object> getSessionInputPort() {
        return this.sessionInputPort;
    }

    public InputPort<Object> getTimerInputPort() {
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

