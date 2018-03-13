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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.timer.ITimeSource;

/**
 * Abstract class usable for all deployment context listeners (Java Servlet).
 *
 * @author Reiner Jung
 *
 */
public abstract class AbstractDeploymentContextListener implements ServletContextListener {

    /** deployment id constant. */
    protected static final String DEPLOYMENT_ID = "deploymentId";

    /** Kieker monitoring controller. */
    protected final IMonitoringController monitoringCtrl = MonitoringController.getInstance();
    /** Kieker time source. */
    protected final ITimeSource timeSource = this.monitoringCtrl.getTimeSource();

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        if (this.monitoringCtrl.isMonitoringEnabled()) {
            // if (this.monitoringCtrl.isProbeActivated(signature)) {
            this.triggerDeployedEvent(event);
            // }
        }
    }

    /**
     * Collect data and send monitoring event.
     *
     * @param event
     *            the triggering event
     */
    protected abstract void triggerDeployedEvent(ServletContextEvent event);

    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        if (this.monitoringCtrl.isMonitoringEnabled()) {
            // if (this.monitoringCtrl.isProbeActivated(signature)) {
            this.triggerUndeployedEvent(event);
            // }
        }
    }

    /**
     * Collect data and send monitoring event.
     *
     * @param event
     *            the triggering event
     */
    protected abstract void triggerUndeployedEvent(ServletContextEvent event);

}
