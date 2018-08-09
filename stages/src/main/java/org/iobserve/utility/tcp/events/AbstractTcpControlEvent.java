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
package org.iobserve.utility.tcp.events;

/**
 * Contains the information needed to control a certain probe via TCP.
 *
 * @author Marc Adolf
 *
 */
public abstract class AbstractTcpControlEvent {

    private String ip;
    private int port;
    private String hostname;

    private final String pattern;

    /**
     * Creates a complete control event.
     *
     * @param ip
     *            Address of the monitored application.
     * @param port
     *            Port of the TCP controller.
     * @param hostname
     *            The name of the component which is using this IP and port.
     * @param pattern
     *            The pattern of the method that should be monitored.
     */
    public AbstractTcpControlEvent(final String ip, final int port, final String hostname, final String pattern) {
        this(pattern);
        this.ip = ip;
        this.port = port;
        this.hostname = hostname;
    }

    /**
     * Creates a new control event without content except the pattern.
     *
     * @param pattern
     *            The pattern of the method that should be monitored.
     */
    public AbstractTcpControlEvent(final String pattern) {
        this.pattern = pattern;
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    public String getHostname() {
        return this.hostname;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }

}
