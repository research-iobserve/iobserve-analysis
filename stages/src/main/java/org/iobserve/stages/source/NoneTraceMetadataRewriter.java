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
package org.iobserve.stages.source;

import java.io.IOException;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.OutputPort;

/**
 * This is an empty rewriter which sends all received records to the output port.
 *
 * @author Reiner Jung
 *
 */
public class NoneTraceMetadataRewriter implements ITraceMetadataRewriter {

    /*
     * (non-Javadoc)
     *
     * @see org.iobserve.stages.source.ITraceMetadataRewriter#rewrite(org.iobserve.stages.source.
     * Connection, kieker.common.record.IMonitoringRecord, long, teetime.framework.OutputPort)
     */
    @Override
    public void rewrite(final Connection connection, final IMonitoringRecord record, final long loggingTimestamp,
            final OutputPort<IMonitoringRecord> outputPort) throws IOException {
        outputPort.send(record);
    }

}
