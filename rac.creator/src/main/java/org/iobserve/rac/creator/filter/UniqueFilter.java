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
package org.iobserve.rac.creator.filter;

import java.util.ArrayList;
import java.util.Collection;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Find duplicates in an event stream and filters them out. Note: This filter has a state and the
 * memory demand may grow depending on the number of different strings passing through.
 *
 * @author Reiner Jung
 *
 */
public class UniqueFilter extends AbstractConsumerStage<String> {

    private final Collection<String> stringCollection = new ArrayList<>();

    private final OutputPort<String> outputPort = this.createOutputPort();

    public OutputPort<String> getOutputPort() {
        return this.outputPort;
    }

    @Override
    protected void execute(final String element) throws Exception {
        if (!this.stringCollection.contains(element)) {
            this.stringCollection.add(element);
            this.outputPort.send(element);
        }
    }
}
