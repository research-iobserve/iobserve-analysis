/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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

import org.iobserve.common.record.ServletDeployedEvent;
import org.iobserve.common.record.ServletUndeployedEvent;

/**
 * This servlet listener allows to monitor the deployment and undeployment of a war file and its
 * containing servlets, which are seen as one component of an application.
 *
 * The listener produces either a {@link org.spp.iobserve.common.record.ServletDeployedEvent} or a
 * {@link org.spp.iobserve.common.record.ServletUndeployedEvent} record on deployment and
 * undeployment respectively. It requires an deploymentId value to identify the deployment which
 * must be added to the web.xml file.
 *
 * {@code <context-param>
 * 		<param-name>deploymentId</param-name>
 * 		<param-value>UNIQUE-IDENTIFIER</param-value>
 * 	</context-param>}
 *
 * @author Reiner Jung
 *
 */
public class DeploymentContextListener extends AbstractDeploymentContextListener {

    /**
     * initialize context listener.
     */
    public DeploymentContextListener() {
        // nothing to be done here
    }

    /**
     * Collect data and send monitoring event.
     *
     * @param event
     *            the triggering event
     */
    @Override
    protected void triggerDeployedEvent(final ServletContextEvent event) {
        final ServletContext servletContext = event.getServletContext();
        final String service = servletContext.getVirtualServerName();
        final String context = servletContext.getServletContextName();

        final String deploymentId = servletContext.getInitParameter(AbstractDeploymentContextListener.DEPLOYMENT_ID);
        this.monitoringCtrl.newMonitoringRecord(
                new ServletDeployedEvent(this.timeSource.getTime(), service, context, deploymentId));
    }

    /**
     * Collect data and send monitoring event.
     *
     * @param event
     *            the triggering event
     */
    @Override
    protected void triggerUndeployedEvent(final ServletContextEvent event) {
        final ServletContext servletContext = event.getServletContext();
        final String service = servletContext.getVirtualServerName();
        final String context = servletContext.getServletContextName();

        final String deploymentId = servletContext.getInitParameter(AbstractDeploymentContextListener.DEPLOYMENT_ID);
        this.monitoringCtrl.newMonitoringRecord(
                new ServletUndeployedEvent(this.timeSource.getTime(), service, context, deploymentId));
        // TODO remove if not necessary
        this.monitoringCtrl.terminateMonitoring();
    }

}
