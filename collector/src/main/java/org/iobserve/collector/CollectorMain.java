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
package org.iobserve.collector;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import kieker.analysis.common.ConfigurationException;
import kieker.common.configuration.Configuration;
import kieker.tools.common.AbstractService;

import org.iobserve.service.CommandLineParameterEvaluation;
import org.iobserve.service.CommonConfigurationKeys;

/**
 * The collector allows to collect input from different input sources, including TCP and Kieker
 * files. In future, we may add a nice mechanism to add other
 *
 * @author Reiner Jung
 */
public final class CollectorMain extends AbstractService<CollectorConfiguration, CollectorMain> {

    @Parameter(names = { "-c",
            "--configuration" }, required = true, description = "Configuration file.", converter = FileConverter.class)
    private File configurationFile;

    /**
     * This is a simple main class which does not need to be instantiated.
     */
    private CollectorMain() {

    }

    /**
     * Configure and execute the TCP Kieker data collector.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {
        final CollectorMain collector = new CollectorMain();
        System.exit(collector.run("Collector", "collector", args, collector));
    }

    @Override
    protected CollectorConfiguration createTeetimeConfiguration() throws ConfigurationException {
        return new CollectorConfiguration(this.kiekerConfiguration);
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        try {
            return CommandLineParameterEvaluation.isFileReadable(this.configurationFile, "Configuration File");
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected File getConfigurationFile() {
        return this.configurationFile;
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        configuration.getStringProperty(CommonConfigurationKeys.SOURCE_STAGE);
        return true;
    }

    @Override
    protected void shutdownService() {
        // nothing special to shutdown
    }

}
