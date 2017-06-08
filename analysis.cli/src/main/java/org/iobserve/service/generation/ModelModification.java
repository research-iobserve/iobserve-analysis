package org.iobserve.service.generation;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.graph.ModelCollection;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

public class ModelModification {

	private static final Logger LOG = LogManager.getLogger(ModelModification.class);

	public static void createNewModel(CommandLine commandLine) throws InitializationException, IOException {
		LOG.info("Modifying model!");

		URI inputModels = URI.createFileURI(commandLine.getOptionValue("i"));
		URI outputLocation = URI.createFileURI(commandLine.getOptionValue("o"));

		LOG.info("Copying models to new location.");
		InitializeModelProviders modelProviders = new InitializeModelProviders(new File(inputModels.toFileString()));
		URI copyURI = copyRepoToOutput(outputLocation, modelProviders);

		modelProviders = new InitializeModelProviders(new File(copyURI.toFileString()));
		ModelCollection models = modelProviders.getModelCollection();

		ResourceEnvironmentModification resEnvMod = new ResourceEnvironmentModification(models.getResourceEnvironmentModel());

		LOG.info("Terminating Server");
		List<ResourceContainer> terminatedResourceContainers = resEnvMod.modifyResEnv_terminate(Integer.parseInt(commandLine.getOptionValue("ac")));
		
		LOG.info("Acquiring Server");
		resEnvMod.modifyResEnv_acquire(Integer.parseInt(commandLine.getOptionValue("ac")));

		LOG.info("Fixing Allocation after terminating");
		AllocationModification allocMod = new AllocationModification(models.getAllocationModel(), models.getSystemModel(),
				models.getResourceEnvironmentModel());
		int terminationMigrations = allocMod.modifyAllocation_FixTerminations(terminatedResourceContainers);

		LOG.info("Deallocating Components");
		SystemModification sysMod = new SystemModification(models.getSystemModel(), models.getRepositoryModel());
		List<AssemblyContext> deallocatedACs = sysMod.modifySystem_Deallocations(Integer.parseInt(commandLine.getOptionValue("de")));

		LOG.info("Fixing Allocation after deallocating");
		allocMod = new AllocationModification(models.getAllocationModel(), models.getSystemModel(), models.getResourceEnvironmentModel());
		allocMod.modifyAllocation_FixDeallocations(deallocatedACs);

		// LOG.info("Exchanging Components");
		// sysMod.modifySystem_ChangeComp(Integer.parseInt(commandLine.getOptionValue("cr")));

		LOG.info("Allocating new Components");
		List<AssemblyContext> allocatedACs = sysMod.modifySystem_Allocate(Integer.parseInt(commandLine.getOptionValue("al")));

		LOG.info("Creating Allocation for new components");
		allocMod = new AllocationModification(models.getAllocationModel(), models.getSystemModel(), models.getResourceEnvironmentModel());
		allocMod.modifyAllocation_FixAllocations(allocatedACs);
		int allocationMigrations = allocatedACs.size();

		LOG.info("Creating migrations");
		allocMod = new AllocationModification(models.getAllocationModel(), models.getSystemModel(), models.getResourceEnvironmentModel());
		int totalMigrations = Integer.parseInt(commandLine.getOptionValue("al"));
		int migrationsToPerform = totalMigrations - (terminationMigrations); // allocationMigrations
		if (migrationsToPerform > 0) {
			LOG.info("Migrations to Perform: " + migrationsToPerform);
			allocMod.modifyAllocation_Migrate(migrationsToPerform);
		} else {
			LOG.info(String.format("All migrations (%d) are already perfomed by Server-Termination (%d)!", totalMigrations, migrationsToPerform));
		}
		LOG.info("Saving models!");

		modelProviders.getRepositoryModelProvider().save();
		modelProviders.getSystemModelProvider().save();
		modelProviders.getResourceEnvironmentModelProvider().save();
		modelProviders.getAllocationModelProvider().save();

		LOG.info("Modification done!");
	}

	/*
	 * Copys all files to the given output location.
	 */
	private static URI copyRepoToOutput(URI outputLocation, InitializeModelProviders modelProviders) {
		SnapshotBuilder.setBaseSnapshotURI(outputLocation);
		SnapshotBuilder snapshotBuilder;
		URI snapshotURI = null;

		try {
			snapshotBuilder = new SnapshotBuilder("", modelProviders);
			snapshotURI = snapshotBuilder.createSnapshot();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InitializationException e1) {
			e1.printStackTrace();
		}
		return snapshotURI;
	}

}
