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
package org.iobserve.stages.tcp;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.stages.data.Alarms;
import org.iobserve.stages.data.IErrorMessages;
import org.iobserve.utility.tcp.RemoteControlFailedException;
import org.iobserve.utility.tcp.TcpProbeController;
import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;

/**
 * Receives {@link AbstractTcpControlEvent control events}, processes them and in case of error
 * sends the error message to an error sink.
 *
 * @author Marc Adolf
 *
 */
public class ProbeControlFilter extends AbstractConsumerStage<AbstractTcpControlEvent> {
    private final TcpProbeController probeController;
    private final OutputPort<IErrorMessages> outputPort = this.createOutputPort();

    /**
     * Initiates the filter with a new {@link TcpProbeController}.
     */
    public ProbeControlFilter() {
        this.probeController = new TcpProbeController();
    }

    @Override
    protected void execute(final AbstractTcpControlEvent event) {
        try {
            this.probeController.controlProbe(event);
        } catch (final RemoteControlFailedException e) {
            final String alarmMessage = "could not send probe control event " + e.getMessage();
            final Alarms alarms = new Alarms();
            alarms.addMessage(alarmMessage);
            this.outputPort.send(alarms);
        }
    }

    public OutputPort<IErrorMessages> getOutputPort() {
        return this.outputPort;
    }

}
