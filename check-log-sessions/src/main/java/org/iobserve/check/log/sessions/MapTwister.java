/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.check.log.sessions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public class MapTwister extends AbstractConsumerStage<Map<String, SessionModel>> {

    private final OutputPort<Map<String, Object>> outputPort = this.createOutputPort();

    @Override
    protected void execute(final Map<String, SessionModel> element) throws Exception {
        for (final Entry<String, SessionModel> entry : element.entrySet()) {
            final Map<String, Object> row = new HashMap<>();
            row.put("sessionId", entry.getKey());
            row.put("count", entry.getValue().getCounter());
            row.put("first", entry.getValue().getFirst());
            row.put("last", entry.getValue().getLast());

            this.outputPort.send(row);
        }
    }

    public OutputPort<Map<String, Object>> getOutputPort() {
        return this.outputPort;
    }

}
