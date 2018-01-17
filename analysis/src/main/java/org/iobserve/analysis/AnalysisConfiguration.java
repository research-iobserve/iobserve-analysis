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

import java.lang.reflect.InvocationTargetException;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;

import teetime.framework.Configuration;

import org.iobserve.stages.general.RecordSwitch;

/**
 * @author Reiner Jung
 *
 */
public class AnalysisConfiguration extends Configuration {

    private static final Log LOG = LogFactory.getLog(AnalysisConfiguration.class);

    private static final String BEHAVIOR_CLUSTERING = IBehaviorCompositeStage.class.getCanonicalName();

    private static final String BEHAVIOR_CLUSTERING_GRAPHICAL_SINK = AnalysisConfiguration.BEHAVIOR_CLUSTERING
            + ".sink.visual";

    private static final String DEPLOYMENT = IDeploymentCompositeStage.class.getCanonicalName();

    private static final String TRACES = ITraceCompositeStage.class.getCanonicalName();

    private static final String DATA_FLOW = IDataFlowCompositeStage.class.getCanonicalName();

    private static final String GEO_LOCATION = IGeoLocationCompositeStage.class.getCanonicalName();

    private static final String SOURCE = ISourceCompositeStage.class.getCanonicalName();

    protected final RecordSwitch recordSwitch;

    public AnalysisConfiguration(final kieker.common.configuration.Configuration configuration)
            throws ConfigurationException {
        this.recordSwitch = new RecordSwitch();

        final String sourceClassName = configuration.getStringProperty(AnalysisConfiguration.SOURCE);
        if (!sourceClassName.isEmpty()) {
            final ISourceCompositeStage sourceCompositeStage = this.createAndInitialize(ISourceCompositeStage.class, sourceClassName,
                    configuration);

            this.connectPorts(sourceCompositeStage.getOutputPort(), this.recordSwitch.getInputPort());

            this.deployment(configuration);
            this.trace(configuration);
            this.geoLocation(configuration);
        } else {
            AnalysisConfiguration.LOG.error("Initialization incomplete: No source stage specified.");
            throw new ConfigurationException("Initialization incomplete: No source stage specified.");
        }
    }

    private void deployment(final kieker.common.configuration.Configuration configuration) {
        final String deploymentClassName = configuration.getStringProperty(AnalysisConfiguration.DEPLOYMENT);
        if (!deploymentClassName.isEmpty()) {

        }

    }

    private void trace(final kieker.common.configuration.Configuration configuration) {
        final String traceClassName = configuration.getStringProperty(AnalysisConfiguration.TRACES);
        if (!traceClassName.isEmpty()) {
            this.behaviorClustering(configuration);
            this.dataflow(configuration);
        }
    }

    private void dataflow(final kieker.common.configuration.Configuration configuration) {
        final String dataflowClassName = configuration.getStringProperty(AnalysisConfiguration.DATA_FLOW);
        if (!dataflowClassName.isEmpty()) {

        }
    }

    private void geoLocation(final kieker.common.configuration.Configuration configuration) {
        final String geoLocationClassName = configuration.getStringProperty(AnalysisConfiguration.GEO_LOCATION);
        if (!geoLocationClassName.isEmpty()) {
        }
    }

    /**
     * Create the behavioral clustering.
     *
     * @param configuration
     *            analysis configuration
     */
    private void behaviorClustering(final kieker.common.configuration.Configuration configuration) {
        final String behaviorClustringClassName = configuration
                .getStringProperty(AnalysisConfiguration.BEHAVIOR_CLUSTERING);
        if (!behaviorClustringClassName.isEmpty()) {
            final IBehaviorCompositeStage behavior = this.createAndInitialize(IBehaviorCompositeStage.class,
                    behaviorClustringClassName, configuration);
            this.connectPorts(this.recordSwitch.getFlowOutputPort(), behavior.getFlowInputPort());
            this.connectPorts(this.recordSwitch.getSessionEventOutputPort(), behavior.getSessionEventInputPort());

            if (configuration.getBooleanProperty(AnalysisConfiguration.BEHAVIOR_CLUSTERING_GRAPHICAL_SINK)) {
                // TODO needs visualization trigger
            }
        }
    }

    /**
     * This is a helper method trying to find, create and initialize the given class, using its
     * public constructor which accepts a single {@link Configuration}.
     *
     * @param c
     *            This class defines the expected result of the method call.
     * @param classname
     *            The name of the class to be created.
     * @param configuration
     *            The configuration which will be used to initialize the class in question.
     *
     * @return A new and initializes class instance if everything went well.
     *
     * @param <C>
     *            The type of the returned class.
     */
    @SuppressWarnings("unchecked")
    public <C> C createAndInitialize(final Class<C> c, final String className,
            final kieker.common.configuration.Configuration configuration) {
        C createdClass = null; // NOPMD (null)
        try {
            final Class<?> clazz = Class.forName(className);
            if (c.isAssignableFrom(clazz)) {
                createdClass = (C) this.instantiate(clazz, className, configuration);
            } else {
                AnalysisConfiguration.LOG
                        .error("Class '" + className + "' has to implement '" + c.getSimpleName() + "'");
            }
        } catch (final ClassNotFoundException e) {
            AnalysisConfiguration.LOG.error(c.getSimpleName() + ": Class '" + className + "' not found", e);
        } catch (final NoSuchMethodException e) {
            AnalysisConfiguration.LOG.error(c.getSimpleName() + ": Class '" + className
                    + "' has to implement a (public) constructor that accepts a single Configuration", e);
        } catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
            // SecurityException, IllegalAccessException, IllegalArgumentException,
            // InstantiationException, InvocationTargetException
            AnalysisConfiguration.LOG.error(c.getSimpleName() + ": Failed to load class for name '" + className + "'",
                    e);
        }
        return createdClass;
    }

    private <C> C instantiate(final Class<C> clazz, final String className,
            final kieker.common.configuration.Configuration configuration)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {

        // Choose the appropriate configuration to pass
        final kieker.common.configuration.Configuration configurationToPass;
        if (clazz.isAnnotationPresent(ReceiveUnfilteredConfiguration.class)) {
            configurationToPass = configuration.flatten();
        } else {
            configurationToPass = configuration.getPropertiesStartingWith(className);
        }

        return clazz.getConstructor(Configuration.class).newInstance(configurationToPass);
    }

    public RecordSwitch getRecordSwitch() {
        return this.recordSwitch;
    }
}
