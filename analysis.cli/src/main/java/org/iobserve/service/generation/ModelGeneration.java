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
package org.iobserve.service.generation;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

public class ModelGeneration {

    private static final Logger LOG = LogManager.getLogger(ModelGeneration.class);

    public static void createNewModel(final CommandLine commandLine) throws InitializationException, IOException {
        ModelGeneration.LOG.info("Creating new model!");

        final URI repoLocation = URI.createFileURI(commandLine.getOptionValue("i"));
        final URI outputLocation = URI.createFileURI(commandLine.getOptionValue("o"));

        ModelGeneration.LOG.info("Copying repository model to new location.");
        final RepositoryModelProvider repoModelProvider = new RepositoryModelProvider(repoLocation);
        ModelGeneration.copyRepoToOutput(outputLocation, repoModelProvider);

        ModelGeneration.LOG.info("Generating system model.");
        final System systemModel = ModelGeneration.generateAndSaveSystem(commandLine, outputLocation);
        ModelGeneration.LOG.info("Generating resource environment model.");
        final ResourceEnvironment resEnvModel = ModelGeneration.generateAndSaveResourceEnvironment(commandLine,
                outputLocation, systemModel.getEntityName());
        ModelGeneration.LOG.info("Generating allocation model.");
        ModelGeneration.generateAndSaveAllocation(outputLocation, systemModel, resEnvModel);
        ModelGeneration.LOG.info("Generating done!");
    }

    private static System generateAndSaveSystem(final CommandLine commandLine, final URI outputLocation) {
        final InitializeModelProviders modelProviders = new InitializeModelProviders(
                new File(outputLocation.toFileString()));
        final SystemGeneration systemGen = new SystemGeneration(modelProviders.getRepositoryModelProvider().getModel());
        final System systemModel = systemGen.generateSystemModel(Integer.parseInt(commandLine.getOptionValue("a")));

        final SystemModelProvider systemModelProvider = new SystemModelProvider();
        systemModelProvider.setModel(systemModel);
        final URI systemModelURI = URI.createFileURI(
                outputLocation.toFileString() + File.separator + systemModel.getEntityName() + ".system");
        systemModelProvider.setModelUri(systemModelURI);
        systemModelProvider.save();
        return systemModel;
    }

    private static ResourceEnvironment generateAndSaveResourceEnvironment(final CommandLine commandLine,
            final URI outputLocation, final String modelName) {
        final ResourceEnvironmentGeneration resEnvGen = new ResourceEnvironmentGeneration(modelName);
        final ResourceEnvironment resEnvModel = resEnvGen
                .craeteResourceEnvironment(Integer.parseInt(commandLine.getOptionValue("r")));

        final ResourceEnvironmentModelProvider resEnvModelProvider = new ResourceEnvironmentModelProvider();
        resEnvModelProvider.setModel(resEnvModel);
        final URI resEnvModelURI = URI.createFileURI(
                outputLocation.toFileString() + File.separator + resEnvModel.getEntityName() + ".resourceenvironment");
        resEnvModelProvider.setModelUri(resEnvModelURI);
        resEnvModelProvider.save();
        return resEnvModel;
    }

    private static Allocation generateAndSaveAllocation(final URI outputLocation, final System systemModel,
            final ResourceEnvironment resEnvModel) {
        final AllocationGeneration allocationGen = new AllocationGeneration(systemModel, resEnvModel);
        final Allocation allocationModel = allocationGen.generateAllocation();

        final AllocationModelProvider allocationModelProvider = new AllocationModelProvider();
        allocationModelProvider.setModel(allocationModel);
        final URI allocationModelURI = URI.createFileURI(
                outputLocation.toFileString() + File.separator + resEnvModel.getEntityName() + ".allocation");
        allocationModelProvider.setModelUri(allocationModelURI);
        allocationModelProvider.save();

        return allocationModel;
    }

    private static boolean copyRepoToOutput(final URI outputLocation, final RepositoryModelProvider repoModelProvider)
            throws InitializationException, IOException {
        SnapshotBuilder.setBaseSnapshotURI(outputLocation);
        final SnapshotBuilder snapshotBuilder = new SnapshotBuilder("", null);

        snapshotBuilder.createModelSnapshot(repoModelProvider);
        return false;
    }

}
