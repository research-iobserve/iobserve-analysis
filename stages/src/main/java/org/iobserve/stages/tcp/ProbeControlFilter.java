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

import org.iobserve.utility.tcp.RemoteControlFailedException;
import org.iobserve.utility.tcp.TcpProbeController;
import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;

import teetime.framework.AbstractConsumerStage;

/**
 * @author Marc Adolf
 *
 */
public class ProbeControlFilter extends AbstractConsumerStage<AbstractTcpControlEvent> {
    // TODO data type for transmitting information what should be activated and what deactivated
    // TODO add Probecontroller (maybe make Probecontroller singleton?)
    private final TcpProbeController probeController;

    /**
     * Iniatews the filter with a new {@link TcpProbeController}.
     */
    public ProbeControlFilter() {
        this.probeController = new TcpProbeController();
    }

    @Override
    protected void execute(final AbstractTcpControlEvent event) {
        // TODO error handling
        try {
            this.probeController.controlProbe(event);
        } catch (final RemoteControlFailedException e) {
            // TODO Auto-generated catch block
        }
    }

}
