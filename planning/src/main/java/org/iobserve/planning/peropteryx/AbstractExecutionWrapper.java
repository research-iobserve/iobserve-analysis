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
package org.iobserve.planning.peropteryx;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is an abstract execution warpper for starting an headless PerOpteryx instance.
 *
 * @author Philipp Weimann
 *
 */
public abstract class AbstractExecutionWrapper {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ExecutionWrapper.class);

    private final URI inputModelDir;
    private final URI perOpteryxDir;
    private final URI lqnsDir;

    /**
     * The constructor.
     *
     * @param inputModelDir
     *            the directory containing the pcm model for modification
     * @param perOpteryxDir
     *            the headless PerOpteryx directory
     * @param lqnsDir
     *            layered queuing networks directory
     */
    public AbstractExecutionWrapper(final URI inputModelDir, final URI perOpteryxDir, final URI lqnsDir) {
        this.inputModelDir = inputModelDir;
        this.perOpteryxDir = perOpteryxDir;
        this.lqnsDir = lqnsDir;
    }

    /**
     * Generates and starts the execution of PerOpteryx. The main thread gets blocked until the
     * execution is finished.
     *
     * @return the process exit value
     */
    public int startModelGeneration() {
        final ProcessBuilder processBuilder = this.createProcess();
        Process process = null;
        try {
            process = processBuilder.start();
            this.watch(process);
        } catch (IOException | InterruptedException e) {
            AbstractExecutionWrapper.LOGGER
                    .error("Could not start PerOpteryx executable process. Check the parameters and the log files!", e);
            return -1;
        }
        return process.exitValue();
    }

    /**
     * This method creates the PerOpteryx headless process with all the command line arguments. It
     * doesn't start the process.
     *
     * @return the PerOpteryx process
     */
    public abstract ProcessBuilder createProcess();

    /**
     * This method watches the process, writes the process output into the console and blocks the
     * main thread until PerOpteryx finishes execution.
     *
     * @param process
     *            the process executing the PerOpteryx headless
     * @throws InterruptedException
     *             when the thread gets interrupted
     */
    public abstract void watch(final Process process) throws InterruptedException;

    /*
     * HELPERS
     */
    /**
     * @return the inputModelDir
     */
    protected URI getInputModelDir() {
        return this.inputModelDir;
    }

    /**
     * @return the perOpteryxDir
     */
    protected URI getPerOpteryxDir() {
        return this.perOpteryxDir;
    }

    protected URI getLQNSDir() {
        return this.lqnsDir;
    }
}
