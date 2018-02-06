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
package org.iobserve.utility.tcp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.remotecontrol.ActivationEvent;
import kieker.common.record.remotecontrol.DeactivationEvent;
import kieker.monitoring.writer.tcp.ConnectionTimeoutException;
import kieker.monitoring.writer.tcp.SingleSocketTcpWriter;

/**
 * Controller to send remote control events for probes to given addresses. Establishes TCP
 * connections and keeps them open.
 *
 * @author Marc Adolf
 *
 */
public class TcpProbeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpProbeController.class);
    private static final int connTimeoutInMs = 100;
    /**
     * Saves already established connections, the key pattern is "hostname:port".
     */
    private final Map<String, SingleSocketTcpWriter> knownAddresses = new HashMap<>();

    public void activateMonitoredPattern(final String hostname, final int port, final String pattern)
            throws RemoteControlFailedException {
        this.sendTcpCommand(hostname, port, new ActivationEvent(pattern));
    }

    public void deactivateMonitoredPattern(final String hostname, final int port, final String pattern)
            throws RemoteControlFailedException {
        this.sendTcpCommand(hostname, port, new DeactivationEvent(pattern));
    }

    private void sendTcpCommand(final String hostname, final int port, final IMonitoringRecord monitoringRecord)
            throws RemoteControlFailedException {
        // TODO replace with IREMOTE ...
        final String writerKey = hostname + ":" + port;
        final SingleSocketTcpWriter tcpWriter;

        if (!this.knownAddresses.containsKey(writerKey)) {
            this.createNewTcpWriter(hostname, port);
        }

        tcpWriter = this.knownAddresses.get(writerKey);

        if (tcpWriter == null) {
            throw new RemoteControlFailedException("TCP Writer was not found");
        }
        tcpWriter.writeMonitoringRecord(monitoringRecord);
        if (TcpProbeController.LOGGER.isDebugEnabled()) {
            TcpProbeController.LOGGER.debug(
                    "Send record " + monitoringRecord.getClass().getName() + " to " + hostname + " on port: " + port);
        }
    }

    private SingleSocketTcpWriter createNewTcpWriter(final String hostname, final int port)
            throws RemoteControlFailedException {
        final Configuration configuration = new Configuration();

        configuration.setProperty(SingleSocketTcpWriter.CONFIG_HOSTNAME, hostname);
        configuration.setProperty(SingleSocketTcpWriter.CONFIG_PORT, port);
        configuration.setProperty(SingleSocketTcpWriter.CONFIG_CONN_TIMEOUT_IN_MS, TcpProbeController.connTimeoutInMs);
        configuration.setProperty(SingleSocketTcpWriter.CONFIG_FLUSH, true);
        configuration.setProperty(SingleSocketTcpWriter.CONFIG_BUFFERSIZE, 65535);
        final SingleSocketTcpWriter tcpWriter;
        try {
            tcpWriter = new SingleSocketTcpWriter(configuration);
            tcpWriter.onStarting();
            this.knownAddresses.put(hostname + ":" + port, tcpWriter);
        } catch (final ConnectionTimeoutException | IOException e) {
            // runtime exception is thrown after timeout
            if (TcpProbeController.LOGGER.isErrorEnabled()) {
                TcpProbeController.LOGGER.error("Could not create TCP connections to " + hostname + " on port " + port,
                        e);
            }
            throw new RemoteControlFailedException("Could not create TCP connections to " + hostname + " on port "
                    + port + ", writer was not created ");
        }
        return tcpWriter;
    }

    public boolean isKnownHost(final String hostname, final int port) {
        return this.knownAddresses.keySet().contains(hostname + ":" + port);
    }
}
