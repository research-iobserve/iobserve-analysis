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

import org.iobserve.planning.ModelOptimization;
import org.iobserve.planning.ModelProcessing;
import org.iobserve.stages.model.ModelDir2ModelFilesStage;
import org.iobserve.stages.model.ModelFiles2ModelDirCollectorStage;
import org.iobserve.stages.source.SingleConnectionTcpReaderStage;
import org.iobserve.stages.source.SingleConnectionTcpWriterStage;

import teetime.framework.Configuration;

/**
 * Configuration for the stages of the planning service.
 *
 * @author Lars Bluemke
 *
 */
public class PlanningConfiguration extends Configuration {

    private final SingleConnectionTcpReaderStage tcpReader;
    private final ModelFiles2ModelDirCollectorStage modelFilesCollector;
    private final ModelProcessing modelPreProcessor;
    private final ModelOptimization modelOptimizer;
    private final ModelDir2ModelFilesStage modelDir2ModelFiles;
    private final SingleConnectionTcpWriterStage tcpWriter;

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
            final File lqnsDir, final String adaptationHostname, final int adaptationInputPort) {

        this.tcpReader = new SingleConnectionTcpReaderStage(planningInputPort, modelDirectory);
        this.modelFilesCollector = new ModelFiles2ModelDirCollectorStage();

        // TODO: Only removed for debugging
        // if ((perOpteryxHeadless != null) && (lqnsDir != null)) {
        this.modelPreProcessor = new ModelProcessing(perOpteryxHeadless, lqnsDir);
        // } else {
        // throw new IllegalArgumentException(
        // "Failed to initialize ModelProcessing. Path to PerOpteryx or LQN solver must not be
        // null!");
        // }

        this.modelOptimizer = new ModelOptimization();
        this.modelDir2ModelFiles = new ModelDir2ModelFilesStage();
        this.tcpWriter = new SingleConnectionTcpWriterStage(adaptationHostname, adaptationInputPort);

        this.connectPorts(this.tcpReader.getOutputPort(), this.modelFilesCollector.getInputPort());
        this.connectPorts(this.modelFilesCollector.getOutputPort(), this.modelPreProcessor.getInputPort());
        this.connectPorts(this.modelPreProcessor.getOutputPort(), this.modelOptimizer.getInputPort());
        this.connectPorts(this.modelOptimizer.getOutputPort(), this.modelDir2ModelFiles.getInputPort());
        this.connectPorts(this.modelDir2ModelFiles.getOutputPort(), this.tcpWriter.getInputPort());
    }
}
