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
package org.iobserve.analysis;

import org.iobserve.analysis.deployment.data.IPCMDeploymentEvent;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.common.record.EventTypes;
import org.iobserve.common.record.ObservationPoint;
import org.iobserve.stages.data.ExperimentLogging;

/**
 * @author Reiner Jung
 *
 */
public final class AnalysisExperimentLoggingUtils {

    private AnalysisExperimentLoggingUtils() {

    }

    /**
     * Measure a time stamp.
     * 
     * @param event
     *            event being processed by the analysis
     * @param point
     *            kind of measurement point
     */
    public static void measure(final IPCMDeploymentEvent event, final ObservationPoint point) {
        if (event instanceof PCMDeployedEvent) {
            ExperimentLogging.logEvent(event.getTimestamp(), EventTypes.DEPLOYMENT, point);
        } else if (event instanceof PCMUndeployedEvent) {
            ExperimentLogging.logEvent(event.getTimestamp(), EventTypes.UNDEPLOYMENT, point);
        }
    }

}
