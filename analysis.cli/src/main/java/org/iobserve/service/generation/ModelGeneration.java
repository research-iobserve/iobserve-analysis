package org.iobserve.service.generation;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.graph.ModelCollection;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

public class ModelGeneration {

	private static final Logger LOG = LogManager.getLogger(ModelGeneration.class);

	/**
	 * Generates a NEW model!
	 * 
	 * @param commandLine
	 *            the command Line arguemtns
	 * @throws InitializationException
	 * @throws IOException
	 */
	public static void createNewModel(CommandLine commandLine) throws InitializationException, IOException {
		LOG.info("Creating new model!");

		URI repoLocation = URI.createFileURI(commandLine.getOptionValue("i"));
		URI outputLocation = URI.createFileURI(commandLine.getOptionValue("o"));

		LOG.info("Copying repository model to new location.");
		RepositoryModelProvider repoModelProvider = new RepositoryModelProvider(repoLocation);
		ModelGeneration.copyRepoToOutput(outputLocation, repoModelProvider);

		LOG.info("Generating system model.");
		System systemModel = generateAndSaveSystem(commandLine, outputLocation);
		LOG.info("Generating resource environment model.");
		ResourceEnvironment resEnvModel = generateAndSaveResourceEnvironment(commandLine, outputLocation, systemModel.getEntityName());
		LOG.info("Generating allocation model.");
		generateAndSaveAllocation(outputLocation, systemModel, resEnvModel);
		LOG.info("Generating done!");
	}

	/*
	 * Genertes the System Model
	 */
	private static System generateAndSaveSystem(CommandLine commandLine, URI outputLocation) {
		InitializeModelProviders modelProviders = new InitializeModelProviders(new File(outputLocation.toFileString()));
		SystemGeneration systemGen = new SystemGeneration(modelProviders.getRepositoryModelProvider().getModel());
		System systemModel = systemGen.generateSystemModel(Integer.parseInt(commandLine.getOptionValue("a")));

		SystemModelProvider systemModelProvider = new SystemModelProvider();
		systemModelProvider.setModel(systemModel);
		URI systemModelURI = URI.createFileURI(outputLocation.toFileString() + File.separator + systemModel.getEntityName() + ".system");
		systemModelProvider.setModelUri(systemModelURI);
		systemModelProvider.save();
		return systemModel;
	}

	/*
	 * Generates the Resource Environment Model
	 */
	private static ResourceEnvironment generateAndSaveResourceEnvironment(CommandLine commandLine, URI outputLocation, String modelName) {
		ResourceEnvironmentGeneration resEnvGen = new ResourceEnvironmentGeneration(modelName);
		ResourceEnvironment resEnvModel = resEnvGen.craeteResourceEnvironment(Integer.parseInt(commandLine.getOptionValue("r")));

		ResourceEnvironmentModelProvider resEnvModelProvider = new ResourceEnvironmentModelProvider();
		resEnvModelProvider.setModel(resEnvModel);
		URI resEnvModelURI = URI.createFileURI(outputLocation.toFileString() + File.separator + resEnvModel.getEntityName() + ".resourceenvironment");
		resEnvModelProvider.setModelUri(resEnvModelURI);
		resEnvModelProvider.save();
		return resEnvModel;
	}

	/*
	 * Generates the Allocation Model
	 */
	private static Allocation generateAndSaveAllocation(URI outputLocation, System systemModel, ResourceEnvironment resEnvModel) {
		AllocationGeneration allocationGen = new AllocationGeneration(systemModel, resEnvModel);
		Allocation allocationModel = allocationGen.generateAllocation();

		AllocationModelProvider allocationModelProvider = new AllocationModelProvider();
		allocationModelProvider.setModel(allocationModel);
		URI allocationModelURI = URI.createFileURI(outputLocation.toFileString() + File.separator + resEnvModel.getEntityName() + ".allocation");
		allocationModelProvider.setModelUri(allocationModelURI);
		allocationModelProvider.save();

		return allocationModel;
	}

	/*
	 * Copies the Repository Model to the Output Folder
	 */
	private static boolean copyRepoToOutput(URI outputLocation, RepositoryModelProvider repoModelProvider)
			throws InitializationException, IOException {
		SnapshotBuilder.setBaseSnapshotURI(outputLocation);
		SnapshotBuilder snapshotBuilder = new SnapshotBuilder("", null);

		snapshotBuilder.createModelSnapshot(repoModelProvider);
		return false;
	}

	/*
	 * Generates Data File
	 */
	

}
