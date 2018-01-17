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
import com.beust.jcommander.converters.IntegerConverter;

import org.iobserve.analysis.ConfigurationException;
import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class CollectorMain extends AbstractServiceMain<SimpleBridgeConfiguration> {
    @Parameter(names = { "-d",
            "--data" }, required = true, description = "Output data directory.", converter = FileConverter.class)
    private File dataLocation;

    @Parameter(names = { "-p",
            "--port" }, required = true, description = "Input port.", converter = IntegerConverter.class)
    private Integer inputPort;

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
        new CollectorMain().run("Collector", "collector", args);
    }

    @Override
    protected SimpleBridgeConfiguration createConfiguration() throws ConfigurationException {
        try {
            return new SimpleBridgeConfiguration(this.dataLocation.getCanonicalPath(), this.inputPort);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        try {
            return CommandLineParameterEvaluation.checkDirectory(this.dataLocation, "Output Kieker directory",
                    commander);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

}
