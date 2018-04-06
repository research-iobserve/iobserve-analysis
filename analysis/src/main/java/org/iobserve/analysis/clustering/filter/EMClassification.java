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
package org.iobserve.analysis.clustering.filter;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.clustering.ExpectationMaximizationClustering;
import org.iobserve.analysis.clustering.birch.SessionsToInstances;
import org.iobserve.analysis.clustering.filter.composite.EMClusteringProcess;
import org.iobserve.analysis.clustering.filter.models.BehaviorModel;
import org.iobserve.analysis.clustering.filter.models.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.session.data.UserSession;



/** This class handles the classification process with the
 * em algorithm. Transforms user sessions to behavior
 * models.
 * @author Melf Lorenzen
 *
 */
public class EMClassification extends CompositeStage {
	   private final InputPort<UserSession> sessionInputPort;
	    private final InputPort<Long> timerInputPort;
	    
	    private final OutputPort<BehaviorModel> outputPort;
	       

	    /**
	     * @param keepTime
	     * @param minCollectionSize
	     * @param representativeStrategy
	     * @param keepEmptyTransitions
	     */
	    public EMClassification(final long keepTime, final int minCollectionSize, 
	    		final IRepresentativeStrategy representativeStrategy, final boolean keepEmptyTransitions) {

	        final SessionsToInstances sessionsToInstances = new SessionsToInstances(keepTime, minCollectionSize, 
	        		representativeStrategy, keepEmptyTransitions);
	        final EMClusteringProcess tVectorQuantizationClustering =  
	        		new EMClusteringProcess(new ExpectationMaximizationClustering());
	        final TBehaviorModelCreation tBehaviorModelCreation = new TBehaviorModelCreation("EM-");   

	        
	        this.sessionInputPort = sessionsToInstances.getSessionInputPort();
	        this.timerInputPort = sessionsToInstances.getTimerInputPort();
	        this.outputPort = tBehaviorModelCreation.getOutputPort();
	        
	        this.connectPorts(sessionsToInstances.getOutputPort(), tVectorQuantizationClustering.getInputPort());
	        this.connectPorts(tVectorQuantizationClustering.getOutputPort(), tBehaviorModelCreation.getInputPort());
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