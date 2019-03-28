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
package org.iobserve.splitter;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import kieker.analysis.common.ConfigurationException;
import kieker.common.configuration.Configuration;
import kieker.tools.common.AbstractService;

import org.iobserve.service.CommandLineParameterEvaluation;

/**
 * Splitter main class.
 *
 * @author Reiner Jung
 */
public final class SplitterMain extends AbstractService<SimpleSplitterConfiguration, SplitterMain> {

    @Parameter(names = { "-i",
            "--input" }, required = true, description = "Input directory.", converter = FileConverter.class)
    private File sourceLocation;

    @Parameter(names = { "-o",
            "--output" }, required = true, description = "Output directory.", converter = FileConverter.class)
    private File targetLocation;

    @Parameter(names = { "-H", "--hosts" }, required = true, description = "List of hosts.")
    private String[] hostnames;

    /**
     * This is a simple main class which does not need to be instantiated.
     */
    private SplitterMain() {

    }

    /**
     * Configure and execute the splitter.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {
        final SplitterMain main = new SplitterMain();
        System.exit(main.run("Splitter", "splitter", args, main));
    }

    @Override
    protected SimpleSplitterConfiguration createTeetimeConfiguration() throws ConfigurationException {
        try {
            return new SimpleSplitterConfiguration(this.sourceLocation, this.targetLocation, this.hostnames);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        try {
            return CommandLineParameterEvaluation.checkDirectory(this.sourceLocation, "Source", commander)
                    && CommandLineParameterEvaluation.checkDirectory(this.targetLocation, "Target", commander);
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
        // nothing to be done specifically
    }

}
