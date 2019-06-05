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

import java.util.List;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import mtree.MTree;

/**
 *
 * @author Lars Jürgensen
 *
 * @param <T>
 */
public class OpticsStage<T> extends AbstractStage {

    private final OutputPort<List<OpticsData>> outputPort = this.createOutputPort();
    private final InputPort<MTree<T>> mTreeInputPort = this.createInputPort();
    private final InputPort<List<T>> modelsInputPort = this.createInputPort();

    private final double maxDistance;
    private final int minPTs;

    public OpticsStage(final double maxDistance, final int minPTs) {
        this.minPTs = minPTs;
        this.maxDistance = maxDistance;
    }

    @Override
    protected void execute() throws Exception {
        final MTree<T> mtree = this.mTreeInputPort.receive();
        final List<T> models = this.modelsInputPort.receive();

    }

    public OutputPort<List<OpticsData>> getOutputPort() {
        return this.outputPort;
    }

    public InputPort<MTree<T>> getmTreeInputPort() {
        return this.mTreeInputPort;
    }

    public InputPort<List<T>> getModelsInputPort() {
        return this.modelsInputPort;
    }

}
