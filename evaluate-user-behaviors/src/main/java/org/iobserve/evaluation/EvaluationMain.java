/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.evaluation;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import org.iobserve.analysis.ConfigurationException;
import org.iobserve.service.AbstractServiceMain;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class EvaluationMain extends AbstractServiceMain<EvaluationConfiguration> {

    private static final Log LOG = LogFactory.getLog(EvaluationMain.class);

    @Parameter(names = { "-b",
            "--baseline-model" }, required = true, description = "Baseline model file.", converter = FileConverter.class)
    private File baselineModelLocation;

    @Parameter(names = { "-t",
            "--test-model" }, required = true, description = "Test model file.", converter = FileConverter.class)
    private File testModelLocation;

    @Parameter(names = { "-o",
            "--output" }, required = true, description = "Result file.", converter = FileConverter.class)
    private File targetLocation;

    /**
     * This is a simple main class which does not need to be instantiated.
     */
    private EvaluationMain() {

    }

    /**
     * Configure and execute the evaluation tool.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {
        new EvaluationMain().run("Evaluation of behavior models.", "evaluation", args);
    }

    @Override
    protected EvaluationConfiguration createConfiguration(final Configuration configuration)
            throws ConfigurationException {
        try {
            return new EvaluationConfiguration(this.baselineModelLocation, this.testModelLocation, this.targetLocation);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        try {
            if (!this.baselineModelLocation.canRead()) {
                EvaluationMain.LOG.error("reading baseline failed: " + this.baselineModelLocation.getCanonicalPath());
                commander.usage();
                return false;
            }
            if (!this.testModelLocation.canRead()) {
                EvaluationMain.LOG.error("reading test model failed: " + this.testModelLocation.getCanonicalPath());
                commander.usage();
                return false;
            }

            return true;
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected File getConfigurationFile() {
        return null;
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        return true;
    }

    @Override
    protected void shutdownService() {

    }

}
