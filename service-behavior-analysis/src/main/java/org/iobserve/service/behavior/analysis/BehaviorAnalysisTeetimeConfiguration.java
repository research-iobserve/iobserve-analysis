/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.service.behavior.analysis;

import kieker.common.exception.ConfigurationException;

import teetime.framework.Configuration;

import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.service.behavior.analysis.clustering.ClusteringCompositeStage;
import org.iobserve.service.behavior.analysis.model.generation.ModelGenerationCompositeStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class BehaviorAnalysisTeetimeConfiguration extends Configuration {
    private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorAnalysisTeetimeConfiguration.class);

    public BehaviorAnalysisTeetimeConfiguration(final kieker.common.configuration.Configuration configuration)
            throws ConfigurationException {

        final ModelGenerationCompositeStage modelGeneration = new ModelGenerationCompositeStage(configuration);

        final ClusteringCompositeStage clustering = new ClusteringCompositeStage(configuration);

        final String outputUrl = configuration.getStringProperty(ConfigurationKeys.RESULT_URL, "");
        if ("".equals(outputUrl)) {
            BehaviorAnalysisTeetimeConfiguration.LOGGER
                    .error("Initialization incomplete: No result dictionary specified.");
            throw new ConfigurationException("Initialization incomplete: No result dictionary specified");
        }

        if ("".equals(outputUrl)) {
            BehaviorAnalysisTeetimeConfiguration.LOGGER
                    .error("Initialization incomplete: No result dictionary specified.");
            throw new ConfigurationException("Initialization incomplete: No result dictionary specified");
        }

        final ClusteringSink sink = new ClusteringSink(outputUrl);

        this.connectPorts(modelGeneration.getModelOutputPort(), clustering.getModelInputPort());
        this.connectPorts(modelGeneration.getTimerOutputPort(), clustering.getTimerInputPort());
        this.connectPorts(clustering.getOutputPort(), sink.getInputPort());
    }

    public static void main(final String[] args) {

    }

}
