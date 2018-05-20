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
package org.iobserve.planning.configurations;

import java.io.File;

import teetime.framework.Configuration;

import org.iobserve.planning.stages.ModelOptimizationMockup;
import org.iobserve.stages.model.ModelDir2ModelFilesStage;
import org.iobserve.stages.source.SingleConnectionTcpWriterStage;

/**
 * A configuration to mock iObserve's planning service.
 *
 * @author Lars Bluemke
 *
 */
public class PlanningConfigurationMockup extends Configuration {

    /**
     * Creates a new instance of this class.
     *
     * @param runtimeModelDir
     *            Directory where runtime models are stored
     * @param redeploymentModelDir
     *            Directory where redeployment models are stored
     * @param adaptationHostname
     *            Host name of adaptation service
     * @param adaptationRuntimeModelInputPort
     *            Input port for runtime models at adaptation service
     * @param adaptationRedeploymentModelInputPort
     *            Input port for redeployment models at adaptation service
     */
    public PlanningConfigurationMockup(final File runtimeModelDir, final File redeploymentModelDir,
            final String adaptationHostname, final int adaptationRuntimeModelInputPort,
            final int adaptationRedeploymentModelInputPort) {
        final ModelOptimizationMockup modelOptimizationMockup;
        final ModelDir2ModelFilesStage runtimeModelDir2ModelFiles;
        final ModelDir2ModelFilesStage redeploymentModelDir2ModelFiles;
        final SingleConnectionTcpWriterStage runtimeTcpWriter;
        final SingleConnectionTcpWriterStage redeploymentTcpWriter;

        modelOptimizationMockup = new ModelOptimizationMockup(runtimeModelDir, redeploymentModelDir);
        runtimeModelDir2ModelFiles = new ModelDir2ModelFilesStage();
        redeploymentModelDir2ModelFiles = new ModelDir2ModelFilesStage();
        runtimeTcpWriter = new SingleConnectionTcpWriterStage(adaptationHostname, adaptationRuntimeModelInputPort);
        redeploymentTcpWriter = new SingleConnectionTcpWriterStage(adaptationHostname,
                adaptationRedeploymentModelInputPort);

        this.connectPorts(modelOptimizationMockup.getRuntimeModelOutputPort(),
                runtimeModelDir2ModelFiles.getInputPort());
        this.connectPorts(modelOptimizationMockup.getRedeploymentModelOutputPort(),
                redeploymentModelDir2ModelFiles.getInputPort());
        this.connectPorts(runtimeModelDir2ModelFiles.getOutputPort(), runtimeTcpWriter.getInputPort());
        this.connectPorts(redeploymentModelDir2ModelFiles.getOutputPort(), redeploymentTcpWriter.getInputPort());

        modelOptimizationMockup.declareActive();
    }
}
