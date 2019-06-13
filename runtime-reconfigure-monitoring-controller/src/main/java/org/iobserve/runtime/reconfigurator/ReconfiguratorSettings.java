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

import com.beust.jcommander.Parameter;

/**
 * @author Reiner Jung
 *
 */
public class ReconfiguratorSettings {

    @Parameter(names = { "-ws", "--whitelist-start" }, required = true, converter = ParameterIPConverter.class)
    private Inet4Address whiteStart;
    @Parameter(names = { "-we", "--whitelist-end" }, required = true, converter = ParameterIPConverter.class)
    private Inet4Address whiteEnd;

    @Parameter(names = { "-bs", "--blacklist-start" }, required = true, converter = ParameterIPConverter.class)
    private Inet4Address blackStart;
    @Parameter(names = { "-be", "--blacklist-end" }, required = true, converter = ParameterIPConverter.class)
    private Inet4Address blackEnd;

    @Parameter(names = { "-p", "--port" }, required = true)
    private Integer port;

    @Parameter(names = { "-h", "--host" }, required = true)
    private String host;

    public Inet4Address getWhiteStart() {
        return this.whiteStart;
    }

    public Inet4Address getWhiteEnd() {
        return this.whiteEnd;
    }

    public Inet4Address getBlackStart() {
        return this.blackStart;
    }

    public Inet4Address getBlackEnd() {
        return this.blackEnd;
    }

    public Integer getPort() {
        return this.port;
    }

    public String getHost() {
        return this.host;
    }
}
