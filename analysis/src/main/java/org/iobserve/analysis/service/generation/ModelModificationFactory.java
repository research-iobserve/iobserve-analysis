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
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.model.PCMModelHandler;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ToDo .
 *
 * @author unknown
 *
 */
public final class ModelModificationFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelModificationFactory.class);

    private ModelModificationFactory() {
        // empty constructor for factory
    }

    public static void createNewModel(final CommandLine commandLine) throws InitializationException, IOException {
        ModelModificationFactory.LOGGER.info("Modifying model!");

        final URI inputModels = URI.createFileURI(commandLine.getOptionValue("i"));
        final URI outputLocation = URI.createFileURI(commandLine.getOptionValue("o"));

        ModelModificationFactory.LOGGER.info("Copying models to new location.");

        PCMModelHandler modelProviders = new PCMModelHandler(new File(inputModels.toFileString()));

        final URI copyURI = ModelModificationFactory.copyRepoToOutput(outputLocation, modelProviders);
        modelProviders = new PCMModelHandler(new File(copyURI.toFileString()));

        final Allocation allocationModel = modelProviders.getAllocationModel();
        final ResourceEnvironment resourceEnvironmentModel = modelProviders.getResourceEnvironmentModel();
        final System systemModel = modelProviders.getSystemModel();
        final Repository repositoryModel = modelProviders.getRepositoryModel();

        final ResourceEnvironmentModification resEnvMod = new ResourceEnvironmentModification(resourceEnvironmentModel);

        ModelModificationFactory.LOGGER.info("Terminating Server");
        final List<ResourceContainer> terminatedResourceContainers = resEnvMod
                .modifyResEnvTerminate(Integer.parseInt(commandLine.getOptionValue("ac")));

        ModelModificationFactory.LOGGER.info("Acquiring Server");
        resEnvMod.modifyResEnvAcquire(Integer.parseInt(commandLine.getOptionValue("ac")));

        ModelModificationFactory.LOGGER.info("Fixing Allocation after terminating");
        AllocationModification allocMod = new AllocationModification(allocationModel, systemModel,
                resourceEnvironmentModel);
        final int terminationMigrations = allocMod.modifyAllocationFixTerminations(terminatedResourceContainers);

        ModelModificationFactory.LOGGER.info("Deallocating Components");
        final SystemModification sysMod = new SystemModification(systemModel, repositoryModel);
        final List<AssemblyContext> deallocatedACs = sysMod
                .modifySystemDeallocations(Integer.parseInt(commandLine.getOptionValue("de")));

        ModelModificationFactory.LOGGER.info("Fixing Allocation after deallocating");
        allocMod = new AllocationModification(allocationModel, systemModel, resourceEnvironmentModel);
        allocMod.modifyAllocationFixDeallocations(deallocatedACs);

        // LOG.info("Exchanging Components");
        // sysMod.modifySystem_ChangeComp(Integer.parseInt(commandLine.getOptionValue("cr")));

        ModelModificationFactory.LOGGER.info("Allocating new Components");
        final List<AssemblyContext> allocatedACs = sysMod
                .modifySystemAllocate(Integer.parseInt(commandLine.getOptionValue("al")));

        ModelModificationFactory.LOGGER.info("Creating Allocation for new components");
        allocMod = new AllocationModification(allocationModel, systemModel, resourceEnvironmentModel);
        allocMod.modifyAllocationFixAllocations(allocatedACs);

        ModelModificationFactory.LOGGER.info("Creating migrations");
        allocMod = new AllocationModification(allocationModel, systemModel, resourceEnvironmentModel);
        final int totalMigrations = Integer.parseInt(commandLine.getOptionValue("al"));
        final int migrationsToPerform = totalMigrations - terminationMigrations; // allocationMigrations
        if (migrationsToPerform > 0) {
            ModelModificationFactory.LOGGER.info("Migrations to Perform: {}", migrationsToPerform);
            allocMod.modifyAllocationMigrate(migrationsToPerform);
        } else {
            ModelModificationFactory.LOGGER
                    .info(String.format("All migrations (%d) are already perfomed by Server-Termination (%d)!",
                            totalMigrations, migrationsToPerform));
        }
        ModelModificationFactory.LOGGER.info("Saving models!");

        modelProviders.save(outputLocation);

        ModelModificationFactory.LOGGER.info("Modification done!");
    }

    /**
     * Copies all files to the given output location.
     */
    private static URI copyRepoToOutput(final URI outputLocation, final PCMModelHandler modelProviders) {
        SnapshotBuilder.setBaseSnapshotURI(outputLocation);
        final SnapshotBuilder snapshotBuilder;
        URI snapshotURI = null;

        try {
            snapshotBuilder = new SnapshotBuilder("", modelProviders);
            snapshotURI = snapshotBuilder.createSnapshot();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final InitializationException e1) {
            e1.printStackTrace();
        }
        return snapshotURI;
    }

}
