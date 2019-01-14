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
package org.iobserve.simulate.petstore.data;

import org.iobserve.common.record.ISOCountryCode;

/**
 * @author Reiner Jung
 *
 */
public class Service {

    private final String name;
    private final String ip;
    private ISOCountryCode country;

    /**
     * A new service object.
     *
     * @param name
     *            name of the service
     * @param ip
     *            ip of the service
     * @param country
     *            country where the service is running
     */
    public Service(final String name, final String ip, final ISOCountryCode country) {
        this.name = name;
        this.ip = ip;
        this.country = country;
    }

    public final String getName() {
        return this.name;
    }

    public final String getIp() {
        return this.ip;
    }

    public final ISOCountryCode getCountry() {
        return this.country;
    }

    public void setCountry(final ISOCountryCode country) {
        this.country = country;
    }

}
