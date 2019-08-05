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
package org.iobserve.service.behavior.analysis.clustering;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class ClusteringCompositeStage extends CompositeStage {

    private final InputPort<BehaviorModelGED> modelInputPort;
    private final InputPort<Long> timerInputPort;
    private final OutputPort<Clustering> outputPort;

    public ClusteringCompositeStage(final kieker.common.configuration.Configuration configuration) {

        GraphEditDistance.setConfiguration(configuration);

        final double clusteringDistance = configuration.getDoubleProperty(ConfigurationKeys.EPSILON, 10);

        final int minPts = configuration.getIntProperty(ConfigurationKeys.MIN_PTS, 20);

        final int maxAmount = configuration.getIntProperty(ConfigurationKeys.MAX_MODEL_AMOUNT, -1);

        final BehaviorModelToOpticsDataConverter modelToOptics = new BehaviorModelToOpticsDataConverter();

        final OpticsStage optics = new OpticsStage(clusteringDistance, minPts);

        final MTreeGenerator<OpticsData> mTreeGenerator = new MTreeGenerator<>(OpticsData.getDistanceFunction());

        final DataCollector<OpticsData> collector;

        if (maxAmount != -1) {
            collector = new DataCollector<>(maxAmount);

        } else {
            collector = new DataCollector<>();
        }

        final ExtractDBScanClusters clustering = new ExtractDBScanClusters(clusteringDistance);

        this.modelInputPort = this.createInputPort(modelToOptics.getInputPort());

        this.timerInputPort = this.createInputPort(collector.getTimeTriggerInputPort());

        this.connectPorts(modelToOptics.getOutputPort(), collector.getDataInputPort());

        this.connectPorts(collector.getmTreeOutputPort(), mTreeGenerator.getInputPort());

        this.connectPorts(mTreeGenerator.getOutputPort(), optics.getmTreeInputPort());

        this.connectPorts(collector.getOpticsOutputPort(), optics.getModelsInputPort());

        this.connectPorts(optics.getOutputPort(), clustering.getInputPort());

        this.outputPort = this.createOutputPort(clustering.getOutputPort());

    }

    public InputPort<BehaviorModelGED> getModelInputPort() {
        return this.modelInputPort;
    }

    public InputPort<Long> getTimerInputPort() {
        return this.timerInputPort;
    }

    public OutputPort<Clustering> getOutputPort() {
        return this.outputPort;
    }

}
