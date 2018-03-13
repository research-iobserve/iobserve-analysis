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
package org.iobserve.analysis.configurations;

import org.iobserve.analysis.feature.IGeoLocationCompositeStage;

/**
 *
 * @author Reiner Jung
 *
 * @since 0.0.2
 *
 */
public final class ConfigurationKeys {
    private static final String PREFIX = "iobserve.analysis.";

    /**
     * Container Management.
     */

    /** Set whether container management shall be activated. BOOLEAN */
    public static final String CONTAINER_MANAGEMENT = ConfigurationKeys.PREFIX + "container.management.analysis";

    /** Set whether container management visualization sinks shall be created. STRING ARRAY */
    public static final String CONTAINER_MANAGEMENT_SINK = ConfigurationKeys.PREFIX + "container.management.sink";

    /** Visualization. */
    public static final String IOBSERVE_VISUALIZATION_URL = ConfigurationKeys.PREFIX
            + "container.management.sink.visualizationUrl";

    /**
     * Traces.
     */

    /** Set whether trace reconstruction should be activated. BOOLEAN */
    public static final String TRACES = ConfigurationKeys.PREFIX + "traces";

    /**
     * Data Flow.
     */

    /** Set whether data flow analysis should be activated. BOOLEAN */
    public static final String DATA_FLOW = ConfigurationKeys.PREFIX + "dataFlow";

    /**
     * PerOpteryx.
     */

    public static final String PER_OPTERYX_URI_PATH = ConfigurationKeys.PREFIX + "perOpteryxUriPath";

    public static final String LQNS_URI_PATH = ConfigurationKeys.PREFIX + "lqnsUriPath";

    public static final String DEPLOYABLES_FOLDER_PATH = ConfigurationKeys.PREFIX + "deployablesFolderPath";

    /**
     * Geolocation.
     */

    /** Set whether separate geo location events should be processed. BOOLEAN */
    public static final String GEO_LOCATION = IGeoLocationCompositeStage.class.getCanonicalName(); // NOCS

    /**
     * Source.
     */

    /** Set the preferred source. STRING */
    public static final String SOURCE = ConfigurationKeys.PREFIX + "source";

    /**
     * Behavior.
     */

    /** Select clustering filter. */
    public static final String BEHAVIOR_CLUSTERING = ConfigurationKeys.PREFIX + "behaviour.filter";

    /** Set whether a behavior visualization sink shall be created. STRING ARRAY. */
    public static final String BEHAVIOR_CLUSTERING_SINK = ConfigurationKeys.PREFIX + "behavior.sink.visual";

    /** poepke approach. */
    public static final String BEHAVIOR_VISUALIZATION_URL = ConfigurationKeys.PREFIX + "behavior.visualizationUrl";

    public static final String BEHAVIOR_CLOSED_WORKLOAD = ConfigurationKeys.PREFIX + "behavior.closed.workload";

    public static final String BEHAVIOR_THINK_TIME = ConfigurationKeys.PREFIX + "behavior.think.time";

    public static final String BEHAVIOR_VARIANCE_OF_USER_GROUPS = ConfigurationKeys.PREFIX
            + "behavior.variance.of.user.groups";

    /**
     * Model.
     */

    public static final String PCM_MODEL_DB_DIRECTORY = ConfigurationKeys.PREFIX + "model.pcm.directory.db";

    public static final String PCM_MODEL_INIT_DIRECTORY = ConfigurationKeys.PREFIX + "model.pcm.directory.init";

    /**
     * Factory, do not instantiate.
     */
    private ConfigurationKeys() {
        // empty constructor.
    }
}
