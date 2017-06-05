package org.iobserve.service.generation;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
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

	public static void main(String[] args) {
		
		final CommandLineParser parser = new DefaultParser();
		try {
			CommandLine commandLine = parser.parse(ModelGeneration.createHelpOptions(), args);

			if (commandLine.hasOption("h")) {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("iobserve-analysis", ModelGeneration.createOptions());
			} else {
				commandLine = parser.parse(ModelGeneration.createOptions(), args);

				if (commandLine.hasOption("n")) {
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
				} else if (commandLine.hasOption("m")) {

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

	private static boolean copyRepoToOutput(URI outputLocation, RepositoryModelProvider repoModelProvider)
			throws InitializationException, IOException {
		SnapshotBuilder.setBaseSnapshotURI(outputLocation);
		SnapshotBuilder snapshotBuilder = new SnapshotBuilder("", null);

		snapshotBuilder.createModelSnapshot(repoModelProvider);
		return false;
	}

	/**
	 * Create the command line parameter setup.
	 *
	 * @return options for the command line parser
	 */
	private static Options createOptions() {
		final Options options = new Options();

		options.addOption(Option.builder("i").required(true).longOpt("input").hasArg().desc("the repository model file").build());
		options.addOption(Option.builder("o").required(true).longOpt("output").hasArg().desc("the output directory").build());
		options.addOption(Option.builder("n").required(false).longOpt("generate-new").desc("generates new model, based on input repo").build());
		options.addOption(Option.builder("m").required(false).longOpt("modify").desc("modify the input model").build());

		options.addOption(Option.builder("a").required(false).longOpt("allocation-contexts").hasArg().desc("allocation context count").build());
		options.addOption(Option.builder("r").required(false).longOpt("resource-container").hasArg().desc("resource container count").build());

		options.addOption(Option.builder("al").required(false).longOpt("allocation").hasArg().desc("allocate actions").build());
		options.addOption(Option.builder("mi").required(false).longOpt("migrate").hasArg().desc("migrate actions").build());
		options.addOption(Option.builder("de").required(false).longOpt("deallocate").hasArg().desc("deallocate actions").build());
		options.addOption(Option.builder("cr").required(false).longOpt("change-repo").hasArg().desc("change repo actions").build());

		options.addOption(Option.builder("ac").required(false).longOpt("acquire").hasArg().desc("acquire actions").build());
		options.addOption(Option.builder("re").required(false).longOpt("replicate").hasArg().desc("replicate actions").build());
		options.addOption(Option.builder("te").required(false).longOpt("terminate").hasArg().desc("terminate actions").build());

		/** help */
		options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());

		return options;
	}

	/**
	 * Create a command line setup with only the help option.
	 *
	 * @return returns simplified options
	 */
	private static Options createHelpOptions() {
		final Options options = new Options();

		options.addOption(Option.builder("i").required(true).longOpt("input").hasArg().desc("the repository model file").build());
		options.addOption(Option.builder("o").required(true).longOpt("output").hasArg().desc("the output directory").build());
		options.addOption(Option.builder("n").required(false).longOpt("generate-new").desc("generates new model, based on input repo").build());
		options.addOption(Option.builder("m").required(false).longOpt("modify").desc("modify the input model").build());

		options.addOption(Option.builder("a").required(false).longOpt("allocation-contexts").hasArg().desc("allocation context count").build());
		options.addOption(Option.builder("r").required(false).longOpt("resource-container").hasArg().desc("resource container count").build());

		options.addOption(Option.builder("al").required(false).longOpt("allocation").hasArg().desc("allocate actions").build());
		options.addOption(Option.builder("mi").required(false).longOpt("migrate").hasArg().desc("migrate actions").build());
		options.addOption(Option.builder("de").required(false).longOpt("deallocate").hasArg().desc("deallocate actions").build());
		options.addOption(Option.builder("cr").required(false).longOpt("change-repo").hasArg().desc("change repo actions").build());

		options.addOption(Option.builder("ac").required(false).longOpt("acquire").hasArg().desc("acquire actions").build());
		options.addOption(Option.builder("re").required(false).longOpt("replicate").hasArg().desc("replicate actions").build());
		options.addOption(Option.builder("te").required(false).longOpt("terminate").hasArg().desc("terminate actions").build());

		/** help */
		options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());
		return options;
	}

}
