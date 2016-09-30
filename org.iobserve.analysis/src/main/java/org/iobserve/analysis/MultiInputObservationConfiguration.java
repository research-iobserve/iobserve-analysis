/***************************************************************************
 * Copyright 2016 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis;

import org.iobserve.analysis.model.ModelProviderPlatform;

import teetime.stage.io.network.TcpReaderStage;

/**
 * Configuration prepared to handle multiple TCP input streams.
 *
 * @author Reiner Jung
 *
 */
public class MultiInputObservationConfiguration extends AbstractObservationConfiguration {

    private static final int CAPACITY = 1024 * 1024;

    /**
     * Construct an analysis for multiple TCP inputs.
     *
     * @param inputPort
     *            the input port where the analysis is listening
     * @param platform
     *            the platform model handler
     */
    public MultiInputObservationConfiguration(final int inputPort, final ModelProviderPlatform platform) {
        super(platform);

        // TODO we need a multi input reader (issue exists with TeeTime)

        final TcpReaderStage reader = new TcpReaderStage(inputPort, MultiInputObservationConfiguration.CAPACITY,
                inputPort + 1);
        this.connectPorts(reader.getOutputPort(), this.recordSwitch.getInputPort());
    }

}
