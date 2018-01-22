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
package org.iobserve.analysis.source;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.OutputPort;

import org.iobserve.analysis.AbstractConfigurableCompositeStage;
import org.iobserve.analysis.ISourceCompositeStage;
import org.iobserve.stages.source.MultipleConnectionTcpReaderStage;

/**
 * Multiple TCP input stage.
 *
 * @author Reiner Jung
 *
 */
public class TCPSourceCompositeStage extends AbstractConfigurableCompositeStage implements ISourceCompositeStage {

    private static final String PREFIX = TCPSourceCompositeStage.class.getCanonicalName();

    public static final String SOURCE_PORT = TCPSourceCompositeStage.PREFIX + ".port";
    private static final int DEFAULT_SOURCE_PORT = 9876;
    private static final String CAPACITY = TCPSourceCompositeStage.PREFIX + ".capacity";
    private static final int DEFAULT_CAPACITY = 1024 * 1024;

    private final MultipleConnectionTcpReaderStage reader;

    /**
     * Create a composite reader stage.
     *
     * @param configuration
     *            configuration parameters
     */
    // TODO externalize parameters, all parameters shall be parsed in the central configuration
    // object
    public TCPSourceCompositeStage(final Configuration configuration) {
        super(configuration);
        final int inputPort = configuration.getIntProperty(TCPSourceCompositeStage.SOURCE_PORT,
                TCPSourceCompositeStage.DEFAULT_SOURCE_PORT);
        final int capacity = configuration.getIntProperty(TCPSourceCompositeStage.CAPACITY,
                TCPSourceCompositeStage.DEFAULT_CAPACITY);
        this.reader = new MultipleConnectionTcpReaderStage(inputPort, capacity);
    }

    @Override
    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.reader.getOutputPort();
    }

}
