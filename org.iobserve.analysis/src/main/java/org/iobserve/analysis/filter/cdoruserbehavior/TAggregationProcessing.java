/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 ***************************************************************************/
package org.iobserve.analysis.filter.cdoruserbehavior;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import weka.core.Instances;

/**
*
* @author Christoph Dornieden
*/
public class TAggregationProcessing extends CompositeStage {
    /** logger. */
    private static final Log LOG = LogFactory.getLog(TAggregationProcessing.class);
    private final TClustering tClustering;
    
    
    
    public TAggregationProcessing(){
    	final int userGroups = 1;
    	final int variance = 1;
    	
    	this.tClustering = new TClustering(userGroups,variance);      	
    	
    	
    }
    
    
    
    /**
     * getter
     * 
     * @return input port
     */
    public InputPort<Instances> getInputPort(){
    	return tClustering.getInputPort();   	
    	
    }

}
