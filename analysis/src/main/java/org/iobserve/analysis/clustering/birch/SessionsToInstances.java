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
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;
import teetime.stage.basic.merger.Merger;
import teetime.stage.basic.merger.strategy.BlockingBusyWaitingRoundRobinMergerStrategy;

import org.iobserve.analysis.clustering.filter.TBehaviorModelPreperation;
import org.iobserve.analysis.clustering.filter.TBehaviorModelTableGeneration;
import org.iobserve.analysis.clustering.filter.TInstanceTransformations;
import org.iobserve.analysis.clustering.filter.models.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.analysis.session.CollectUserSessionsFilter;
import org.iobserve.analysis.session.data.UserSession;

import weka.core.Instances;
/**
 * @author Melf Lorenzen
 *
 */
public class SessionsToInstances extends CompositeStage{ 

	private final InputPort<UserSession> sessionInputPort;
    
	private final InputPort<Long> timerInputPort;
    
    private final OutputPort<Instances> outputPort;
    
	/**
	 * constructor for the SessionsToInstances class.
     * @param keepTime the time interval to keep user sessions
     * @param minCollectionSize  minimal number of collected user session
     * @param representativeStrategy representative strategy for behavior model table generation
     * @param keepEmptyTransitions allows behavior model table generation to keep empty transitions
	 */
	public SessionsToInstances(final long keepTime, final int minCollectionSize, 
    		final IRepresentativeStrategy representativeStrategy, 
    		final boolean keepEmptyTransitions) {
		
        final IDistributorStrategy strategy = new CopyByReferenceStrategy();
        final Distributor<EntryCallSequenceModel> distributor = new Distributor<>(strategy);

		final Merger<Object> merger = new Merger<>(new BlockingBusyWaitingRoundRobinMergerStrategy());
		final CollectUserSessionsFilter collectUserSessionsFilter = 
				new CollectUserSessionsFilter(keepTime, minCollectionSize);
		
		final TInstanceTransformations tInstanceTransformations = new TInstanceTransformations(); 
                
        final TBehaviorModelTableGeneration tBehaviorModelTableGeneration = new TBehaviorModelTableGeneration(
        		representativeStrategy, keepEmptyTransitions);	

        final TBehaviorModelPreperation tBehaviorModelPreperation = new TBehaviorModelPreperation(
        		keepEmptyTransitions);

        this.sessionInputPort = collectUserSessionsFilter.getUserSessionInputPort();
        this.timerInputPort = collectUserSessionsFilter.getTimeTriggerInputPort();
        this.outputPort = tInstanceTransformations.getOutputPort();
        
        this.connectPorts(collectUserSessionsFilter.getOutputPort(), distributor.getInputPort());
        this.connectPorts(distributor.getNewOutputPort(), tBehaviorModelTableGeneration.getInputPort());
        this.connectPorts(distributor.getNewOutputPort(), merger.getNewInputPort());

        this.connectPorts(tBehaviorModelTableGeneration.getOutputPort(), merger.getNewInputPort());

        this.connectPorts(merger.getOutputPort(), tBehaviorModelPreperation.getInputPort());
        this.connectPorts(tBehaviorModelPreperation.getOutputPort(), tInstanceTransformations.getInputPort());
	}
	    
    public InputPort<Long> getTimerInputPort() {
		return timerInputPort;
	}
	
	public InputPort<UserSession> getSessionInputPort() {
		return this.sessionInputPort;
	}

	public OutputPort<Instances> getOutputPort() {
		return this.outputPort;
	}
}
