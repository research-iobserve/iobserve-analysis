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

    /** composite input port. */
    private final InputPort<I> inputPort;
    /** composite output port, which sends whether privacy is violated. */
    private final OutputPort<O> outputPort;

    /**
     * Create an abstract linear composition.
     *
     * @param inputPort
     *            input port
     * @param outputPort
     *            output port
     */
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
