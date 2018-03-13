package org.iobserve.analysis.clustering.filter.similaritymatching;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;

public class BehaviorModelCompositeSinkStage extends CompositeStage {
    InputPort<BehaviorModel[]> inputPort;

    public BehaviorModelCompositeSinkStage(final String baseURL) {
        final BehaviorModelDecollectorStage decolStage = new BehaviorModelDecollectorStage();
        final BehaviorModelSink writerStage = new BehaviorModelSink(baseURL, null);

        this.inputPort = decolStage.getInputPort();

        this.connectPorts(decolStage.getOutputPort(), writerStage.getInputPort());
    }

    public InputPort<BehaviorModel[]> getInputPort() {
        return this.inputPort;
    }
}
