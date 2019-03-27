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
package org.iobserve.analysis.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.CorrespondenceModelProvider;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.IntegerConverter;

import teetime.framework.Configuration;

/**
 * @author Reiner Jung
 *
 */
public class AnalysisDaemon implements Daemon {

    @Parameter(names = "--help", help = true)
    private boolean help;

    @Parameter(names = { "-v",
            "--variance-of-user-groups" }, required = true, description = "Variance of user groups.", converter = IntegerConverter.class)
    private int varianceOfUserGroups;

    @Parameter(names = { "-t",
            "--think-time" }, required = true, description = "Think time.", converter = IntegerConverter.class)
    private int thinkTime;

    @Parameter(names = { "-c",
            "--closed-workload" }, required = false, description = "Closed workload.", converter = IntegerConverter.class)
    private final boolean closedWorkload = false;

    @Parameter(names = { "-i",
            "--input" }, required = true, description = "port number to listen for new connections of Kieker writers.", converter = IntegerConverter.class)
    private int listenPort;

    @Parameter(names = { "-o",
            "--output" }, required = true, description = "hostname and port of the iobserve visualization, e.g., visualization:80.")
    private String output;

    @Parameter(names = { "-s", "--system" }, required = true, description = "system id.")
    private String systemId;

    @Parameter(names = { "-p",
            "--pcm" }, required = true, description = "Directory containing PCM model data.", converter = FileConverter.class)
    private File pcmModelsDirectory;

    private AnalysisThread thread;
    private boolean running = false;

    /**
     * Empty default constructor.
     */
    public AnalysisDaemon() {
    }

    @Override
    public void init(final DaemonContext context) throws DaemonInitException, MalformedURLException {
        final JCommander commander = new JCommander(this);
        final String[] args = context.getArguments();
        try {
            commander.parse(args);
            this.execute(commander);
        } catch (final ParameterException e) {
            System.err.println(e.getLocalizedMessage());
            commander.usage();
        } catch (final IOException e) {
            System.err.println(e.getLocalizedMessage());
            commander.usage();
        }
    }

    private void execute(final JCommander commander) throws IOException {

        /** get configuration parameter. */
        this.checkDirectory(this.pcmModelsDirectory, "Palladio Model", commander);

        final String[] outputs = this.output.split(":");
        if (this.output.length() == 2) {
            final String outputHostname = outputs[0];
            final String outputPort = outputs[1];

            /** process parameter. */
            final InitializeModelProviders modelProvider = new InitializeModelProviders(this.pcmModelsDirectory);

            final CorrespondenceModelProvider correspondenceModelProvider = 
            		modelProvider.getCorrespondenceModelProvider();
            final UsageModelProvider usageModelProvider = modelProvider.getUsageModelProvider();
            final RepositoryModelProvider repositoryModelProvider = modelProvider.getRepositoryModelProvider();
            final ResourceEnvironmentModelProvider resourceEvnironmentModelProvider = modelProvider
                    .getResourceEnvironmentModelProvider();
            final AllocationModelProvider allocationModelProvider = modelProvider.getAllocationModelProvider();
            final SystemModelProvider systemModelProvider = modelProvider.getSystemModelProvider();

            final Configuration configuration = new ServiceConfiguration(this.listenPort, outputHostname, outputPort,
                    this.systemId, this.varianceOfUserGroups, this.thinkTime, this.closedWorkload, 
                    correspondenceModelProvider, usageModelProvider, repositoryModelProvider, 
                    resourceEvnironmentModelProvider, allocationModelProvider, systemModelProvider);

            this.thread = new AnalysisThread(this, configuration);
        } else {
            commander.usage();
            System.exit(2);
        }

    }

    @Override
    public void start() throws Exception {
        this.running = true;
        this.thread.start();
    }

    @Override
    public void stop() throws Exception {
        this.running = false;
        try {
            this.thread.join(1000);
        } catch (final InterruptedException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public void destroy() {
        this.thread = null;
    }

    public boolean isRunning() {
        return this.running;
    }

    private void checkDirectory(final File location, final String locationLabel, final JCommander commander)
            throws IOException {
        if (!location.exists()) {
            System.err.println(locationLabel + " path " + location.getCanonicalPath() + " does not exist.");
            commander.usage();
            System.exit(1);
        }
        if (!location.isDirectory()) {
            System.err.println(locationLabel + " path " + location.getCanonicalPath() + " is not a directory.");
            commander.usage();
            System.exit(1);
        }

    }
}
