/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.reconstructor;

import kieker.tools.common.ConfigurationException;

import teetime.framework.Configuration;

import org.iobserve.service.source.ISourceCompositeStage;
import org.iobserve.service.source.SourceStageFactory;
import org.iobserve.stages.sink.DataSinkStage;

/**
 * Analysis configuration for the data collector.
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 *
 */
public class ReconstructorConfiguration extends Configuration {

    private final DataSinkStage consumer;

    /**
     * Configure analysis.
     *
     * @param configuration
     *            configuration for the collector
     * @throws ConfigurationException
     */
    public ReconstructorConfiguration(final kieker.common.configuration.Configuration configuration)
            throws ConfigurationException {
        final ISourceCompositeStage sourceStage = SourceStageFactory.createSourceCompositeStage(configuration);

        final FixTraceMetadata fixTraceMetadata = new FixTraceMetadata();

        final StartSessionDetector startSessionDetector = new StartSessionDetector();

        final EndSessionDetector endSessionDetector = new EndSessionDetector();

        this.consumer = new DataSinkStage(configuration);

        this.connectPorts(sourceStage.getOutputPort(), fixTraceMetadata.getInputPort());
        this.connectPorts(fixTraceMetadata.getOutputPort(), startSessionDetector.getInputPort());
        this.connectPorts(startSessionDetector.getOutputPort(), endSessionDetector.getInputPort());
        this.connectPorts(endSessionDetector.getOutputPort(), this.consumer.getInputPort());
    }

    public DataSinkStage getCounter() {
        return this.consumer;
    }
}
