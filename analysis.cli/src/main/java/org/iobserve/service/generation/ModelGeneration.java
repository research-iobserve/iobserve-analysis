package org.iobserve.service.generation;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class ModelGeneration {

	public static void main(String[] args) {
		final CommandLineParser parser = new DefaultParser();
		try {
			CommandLine commandLine = parser.parse(ModelGeneration.createHelpOptions(), args);

			if (commandLine.hasOption("h")) {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("iobserve-analysis", ModelGeneration.createOptions());
			} else {
				commandLine = parser.parse(ModelGeneration.createOptions(), args);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * Create the command line parameter setup.
	 *
	 * @return options for the command line parser
	 */
	private static Options createOptions() {
		final Options options = new Options();

		options.addOption(Option.builder("i").required(true).longOpt("input").hasArg().desc("a Kieker logfile directory").build());
		options.addOption(Option.builder("o").required(true).longOpt("output").hasArg().desc("directory containing all PCM models").build());
		options.addOption(Option.builder("n").required(false).longOpt("generate-new").desc("generates new model, based on input repo").build());
		options.addOption(Option.builder("m").required(false).longOpt("modify").desc("modify the input model").build());

		options.addOption(Option.builder("al").required(false).longOpt("allocation").hasArg().desc("allocate actions").build());
		options.addOption(Option.builder("mi").required(false).longOpt("migrate").hasArg().desc("migrate actions").build());
		options.addOption(Option.builder("de").required(false).longOpt("deallocate").hasArg().desc("deallocate actions").build());
		options.addOption(Option.builder("cr").required(false).longOpt("change-repo").hasArg().desc("change repo actions").build());

		options.addOption(Option.builder("ac").required(false).longOpt("acquire").hasArg().desc("acquire actions").build());
		options.addOption(Option.builder("re").required(false).longOpt("replicate").hasArg().desc("replicate actions").build());
		options.addOption(Option.builder("te").required(false).longOpt("terminate").hasArg().desc("terminate actions").build());

		return options;
	}

	/**
	 * Create a command line setup with only the help option.
	 *
	 * @return returns simplified options
	 */
	private static Options createHelpOptions() {
		final Options options = new Options();

		options.addOption(Option.builder("i").required(true).longOpt("input").hasArg().desc("a Kieker logfile directory").build());
		options.addOption(Option.builder("o").required(true).longOpt("output").hasArg().desc("directory containing all PCM models").build());
		options.addOption(Option.builder("n").required(false).longOpt("generate-new").desc("generates new model, based on input repo").build());
		options.addOption(Option.builder("m").required(false).longOpt("modify").desc("modify the input model").build());

		options.addOption(Option.builder("al").required(false).longOpt("allocation").hasArg().desc("allocate actions").build());
		options.addOption(Option.builder("mi").required(false).longOpt("migrate").hasArg().desc("migrate actions").build());
		options.addOption(Option.builder("de").required(false).longOpt("deallocate").hasArg().desc("deallocate actions").build());
		options.addOption(Option.builder("cr").required(false).longOpt("change-repo").hasArg().desc("change repo actions").build());

		options.addOption(Option.builder("ac").required(false).longOpt("acquire").hasArg().desc("acquire actions").build());
		options.addOption(Option.builder("re").required(false).longOpt("replicate").hasArg().desc("replicate actions").build());
		options.addOption(Option.builder("te").required(false).longOpt("terminate").hasArg().desc("terminate actions").build());

		return options;
	}

}
