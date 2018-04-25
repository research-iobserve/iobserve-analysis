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

import org.iobserve.planning.ModelOptimization;
import org.iobserve.planning.ModelProcessing;
import org.iobserve.stages.model.ModelDir2ModelFilesStage;
import org.iobserve.stages.model.ModelFiles2ModelDirCollectorStage;
import org.iobserve.stages.source.SingleConnectionTcpReaderStage;
import org.iobserve.stages.source.SingleConnectionTcpWriterStage;

/**
 * Configuration for the stages of the planning service.
 *
 * @author Lars Bluemke
 *
 */
public class PlanningConfiguration extends Configuration {

    /**
     * Creates a new instance of this class.
     *
     * @param planningInputPort
     *            Port where planning service receives new records via TCP
     * @param modelDirectory
     *            Directory where models are stored
     * @param perOpteryxHeadless
     *            PerOpteryx executable
     * @param lqnsDir
     *            LQN solver executable
     * @param adaptationHostname
     *            Host name of adaptation service
     * @param adaptationInputPort
     *            Input port of adaptation service
     */
    public PlanningConfiguration(final int planningInputPort, final File modelDirectory, final File perOpteryxHeadless,
            final File lqnsDir, final String adaptationHostname, final int adaptationRuntimeModelInputPort,
            final int adaptationRedeploymentModelInputPort) {

        final SingleConnectionTcpReaderStage tcpReader;
        final ModelFiles2ModelDirCollectorStage modelFilesCollector;
        final ModelProcessing modelPreProcessor;
        final ModelOptimization modelOptimizer;
        final ModelDir2ModelFilesStage runtimeModelDir2ModelFiles;
        final ModelDir2ModelFilesStage redeploymentModelDir2ModelFiles;
        final SingleConnectionTcpWriterStage runtimeTcpWriter;
        final SingleConnectionTcpWriterStage redeploymentTcpWriter;

        tcpReader = new SingleConnectionTcpReaderStage(planningInputPort, modelDirectory);
        modelFilesCollector = new ModelFiles2ModelDirCollectorStage();
        modelPreProcessor = new ModelProcessing();

        if ((perOpteryxHeadless != null) && (lqnsDir != null)) {
            modelOptimizer = new ModelOptimization(perOpteryxHeadless, lqnsDir);
        } else {
            throw new IllegalArgumentException(
                    "Failed to initialize ModelProcessing. Path to PerOpteryx or LQN solver must not be null!");
        }

        runtimeModelDir2ModelFiles = new ModelDir2ModelFilesStage();
        redeploymentModelDir2ModelFiles = new ModelDir2ModelFilesStage();
        runtimeTcpWriter = new SingleConnectionTcpWriterStage(adaptationHostname, adaptationRuntimeModelInputPort);
        redeploymentTcpWriter = new SingleConnectionTcpWriterStage(adaptationHostname,
                adaptationRedeploymentModelInputPort);

        this.connectPorts(tcpReader.getOutputPort(), modelFilesCollector.getInputPort());
        this.connectPorts(modelFilesCollector.getOutputPort(), modelPreProcessor.getInputPort());
        this.connectPorts(modelPreProcessor.getOutputPort(), modelOptimizer.getInputPort());
        this.connectPorts(modelOptimizer.getRuntimeModelOutputPort(), runtimeModelDir2ModelFiles.getInputPort());
        this.connectPorts(modelOptimizer.getRedeploymentModelOutputPort(),
                redeploymentModelDir2ModelFiles.getInputPort());
        this.connectPorts(runtimeModelDir2ModelFiles.getOutputPort(), runtimeTcpWriter.getInputPort());
        this.connectPorts(redeploymentModelDir2ModelFiles.getOutputPort(), redeploymentTcpWriter.getInputPort());
    }
}
