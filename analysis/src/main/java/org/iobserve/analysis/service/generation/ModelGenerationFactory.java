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
package org.iobserve.analysis.service.generation;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.iobserve.model.ModelImporter;
import org.iobserve.model.persistence.file.FileModelHandler;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO this class is not really working, missing implementation of central functions.
 *
 * @author unknown
 *
 * @deprecated
 */
@Deprecated
public final class ModelGenerationFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelGenerationFactory.class);

    private static final ResourceSet RESOURCE_SET = new ResourceSetImpl();

    private ModelGenerationFactory() {
        // factory
    }

    public static void createNewModel(final CommandLine commandLine) throws InitializationException, IOException {
        ModelGenerationFactory.LOGGER.info("Creating new model!");

        final URI repoLocation = URI.createFileURI(commandLine.getOptionValue("i"));
        final URI outputLocation = URI.createFileURI(commandLine.getOptionValue("o"));

        ModelGenerationFactory.LOGGER.info("Copying repository model to new location.");
        final FileModelHandler<Repository> repoModelProvider = new FileModelHandler<>(
                ModelGenerationFactory.RESOURCE_SET, RepositoryPackage.eINSTANCE);
        repoModelProvider.load(repoLocation);
        ModelGenerationFactory.copyRepoToOutput(outputLocation, repoModelProvider);

        ModelGenerationFactory.LOGGER.info("Generating system model.");
        final System systemModel = ModelGenerationFactory.generateAndSaveSystem(commandLine, outputLocation);
        ModelGenerationFactory.LOGGER.info("Generating resource environment model.");
        final ResourceEnvironment resEnvModel = ModelGenerationFactory.generateAndSaveResourceEnvironment(commandLine,
                outputLocation, systemModel.getEntityName());
        ModelGenerationFactory.LOGGER.info("Generating allocation model.");
        ModelGenerationFactory.generateAndSaveAllocation(outputLocation, systemModel, resEnvModel);
        ModelGenerationFactory.LOGGER.info("Generating done!");
    }

    private static System generateAndSaveSystem(final CommandLine commandLine, final URI outputLocation) {
        try {
            final ModelImporter modelProviders = new ModelImporter(new File(outputLocation.toFileString()));
            final SystemGeneration systemGen = new SystemGeneration(modelProviders.getRepositoryModel());
            final System systemModel = systemGen.generateSystemModel(Integer.parseInt(commandLine.getOptionValue("a")));

            final FileModelHandler<System> systemModelProvider = new FileModelHandler<>(
                    ModelGenerationFactory.RESOURCE_SET, SystemPackage.eINSTANCE);
            final URI systemModelURI = URI.createFileURI(
                    outputLocation.toFileString() + File.separator + systemModel.getEntityName() + ".system");
            systemModelProvider.save(systemModelURI, systemModel);

            return systemModel;

        } catch (final IOException e) {
            java.lang.System.err.println("Canot load all models " + e.getLocalizedMessage());
            return null;
        }
    }

    private static ResourceEnvironment generateAndSaveResourceEnvironment(final CommandLine commandLine,
            final URI outputLocation, final String modelName) {
        final ResourceEnvironmentGeneration resEnvGen = new ResourceEnvironmentGeneration(modelName);
        final ResourceEnvironment resEnvModel = resEnvGen
                .craeteResourceEnvironment(Integer.parseInt(commandLine.getOptionValue("r")));

        final FileModelHandler<ResourceEnvironment> resEnvModelProvider = new FileModelHandler<>(
                ModelGenerationFactory.RESOURCE_SET, ResourceenvironmentPackage.eINSTANCE);
        final URI resEnvModelURI = URI.createFileURI(
                outputLocation.toFileString() + File.separator + resEnvModel.getEntityName() + ".resourceenvironment");
        resEnvModelProvider.save(resEnvModelURI, resEnvModel);
        return resEnvModel;
    }

    private static Allocation generateAndSaveAllocation(final URI outputLocation, final System systemModel,
            final ResourceEnvironment resEnvModel) {
        final AllocationGeneration allocationGen = new AllocationGeneration(systemModel, resEnvModel);
        final Allocation allocationModel = allocationGen.generateAllocation();

        final FileModelHandler<Allocation> allocationModelProvider = new FileModelHandler<>(
                ModelGenerationFactory.RESOURCE_SET, AllocationPackage.eINSTANCE);
        final URI allocationModelURI = URI.createFileURI(
                outputLocation.toFileString() + File.separator + resEnvModel.getEntityName() + ".allocation");
        allocationModelProvider.save(allocationModelURI, allocationModel);

        return allocationModel;
    }

    private static boolean copyRepoToOutput(final URI outputLocation,
            final FileModelHandler<Repository> repoModelProvider) throws InitializationException, IOException {
        // TODO implement
        return false;
    }

}
