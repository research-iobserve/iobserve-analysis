package org.iobserve.service.behavior.analysis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mtree.DistanceFunction;
import mtree.MTree;
import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

public class MTreeGenerator<T> extends AbstractStage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MTreeGenerator.class);

    private final InputPort<List<T>> inputPort = this.createInputPort();

    private final OutputPort<MTree<T>> outputPort = this.createOutputPort();

    private final DistanceFunction<T> distanceFunction;
    private int minNodeCapacity = 50;
    private int maxNodeCapacity = 99;

    public MTreeGenerator(final DistanceFunction<T> distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    public OutputPort<MTree<T>> getOutputPort() {
        return this.outputPort;
    }

    public InputPort<List<T>> getInputPort() {
        return this.inputPort;
    }

    public int getMaxNodeCapacity() {
        return this.maxNodeCapacity;
    }

    public void setMaxNodeCapacity(final int maxNodeCapacity) {
        this.maxNodeCapacity = maxNodeCapacity;
    }

    public int getMinNodeCapacity() {
        return this.minNodeCapacity;
    }

    public void setMinNodeCapacity(final int minNodeCapacity) {
        this.minNodeCapacity = minNodeCapacity;
    }

    @Override
    protected void execute() throws Exception {

        final List<T> models = this.inputPort.receive();

        final MTree<T> mtree = new MTree<>(this.minNodeCapacity, this.maxNodeCapacity, this.distanceFunction, null);

        if (models != null) {
            MTreeGenerator.LOGGER.info("Receoved " + models.size() + " new models");
            for (final T model : models) {
                mtree.add(model);
            }

            this.outputPort.send(mtree);
            MTreeGenerator.LOGGER.info("Created MTree");

        } else {
            MTreeGenerator.LOGGER.warn("Receoved null as model list");
        }
    }

}
