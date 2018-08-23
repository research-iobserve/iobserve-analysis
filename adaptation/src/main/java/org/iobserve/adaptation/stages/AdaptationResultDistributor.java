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

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * This stage forwards the incoming serialized execution plan to the tcp writer and also triggers
 * the writers for runtime and redeloyment models.
 *
 * @author Lars Bluemke
 *
 */
public class AdaptationResultDistributor extends AbstractConsumerStage<File> {
    private final File runtimeModelDirectory;
    private final File redeploymentModelDirectory;
    private final OutputPort<File> executionPlanOutputPort;
    private final OutputPort<File> runtimeModelDirectoryOutputPort;
    private final OutputPort<File> redeploymentModelDirectoryOutputPort;

    /**
     * Create an adaptation result distributor.
     *
     * @param runtimeModelDirectory
     *            runtime model directory
     * @param redeploymentModelDirectory
     *            redeployment model directory
     */
    public AdaptationResultDistributor(final File runtimeModelDirectory, final File redeploymentModelDirectory) {
        super();
        this.runtimeModelDirectory = runtimeModelDirectory;
        this.redeploymentModelDirectory = redeploymentModelDirectory;
        this.executionPlanOutputPort = this.createOutputPort();
        this.runtimeModelDirectoryOutputPort = this.createOutputPort();
        this.redeploymentModelDirectoryOutputPort = this.createOutputPort();
    }

    @Override
    protected void execute(final File executionPlan) throws Exception {
        // Trigger runtime model writer
        this.runtimeModelDirectoryOutputPort.send(this.runtimeModelDirectory);

        // Trigger redeployment model writer
        this.redeploymentModelDirectoryOutputPort.send(this.redeploymentModelDirectory);

        // Forward the executionPlan
        this.executionPlanOutputPort.send(executionPlan);
    }

    public OutputPort<File> getExecutionPlanOutputPort() {
        return this.executionPlanOutputPort;
    }

    public OutputPort<File> getRuntimeModelDirectoryOutputPort() {
        return this.runtimeModelDirectoryOutputPort;
    }

    public OutputPort<File> getRedeploymentModelDirectoryOutputPort() {
        return this.redeploymentModelDirectoryOutputPort;
    }

}
