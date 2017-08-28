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
package org.iobserve.evaluation;

import org.iobserve.analysis.cdoruserbehavior.filter.models.BehaviorModel;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * @author reiner
 *
 */
public class ModelComparisonStage extends AbstractStage {

    private final InputPort<BehaviorModel> baselineModelInputPort = this.createInputPort();
    private final InputPort<BehaviorModel> testModelInputPort = this.createInputPort();
    private final OutputPort<ComparisonResult> resultPort = this.createOutputPort();

    private BehaviorModel baselineModel;

    private BehaviorModel testModel;

    /*
     * (non-Javadoc)
     *
     * @see teetime.framework.AbstractStage#execute()
     */
    @Override
    protected void execute() throws Exception {
        if (this.baselineModel == null) {
            this.baselineModel = this.baselineModelInputPort.receive();
        }
        if (this.testModel == null) {
            this.testModel = this.testModelInputPort.receive();
        }

        if (this.baselineModel != null && this.testModel != null) {
            final ComparisonResult result = new ComparisonResult();
            this.resultPort.send(result);
        }
    }

    public InputPort<BehaviorModel> getBaselineModelInputPort() {
        return this.baselineModelInputPort;
    }

    public InputPort<BehaviorModel> getTestModelInputPort() {
        return this.testModelInputPort;
    }

    public OutputPort<ComparisonResult> getOutputPort() {
        return this.resultPort;
    }

}
