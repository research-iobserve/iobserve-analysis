package org.iobserve.service.behavior.analysis.clustering;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;

public class ClusteringCompositeStage extends CompositeStage {

    private final InputPort<BehaviorModelGED> modelInputPort;
    private final InputPort<Long> timerInputPort;
    private final OutputPort<Clustering> outputPort;

    public ClusteringCompositeStage() {
        this(false, 0);
    }

    public ClusteringCompositeStage(final boolean hasMaxAmount, final int maxAmount) {

        final double clusteringDistance = 40;

        final BehaviorModelToOpticsDataConverter modelToOptics = new BehaviorModelToOpticsDataConverter();

        final OpticsStage optics = new OpticsStage(clusteringDistance, 20);

        final MTreeGenerator<OpticsData> mTreeGenerator = new MTreeGenerator<>(new OpticsData.OPTICSDataGED());

        DataCollector<OpticsData> collector;
        if (hasMaxAmount) {
            collector = new DataCollector<>(maxAmount);

        } else {
            collector = new DataCollector<>();
        }

        final ExtractClustersFromOptics clustering = new ExtractClustersFromOptics(clusteringDistance);

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
