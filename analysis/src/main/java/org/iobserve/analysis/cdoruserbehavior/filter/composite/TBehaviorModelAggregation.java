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
package org.iobserve.analysis.cdoruserbehavior.filter.composite;

import org.iobserve.analysis.cdoruserbehavior.clustering.ExpectationMaximizationClustering;
import org.iobserve.analysis.cdoruserbehavior.filter.TBehaviorModelCreation;
import org.iobserve.analysis.cdoruserbehavior.filter.TBehaviorModelVisualization;
import org.iobserve.analysis.cdoruserbehavior.filter.TVectorQuantizationClustering;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.BehaviorModelConfiguration;
import org.iobserve.analysis.filter.writer.BehaviorModelWriter;
import org.iobserve.analysis.filter.writer.AbstractModelOutputFilter;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import weka.core.Instances;

/**
 *
 * @author Christoph Dornieden
 */
public class TBehaviorModelAggregation extends CompositeStage {
    /** logger. */
    private static final Log LOG = LogFactory.getLog(TBehaviorModelAggregation.class);
    private TVectorQuantizationClustering tClustering;
    private final TBehaviorModelCreation tBehaviorModelCreation;

    private final BehaviorModelConfiguration configuration;
	private EMClusteringProcess emClustering;

    /**
     * Constructor configuration of the aggregation filters.
     */
    public TBehaviorModelAggregation(final BehaviorModelConfiguration configuration) {
        this.configuration = configuration;
        
        this.tBehaviorModelCreation = new TBehaviorModelCreation(configuration.getNamePrefix());
        
        switch (configuration.getAggregationType()) {
        case XMeansClustering : {
        	this.tClustering = new TVectorQuantizationClustering(this.configuration.getClustering());
        	this.connectPorts(this.tClustering.getOutputPort(), this.tBehaviorModelCreation.getInputPort());
        	break;
        }
        case EMClustering: {
        	this.emClustering = new EMClusteringProcess(new ExpectationMaximizationClustering());
        	this.connectPorts(this.emClustering.getOutputPort(), this.tBehaviorModelCreation.getInputPort());
        	break;
        }
        default:
        	LOG.error("Unknown clustering method " + configuration.getAggregationType());
        }
               
        /** visualization integration. */
        AbstractModelOutputFilter tIObserveUBM = null;
        switch(configuration.getOutputMode()) {
        case UBM_VISUALIZATION: tIObserveUBM = new TBehaviorModelVisualization(configuration.getVisualizationUrl(),
                configuration.getSignatureCreationStrategy());
        case FILE_OUTPUT: tIObserveUBM = new BehaviorModelWriter(configuration.getVisualizationUrl(),
                configuration.getSignatureCreationStrategy());
        default:
        	LOG.error("Unknown visualization method " + configuration.getOutputMode());
        }
   
        this.connectPorts(this.tBehaviorModelCreation.getOutputPort(), tIObserveUBM.getInputPort());
    }

    /**
     * getter
     *
     * @return input port
     */
    public InputPort<Instances> getInputPort() {
    	if (tClustering != null)
    		return this.tClustering.getInputPort();
    	else if (emClustering != null)
    		return this.emClustering.getInputPort();
    	else
    		return null;
    }

}
