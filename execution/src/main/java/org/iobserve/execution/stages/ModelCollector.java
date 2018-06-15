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
package org.iobserve.execution.stages;

import java.io.File;

import teetime.framework.AbstractProducerStage;
import teetime.framework.InputPort;

/**
 * Collects the incoming execution plan as well as the pcm models before the execution is triggered.
 *
 * @author Lars Bluemke
 *
 */
public class ModelCollector extends AbstractProducerStage<File> {
    private final InputPort<File> executionPlanInputPort = this.createInputPort();
    private final InputPort<File> runtimeModelDirInputPort = this.createInputPort();
    private final InputPort<File> redeploymentModelDirInputPort = this.createInputPort();
    private File executionPlan;
    private boolean receivedExecutionPlan;
    private boolean receivedRuntimeModel;
    private boolean receivedRedeploymentModel;

    @Override
    protected void execute() throws Exception {

        if (!this.receivedExecutionPlan) {
            this.executionPlan = this.executionPlanInputPort.receive();

            if (this.executionPlan != null) {
                this.receivedExecutionPlan = true;
            }
        }

        if (!this.receivedRuntimeModel && (this.runtimeModelDirInputPort.receive() != null)) {
            this.receivedRuntimeModel = true;
        }

        if (!this.receivedRedeploymentModel && (this.redeploymentModelDirInputPort.receive() != null)) {
            this.receivedRedeploymentModel = true;
        }

        if (this.receivedExecutionPlan && this.receivedRuntimeModel && this.receivedRedeploymentModel) {
            this.getOutputPort().send(this.executionPlan);
            this.receivedExecutionPlan = false;
            this.receivedRuntimeModel = false;
            this.receivedRedeploymentModel = false;
        }

    }

    public InputPort<File> getExecutionPlanInputPort() {
        return this.executionPlanInputPort;
    }

    public InputPort<File> getRuntimeModelInputPort() {
        return this.runtimeModelDirInputPort;
    }

    public InputPort<File> getRedeploymentModelInputPort() {
        return this.redeploymentModelDirInputPort;
    }
}
