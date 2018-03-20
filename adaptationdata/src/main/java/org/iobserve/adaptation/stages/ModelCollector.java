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
package org.iobserve.adaptation.stages;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;

import teetime.framework.AbstractProducerStage;
import teetime.framework.InputPort;

/**
 * Collects the runtime model as well as the redeployment model and adds them to the AdaptationData.
 *
 * @author Lars Bluemke
 *
 */
public class ModelCollector extends AbstractProducerStage<AdaptationData> {

    private final InputPort<File> runtimeModelDirInputPort = this.createInputPort();
    private final InputPort<File> redeploymentModelDirInputPort = this.createInputPort();
    private File runtimeModelDir = null;
    private File redeploymentModelDir = null;

    @Override
    protected void execute() throws Exception {
        final AdaptationData adaptationData = new AdaptationData();

        if (this.runtimeModelDir == null) {
            this.runtimeModelDir = this.runtimeModelDirInputPort.receive();
            adaptationData.setRuntimeModelURI(URI.createFileURI(this.runtimeModelDir.getAbsolutePath()));
        }

        if (this.redeploymentModelDir == null) {
            this.redeploymentModelDir = this.redeploymentModelDirInputPort.receive();
            adaptationData.setReDeploymentURI(URI.createFileURI(this.redeploymentModelDir.getAbsolutePath()));
        }

        if ((this.runtimeModelDir != null) && (this.redeploymentModelDir != null)) {
            this.getOutputPort().send(adaptationData);
            this.runtimeModelDir = null;
            this.redeploymentModelDir = null;
        }

    }

    public InputPort<File> getRuntimeModelInputPort() {
        return this.runtimeModelDirInputPort;
    }

    public InputPort<File> getRedeploymentModelInputPort() {
        return this.redeploymentModelDirInputPort;
    }

}
