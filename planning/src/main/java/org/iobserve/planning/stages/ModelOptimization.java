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
package org.iobserve.planning.stages;

import java.io.File;
import java.io.IOException;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.apache.commons.io.FileUtils;
import org.iobserve.planning.peropteryx.ExecutionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates potential migration candidates via PerOpteryx. The class is OS independent.
 *
 * @author Philipp Weimann
 */
public class ModelOptimization extends AbstractConsumerStage<File> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelOptimization.class);

    private static final int EXEC_SUCCESS = 0;

    private final OutputPort<File> runtimeModelOutputPort = this.createOutputPort();
    private final OutputPort<File> redeploymentModelOutputPort = this.createOutputPort();

    private final File perOpteryxDir;
    private final File lqnsDir;
    private final File redeploymentModelDirectory;

    /**
     * Creates a new model Optimization stage. The location of executables of PerOpteryx and the
     * required LQN solver have to be defined.
     *
     * @param perOpteryxDir
     *            the location of the headless PerOpteryx executable
     * @param lqnsDir
     *            directory for layered queuing networks
     * @param redeploymentModelDirectory
     *            directory for redeployment model
     */
    public ModelOptimization(final File perOpteryxDir, final File lqnsDir, final File redeploymentModelDirectory) {
        this.perOpteryxDir = perOpteryxDir;
        this.lqnsDir = lqnsDir;
        this.redeploymentModelDirectory = redeploymentModelDirectory;
    }

    @Override
    protected void execute(final File processedRuntimeModelDirectory) throws RuntimeException, IOException {
        final ExecutionWrapper exec = new ExecutionWrapper(this.redeploymentModelDirectory, this.perOpteryxDir,
                this.lqnsDir);

        // Create directory for redeployment model
        FileUtils.copyDirectory(processedRuntimeModelDirectory, this.redeploymentModelDirectory);

        // Execute PerOpteryx (On first start there may occur a NullPointerException for unknown
        // reasons - start twice here?)
        final int result = exec.startModelGeneration();

        if ((result != ModelOptimization.EXEC_SUCCESS) && ModelOptimization.LOGGER.isErrorEnabled()) {
            ModelOptimization.LOGGER.error("PerOpteryx exited with error code " + result);
        } else {
            this.runtimeModelOutputPort.send(processedRuntimeModelDirectory);
            this.redeploymentModelOutputPort.send(this.redeploymentModelDirectory);
        }
    }

    public OutputPort<File> getRuntimeModelOutputPort() {
        return this.runtimeModelOutputPort;
    }

    public OutputPort<File> getRedeploymentModelOutputPort() {
        return this.redeploymentModelOutputPort;
    }

}
