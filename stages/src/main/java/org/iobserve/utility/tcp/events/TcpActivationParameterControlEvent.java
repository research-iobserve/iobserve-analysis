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

import java.util.List;
import java.util.Map;

/**
 * Represents an event for the activation of a probe via TCP. Additionally transfers paramters.
 *
 * @author Marc Adolf
 *
 */
public class TcpActivationParameterControlEvent extends TcpActivationControlEvent implements IParameterEvent {
    Map<String, List<String>> parameters;

    /**
     * Creates a complete parameter activation control event.
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
     *            a map of parameters and a list of each entry per parameter
     *
     */
    public TcpActivationParameterControlEvent(final String ip, final int port, final String hostname,
            final String pattern, final Map<String, List<String>> parameters) {
        super(ip, port, hostname, pattern);
        this.parameters = parameters;
    }

    /**
     * Creates a parameter activation control event without content except the pattern and
     * parameters.
     *
     * @param pattern
     *            The pattern of the method that should be monitored.
     * @param parameters
     *            a map of parameters and a list of each entry per parameter
     */
    public TcpActivationParameterControlEvent(final String pattern, final Map<String, List<String>> parameters) {
        super(pattern);
        this.parameters = parameters;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.utility.tcp.events.IParameterEvent#getParameters()
     */
    @Override
    public Map<String, List<String>> getParameters() {
        return this.parameters;
    }

}
