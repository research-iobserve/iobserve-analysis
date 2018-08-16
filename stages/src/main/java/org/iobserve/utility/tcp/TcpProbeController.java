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
import java.util.List;
import java.util.Map;

import kieker.common.configuration.Configuration;
import kieker.common.record.remotecontrol.ActivationEvent;
import kieker.common.record.remotecontrol.DeactivationEvent;
import kieker.common.record.remotecontrol.IRemoteControlEvent;
import kieker.monitoring.writer.tcp.ConnectionTimeoutException;
import kieker.monitoring.writer.tcp.SingleSocketTcpWriter;

import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;
import org.iobserve.utility.tcp.events.TcpActivationControlEvent;
import org.iobserve.utility.tcp.events.TcpActivationParameterControlEvent;
import org.iobserve.utility.tcp.events.TcpDeactivationControlEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller to send remote control events for probes to given addresses. Establishes TCP
 * connections and keeps them open.
 *
 * @author Marc Adolf
 *
 */
public class TcpProbeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpProbeController.class);
    private static final int CONN_TIMEOUT_IN_MS = 100;

    /**
     * Saves already established connections, the key pattern is "ip:port".
     */
    private final Map<String, TcpControlConnection> knownAddresses = new HashMap<>();

    /**
     * Create the probe controller.
     */
    public TcpProbeController() {
        // empty default constructor
    }

    /**
     * Convenience method for {@link AbstractControlEvent control events}.
     *
     * @param event
     *            The event that contains the information for remote control
     * @throws RemoteControlFailedException
     *             if the connection can not be established within a set timeout.
     */
    public void controlProbe(final AbstractTcpControlEvent event) throws RemoteControlFailedException {
        final String ip = event.getIp();
        final int port = event.getPort();
        final String hostname = event.getHostname();
        final String pattern = event.getPattern();
        if (event instanceof TcpActivationControlEvent) {
            if (event instanceof TcpActivationParameterControlEvent) {
                this.activateParameterMonitoredPattern(ip, port, hostname, pattern,
                        ((TcpActivationParameterControlEvent) event).getParameters());
            } else {
                this.activateMonitoredPattern(ip, port, hostname, pattern);
            }
        } else if (event instanceof TcpDeactivationControlEvent) {
            this.deactivateMonitoredPattern(ip, port, hostname, pattern);
        } else {
            if (TcpProbeController.LOGGER.isErrorEnabled()) {
                TcpProbeController.LOGGER.error("Received Unknown TCP control event: " + event.getClass().getName());
            }
        }

    }

    /**
     * Updates the given parameters for a probe.
     *
     * @param ip
     *            Address of the monitored application.
     * @param port
     *            Port of the TCP controller.
     * @param hostname
     *            The name of the component which is using this IP and port.
     * @param pattern
     *            The pattern of the method that should is monitored.
     * @param parameters
     *            The map of parameters to be set, the key is the name and the values the values for
     *            the parameter.
     * @throws RemoteControlFailedException
     *             if the connection can not be established within a set timeout.
     */
    public void updateProbeParameter(final String ip, final int port, final String hostname, final String pattern,
            final Map<String, List<String>> parameters) {
        final String[] parameterNames = (String[]) parameters.keySet().toArray();
        final String[][] parameterArray = this.computeParameterArray(parameters);

        // TODO add with new Kiekerversion
        // this.sendTcpCommand(ip, port, hostname, new
        // UpdateParameterEvent(pattern,parameterNames,parameterArray));
    }

    /**
     * Activates monitoring of a method (pattern) on one monitored application via TCP.
     *
     * @param ip
     *            Address of the monitored application.
     * @param port
     *            Port of the TCP controller.
     * @param hostname
     *            The name of the component which is using this IP and port.
     * @param pattern
     *            The pattern of the method that should be monitored.
     * @throws RemoteControlFailedException
     *             if the connection can not be established within a set timeout.
     */
    public void activateMonitoredPattern(final String ip, final int port, final String hostname, final String pattern)
            throws RemoteControlFailedException {
        this.sendTcpCommand(ip, port, hostname, new ActivationEvent(pattern));
    }

    /**
     * Activates monitoring of a method (pattern) on one monitored application via TCP and transfers
     * parameter.
     *
     * @param ip
     *            Address of the monitored application.
     * @param port
     *            Port of the TCP controller.
     * @param hostname
     *            The name of the component which is using this IP and port.
     * @param pattern
     *            The pattern of the method that should be monitored.
     * @param parameters
     *            The map of parameters to be set, the key is the name and the values the values for
     *            the parameter.
     * @throws RemoteControlFailedException
     *             if the connection can not be established within a set timeout.
     */
    public void activateParameterMonitoredPattern(final String ip, final int port, final String hostname,
            final String pattern, final Map<String, List<String>> parameters) {
        final String[] parameterNames = (String[]) parameters.keySet().toArray();
        final String[][] parameterArray = this.computeParameterArray(parameters);

        // TODO add with new Kiekerversion
        // this.sendTcpCommand(ip, port, hostname, new
        // ActivationParameterEvent(pattern,parameterNames,parameterArray));
    }

    /**
     * Deactivates monitoring of a method (pattern) on one monitored application via TCP.
     *
     * @param ip
     *            Address of the monitored application.
     * @param port
     *            Port of the TCP controller.
     * @param hostname
     *            The name of the component which is using this IP and port.
     * @param pattern
     *            The pattern of the method that should no longer be monitored.
     * @throws RemoteControlFailedException
     *             if the connection can not be established within a set timeout.
     */
    public void deactivateMonitoredPattern(final String ip, final int port, final String hostname, final String pattern)
            throws RemoteControlFailedException {
        this.sendTcpCommand(ip, port, hostname, new DeactivationEvent(pattern));
    }

    private void sendTcpCommand(final String ip, final int port, final String hostname,
            final IRemoteControlEvent monitoringRecord) throws RemoteControlFailedException {
        final String writerKey = ip + ":" + port;
        final SingleSocketTcpWriter tcpWriter;

        TcpControlConnection currentConnection = this.knownAddresses.get(writerKey);

        // if host was never used or an other module was there before, create a new connection
        if ((currentConnection == null) || (currentConnection.getHostname() != hostname)) {
            currentConnection = new TcpControlConnection(ip, port, hostname, this.createNewTcpWriter(ip, port));
            this.knownAddresses.put(writerKey, currentConnection);
        }
        tcpWriter = currentConnection.getTcpWriter();

        if (tcpWriter == null) {
            throw new RemoteControlFailedException("TCP Writer was not found");
        }
        // currently we have no means to check if the write process was successful or the channel is
        // still active
        tcpWriter.writeMonitoringRecord(monitoringRecord);
        if (TcpProbeController.LOGGER.isDebugEnabled()) {
            TcpProbeController.LOGGER
                    .debug("Send record " + monitoringRecord.getClass().getName() + " to " + ip + " on port: " + port);
        }
    }

    private SingleSocketTcpWriter createNewTcpWriter(final String hostname, final int port)
            throws RemoteControlFailedException {
        final Configuration configuration = new Configuration();

        configuration.setProperty(SingleSocketTcpWriter.CONFIG_HOSTNAME, hostname);
        configuration.setProperty(SingleSocketTcpWriter.CONFIG_PORT, port);
        configuration.setProperty(SingleSocketTcpWriter.CONFIG_CONN_TIMEOUT_IN_MS,
                TcpProbeController.CONN_TIMEOUT_IN_MS);
        configuration.setProperty(SingleSocketTcpWriter.CONFIG_FLUSH, true);
        configuration.setProperty(SingleSocketTcpWriter.CONFIG_BUFFERSIZE, 65535);
        final SingleSocketTcpWriter tcpWriter;
        try {
            tcpWriter = new SingleSocketTcpWriter(configuration);
            tcpWriter.onStarting();
        } catch (final IOException | ConnectionTimeoutException e) {
            // runtime exception is thrown after timeout
            if (TcpProbeController.LOGGER.isDebugEnabled()) {
                TcpProbeController.LOGGER.debug("Could not create TCP connections to " + hostname + " on port " + port,
                        e);
            }
            throw new RemoteControlFailedException("Could not create TCP connections to " + hostname + " on port "
                    + port + ", writer was not created ");
        }
        return tcpWriter;
    }

    /**
     * Checks if a host is known. The searched pattern is ip:port.
     *
     * @param ip
     *            the IP of the host.
     * @param port
     *            the used port of the TCP connections
     * @return true, if the connections to the host was already established.
     */
    public boolean isKnownHost(final String ip, final int port) {
        return this.knownAddresses.keySet().contains(ip + ":" + port);
    }

    private String[][] computeParameterArray(final Map<String, List<String>> parameters) {
        final String[] parameterNames = (String[]) parameters.keySet().toArray();

        final int parameterLength = parameterNames.length;
        final String[][] parameterArray = new String[parameterLength][];

        for (int i = 0; i < parameterLength; i++) {
            parameterArray[i] = (String[]) parameters.get(parameterNames[i]).toArray();
        }
        return parameterArray;
    }

}
