/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.test.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import teetime.framework.Configuration;
import teetime.framework.Execution;

/**
 * Main class for starting the iObserve application.
 *
 * @author Reiner Jung
 */
public final class AnalysisMain {

    private static final Log LOG = LogFactory.getLog(AnalysisMain.class);

    @Parameter(names = { "-i",
            "--input" }, required = true, description = "Directory containing monitoring data.", converter = FileConverter.class)
    private File monitoringDataDirectory;

    @Parameter(names = { "-o",
            "--output" }, required = true, description = "Output directory.", converter = FileConverter.class)
    private File outputLocation;

    /**
     * Main function.
     *
     * @param args
     *            command line arguments.
     */
    public static void main(final String[] args) {
        final AnalysisMain main = new AnalysisMain();
        final JCommander commander = new JCommander(main);
        try {
            commander.parse(args);
            main.execute(commander);
        } catch (final ParameterException e) {
            AnalysisMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        }
    }

    private void execute(final JCommander commander) {
        if (this.outputLocation.isDirectory()) {
            if (this.monitoringDataDirectory.isDirectory()) {
                final Collection<File> monitoringDataDirectories = new ArrayList<>();
                monitoringDataDirectories.add(this.monitoringDataDirectory);

                final Configuration configuration = new ObservationConfiguration(monitoringDataDirectories,
                        this.outputLocation);

                AnalysisMain.LOG.info("Analysis configuration");
                final Execution<Configuration> analysis = new Execution<>(configuration);
                AnalysisMain.LOG.info("Analysis start");
                analysis.executeBlocking();
                AnalysisMain.LOG.info("Anaylsis complete");
            } else {
                AnalysisMain.LOG.error(
                        "CLI error: Input path " + this.monitoringDataDirectory.getName() + " is not a directory.");
                commander.usage();
            }
        } else {
            AnalysisMain.LOG.error("CLI error: Output path " + this.outputLocation.getName() + " is not a directory.");
            commander.usage();
        }

    }

}
