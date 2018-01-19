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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.iobserve.common.record.Privacy_ServletDeployedEvent;
import org.iobserve.common.record.ServletUndeployedEvent;

/**
 * Collects information on deployment and send deployment and geolocation data. On undeployment only
 * the undeployment request is send.
 *
 * @author Reiner Jung
 *
 */
public class DeploymentGeolocationContextListener extends AbstractDeploymentContextListener {

    public static final String COUNTRY_CODE = "countryCode";

    @Override
    protected void triggerDeployedEvent(final ServletContextEvent event) {
        final ServletContext servletContext = event.getServletContext();
        final String service = servletContext.getVirtualServerName();
        final String context = servletContext.getServletContextName();

        final String deploymentId = servletContext.getInitParameter(AbstractDeploymentContextListener.DEPLOYMENT_ID);
        final String countryCode = servletContext.getInitParameter(DeploymentGeolocationContextListener.COUNTRY_CODE);

        this.monitoringCtrl.newMonitoringRecord(new Privacy_ServletDeployedEvent(this.timeSource.getTime(), service,
                context, deploymentId, Short.valueOf(countryCode)));
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
