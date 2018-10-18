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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Wrapper for executing headless PerOpteryx.
 *
 * @author Tobias Pöppke
 * @author Philipp Weimann
 *
 */
public class ExecutionWrapper extends AbstractExecutionWrapper {

    private String execEnvironment;
    private String execEnvironmentParam;
    private String execCommand;

    /**
     * Create an execution wrapper.
     *
     * @param inputModelDir
     *            input model directory
     * @param perOpteryxDir
     *            perOpteryx setup directory
     * @param lqnsDir
     *            layered queuing networks directory
     * @throws IOException
     *             on io error reading files
     */
    public ExecutionWrapper(final File inputModelDir, final File perOpteryxDir, final File lqnsDir) throws IOException {
        super(inputModelDir, perOpteryxDir, lqnsDir);

        if (this.isWindows()) {
            this.execEnvironment = "cmd.exe";
            this.execEnvironmentParam = "/C";
            this.execCommand = "java  -jar .\\plugins\\org.eclipse.equinox.launcher_1.3.201.v20161025-1711.jar";
        } else {
            this.execEnvironment = "/bin/bash";
            this.execEnvironmentParam = "-c";
            this.execCommand = "./eclipse";
        }

    }

    private boolean isWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

    @Override
    public void watch(final Process process) throws InterruptedException {
        final Thread watcherThread = new Thread(() -> {
            final BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            try {
                while ((line = input.readLine()) != null) {
                    if (AbstractExecutionWrapper.LOGGER.isErrorEnabled()) {
                        AbstractExecutionWrapper.LOGGER.error("PerOpteryx Output: " + line);
                        // LOG.info("PerOpteryx Output: " + line);
                    }
                }
            } catch (final IOException e) {
                if (AbstractExecutionWrapper.LOGGER.isErrorEnabled()) {
                    AbstractExecutionWrapper.LOGGER.error("Watcher Thread terminated");
                    // LOG.error("IOException during PerOpteryx run: " +
                    // e.getStackTrace());
                }
            }
        });
        synchronized (watcherThread) {
            if (AbstractExecutionWrapper.LOGGER.isInfoEnabled()) {
                AbstractExecutionWrapper.LOGGER.info("Starting Watcher Thread!");
            }

            watcherThread.start();
            watcherThread.wait();

            if (AbstractExecutionWrapper.LOGGER.isInfoEnabled()) {
                AbstractExecutionWrapper.LOGGER.info("Watcher Thread terminated");
            }
        }
    }

    @Override
    public ProcessBuilder createProcess() {
        if (AbstractExecutionWrapper.LOGGER.isInfoEnabled()) {
            AbstractExecutionWrapper.LOGGER.info("Starting optimization process...");
        }
        final String modelDir = this.getInputModelDir().getAbsolutePath();

        final ProcessBuilder builder = new ProcessBuilder(this.execEnvironment, this.execEnvironmentParam,
                this.execCommand + " -w " + modelDir);

        final String perOpteryxDir = this.getPerOpteryxDir().getAbsolutePath();
        final Map<String, String> env = builder.environment();

        String path;
        if (this.isWindows()) {
            path = env.get("Path");
            path = this.getLQNSDir().getAbsolutePath() + ";" + path;
            env.put("Path", path);
        } else {
            path = env.get("PATH");
            path = this.getLQNSDir().getAbsolutePath() + ":" + path;
            env.put("PATH", path);
        }

        if (AbstractExecutionWrapper.LOGGER.isInfoEnabled()) {
            AbstractExecutionWrapper.LOGGER.info("Environment PATH: " + path);
        }
        builder.directory(new File(perOpteryxDir));
        builder.redirectOutput();
        builder.redirectErrorStream(true);

        return builder;
    }
}
