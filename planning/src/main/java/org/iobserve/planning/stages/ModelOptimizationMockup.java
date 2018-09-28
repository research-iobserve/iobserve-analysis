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
package org.iobserve.planning.stages;

import java.io.File;

import teetime.framework.AbstractStage;
import teetime.framework.OutputPort;

/**
 * Mocks the model optimization by taking predefined runtime and redeployment models and sending it
 * to its output ports.
 *
 * @author Lars Bluemke
 *
 */
public class ModelOptimizationMockup extends AbstractStage {

    private final OutputPort<File> runtimeModelOutputPort = this.createOutputPort();
    private final OutputPort<File> redeploymentModelOutputPort = this.createOutputPort();
    private final File runtimeModelDir;
    private final File redeploymentModelDir;

    /**
     * Creates a new instance of this stage.
     *
     * @param runtimeModelDir
     *            Directory containing the runtime model
     * @param redeploymentModelDir
     *            Directory containing the redeployment model
     */
    public ModelOptimizationMockup(final File runtimeModelDir, final File redeploymentModelDir) {
        this.runtimeModelDir = runtimeModelDir;
        this.redeploymentModelDir = redeploymentModelDir;
    }

    @Override
    protected void execute() throws Exception {
        this.runtimeModelOutputPort.send(this.runtimeModelDir);
        this.redeploymentModelOutputPort.send(this.redeploymentModelDir);

        this.workCompleted();
    }

    public OutputPort<File> getRuntimeModelOutputPort() {
        return this.runtimeModelOutputPort;
    }

    public OutputPort<File> getRedeploymentModelOutputPort() {
        return this.redeploymentModelOutputPort;
    }

}
