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

import org.iobserve.analysis.clustering.filter.models.configuration.IModelGenerationFilterFactory;
import org.iobserve.analysis.clustering.filter.similaritymatching.IClassificationStage;
import org.iobserve.analysis.clustering.filter.similaritymatching.IModelGenerationStrategy;
import org.iobserve.analysis.clustering.filter.similaritymatching.IParameterMetricStrategy;
import org.iobserve.analysis.clustering.filter.similaritymatching.IStructureMetricStrategy;
import org.iobserve.analysis.feature.IGeoLocationCompositeStage;
import org.iobserve.analysis.session.IEntryCallAcceptanceMatcher;
import org.iobserve.analysis.traces.ITraceSignatureCleanupRewriter;
import org.iobserve.stages.general.IEntryCallTraceMatcher;

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

    /**
     * Set whether container management visualization sinks shall be created. STRING
     * ARRAY
     */
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

    /** Set trace matcher required for EntryCallStage. STRING */
    public static final String TRACE_MATCHER = ConfigurationKeys.PREFIX + "behavior."
            + IEntryCallTraceMatcher.class.getCanonicalName();

    /** Set acceptance matcher required for SessionAcceptanceFilter. STRING */
    public static final String ENTRY_CALL_ACCEPTANCE_MATCHER = ConfigurationKeys.PREFIX + "behavior."
            + IEntryCallAcceptanceMatcher.class.getCanonicalName();

    /** Set cleanup rewriter required for TraceOperationCleanupFilter. STRING */
    public static final String CLEANUP_REWRITER = ConfigurationKeys.PREFIX + "behavior."
            + ITraceSignatureCleanupRewriter.class.getCanonicalName();

    /**
     * Set entry call filter rules factory required for TSessionOperationsFilter.
     * STRING
     */
    public static final String ENTRY_CALL_FILTER_RULES_FACTORY = ConfigurationKeys.PREFIX + "behavior."
            + IModelGenerationFilterFactory.class.getCanonicalName();

    /** Set time interval required for TimeTriggerFilter. LONG */
    public static final String TRIGGER_INTERVAL = ConfigurationKeys.PREFIX + "behavior." + "triggerInterval";

    /**
     * Set classification stage (IClassificationStage) used in
     * BehaviorCompositeStage STRING
     */
    public static final String CLASSIFICATION_STAGE = ConfigurationKeys.PREFIX + "behavior."
            + IClassificationStage.class.getCanonicalName();

    /**
     * Set behaviour model sink base url to configure file writing directory STRING
     */
    public static final String SINK_BASE_URL = ConfigurationKeys.PREFIX + "behavior.sink.baseUrl";

    /**
     * Specific to Similarity Matching
     */

    /**
     * Set parameter metric strategy STRING
     */
    public static final String SIM_MATCH_PARAMETER_STRATEGY = ConfigurationKeys.PREFIX + "behavior.sm."
            + IParameterMetricStrategy.class.getCanonicalName();

    /**
     * Set structure metric strategy STRING
     */
    public static final String SIM_MATCH_STRUCTURE_STRATEGY = ConfigurationKeys.PREFIX + "behavior.sm."
            + IStructureMetricStrategy.class.getCanonicalName();

    /**
     * Set model generation strategy STRING
     */
    public static final String SIM_MATCH_MODEL_STRATEGY = ConfigurationKeys.PREFIX + "behavior.sm."
            + IModelGenerationStrategy.class.getCanonicalName();

    /**
     * Set similarity radius DOUBLE
     */
    public static final String SIM_MATCH_RADIUS = ConfigurationKeys.PREFIX + "behavior.sm.radius";

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
