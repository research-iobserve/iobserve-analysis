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
package org.iobserve.execution.cli;

import teetime.framework.Configuration;
import teetime.framework.Execution;

import org.iobserve.execution.configurations.ExecutionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for iObserve's execution service.
 *
 * @author Lars Bluemke
 *
 */
public class ExecutionMain {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutionMain.class);

    public static void main(final String[] args) {
        new ExecutionMain().run();

    }

    private void run() {
        final Execution<ExecutionConfiguration> execution = new Execution<>(new ExecutionConfiguration(null, null));

        this.shutdownHook(execution);

        ExecutionMain.LOG.debug("Running Execution");

        execution.executeBlocking();

        ExecutionMain.LOG.debug("Done");
    }

    private <R extends Configuration> void shutdownHook(final Execution<R> execution) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (execution) {
                    execution.abortEventually();
                    ExecutionMain.this.shutdownService();
                }
            }
        }));

    }

    protected void shutdownService() {
        // TODO serialize PCM models.
    }
}
