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
package org.iobserve.compare.behavior.models;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;

import teetime.framework.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User behavior comparison main class.
 *
 * @author Lars Jürgensen
 */
public final class ComparisonMain extends AbstractService<ComparisionConfiguration, ComparisonMain> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComparisonMain.class);

    @Parameter(names = { "-b",
            "--baseline-model" }, required = true, description = "Baseline model file.", converter = FileConverter.class)
    private File baselineModelLocation;

    @Parameter(names = { "-t",
            "--test-model" }, required = true, description = "DeserializationTest model file.", converter = FileConverter.class)
    private File testModelLocation;

    @Parameter(names = { "-o",
            "--output" }, required = true, description = "Result file.", converter = FileConverter.class)
    private File targetLocation;

    @Parameter(names = { "-s", "--sinkstage" }, required = true, description = "TeeTime sink stage.")
    private String sinkStage;

    /**
     * This is a simple main class which does not need to be instantiated.
     */
    private ComparisonMain() {

    }

    /**
     * Configure and execute the evaluation tool.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {
        try {
            final ComparisonMain main = new ComparisonMain();
            System.exit(main.run("Evaluation of behavior models.", "evaluation", args, main));
        } catch (final ExecutionException ex) {
            final Map<Thread, List<Exception>> exceptions = ex.getThrownExceptions();
            for (final Thread th : exceptions.keySet()) {
                ComparisonMain.LOGGER.error("Exception in thread {}: {}", th.getName(), exceptions.get(th).toString());
            }
        }
    }

    @Override
    protected ComparisionConfiguration createTeetimeConfiguration() throws ConfigurationException {
        try {
            return new ComparisionConfiguration(this.baselineModelLocation, this.testModelLocation, this.targetLocation,
                    this.sinkStage);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        try {
            if (!this.baselineModelLocation.canRead()) {
                ComparisonMain.LOGGER.error("reading baseline failed: {}",
                        this.baselineModelLocation.getCanonicalPath());
                commander.usage();
                return false;
            }
            if (!this.testModelLocation.canRead()) {
                ComparisonMain.LOGGER.error("reading test model failed: {}", this.testModelLocation.getCanonicalPath());
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
        // nothing special to do
    }

}
