/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.runtime.reconfigurator;

import java.net.Inet4Address;

import teetime.framework.Configuration;

import org.iobserve.stages.tcp.ProbeControlFilter;
import org.iobserve.utility.tcp.DummyProbeController;
import org.iobserve.utility.tcp.IProbeController;
import org.iobserve.utility.tcp.TcpProbeController;

/**
 * @author Reiner Jung
 *
 */
public class ReconfiguratorConfiguration extends Configuration {

    public ReconfiguratorConfiguration(final ReconfiguratorSettings parameterConfiguration) {
        final GenerateConfiguration generate = new GenerateConfiguration(parameterConfiguration.getHost(),
                parameterConfiguration.getPort(), this.convert(parameterConfiguration.getWhiteStart()),
                this.convert(parameterConfiguration.getWhiteEnd()),
                this.convert(parameterConfiguration.getBlackStart()),
                this.convert(parameterConfiguration.getBlackEnd()));

        IProbeController controller;
        if (parameterConfiguration.getPort() == null) {
            controller = new DummyProbeController();
        } else {
            controller = new TcpProbeController();
        }

        final ProbeControlFilter probeController = new ProbeControlFilter(controller);

        this.connectPorts(generate.getOutputPort(), probeController.getInputPort());
    }

    private long convert(final Inet4Address address) {
        final byte[] byteAddress = address.getAddress();
        long result = this.fixValue(byteAddress[0]);
        result = result << 8;
        result += this.fixValue(byteAddress[1]);
        result = result << 8;
        result += this.fixValue(byteAddress[2]);
        result = result << 8;
        result += this.fixValue(byteAddress[3]);

        return result;
    }

    private long fixValue(final byte value) {
        if (value < 0) {
            return 256 + value;
        } else {
            return value;
        }
    }

}
