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
package org.iobserve.analysis.behavior.filter.similaritymatching;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.models.extended.BehaviorModel;

/**
 * Stage that accepts an array of behavior models and sends them to its output
 * port individually
 * 
 * @author Jannis Kuckei
 *
 */
public class BehaviorModelDecollectorStage extends AbstractConsumerStage<BehaviorModel[]> {
    private final OutputPort<BehaviorModel> outputPort = this.createOutputPort();

    @Override
    protected void execute(final BehaviorModel[] models) throws Exception {
        for (final BehaviorModel model : models) {
            this.outputPort.send(model);
        }
    }

    public OutputPort<BehaviorModel> getOutputPort() {
        return this.outputPort;
    }
}
