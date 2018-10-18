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

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.sampler.ISampler;

import org.iobserve.common.record.ISOCountryCode;

/**
 * This is an abstract base for all geolocation sampler.
 *
 * @author Philipp Weimann
 *
 * @since 1.13
 */
public abstract class AbstractGeoLocationSampler implements ISampler {

    private final ICountryInvestigator countryInvestigator;

    /**
     * Create a geo location sampler.
     *
     * @param countryInvestigator
     *            set country for sampler
     */
    public AbstractGeoLocationSampler(final ICountryInvestigator countryInvestigator) {
        this.countryInvestigator = countryInvestigator;
    }

    /**
     * Perform one measurement with potential multiple records.
     *
     * @param monitoringController
     *            The monitoring controller for this probe.
     *
     * @throws Exception
     *             depending on the concrete sampler different exceptions can be raised
     */
    @Override
    public void sample(final IMonitoringController monitoringController) throws Exception {

        if (!monitoringController.isMonitoringEnabled()) {
            return;
        }

        final long timestamp = monitoringController.getTimeSource().getTime();
        final String hostname = monitoringController.getHostname();
        final ISOCountryCode countryCode = this.countryInvestigator.getServerGeoLocationCountry();

        final IMonitoringRecord geoLocationRecord = this.getGeoLocationRecord(timestamp, hostname, countryCode,
                monitoringController);

        monitoringController.newMonitoringRecord(geoLocationRecord);
    }

    /**
     * Abstract method used as interface to realize concrete samplers.
     *
     * @param timestamp
     *            the current time.
     * @param hostname
     *            the hostname of the machine where this measurement is performed
     * @param countryCode
     *            country code of the probe
     * @param monitoringCtr
     *            monitoring controller used in the measurement
     *
     * @return returns an a records containing the geolocation
     *
     * @throws Exception
     *             on error in monitoring component
     */
    public abstract IMonitoringRecord getGeoLocationRecord(final long timestamp, final String hostname,
            final ISOCountryCode countryCode, final IMonitoringController monitoringCtr) throws Exception;

}
