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
package org.iobserve.service.privacy.violation;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.CommaParameterSplitter;
import com.beust.jcommander.converters.IntegerConverter;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import teetime.framework.Execution;

import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.model.provider.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class PrivacyViolationDetectionServiceMain {

    private static final Log LOG = LogFactory.getLog(PrivacyViolationDetectionServiceMain.class);

    @Parameter(names = { "-i",
            "--input" }, required = true, description = "Input port.", converter = IntegerConverter.class)
    private int inputPort;

    @Parameter(names = { "-c",
            "--control" }, required = true, description = "Control hosts and ports.", splitter = CommaParameterSplitter.class, converter = HostPortConverter.class)
    private List<ConnectionData> outputs;

    private File warningFile;

    private File alarmsFile;

    private ICorrespondence rac;

    private ResourceEnvironmentModelProvider resourceEnvironmentModelProvider;

    private ModelProvider<Allocation> allocationModelGraphProvider;

    private ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;

    private ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;

    /**
     * This is a simple main class which does not need to be instantiated.
     */
    private PrivacyViolationDetectionServiceMain() {

    }

    /**
     * Configure and execute the TCP Kieker data collector.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {
        final PrivacyViolationDetectionServiceMain main = new PrivacyViolationDetectionServiceMain();
        final JCommander commander = new JCommander(main);
        try {
            commander.parse(args);
            main.execute(commander);
        } catch (final ParameterException e) {
            PrivacyViolationDetectionServiceMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        } catch (final IOException e) {
            PrivacyViolationDetectionServiceMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        }
    }

    private void execute(final JCommander commander) throws IOException {
        PrivacyViolationDetectionServiceMain.LOG.debug("Receiver");
        final PrivacyViolationDetectionConfiguration configuration = new PrivacyViolationDetectionConfiguration(
                this.inputPort, this.outputs, this.rac, this.resourceEnvironmentModelProvider,
                this.allocationModelGraphProvider, this.systemModelGraphProvider,
                this.resourceEnvironmentModelGraphProvider, this.warningFile, this.alarmsFile);

        if (configuration.isSetupComplete()) {

            final Execution<PrivacyViolationDetectionConfiguration> analysis = new Execution<>(configuration);

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (analysis) {
                            analysis.abortEventually();
                        }
                    } catch (final Exception e) { // NOCS

                    }
                }
            }));

            PrivacyViolationDetectionServiceMain.LOG.info("Running analysis");

            analysis.executeBlocking();

            PrivacyViolationDetectionServiceMain.LOG.info("Done");
        } else {
            PrivacyViolationDetectionServiceMain.LOG.info("Setup failed.");
        }

    }

}
