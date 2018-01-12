/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.stages.general;

import java.util.ArrayList;
import java.util.List;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * Accepts events from multiple inputs and sends them through one output.
 *
 * @author Reiner Jung
 *
 * @param <T>
 *            type for input and output ports
 */
public class AggregateEventStage<T> extends AbstractStage {

    private final List<InputPort<T>> inputPorts = new ArrayList<>();
    private OutputPort<T> outputPort;

    /**
     * Create an aggregation stages.
     *
     * @param ports
     *            number of input ports
     */
    public AggregateEventStage(final int ports) {
        for (int i = 0; i < ports; i++) {
            this.inputPorts.add(this.createInputPort());
        }
    }

    @Override
    protected void execute() throws Exception {
        for (final InputPort<T> input : this.inputPorts) {
            final T event = input.receive();
            if (event != null) {
                this.outputPort.send(event);
            }
        }
    }

    /**
     * Get one of the input ports.
     *
     * @param port
     *            port number must be between 0 and the lower than the value specified in ports.
     *
     * @return the port associated with the port number or null on error.
     */
    public InputPort<T> getInputPort(final int port) {
        if (port >= 0 && port < this.inputPorts.size()) {
            return this.inputPorts.get(port);
        } else {
            return null;
        }
    }

    /**
     * Output port.
     *
     * @return returns the output port
     */
    public OutputPort<T> getOutputPort() {
        return this.outputPort;
    }

}
