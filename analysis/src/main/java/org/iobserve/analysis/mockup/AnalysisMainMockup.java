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
package org.iobserve.analysis.mockup;

import teetime.framework.Configuration;
import teetime.framework.Execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for mocking iObserve's analysis service.
 *
 * @author Lars Bluemke
 *
 */
public class AnalysisMainMockup {

    private static final Logger LOG = LoggerFactory.getLogger(AnalysisMainMockup.class);

    public static void main(final String[] args) {
        new AnalysisMainMockup().run();

    }

    private void run() {
        final Execution<AnalysisConfigurationMockup> execution = new Execution<>(new AnalysisConfigurationMockup());

        this.shutdownHook(execution);

        AnalysisMainMockup.LOG.debug("Running Analysis");

        execution.executeBlocking();

        AnalysisMainMockup.LOG.debug("Done");
    }

    private <R extends Configuration> void shutdownHook(final Execution<R> execution) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (execution) {
                        execution.abortEventually();
                        AnalysisMainMockup.this.shutdownService();
                    }
                } catch (final Exception e) { // NOCS NOPMD framework uses Exception

                }
            }
        }));

    }

    protected void shutdownService() {
        // TODO serialize PCM models.
    }
}
