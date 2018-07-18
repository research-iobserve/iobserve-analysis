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
package org.iobserve.monitoring.probe.servlet;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.iobserve.common.record.ISOCountryCode;
import org.iobserve.common.record.Privacy_ServletDeployedEvent;
import org.iobserve.common.record.ServletUndeployedEvent;

/**
 * Collects information on deployment and send deployment and geolocation data. On undeployment only
 * the undeployment request is send. The probe checks for two parameters:
 *
 * <ul>
 * <li>deploymentId which should be an unique identifier for each deployment</li>
 * <li>countryCode which is an ISO 3166 country code</li>
 * </ul>
 *
 * In case the country code is missing a random country code is assigned.
 *
 * @author Reiner Jung
 *
 * @since 0.0.1
 */
public class DeploymentGeolocationContextListener extends AbstractDeploymentContextListener {

    public static final String COUNTRY_CODE = "countryCode";

    /**
     * Create an deployment geolocation context listener.
     */
    public DeploymentGeolocationContextListener() {
        // empty constructor
    }

    @Override
    protected void triggerDeployedEvent(final ServletContextEvent event) {
        final ServletContext servletContext = event.getServletContext();

        String service;
        try {
            final InetAddress address = InetAddress.getByName(servletContext.getVirtualServerName());
            service = address.getHostAddress();
        } catch (final UnknownHostException e) {
            service = servletContext.getVirtualServerName();
        }

        final String context = servletContext.getServletContextName();

        final String deploymentId = servletContext.getInitParameter(AbstractDeploymentContextListener.DEPLOYMENT_ID);
        final String countryCodeNumber = servletContext
                .getInitParameter(DeploymentGeolocationContextListener.COUNTRY_CODE);

        final ISOCountryCode countryCode;

        if (countryCodeNumber == null) {
            final ISOCountryCode[] values = ISOCountryCode.values();
            countryCode = values[(int) (Math.random() * values.length)];
        } else {
            countryCode = ISOCountryCode.getEnum(Short.valueOf(countryCodeNumber));
        }

        this.monitoringCtrl.newMonitoringRecord(new Privacy_ServletDeployedEvent(this.timeSource.getTime(), service,
                context, deploymentId, countryCode));
    }

    @Override
    protected void triggerUndeployedEvent(final ServletContextEvent event) {
        final ServletContext servletContext = event.getServletContext();
        final String service = servletContext.getVirtualServerName();
        final String context = servletContext.getServletContextName();

        final String deploymentId = servletContext.getInitParameter(AbstractDeploymentContextListener.DEPLOYMENT_ID);
        this.monitoringCtrl.newMonitoringRecord(
                new ServletUndeployedEvent(this.timeSource.getTime(), service, context, deploymentId));
    }

}
