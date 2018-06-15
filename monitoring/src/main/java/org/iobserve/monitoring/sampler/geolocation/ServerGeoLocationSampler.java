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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;

import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.common.record.ServerGeoLocation;

/**
 * TODO .
 *
 * @author unknown
 *
 */
public class ServerGeoLocationSampler extends AbstractGeoLocationSampler {

    /**
     * Constructs a new {@link AbstractGeoLocationSampler}. Use GeoLocationSamplerFactory for
     * instance creation.
     *
     * @param countryInvestigator
     *            set sample value
     */
    public ServerGeoLocationSampler(final ICountryInvestigator countryInvestigator) {
        super(countryInvestigator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMonitoringRecord getGeoLocationRecord(final long timestamp, final String hostname,
            final ISOCountryCode countryCode, final IMonitoringController monitoringCtr) throws Exception {

        final Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        ServerGeoLocation record = null;

        while (netInterfaces.hasMoreElements() && record == null) {
            final String address = this.getCorrectIPAdress(netInterfaces.nextElement());
            if (address != null) {
                record = new ServerGeoLocation(timestamp, countryCode, hostname, address);
            }
        }

        if (record == null) {
            throw new UnknownHostException("It was not possible to determine an IP Adress");
        }
        return record;
    }

    /*
     * Iterates via all addresses in the NetIterface and selects the correct IP.
     */
    private String getCorrectIPAdress(final NetworkInterface netInterface) {
        String localNetworkAddress = null;

        final Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
        while (addresses.hasMoreElements() && localNetworkAddress == null) {
            final InetAddress address = addresses.nextElement();
            if (!address.isLoopbackAddress() && !address.isMulticastAddress() && !address.isAnyLocalAddress()) {
                localNetworkAddress = address.getHostAddress();
            }
        }

        return localNetworkAddress;
    }

}
