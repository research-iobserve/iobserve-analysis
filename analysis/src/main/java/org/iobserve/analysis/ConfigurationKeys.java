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

import org.iobserve.analysis.behavior.clustering.similaritymatching.IModelGenerationStrategy;
import org.iobserve.analysis.behavior.clustering.similaritymatching.IParameterMetric;
import org.iobserve.analysis.behavior.clustering.similaritymatching.IStructureMetricStrategy;
import org.iobserve.analysis.behavior.filter.IClassificationStage;
import org.iobserve.analysis.behavior.models.data.configuration.IModelGenerationFilterFactory;
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
public final class ConfigurationKeys { // NOPMD is not a conventional utility class
    public static final String PREFIX = "iobserve.analysis.";

    /**
     * Container Management.
     */

    /** Set whether container management shall be activated. BOOLEAN */
    public static final String CONTAINER_MANAGEMENT = ConfigurationKeys.PREFIX + "container.management.analysis";

    /**
     * Set whether container management visualization sinks shall be created. STRING ARRAY
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
    public static final String BEHAVIOR_CLUSTERING = ConfigurationKeys.PREFIX + "behavior.filter";

    /** Set whether a behavior visualization sink shall be created. STRING ARRAY. */
    public static final String BEHAVIOR_CLUSTERING_SINK = ConfigurationKeys.PREFIX + "behavior.sink";

    /** poepke approach. */
    public static final String BEHAVIOR_VISUALIZATION_URL = ConfigurationKeys.PREFIX + "behavior.visualizationUrl";

    public static final String BEHAVIOR_CLOSED_WORKLOAD = ConfigurationKeys.PREFIX + "behavior.closed.workload";

    public static final String BEHAVIOR_THINK_TIME = ConfigurationKeys.PREFIX + "behavior.think.time";

    public static final String BEHAVIOR_VARIANCE_OF_USER_GROUPS = ConfigurationKeys.PREFIX
            + "behavior.variance.of.user.groups";

    /** Set trace matcher required for EntryCallStage. STRING */
    public static final String TRACE_MATCHER = ConfigurationKeys.PREFIX + "behavior."
            + IEntryCallTraceMatcher.class.getSimpleName();

    /** Set acceptance matcher required for SessionAcceptanceFilter. STRING */
    public static final String ENTRY_CALL_ACCEPTANCE_MATCHER = ConfigurationKeys.PREFIX + "behavior."
            + IEntryCallAcceptanceMatcher.class.getSimpleName();

    /** Set cleanup rewriter required for TraceOperationCleanupFilter. STRING */
    public static final String CLEANUP_REWRITER = ConfigurationKeys.PREFIX + "behavior."
            + ITraceSignatureCleanupRewriter.class.getSimpleName();

    /**
     * Set entry call filter rules factory required for TSessionOperationsFilter. STRING
     */
    public static final String ENTRY_CALL_FILTER_RULES_FACTORY = ConfigurationKeys.PREFIX + "behavior."
            + IModelGenerationFilterFactory.class.getSimpleName();

    /** Set time interval required for TimeTriggerFilter. LONG */
    public static final String TRIGGER_INTERVAL = ConfigurationKeys.PREFIX + "behavior." + "triggerInterval";

    /**
     * Set classification stage (IClassificationStage) used in BehaviorCompositeStage STRING.
     */
    public static final String CLASSIFICATION_STAGE = ConfigurationKeys.PREFIX + "behavior."
            + IClassificationStage.class.getSimpleName();

    /**
     * Set behaviour model sink base url to configure file writing directory STRING.
     */
    public static final String SINK_BASE_URL = ConfigurationKeys.PREFIX + "behavior.sink.baseUrl";

    /**
     * Specific to Similarity Matching.
     */

    /**
     * Set parameter metric strategy STRING.
     */
    public static final String SIM_MATCH_PARAMETER_STRATEGY = ConfigurationKeys.PREFIX + "behavior.sm."
            + IParameterMetric.class.getSimpleName();

    /**
     * Set structure metric strategy STRING.
     */
    public static final String SIM_MATCH_STRUCTURE_STRATEGY = ConfigurationKeys.PREFIX + "behavior.sm."
            + IStructureMetricStrategy.class.getSimpleName();

    /**
     * Set model generation strategy STRING.
     */
    public static final String SIM_MATCH_MODEL_STRATEGY = ConfigurationKeys.PREFIX + "behavior.sm."
            + IModelGenerationStrategy.class.getSimpleName();

    /**
     * Set parameter similarity radius DOUBLE.
     */
    public static final String SIM_MATCH_RADIUS_PARAMS = ConfigurationKeys.PREFIX + "behavior.sm.parameters.radius";

    /**
     * Set structure similarity radius DOUBLE.
     */
    public static final String SIM_MATCH_RADIUS_STRUCTURE = ConfigurationKeys.PREFIX + "behavior.sm.structure.radius";

    public static final String SINGLE_EVENT_MODE = ConfigurationKeys.PREFIX + "singleEventMode";

    /**
     * Set similarity radius DOUBLE.
     */
    public static final String SIM_MATCH_RADIUS = ConfigurationKeys.PREFIX + "behavior.sm.radius";

    /**
     * Model.
     */

    public static final String PCM_MODEL_DB_DIRECTORY = ConfigurationKeys.PREFIX + "model.pcm.databaseDirectory";

    public static final String PCM_MODEL_INIT_DIRECTORY = ConfigurationKeys.PREFIX
            + "model.pcm.initializationDirectory";

    public static final String PCM_FEATURE = ConfigurationKeys.PREFIX + "model.pcm";

    public static final String CONTAINER_MANAGEMENT_VISUALIZATION_FEATURE = ConfigurationKeys.PREFIX
            + "containerManagement.visualization";

    public static final String SYSTEM_ID = ConfigurationKeys.PREFIX + "systemId";

    /**
     * Classification pre-processing.
     */

    public static final String KEEP_EMPTY_TRANS = ConfigurationKeys.PREFIX + "behavior.preprocess.keepEmpty";

    public static final String MIN_SIZE = ConfigurationKeys.PREFIX + "behavior.preprocess.minSize";

    public static final String KEEP_TIME = ConfigurationKeys.PREFIX + "behavior.preprocess.keepTime";

    /**
     * Classification stage selection.
     */

    public static final String CLASS_STAGE = ConfigurationKeys.PREFIX + "behavior.classification";

    /**
     * Xmeans Classification.
     */

    public static final String XM_VAR = ConfigurationKeys.PREFIX + "behavior.xmeans.variance";

    public static final String XM_EXP_CLUS = ConfigurationKeys.PREFIX + "behavior.xmeans.expectedClusters";

    /**
     * Hierarchcal Clustering.
     */

    public static final String HIER_DIST = ConfigurationKeys.PREFIX + "behavior.clustering.hierarchical.distance";

    public static final String HIER_SEL_METHOD = ConfigurationKeys.PREFIX
            + "behavior.clustering.hierarchical.clusterSelectionMethod";

    public static final String HIER_LINKAGE = ConfigurationKeys.PREFIX + "behavior.clustering.hierarchical.linkage";

    /**
     * Birch Classification.
     */

    public static final String LEAF_TH = ConfigurationKeys.PREFIX + "behavior.birch.leafThreshold";

    public static final String MAX_LEAF_SIZE = ConfigurationKeys.PREFIX + "behavior.birch.maxLeafSize";

    public static final String MAX_NODE_SIZE = ConfigurationKeys.PREFIX + "behavior.birch.maxNodeSize";

    public static final String MAX_LEAF_ENTRIES = ConfigurationKeys.PREFIX + "behavior.birch.maxLeafEntries";

    public static final String EXP_NUM_OF_CLUSTERS = ConfigurationKeys.PREFIX
            + "behavior.birch.expectedNumberOfClusters";

    public static final String USE_CNM = ConfigurationKeys.PREFIX + "behavior.birch.useClusterNumberMetric";

    public static final String CLUSTER_METRIC_STRATEGY = ConfigurationKeys.PREFIX
            + "behavior.birch.clusterMetricStrategy";

    public static final String LMETHOD_EVAL_STRATEGY = ConfigurationKeys.PREFIX + "behavior.birch.lmethodEvalStrategy";

    /**
     * Optics Clustering.
     */

    public static final String GED_PREFIX = "org.iobserve.service.behavior.analysis.";

    public static final String EPSILON = ConfigurationKeys.GED_PREFIX + "epsilon";

    public static final String MIN_PTS = ConfigurationKeys.GED_PREFIX + "minPts";

    public static final String MAX_MODEL_AMOUNT = ConfigurationKeys.GED_PREFIX + "maxModelAmount";

    public static final String RESULT_URL = ConfigurationKeys.GED_PREFIX + "outputUrl";

    /**
     * Graph Edit Distance.
     */

    public static final String NODE_INSERTION_COST = ConfigurationKeys.GED_PREFIX + "nodeInsertionCost";

    public static final String EDGE_INSERTION_COST = ConfigurationKeys.GED_PREFIX + "edgeInsertionCost";

    public static final String EVENT_GROUP_INSERTION_COST = ConfigurationKeys.GED_PREFIX + "eventGroupInsertionCost";

    /**
     * Factory, do not instantiate.
     */
    private ConfigurationKeys() {
        // empty constructor.
    }
}