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
package org.iobserve.adaptation.configurations;

import java.io.File;

import teetime.framework.Configuration;

import org.iobserve.adaptation.stages.AdaptationResultDistributor;
import org.iobserve.adaptation.stages.ExecutionPlanSerializationMockup;
import org.iobserve.stages.model.ModelDir2ModelFilesStage;
import org.iobserve.stages.source.SingleConnectionTcpWriterStage;

/**
 * A configuration for mocking iObserve's adaptation service.
 *
 * @author Lars Bluemke
 *
 */
public class AdaptationConfigurationMockup extends Configuration {

    public AdaptationConfigurationMockup(final File runtimeModelDirectory, final File redeploymentModelDirectory,
            final File executionPlanURI, final String executionHostname, final int executionPlanInputPort,
            final int executionRuntimeModelInputPort, final int executionRedeploymentModelInputPort) {
        final ExecutionPlanSerializationMockup executionPlanSerializer = new ExecutionPlanSerializationMockup(
                executionPlanURI);
        final AdaptationResultDistributor adaptationResultDistributor = new AdaptationResultDistributor(
                runtimeModelDirectory, redeploymentModelDirectory);
        final ModelDir2ModelFilesStage runtimeModelDir2ModelFiles = new ModelDir2ModelFilesStage();
        final ModelDir2ModelFilesStage redeploymentModelDir2ModelFiles = new ModelDir2ModelFilesStage();
        final SingleConnectionTcpWriterStage executionPlanWriter = new SingleConnectionTcpWriterStage(executionHostname,
                executionPlanInputPort);
        final SingleConnectionTcpWriterStage runtimeModelWriter = new SingleConnectionTcpWriterStage(executionHostname,
                executionRuntimeModelInputPort);
        final SingleConnectionTcpWriterStage redeploymentModelWriter = new SingleConnectionTcpWriterStage(
                executionHostname, executionRedeploymentModelInputPort);

        // Distribute adaptation results (execution plan and models) and send them to execution
        this.connectPorts(executionPlanSerializer.getOutputPort(), adaptationResultDistributor.getInputPort());
        this.connectPorts(adaptationResultDistributor.getExecutionPlanOutputPort(), executionPlanWriter.getInputPort());
        this.connectPorts(adaptationResultDistributor.getRuntimeModelDirectoryOutputPort(),
                runtimeModelDir2ModelFiles.getInputPort());
        this.connectPorts(adaptationResultDistributor.getRedeploymentModelDirectoryOutputPort(),
                redeploymentModelDir2ModelFiles.getInputPort());
        this.connectPorts(runtimeModelDir2ModelFiles.getOutputPort(), runtimeModelWriter.getInputPort());
        this.connectPorts(redeploymentModelDir2ModelFiles.getOutputPort(), redeploymentModelWriter.getInputPort());
    }

}
