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
package org.iobserve.service.behavior.analysis;

import java.io.File;

import com.beust.jcommander.JCommander;

import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;

/**
 *
 * @author Lars Jürgensen
 *
 */
public final class BehaviorAnalysisServiceMain
        extends AbstractService<BehaviorAnalysisTeetimeConfiguration, BehaviorAnalysisSettings> {

    /**
     * Default constructor.
     */
    private BehaviorAnalysisServiceMain() {
        // do nothing here
    }

    @Override
    protected BehaviorAnalysisTeetimeConfiguration createTeetimeConfiguration() throws ConfigurationException {

        return new BehaviorAnalysisTeetimeConfiguration(this.kiekerConfiguration);

    }

    /**
     * Main function.
     *
     * @param args
     *            command line arguments.
     */
    public static void main(final String[] args) {
        java.lang.System.exit(new BehaviorAnalysisServiceMain().run("Service Behavior Analysis",
                "service-behavior-analysis", args, new BehaviorAnalysisSettings()));
    }

    @Override
    protected File getConfigurationFile() {
        return this.parameterConfiguration.getConfigurationFile();
    }

    @Override
    protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration,
            final JCommander commander) {
        return true;
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        return true;
    }

    @Override
    protected void shutdownService() {
    }

}
