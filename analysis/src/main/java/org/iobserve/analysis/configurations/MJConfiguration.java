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

import org.iobserve.analysis.ConfigurationException;
import org.iobserve.analysis.IBehaviorCompositeStage;
import org.iobserve.analysis.ISourceCompositeStage;
import org.iobserve.analysis.InstantiationFactory;
import org.iobserve.analysis.clustering.filter.models.configuration.IModelGenerationFilterFactory;
import org.iobserve.analysis.session.IEntryCallAcceptanceMatcher;
import org.iobserve.analysis.traces.ITraceSignatureCleanupRewriter;
import org.iobserve.stages.general.IEntryCallTraceMatcher;
import org.iobserve.stages.general.RecordSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teetime.framework.Configuration;

public class MJConfiguration extends Configuration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MJConfiguration.class);

    /** Set the used behavior aggregation and clustering stage class name. STRING */
    public static final String BEHAVIOR_CLUSTERING = IBehaviorCompositeStage.class.getCanonicalName();

    /** Set whether a behavior visualization sink shall be created. STRING ARRAY. */
    public static final String BEHAVIOR_CLUSTERING_SINK = MJConfiguration.BEHAVIOR_CLUSTERING + ".sink.visual";

    /** Set the preferred source. STRING */
    public static final String SOURCE = ISourceCompositeStage.class.getCanonicalName();

    /** Set trace matcher required for EntryCallStage. STRING */
    public static final String TRACE_MATCHER = IEntryCallTraceMatcher.class.getCanonicalName();

    /** Set acceptance matcher required for SessionAcceptanceFilter. STRING */
    public static final String ENTRY_CALL_ACCEPTANCE_MATCHER = IEntryCallAcceptanceMatcher.class.getCanonicalName();

    /** Set cleanup rewriter required for TraceOperationCleanupFilter. STRING */
    public static final String CLEANUP_REWRITER = ITraceSignatureCleanupRewriter.class.getCanonicalName();

    /**
     * Set entry call filter rules factory required for TSessionOperationsFilter.
     * STRING
     */
    public static final String ENTRY_CALL_FILTER_RULES_FACTORY = IModelGenerationFilterFactory.class.getCanonicalName();

    /** Set time interval required for TimeTriggerFilter. LONG */
    public static final String TRIGGER_INTERVAL = "triggerInterval";

    private final RecordSwitch recordSwitch;

    /**
     * Create an analysis configuration, configured by a configuration object.
     *
     * @param configuration
     *            the configuration parameter
     * @param resourceEnvironmentModelProvider
     *            provider for an environment model
     * @param allocationModelProvider
     *            provider for the allocation model
     * @param systemModelProvider
     *            provider for the system model
     * @param correspondenceModelProvider
     *            provider for the correspondence model
     * @throws ConfigurationException
     *             if the configuration faisl
     */
    public MJConfiguration(final kieker.common.configuration.Configuration configuration)
            throws ConfigurationException {
        this.recordSwitch = new RecordSwitch();

        /** Source stage. */
        final String sourceClassName = configuration.getStringProperty(MJConfiguration.SOURCE);
        if (!sourceClassName.isEmpty()) {
            final ISourceCompositeStage sourceCompositeStage = InstantiationFactory
                    .createAndInitialize(ISourceCompositeStage.class, sourceClassName, configuration);

            this.connectPorts(sourceCompositeStage.getOutputPort(), this.recordSwitch.getInputPort());

            this.trace(configuration);
        } else {
            MJConfiguration.LOGGER.error("Initialization incomplete: No source stage specified.");
            throw new ConfigurationException("Initialization incomplete: No source stage specified.");
        }
    }

    /**
     * Create trace related filter chains.
     *
     * @param configuration
     *            filter configurations
     */
    private void trace(final kieker.common.configuration.Configuration configuration) throws ConfigurationException {
    }

    public RecordSwitch getRecordSwitch() {
        return this.recordSwitch;
    }

}
