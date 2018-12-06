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
package org.iobserve.stages.data;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.timer.ITimeSource;

import org.iobserve.common.record.EventTypes;
import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.common.record.IDeploymentChange;
import org.iobserve.common.record.IEvent;
import org.iobserve.common.record.IUndeployedEvent;
import org.iobserve.common.record.MeasureEventOccurance;
import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;

/**
 * @author Reiner Jung
 *
 */
public class ExperimentLogging {

    private static IMonitoringController CTRL = MonitoringController.getInstance();
    private static ITimeSource timeSource = ExperimentLogging.CTRL.getTimeSource();

    public static void logEvent(final long id, final EventTypes type, final String label) {
        ExperimentLogging.CTRL.newMonitoringRecord(
                new MeasureEventOccurance(ExperimentLogging.timeSource.getTime(), id, type, label));
    }

    public static void measure(final IEvent event, final String label) {
        if (event instanceof IDeploymentChange) {
            if (event instanceof IDeployedEvent) {
                ExperimentLogging.logEvent(event.getTimestamp(), EventTypes.DEPLOYMENT, label);
            } else if (event instanceof IUndeployedEvent) {
                ExperimentLogging.logEvent(event.getTimestamp(), EventTypes.UNDEPLOYMENT, label);
            }
        }
    }

    public static void measure(final AbstractTcpControlEvent event, final String label) {
        ExperimentLogging.logEvent(0, EventTypes.NONE, label);
    }

}
