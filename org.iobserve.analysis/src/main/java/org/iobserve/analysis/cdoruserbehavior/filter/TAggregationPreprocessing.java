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
package org.iobserve.analysis.cdoruserbehavior.filter;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.cdoruserbehavior.filter.models.EditableBehaviorModelTable;
import org.iobserve.analysis.cdoruserbehavior.filter.models.JPetstoreStrategy;
import org.iobserve.analysis.filter.RecordSwitch;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;
import teetime.stage.basic.merger.Merger;
import teetime.stage.basic.merger.strategy.SkippingBusyWaitingRoundRobinStrategy;
import weka.core.Instances;

/**
 *
 * @author Christoph Dornieden
 */

public class TAggregationPreprocessing extends CompositeStage {

    /** logger. */
    private static final Log LOG = LogFactory.getLog(TAggregationPreprocessing.class);

    private final Distributor<EntryCallSequenceModel> distributor;
    private final Merger<Object> merger;
    private final TBehaviorModelTableGeneration tBehaviorModelGeneration;
    private final TBehaviorModelPreperation tBehaviorModelPreperation;

    private final TInstanceTransformations tInstanceTransformations;

    /**
     * constructor
     */
    public TAggregationPreprocessing() {
        final List<String> filterBlackList = new ArrayList<>();

        final IDistributorStrategy strategy = new CopyByReferenceStrategy();
        this.distributor = new Distributor<>(strategy);

        this.merger = new Merger<>(new SkippingBusyWaitingRoundRobinStrategy());

        filterBlackList.add("(jpetstore\\.images).*\\)");
        filterBlackList.add("(jpetstore\\.css).*\\)");

        final EditableBehaviorModelTable modelTable = new EditableBehaviorModelTable(new JPetstoreStrategy(),
                filterBlackList, true);
        this.tBehaviorModelGeneration = new TBehaviorModelTableGeneration(modelTable);
        this.tBehaviorModelPreperation = new TBehaviorModelPreperation();

        this.tInstanceTransformations = new TInstanceTransformations();

        this.connectPorts(this.distributor.getNewOutputPort(), this.tBehaviorModelGeneration.getInputPort());
        this.connectPorts(this.distributor.getNewOutputPort(), this.merger.getNewInputPort());

        this.connectPorts(this.tBehaviorModelGeneration.getOutputPort(), this.merger.getNewInputPort());

        this.connectPorts(this.merger.getOutputPort(), this.tBehaviorModelPreperation.getInputPort());
        this.connectPorts(this.tBehaviorModelPreperation.getOutputPort(), this.tInstanceTransformations.getInputPort());

    }

    /**
     * getter
     *
     * @return input port
     */
    public InputPort<EntryCallSequenceModel> getInputPort() {
        return this.distributor.getInputPort();
    }
    
    /**
     * getter
     * 
     * @return outputPort
     */
    public OutputPort<Instances> getOutputPort(){
    	return tInstanceTransformations.getOutputPort();
    }
    

}
