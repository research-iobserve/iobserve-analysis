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
import org.iobserve.analysis.graph.GraphFactory;
import org.iobserve.analysis.graph.ModelCollection;
import org.iobserve.analysis.graph.ModelGraph;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.privacy.ComponentClassificationAnalysis;
import org.iobserve.analysis.privacy.DeploymentAnalysis;
import org.iobserve.analysis.privacyanalysis.GraphCreation;
import org.iobserve.analysis.privacyanalysis.PrivacyAnalysis;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.analysis.utils.TimingHelper;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import com.neovisionaries.i18n.CountryCode;

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
		Allocation allocationModel = generateAndSaveAllocation(outputLocation, systemModel, resEnvModel);
		LOG.info("Generating done!");
		
//		makeAnalysisTimeMeasurement(repoModelProvider, systemModel, resEnvModel, allocationModel);
		
	}

	private static void makeAnalysisTimeMeasurement(RepositoryModelProvider repoModelProvider, System systemModel, ResourceEnvironment resEnvModel,
			Allocation allocationModel) {
		try {
			CountryCode[] countryCodes = CountryCode.values();
			HashSet<Integer> legalCountryCodes = new HashSet<Integer>();
			
			for (int i = 0; i < 30; i++)
			{
				int randGeoLocation = ThreadLocalRandom.current().nextInt(countryCodes.length);
				legalCountryCodes.add(countryCodes[randGeoLocation].getNumeric());
			}
			
			
			LOG.info("Building Graph!");
			TimingHelper.start("Building Graph");
			
			ModelCollection modelCollection = new ModelCollection(repoModelProvider.getModel(), systemModel, allocationModel, resEnvModel);
			GraphFactory graphFactory = new GraphFactory();
			ModelGraph graph = graphFactory.buildGraph(modelCollection);
			
			LOG.info("Component Classification!");
			TimingHelper.createRound("Component Classification");

			ComponentClassificationAnalysis classificationAnalysis = new ComponentClassificationAnalysis(graph);
			classificationAnalysis.start();

			LOG.info("Deployment Analysis!");
			TimingHelper.createRound("Deployment Analysis");

			DeploymentAnalysis deploymentAnalysis = new DeploymentAnalysis(graph, legalCountryCodes);
			deploymentAnalysis.start();

			LOG.info("Done!");
			TimingHelper.end("End");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Genertes the System Model
	 */
	private static System generateAndSaveSystem(CommandLine commandLine, URI outputLocation) {
		InitializeModelProviders modelProviders = new InitializeModelProviders(new File(outputLocation.toFileString()));
		SystemGeneration systemGen = new SystemGeneration(modelProviders.getRepositoryModelProvider().getModel());
		int assemblyContexts = Integer.parseInt(commandLine.getOptionValue("a"));
		System systemModel = systemGen.generateSystemModel(assemblyContexts);

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
