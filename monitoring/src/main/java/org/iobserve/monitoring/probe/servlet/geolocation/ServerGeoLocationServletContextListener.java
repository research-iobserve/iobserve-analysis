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
package org.iobserve.monitoring.probe.servlet.geolocation;

import org.iobserve.monitoring.sampler.geolocation.GeoLocationSamplerFactory;

import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.probe.servlet.AbstractRegularSamplingServletContextListener;

/**
 * <p>
 * Starts and stops the periodic logging of the JIT compilation. <br/>
 * The initial delay and the sampling period use the default values.
 *
 *
 * @author Philipp Weimann
 *
 * @since 1.30
 */
public class ServerGeoLocationServletContextListener extends AbstractRegularSamplingServletContextListener {

    /**
     * Empty Constructor.
     */
    public ServerGeoLocationServletContextListener() {

    }

    @Override
    protected String getContextParameterNameSamplingIntervalSeconds() {
        return "";
    }

    @Override
    protected String getContextParameterNameSamplingDelaySeconds() {
        return "";
    }

    @Override
    protected ISampler[] createSamplers() {
        return new ISampler[] { GeoLocationSamplerFactory.getDummyGeoLocationSampler() };
    }

}
