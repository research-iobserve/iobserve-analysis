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
package org.iobserve.analysis.behavior.clustering.similaritymatching;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;

import org.iobserve.analysis.behavior.models.extended.BehaviorModel;

/**
 * Composite stage that writes an array of behavior models into files
 * 
 * @author Jannis Kuckei
 *
 */
public class BehaviorModelCompositeSinkStage extends CompositeStage {
    InputPort<BehaviorModel[]> inputPort;

    /**
     * Constructor
     * 
     * @param baseURL
     *            Location where to write the files
     */
    public BehaviorModelCompositeSinkStage(final String baseURL) {
        final BehaviorModelDecollectorStage decolStage = new BehaviorModelDecollectorStage();
        final BehaviorModelSink writerStage = new BehaviorModelSink(baseURL, null);

        this.inputPort = decolStage.getInputPort();

        this.connectPorts(decolStage.getOutputPort(), writerStage.getInputPort());
    }

    public InputPort<BehaviorModel[]> getInputPort() {
        return this.inputPort;
    }
}
