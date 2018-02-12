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
 * Represents an event for the deactivation of a probe via TCP.
 *
 * @author Marc Adolf
 *
 */
public class TcpDeactivationEvent extends AbstractTcpControlEvent {

    /**
     * Creates a complete deactivation control event.
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
    public TcpDeactivationEvent(final String ip, final int port, final String hostname, final String pattern) {
        super(ip, port, hostname, pattern);
    }

}
