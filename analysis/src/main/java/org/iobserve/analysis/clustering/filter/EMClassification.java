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

import kieker.common.configuration.Configuration;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.clustering.ExpectationMaximizationClustering;
import org.iobserve.analysis.clustering.birch.SessionsToInstances;
import org.iobserve.analysis.clustering.filter.composite.EMClusteringProcess;
import org.iobserve.analysis.clustering.filter.models.BehaviorModel;
import org.iobserve.analysis.clustering.filter.models.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.clustering.filter.models.configuration.examples.JPetstoreStrategy;
import org.iobserve.analysis.clustering.shared.IClassificationStage;
import org.iobserve.analysis.configurations.ConfigurationKeys;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.stages.general.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





/** This class handles the classification process with the
 * em algorithm. Transforms user sessions to behavior
 * models.
 * @author Melf Lorenzen
 *
 */
public class EMClassification extends CompositeStage implements IClassificationStage {
        private static final Logger LOGGER = LoggerFactory.getLogger(EMClassification.class);
	    private InputPort<UserSession> sessionInputPort;
	    private InputPort<Long> timerInputPort;
	    private OutputPort<BehaviorModel> outputPort;

		@Override
		public void setupStage(final Configuration configuration) throws ConfigurationException {
	        /** Get keep time for user sessions*/
	        final long keepTime = configuration.getLongProperty(ConfigurationKeys.KEEP_TIME, -1);
	        if (keepTime < 0) {
	        	EMClassification.LOGGER.error("Initialization incomplete: No keep time interval specified.");
	            throw new ConfigurationException("Initialization incomplete: No keep time interval specified.");
	        }
	        
	        final int minCollectionSize = configuration.getIntProperty(ConfigurationKeys.MIN_SIZE, -1);
	        if (minCollectionSize < 0) {
	        	EMClassification.LOGGER.error("Initialization incomplete: No min size for user sessions specified.");
	            throw new ConfigurationException("Initialization incomplete: No min size for user sessions specified.");
	        }
			
	        final boolean keepEmptyTransitions = configuration.getBooleanProperty(ConfigurationKeys.KEEP_EMPTY_TRANS, true);
	        
	        /** Todo: incoperate to config */
			final IRepresentativeStrategy representativeStrategy = new JPetstoreStrategy();
			
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