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
package org.iobserve.planning;

import java.io.File;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class creates potential migration candidates via PerOpteryx. The class is OS independent.
 *
 * @author Philipp Weimann
 */
public class ModelOptimization extends AbstractTransformation<File, File> {

    private static final int EXEC_SUCCESS = 0;

    private final File perOpteryxDir;
    private final File lqnsDir;

    /**
     * Creates a new model Optimization stage. The location of executables of PerOpteryx and the
     * required LQN solver have to be defined.
     *
     * @param perOpteryxDir
     *            the location of the headless PerOpteryx executable
     * @param lqnsDir
     *            directory for layered queuing networks
     */
    public ModelOptimization(final File perOpteryxDir, final File lqnsDir) {
        this.perOpteryxDir = perOpteryxDir;
        this.lqnsDir = lqnsDir;
    }

    @Override
    protected void execute(final File modelDirectory) throws Exception {

        // final int result = 2; // exec.startModelGeneration();
        //
        // if (result != ModelOptimization.EXEC_SUCCESS) {
        // // adaptationData.setReDeploymentURI(URI.createFileURI(
        // //
        // "C:\\GitRepositorys\\iobserve-analysis\\analysis\\res\\working_dir\\snapshot\\processedModel\\PerOpteryx_results\\costOptimalModel"));
        // throw new RuntimeException("PerOpteryx exited with error code " + result);
        // // String uriString =
        // // "C:\\GitRepositorys\\iobserve-analysis\\analysis\\res\\working_dir\\snapshot\\Test";
        //
        // // adaptationData.setReDeploymentURI(URI.createFileURI(uriString));
        // }

        // TODO: Execute PerOpteryx here

        // dead code.. what was the purpose?
        this.outputPort.send(modelDirectory);
    }

}
