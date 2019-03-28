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
import kieker.tools.source.LogsReaderCompositeStage;

import teetime.framework.OutputPort;

/**
 * This class is necessary, as the class in Kieker does not implement the ISourceCompositeStage
 * inteface.
 *
 * @author Reiner Jung
 *
 */
public class GenericFileSourceCompositeStage extends LogsReaderCompositeStage implements ISourceCompositeStage {

    /**
     * Generic source file reader.
     *
     * @param configuration
     *            configuration for LogsReaderCompositeStage
     */
    public GenericFileSourceCompositeStage(final Configuration configuration) {
        super(configuration);
    }

    @Override
    public OutputPort<IMonitoringRecord> getOutputPort() { // NOPMD missing source interface in
                                                           // Kieker
        return super.getOutputPort();
    }

}
