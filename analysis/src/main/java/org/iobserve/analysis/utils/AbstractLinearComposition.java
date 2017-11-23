package org.iobserve.analysis.utils;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * TODO add description.
 *
 * @author unknown
 *
 * @param <I>
 * @param <O>
 */
public abstract class AbstractLinearComposition<I, O> extends CompositeStage {

    /** composite input port */
    private final InputPort<I> inputPort;
    /** composite output port, which sends whether privacy is violated */
    private final OutputPort<O> outputPort;

    public AbstractLinearComposition(final InputPort<I> inputPort, final OutputPort<O> outputPort) {
        this.inputPort = inputPort;
        this.outputPort = outputPort;
    }

    /**
     * @return the input port for the complete composite stage
     */
    public InputPort<I> getInputPort() {
        return this.inputPort;
    }

    /**
     * @return the output port for the complete composite stage
     */
    public OutputPort<O> getOutputPort() {
        return this.outputPort;
    }

}
