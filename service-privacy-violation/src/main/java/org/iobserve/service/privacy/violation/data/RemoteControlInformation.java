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
package org.iobserve.service.privacy.violation.data;

/**
 * Contains the information to remotely access the (tcp) probe controller of one component.
 *
 * @author Marc Adolf
 *
 */
public class RemoteControlInformation {

    private final String ip;
    private final int port;
    private final String hostname;

    /**
     * Create a new set of remote control information.
     *
     * @param ip
     *            The IP of the component.
     * @param port
     *            The port used by the probe controller of the component.
     * @param hostname
     *            The hostname of the component.
     */
    public RemoteControlInformation(final String ip, final int port, final String hostname) {
        this.ip = ip;
        this.port = port;
        this.hostname = hostname;
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

}
