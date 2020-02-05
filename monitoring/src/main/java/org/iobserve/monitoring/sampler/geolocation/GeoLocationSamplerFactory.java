/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.monitoring.sampler.geolocation;

/**
 * TODO .
 *
 * @author unknown
 *
 */
public final class GeoLocationSamplerFactory { // NOPMD its a factory

    private GeoLocationSamplerFactory() {
        // factory constructor
    }

    /**
     * @return ServerGeoLocationSampler with the DummyCountryInvestigator
     */
    public static AbstractGeoLocationSampler getDummyGeoLocationSampler() {
        return new ServerGeoLocationSampler(new DummyCountryInvestigator());
    }

}
