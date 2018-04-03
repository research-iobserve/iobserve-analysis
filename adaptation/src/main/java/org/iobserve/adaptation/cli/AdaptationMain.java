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
package org.iobserve.adaptation.cli;

import teetime.framework.Configuration;
import teetime.framework.Execution;

import org.iobserve.adaptation.configurations.AdaptationConfigurationDebug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for iObserve's adaptation service.
 *
 * @author Lars Bluemke
 *
 */
public class AdaptationMain {

    private static final Logger LOG = LoggerFactory.getLogger(AdaptationMain.class);

    public static void main(final String[] args) {
        new AdaptationMain().run();

    }

    private void run() {
        final Execution<AdaptationConfigurationDebug> execution = new Execution<>(new AdaptationConfigurationDebug());

        this.shutdownHook(execution);

        AdaptationMain.LOG.debug("Running Adaptation");

        execution.executeBlocking();

        AdaptationMain.LOG.debug("Done");
    }

    private <R extends Configuration> void shutdownHook(final Execution<R> execution) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (execution) {
                    execution.abortEventually();
                    AdaptationMain.this.shutdownService();
                }
            }
        }));

    }

    protected void shutdownService() {
        // serialize PCM models.
    }
}
