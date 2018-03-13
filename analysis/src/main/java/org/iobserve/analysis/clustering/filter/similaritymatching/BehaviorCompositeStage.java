package org.iobserve.analysis.clustering.filter.similaritymatching;

import org.iobserve.analysis.clustering.filter.models.configuration.IModelGenerationFilterFactory;
import org.iobserve.analysis.configurations.ConfigurationKeys;
import org.iobserve.analysis.configurations.MJConfiguration;
import org.iobserve.analysis.feature.IBehaviorCompositeStage;
import org.iobserve.analysis.session.IEntryCallAcceptanceMatcher;
import org.iobserve.analysis.traces.ITraceSignatureCleanupRewriter;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.stages.general.ConfigurationException;
import org.iobserve.stages.general.IEntryCallTraceMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import teetime.framework.InputPort;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;

public class BehaviorCompositeStage implements IBehaviorCompositeStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MJConfiguration.class);

    public BehaviorCompositeStage(final Configuration configuration) throws ConfigurationException {
        /** Instantiate objects for PreprocessingCompositeStage */

        /** For EntryCallStage */
        final String traceMatcherClassName = configuration.getStringProperty(ConfigurationKeys.TRACE_MATCHER);
        if (traceMatcherClassName.isEmpty()) {
            BehaviorCompositeStage.LOGGER.error("Initialization incomplete: No trace matcher specified.");
            throw new ConfigurationException("Initialization incomplete: No trace matcher specified.");
        }
        final IEntryCallTraceMatcher traceMatcher = InstantiationFactory
                .createWithConfiguration(IEntryCallTraceMatcher.class, traceMatcherClassName, configuration);

        /** For SessionAcceptanceFilter */
        final String entryCallMatcherClassName = configuration
                .getStringProperty(ConfigurationKeys.ENTRY_CALL_ACCEPTANCE_MATCHER);
        if (entryCallMatcherClassName.isEmpty()) {
            BehaviorCompositeStage.LOGGER
                    .error("Initialization incomplete: No entry call acceptance matcher specified.");
            throw new ConfigurationException("Initialization incomplete: No entry call acceptance matcher specified.");
        }
        final IEntryCallAcceptanceMatcher entryCallMatcher = InstantiationFactory
                .createWithConfiguration(IEntryCallAcceptanceMatcher.class, entryCallMatcherClassName, configuration);

        /** For TraceOperationsCleanupFilter */
        final String cleanupRewriterClassName = configuration.getStringProperty(ConfigurationKeys.CLEANUP_REWRITER);
        if (cleanupRewriterClassName.isEmpty()) {
            BehaviorCompositeStage.LOGGER.error("Initialization incomplete: No signature cleanup rewriter specified.");
            throw new ConfigurationException("Initialization incomplete: No signature cleanup rewriter specified.");
        }
        final ITraceSignatureCleanupRewriter cleanupRewriter = InstantiationFactory
                .createAndInitialize(ITraceSignatureCleanupRewriter.class, cleanupRewriterClassName, configuration);

        /** For TSessionOperationsFilter */
        final String entryCallFilterFactoryClassName = configuration
                .getStringProperty(ConfigurationKeys.ENTRY_CALL_FILTER_RULES_FACTORY);
        if (entryCallFilterFactoryClassName.isEmpty()) {
            BehaviorCompositeStage.LOGGER
                    .error("Initialization incomplete: No entry call filter rules factory specified.");
            throw new ConfigurationException(
                    "Initialization incomplete: No entry call filter rules factory specified.");
        }
        final IModelGenerationFilterFactory filterRulesFactory = InstantiationFactory.createAndInitialize(
                IModelGenerationFilterFactory.class, entryCallFilterFactoryClassName, configuration);

        /** Instantiate IClassificationStage */
        final String classificationStageClassName = configuration
                .getStringProperty(ConfigurationKeys.CLASSIFICATION_STAGE);
        if (classificationStageClassName.isEmpty()) {
            BehaviorCompositeStage.LOGGER.error("Initialization incomplete: No classification stage specified.");
            throw new ConfigurationException("Initialization incomplete: No classification stage specified.");
        }
        final IClassificationStage classificationStage = InstatiationFactory
                .createAndInitialize(IClassificationStage.class, classificationStageClassName, configuration);

    }

    @Override
    public InputPort<EventBasedTrace> getEventBasedTracePort() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputPort<ISessionEvent> getSessionEventInputPort() {
        // TODO Auto-generated method stub
        return null;
    }

}
