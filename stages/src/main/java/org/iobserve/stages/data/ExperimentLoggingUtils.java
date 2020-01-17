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

import java.util.concurrent.TimeUnit;

import kieker.common.record.flow.IEventRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.sampler.mxbean.MemorySampler;
import kieker.monitoring.sampler.oshi.IOshiSamplerFactory;
import kieker.monitoring.sampler.oshi.OshiSamplerFactory;
import kieker.monitoring.timer.ITimeSource;

import org.iobserve.common.record.EventTypes;
import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.common.record.IDeploymentChange;
import org.iobserve.common.record.IUndeployedEvent;
import org.iobserve.common.record.MeasureEventOccurance;
import org.iobserve.common.record.ObservationPoint;
import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;

/**
 * @author Reiner Jung
 *
 */
public final class ExperimentLoggingUtils {

    private static final IMonitoringController CTRL = MonitoringController.getInstance();
    private static final ITimeSource TIME_SOURCE = ExperimentLoggingUtils.CTRL.getTimeSource();

    private ExperimentLoggingUtils() {
    }

    /**
     * Log an experiment event.
     *
     * @param id
     *            event id aka event timestamp
     * @param type
     *            event type
     * @param point
     *            location
     */
    public static void logEvent(final long id, final EventTypes type, final ObservationPoint point) {
        ExperimentLoggingUtils.CTRL.newMonitoringRecord(
                new MeasureEventOccurance(ExperimentLoggingUtils.TIME_SOURCE.getTime(), id, type, point));
    }

    /**
     * Measure a deployment/undeployment event.
     *
     * @param event
     *            the event
     * @param point
     *            measurement location
     */
    public static void measureDeploymentEvent(final IEventRecord event, final ObservationPoint point) {
        if (event instanceof IDeploymentChange) {
            if (event instanceof IDeployedEvent) {
                ExperimentLoggingUtils.logEvent(event.getTimestamp(), EventTypes.DEPLOYMENT, point);
            } else if (event instanceof IUndeployedEvent) {
                ExperimentLoggingUtils.logEvent(event.getTimestamp(), EventTypes.UNDEPLOYMENT, point);
            }
        }
    }

    /**
     * Measure control events.
     *
     * @param event
     *            control event
     * @param point
     *            location
     */
    public static void measureControlEvent(final AbstractTcpControlEvent event, final ObservationPoint point) {
        ExperimentLoggingUtils.logEvent(event.getTriggerTimestamp(), EventTypes.NONE, point);
    }

    public static void createSamplers() {
        final IOshiSamplerFactory sigarFactory = OshiSamplerFactory.INSTANCE;
        final ISampler[] samplers = { sigarFactory.createSensorCPUsDetailedPerc(),
                sigarFactory.createSensorMemSwapUsage(), new MemorySampler() };
        for (final ISampler sampler : samplers) {
            ExperimentLoggingUtils.CTRL.schedulePeriodicSampler(sampler, 0, 100, TimeUnit.MILLISECONDS);
        }
    }
}
