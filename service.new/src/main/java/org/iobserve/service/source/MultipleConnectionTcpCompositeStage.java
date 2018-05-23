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
package org.iobserve.service.source;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.CompositeStage;
import teetime.framework.OutputPort;

import org.iobserve.service.InstantiationFactory;
import org.iobserve.stages.general.ConfigurationException;
import org.iobserve.stages.source.ITraceMetadataRewriter;
import org.iobserve.stages.source.MultipleConnectionTcpReaderStage;
import org.iobserve.stages.source.NoneTraceMetadataRewriter;

/**
 * Multiple TCP input stage.
 *
 * @author Reiner Jung
 *
 */
public class MultipleConnectionTcpCompositeStage extends CompositeStage implements ISourceCompositeStage {

    private static final String PREFIX = MultipleConnectionTcpCompositeStage.class.getCanonicalName();

    public static final String SOURCE_PORT = MultipleConnectionTcpCompositeStage.PREFIX + ".port";
    private static final int DEFAULT_SOURCE_PORT = 9876;
    private static final String CAPACITY = MultipleConnectionTcpCompositeStage.PREFIX + ".capacity";
    private static final int DEFAULT_CAPACITY = 1024 * 1024;

    private static final String REWRITER = MultipleConnectionTcpCompositeStage.PREFIX + ".recordRewriter";

    private final MultipleConnectionTcpReaderStage reader;

    /**
     * Create a composite reader stage.
     *
     * @param configuration
     *            configuration parameters
     * @throws ConfigurationException
     */
    public MultipleConnectionTcpCompositeStage(final Configuration configuration) throws ConfigurationException {
        final int inputPort = configuration.getIntProperty(MultipleConnectionTcpCompositeStage.SOURCE_PORT,
                MultipleConnectionTcpCompositeStage.DEFAULT_SOURCE_PORT);
        final int capacity = configuration.getIntProperty(MultipleConnectionTcpCompositeStage.CAPACITY,
                MultipleConnectionTcpCompositeStage.DEFAULT_CAPACITY);
        final String rewriterClassName = configuration.getStringProperty(MultipleConnectionTcpCompositeStage.REWRITER,
                NoneTraceMetadataRewriter.class.getName());
        final Class<?>[] classes = null;
        final ITraceMetadataRewriter rewriter = InstantiationFactory.create(ITraceMetadataRewriter.class,
                rewriterClassName, classes);
        this.reader = new MultipleConnectionTcpReaderStage(inputPort, capacity, rewriter);
    }

    @Override
    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.reader.getOutputPort();
    }

}
