package org.iobserve.service.generation;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class EvaluationGeneration {

//	private static final Logger LOG = LogManager.getLogger(EvaluationGeneration.class);

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
					ModelGeneration.createNewModel(commandLine);
				} 
				if (commandLine.hasOption("m")) {
					ModelModification.createNewModel(commandLine);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
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
