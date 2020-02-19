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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teetime.framework.AbstractProducerStage;

import org.iobserve.monitoring.probe.servlet.EListType;
import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;
import org.iobserve.utility.tcp.events.TcpActivationParameterControlEvent;

/**
 * @author Reiner Jung
 *
 */
public class GenerateConfigurationStage extends AbstractProducerStage<AbstractTcpControlEvent> {

    private final long blackStart;
    private final long blackEnd;
    private final long whiteStart;
    private final long whiteEnd;
    private final String host;
    private final int port;
    private final List<String> whitelist;

    private final String[] operations = new String[] { "/jpetstore-account/request-user",
            "/jpetstore-account/request-user", "/jpetstore-account/update-account",
            "/jpetstore-account/insert-account" };

    private final String[][] parameters = new String[][] { { "username", "password" }, { "username" }, { "Object" },
            { "Object" } };

    public GenerateConfigurationStage(final String host, final int port, final List<String> whitelist, final long whiteStart,
            final long whiteEnd, final long blackStart, final long blackEnd) {
        this.host = host;
        this.port = port;
        this.whiteStart = whiteStart;
        this.whiteEnd = whiteEnd;
        this.whitelist = whitelist;
        this.blackStart = blackStart;
        this.blackEnd = blackEnd;
    }

    @Override
    protected void execute() throws Exception {
        AbstractTcpControlEvent element;

        final String hostname = "account-service";

        for (int i = 0; i < this.operations.length; i++) {
            final String operationSignature = this.createSignaturePattern(this.operations[i], this.parameters[i]);
            final long triggerTimestamp = 0;
            final Map<String, List<String>> parameters = new HashMap<>();
            final List<String> blacklist = new ArrayList<>();

            this.populateList(blacklist, this.blackStart, this.blackEnd);

            parameters.put(EListType.BLACKLIST.name(), blacklist);

            final List<String> completeWhitelist = new ArrayList<>(this.whitelist);

            completeWhitelist.add(this.ipv4Generator(127, 0, 0, 1));
            this.populateList(blacklist, this.whiteStart, this.whiteEnd);

            parameters.put(EListType.WHITELIST.name(), completeWhitelist);

            element = new TcpActivationParameterControlEvent(this.host, this.port, hostname, operationSignature,
                    triggerTimestamp, parameters);

            this.outputPort.send(element);
        }
        this.workCompleted();
    }

    private String createSignaturePattern(final String uri, final String[] parameters) {
        String parameterSequence = "";
        for (final String parameter : parameters) {
            if ("".equals(parameterSequence)) {
                parameterSequence = parameter;
            } else {
                parameterSequence += ", " + parameter;
            }
        }
        return String.format("%s (%s)", uri, parameterSequence);
    }

    private void populateList(final List<String> list, final long start, final long end) {
        for (long i = start; i <= end; i++) {
            final long d = i % 256;
            final long cLevel = i / 256;
            final long c = cLevel % 256;
            final long bLevel = cLevel / 256;
            final long b = bLevel % 256;
            final long a = bLevel / 256;
            list.add(this.ipv4Generator(a, b, c, d));
        }

    }

    private String ipv4Generator(final long a, final long b, final long c, final long d) {
        return String.format("%d.%d.%d.%d", a, b, c, d);
    }

}
