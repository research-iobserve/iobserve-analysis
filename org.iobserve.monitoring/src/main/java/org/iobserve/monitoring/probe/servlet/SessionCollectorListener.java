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

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.iobserve.common.record.SessionEndEvent;
import org.iobserve.common.record.SessionStartEvent;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.timer.ITimeSource;

/**
 * The listener triggers events for session creation and destruction.
 * 
 * <web-app ...>
 * 	<listener>
 *		<listener-class>rg.iobserve.monitoring.probe.servlet.SessionCollectorListener</listener-class>
 * 	</listener>
 * </web-app>
 * 
 * @author Reiner Jung
 *
 */
public class SessionCollectorListener implements HttpSessionListener {
	
	/** Kieker monitoring controller. */
    private final IMonitoringController monitoringCtrl = MonitoringController.getInstance();
    /** Kieker time source. */
    private final ITimeSource timeSource = this.monitoringCtrl.getTimeSource();

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		if (this.monitoringCtrl.isMonitoringEnabled()) {
            final String session = event.getSession().getId();
            final long time = this.timeSource.getTime();

            // if (this.monitoringCtrl.isProbeActivated(signature)) {
            this.monitoringCtrl.newMonitoringRecord(new SessionStartEvent(time, session));
            // }
        }
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		if (this.monitoringCtrl.isMonitoringEnabled()) {
			final String session = event.getSession().getId();
            final long time = this.timeSource.getTime();

            // if (this.monitoringCtrl.isProbeActivated(signature)) {
            this.monitoringCtrl.newMonitoringRecord(new SessionEndEvent(time, session));
            // }
        }
	}

}
