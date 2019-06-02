package org.iobserve.service.behavior.analysis;

import java.util.List;

import mtree.MTree;
import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

public class OpticsStage<T> extends AbstractStage {

    private final OutputPort<List<List<T>>> outputPort = this.createOutputPort();
    private final InputPort<MTree<T>> mTreeInputPort = this.createInputPort();
    private final InputPort<List<T>> modelsInputPort = this.createInputPort();

    private final double maxDistance;
    private final int minPTs;

    public OpticsStage(final double maxDistance, final int minPTs) {
        this.minPTs = minPTs;
        this.maxDistance = maxDistance;
    }

    @Override
    protected void execute() throws Exception {
        final MTree<T> mtree = this.mTreeInputPort.receive();
        final List<T> models = this.modelsInputPort.receive();

    }

    public OutputPort<List<List<T>>> getOutputPort() {
        return this.outputPort;
    }

    public InputPort<MTree<T>> getmTreeInputPort() {
        return this.mTreeInputPort;
    }

    public InputPort<List<T>> getModelsInputPort() {
        return this.modelsInputPort;
    }

}
