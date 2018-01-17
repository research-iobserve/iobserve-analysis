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
import com.beust.jcommander.converters.CommaParameterSplitter;
import com.beust.jcommander.converters.IntegerConverter;

import org.iobserve.analysis.ConfigurationException;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.model.provider.neo4j.ModelProvider;
import org.iobserve.service.AbstractServiceMain;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class PrivacyViolationDetectionServiceMain
        extends AbstractServiceMain<PrivacyViolationDetectionConfiguration> {

    @Parameter(names = { "-i",
            "--input" }, required = true, description = "Input port.", converter = IntegerConverter.class)
    private int inputPort;

    @Parameter(names = { "-c",
            "--control" }, required = true, description = "Control hosts and ports.", splitter = CommaParameterSplitter.class, converter = HostPortConverter.class)
    private List<ConnectionData> outputs;

    private File warningFile;

    private File alarmsFile;

    private ICorrespondence rac;

    private ModelProvider<Allocation> allocationModelProvider;

    private ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;

    private ModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider;

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
        new PrivacyViolationDetectionServiceMain().run("Privacy Violation Detection Service", "service", args);
    }

    @Override
    protected PrivacyViolationDetectionConfiguration createConfiguration() throws ConfigurationException {
        // TODO we need to initialize the model providers.
        try {
            return new PrivacyViolationDetectionConfiguration(this.inputPort, this.outputs, this.rac,
                    this.resourceEnvironmentModelProvider, this.allocationModelProvider, this.systemModelGraphProvider,
                    this.warningFile, this.alarmsFile);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        return true;
    }

}
