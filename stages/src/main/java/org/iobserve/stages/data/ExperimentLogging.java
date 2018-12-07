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
import org.iobserve.common.record.ObservationPoint;
import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;

/**
 * @author Reiner Jung
 *
 */
public class ExperimentLogging {

    private static IMonitoringController CTRL = MonitoringController.getInstance();
    private static ITimeSource timeSource = ExperimentLogging.CTRL.getTimeSource();

    public static void logEvent(final long id, final EventTypes type, final ObservationPoint point) {
        ExperimentLogging.CTRL.newMonitoringRecord(
                new MeasureEventOccurance(ExperimentLogging.timeSource.getTime(), id, type, point));
    }

    public static void measure(final IEvent event, final ObservationPoint point) {
        if (event instanceof IDeploymentChange) {
            if (event instanceof IDeployedEvent) {
                ExperimentLogging.logEvent(event.getTimestamp(), EventTypes.DEPLOYMENT, point);
            } else if (event instanceof IUndeployedEvent) {
                ExperimentLogging.logEvent(event.getTimestamp(), EventTypes.UNDEPLOYMENT, point);
            }
        }
    }

    public static void measure(final AbstractTcpControlEvent event, final ObservationPoint point) {
        ExperimentLogging.logEvent(event.getTriggerTimestamp(), EventTypes.NONE, point);
    }

}
