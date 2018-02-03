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
package org.iobserve.planning.cli;

import java.io.File;

import org.iobserve.planning.configurations.PlanningConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import teetime.framework.Configuration;
import teetime.framework.Execution;

/**
 * Main class for iObserve's adaptation service.
 *
 * @author Lars Bluemke
 *
 */
public class PlanningMainDebug {

    private static final Logger LOG = LoggerFactory.getLogger(PlanningMainDebug.class);

    private final int planningInputPort = 12349;
    private final File modelDirectory = new File("/Users/LarsBlumke/Documents/CAU/Masterarbeit/working-dir-planning");
    private final File perOpteryxHeadless = new File("/Users/LarsBlumke/Documents/CAU/Masterarbeit/imaginaryPath/PO");
    private final File lqnsDir = new File("/Users/LarsBlumke/Documents/CAU/Masterarbeit/imaginaryPath/LQNS");
    private final String adaptationHostname = "localhost";
    private final int adaptationInputPort = 12346;

    public static void main(final String[] args) {
        new PlanningMainDebug().run();

    }

    private void run() {
        final Execution<PlanningConfiguration> execution = new Execution<>(
                new PlanningConfiguration(this.planningInputPort, this.modelDirectory, this.perOpteryxHeadless,
                        this.lqnsDir, this.adaptationHostname, this.adaptationInputPort));

        this.shutdownHook(execution);

        PlanningMainDebug.LOG.debug("Running Adaptation");

        execution.executeBlocking();

        PlanningMainDebug.LOG.debug("Done");
    }

    private <R extends Configuration> void shutdownHook(final Execution<R> execution) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (execution) {
                        execution.abortEventually();
                        PlanningMainDebug.this.shutdownService();
                    }
                } catch (final Exception e) { // NOCS

                }
            }
        }));

    }

    protected void shutdownService() {
        // TODO serialize PCM models.
    }
}
