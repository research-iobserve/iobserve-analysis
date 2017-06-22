package org.iobserve.service.generation;

import java.io.File;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.graph.GraphFactory;

/**
 * Generates a valid PCM Privacy model for evaluation
 * 
 * @author Philipp Weimann
 * @author Robert Heinrich
 */
public class EvaluationGeneration {

	private static final Logger LOG = LogManager.getLogger(EvaluationGeneration.class);

	public static void main(String[] args) {

		final CommandLineParser parser = new DefaultParser();
		try {
			CommandLine commandLine = parser.parse(createHelpOptions(), args);

			if (commandLine.hasOption("h")) {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("iobserve-analysis", createOptions());
			} else {
				commandLine = parser.parse(createOptions(), args);

				if (commandLine.hasOption("n")) {
					clearDirectory(commandLine.getOptionValue("o"));
					ModelGeneration.createNewModel(commandLine);

					InitializeModelProviders modelProviers = new InitializeModelProviders(new File(commandLine.getOptionValue("o")));
					GraphFactory graphFactory = new GraphFactory();
					graphFactory.buildGraph(modelProviers.getModelCollection());
				}
				if (commandLine.hasOption("m")) {
					clearDirectory(commandLine.getOptionValue("o"));
					ModelModification.createModifiedModel(commandLine);

					InitializeModelProviders modelProviers = new InitializeModelProviders(new File(commandLine.getOptionValue("o")));
					GraphFactory graphFactory = new GraphFactory();
					graphFactory.buildGraph(modelProviers.getModelCollection());
				}
				if (commandLine.hasOption("d")) {
					InitializeModelProviders modelProviers = new InitializeModelProviders(new File(commandLine.getOptionValue("i")));
					DatGenerator datGen = new DatGenerator(modelProviers.getModelCollection());

					URI outputFile = URI.createFileURI(commandLine.getOptionValue("o"));
					datGen.generateDatFile(Integer.parseInt(commandLine.getOptionValue("c")), outputFile);
				}
				if (commandLine.hasOption("dif")) {
					InitializeModelProviders initModels = new InitializeModelProviders(new File(commandLine.getOptionValue("i")));
					InitializeModelProviders difModels = new InitializeModelProviders(new File(commandLine.getOptionValue("o")));

					ModelDif difCreator = new ModelDif();
					List<String> difs = difCreator.difModels(initModels.getModelCollection(), difModels.getModelCollection());

					if (difs.size() == 0) {
						LOG.info("NO difs detected!");
					} else {
						StringBuilder sb = new StringBuilder();
						for (String dif : difs) {
							sb.append(dif + "\n");
						}

						LOG.warn("The difs are:\n" + sb.toString());
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Clears the files in the given directory. Doesn't touch Directories!
	 */
	private static void clearDirectory(String fileURI) {
		LOG.info("Clearing output folder: " + fileURI);
		File outputDir = new File(fileURI);

		if (outputDir.exists()) {
			for (File file : outputDir.listFiles())
				if (!file.isDirectory())
					file.delete();
		}
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
		options.addOption(Option.builder("d").required(false).longOpt("datFile").desc("creates a dat file").build());
		options.addOption(Option.builder("dif").required(false).longOpt("make-dif").desc("prints out the diferences between the models").build());

		options.addOption(Option.builder("a").required(false).longOpt("allocation-contexts").hasArg().desc("allocation context count").build());
		options.addOption(Option.builder("r").required(false).longOpt("resource-container").hasArg().desc("resource container count").build());

		options.addOption(Option.builder("al").required(false).longOpt("allocation").hasArg().desc("allocate actions").build());
		options.addOption(Option.builder("mi").required(false).longOpt("migrate").hasArg().desc("migrate actions").build());
		options.addOption(Option.builder("de").required(false).longOpt("deallocate").hasArg().desc("deallocate actions").build());
		options.addOption(Option.builder("cr").required(false).longOpt("change-repo").hasArg().desc("change repo actions").build());

		options.addOption(Option.builder("ac").required(false).longOpt("acquire").hasArg().desc("acquire actions").build());
		options.addOption(Option.builder("re").required(false).longOpt("replicate").hasArg().desc("replicate actions").build());
		options.addOption(Option.builder("te").required(false).longOpt("terminate").hasArg().desc("terminate actions").build());

		options.addOption(Option.builder("c").required(false).longOpt("commands").hasArg().desc("command counts in dat file").build());

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
		options.addOption(Option.builder("d").required(false).longOpt("datFile").desc("creates a dat file").build());
		options.addOption(Option.builder("dif").required(false).longOpt("make-dif").desc("prints out the diferences between the models").build());

		options.addOption(Option.builder("a").required(false).longOpt("allocation-contexts").hasArg().desc("allocation context count").build());
		options.addOption(Option.builder("r").required(false).longOpt("resource-container").hasArg().desc("resource container count").build());

		options.addOption(Option.builder("al").required(false).longOpt("allocation").hasArg().desc("allocate actions").build());
		options.addOption(Option.builder("mi").required(false).longOpt("migrate").hasArg().desc("migrate actions").build());
		options.addOption(Option.builder("de").required(false).longOpt("deallocate").hasArg().desc("deallocate actions").build());
		options.addOption(Option.builder("cr").required(false).longOpt("change-repo").hasArg().desc("change repo actions").build());

		options.addOption(Option.builder("ac").required(false).longOpt("acquire").hasArg().desc("acquire actions").build());
		options.addOption(Option.builder("re").required(false).longOpt("replicate").hasArg().desc("replicate actions").build());
		options.addOption(Option.builder("te").required(false).longOpt("terminate").hasArg().desc("terminate actions").build());

		options.addOption(Option.builder("c").required(false).longOpt("commands").hasArg().desc("command counts in dat file").build());

		/** help */
		options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());
		return options;
	}
}
