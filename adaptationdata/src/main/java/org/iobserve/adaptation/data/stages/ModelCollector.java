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
package org.iobserve.adaptation.data.stages;

import java.io.File;

import teetime.framework.AbstractProducerStage;
import teetime.framework.InputPort;

import org.iobserve.adaptation.data.AdaptationData;

/**
 * Collects the runtime model as well as the redeployment model and adds them to the AdaptationData.
 *
 * @author Lars Bluemke
 *
 */
public class ModelCollector extends AbstractProducerStage<AdaptationData> {

    private final InputPort<File> runtimeModelDirInputPort = this.createInputPort();
    private final InputPort<File> redeploymentModelDirInputPort = this.createInputPort();
    private File runtimeModelDir;
    private File redeploymentModelDir;
    private boolean receivedRuntimeModel;
    private boolean receivedRedeploymentModel;

    @Override
    protected void execute() throws Exception {
        final AdaptationData adaptationData = new AdaptationData();

        if (!this.receivedRuntimeModel) {
            this.receivedRuntimeModel = true;
            this.runtimeModelDir = this.runtimeModelDirInputPort.receive();
            adaptationData.setRuntimeModelDir(this.runtimeModelDir);
        }

        if (!this.receivedRedeploymentModel) {
            this.receivedRedeploymentModel = true;
            this.redeploymentModelDir = this.redeploymentModelDirInputPort.receive();
            adaptationData.setReDeploymentModelDir(this.redeploymentModelDir);
        }

        if (this.receivedRuntimeModel && this.receivedRedeploymentModel) {
            this.getOutputPort().send(adaptationData);
            this.receivedRuntimeModel = false;
            this.receivedRedeploymentModel = false;
        }

    }

    public InputPort<File> getRuntimeModelInputPort() {
        return this.runtimeModelDirInputPort;
    }

    public InputPort<File> getRedeploymentModelInputPort() {
        return this.redeploymentModelDirInputPort;
    }

}
