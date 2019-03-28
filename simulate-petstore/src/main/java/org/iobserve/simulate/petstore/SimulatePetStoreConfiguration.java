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
package org.iobserve.simulate.petstore;

import java.util.List;

import com.beust.jcommander.Parameter;

import org.iobserve.common.record.ISOCountryCode;

/**
 * @author Reiner Jung
 *
 */
public class SimulatePetStoreConfiguration {

    @Parameter(names = { "-a" }, description = "Number of accounting components", required = true)
    private Integer accounting;

    @Parameter(names = { "-l",
            "-locations" }, description = "Alternating locations", required = true, variableArity = true, converter = CountryCodeConverter.class)
    private List<ISOCountryCode> locations;

    @Parameter(names = { "-d", "--delay" }, description = "Delay between redeployments", required = true)
    private long delay;

    @Parameter(names = { "-i", "--iterations" }, description = "Iterations of redeployments", required = true)
    private int iterations;

    @Parameter(names = { "-p",
            "--port" }, description = "Listening port for service configuration changes", required = false)
    private Integer port;

    public final Integer getAccounting() {
        return this.accounting;
    }

    public final List<ISOCountryCode> getLocations() {
        return this.locations;
    }

    public final long getDelay() {
        return this.delay;
    }

    public final int getIterations() {
        return this.iterations;
    }

    public final Integer getPort() {
        return this.port;
    }

}
