package org.iobserve.service.behavior.analysis.clustering;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.service.behavior.analysis.BehaviorModelToOpticsDataConverter;
import org.iobserve.service.behavior.analysis.Clustering;
import org.iobserve.service.behavior.analysis.DataCollector;
import org.iobserve.service.behavior.analysis.ExtractClustersFromOptics;
import org.iobserve.service.behavior.analysis.MTreeGenerator;
import org.iobserve.service.behavior.analysis.OpticsData;
import org.iobserve.service.behavior.analysis.OpticsStage;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;

public class ClusteringCompositeStage extends CompositeStage {

    private final InputPort<BehaviorModelGED> modelInputPort;
    private final InputPort<Long> timerInputPort;
    private final OutputPort<Clustering> outputPort;

    public ClusteringCompositeStage() {

        final BehaviorModelToOpticsDataConverter modelToOptics = new BehaviorModelToOpticsDataConverter();

        final OpticsStage<OpticsData> optics = new OpticsStage<>(10.0, 10);

        final MTreeGenerator<OpticsData> mTreeGenerator = new MTreeGenerator<>(new OpticsData.OPTICSDataGED());

        final DataCollector<OpticsData> collector = new DataCollector<>();

        final ExtractClustersFromOptics clustering = new ExtractClustersFromOptics(10);

        this.modelInputPort = this.createInputPort(modelToOptics.getInputPort());

        this.timerInputPort = this.createInputPort(collector.getTimeTriggerInputPort());

        this.connectPorts(modelToOptics.getOutputPort(), collector.getDataInputPort());

        this.connectPorts(collector.getmTreeOutputPort(), mTreeGenerator.getInputPort());

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
