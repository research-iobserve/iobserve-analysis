/***************************************************************************
 * Copyright 2016 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis.service;

import java.net.MalformedURLException;

import teetime.framework.Configuration;
import teetime.framework.Execution;

/**
 * Thread class for the analysis service.
 *
 * @author Reiner Jung
 *
 */
public class AnalysisThread extends Thread {

    private final AnalysisDaemon daemon;

    private final Configuration configuration;

    public AnalysisThread(final AnalysisDaemon daemon, final Configuration configuration) throws MalformedURLException {
        this.daemon = daemon;
        this.configuration = configuration;

    }

    @Override
    public void run() {
        if (this.daemon.isRunning()) {
            final Execution<Configuration> analysis = new Execution<>(this.configuration);
            analysis.executeBlocking();
        }
    }

}