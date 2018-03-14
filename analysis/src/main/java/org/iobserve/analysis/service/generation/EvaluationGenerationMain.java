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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.iobserve.analysis.data.graph.GraphFactory;
import org.iobserve.model.PCMModelHandler;

/**
 * TODO move class into own project if this is still used. TODO convert to JCommander based program.
 *
 * @author unknown
 *
 */
public final class EvaluationGenerationMain {

    private static final Logger LOGGER = LogManager.getLogger(EvaluationGenerationMain.class);

    private EvaluationGenerationMain() {
        // all static class
    }

    public static void main(final String[] args) {

        final CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(EvaluationGenerationMain.createHelpOptions(), args);

            if (commandLine.hasOption("h")) {
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("iobserve-analysis", EvaluationGenerationMain.createOptions());
            } else {
                commandLine = parser.parse(EvaluationGenerationMain.createOptions(), args);

                final File model = new File(commandLine.getOptionValue("o"));

                if (commandLine.hasOption("n")) {
                    EvaluationGenerationMain.clearDirectory(commandLine.getOptionValue("o"));
                    ModelGenerationFactory.createNewModel(commandLine);

                    final PCMModelHandler modelProviders = new PCMModelHandler(model);
                    final GraphFactory graphFactory = new GraphFactory();
                    graphFactory.buildGraph(modelProviders);
                }
                if (commandLine.hasOption("m")) {
                    EvaluationGenerationMain.clearDirectory(commandLine.getOptionValue("o"));
                    ModelModificationFactory.createNewModel(commandLine);

                    final PCMModelHandler modelProviers = new PCMModelHandler(model);
                    final GraphFactory graphFactory = new GraphFactory();
                    graphFactory.buildGraph(modelProviers);
                }
            }
        } catch (final Exception e) { // NOCS NOPMD
            e.printStackTrace();
        }
    }

    private static void clearDirectory(final String fileURI) {
        if (EvaluationGenerationMain.LOGGER.isInfoEnabled()) {
            EvaluationGenerationMain.LOGGER.info("Clearing output folder: " + fileURI);
        }
        final File outputDir = new File(fileURI);

        if (outputDir.exists()) {
            for (final File file : outputDir.listFiles()) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }
    }

    /**
     * Create the command line parameter setup.
     *
     * @return options for the command line parser
     */
    private static Options createOptions() {
        final Options options = new Options();

        options.addOption(
                Option.builder("i").required(true).longOpt("input").hasArg().desc("the repository model file").build());
        options.addOption(
                Option.builder("o").required(true).longOpt("output").hasArg().desc("the output directory").build());
        options.addOption(Option.builder("n").required(false).longOpt("generate-new")
                .desc("generates new model, based on input repo").build());
        options.addOption(Option.builder("m").required(false).longOpt("modify").desc("modify the input model").build());

        options.addOption(Option.builder("a").required(false).longOpt("allocation-contexts").hasArg()
                .desc("allocation context count").build());
        options.addOption(Option.builder("r").required(false).longOpt("resource-container").hasArg()
                .desc("resource container count").build());

        options.addOption(
                Option.builder("al").required(false).longOpt("allocation").hasArg().desc("allocate actions").build());
        options.addOption(
                Option.builder("mi").required(false).longOpt("migrate").hasArg().desc("migrate actions").build());
        options.addOption(
                Option.builder("de").required(false).longOpt("deallocate").hasArg().desc("deallocate actions").build());
        options.addOption(Option.builder("cr").required(false).longOpt("change-repo").hasArg()
                .desc("change repo actions").build());

        options.addOption(
                Option.builder("ac").required(false).longOpt("acquire").hasArg().desc("acquire actions").build());
        options.addOption(
                Option.builder("re").required(false).longOpt("replicate").hasArg().desc("replicate actions").build());
        options.addOption(
                Option.builder("te").required(false).longOpt("terminate").hasArg().desc("terminate actions").build());

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

        options.addOption(
                Option.builder("i").required(true).longOpt("input").hasArg().desc("the repository model file").build());
        options.addOption(
                Option.builder("o").required(true).longOpt("output").hasArg().desc("the output directory").build());
        options.addOption(Option.builder("n").required(false).longOpt("generate-new")
                .desc("generates new model, based on input repo").build());
        options.addOption(Option.builder("m").required(false).longOpt("modify").desc("modify the input model").build());

        options.addOption(Option.builder("a").required(false).longOpt("allocation-contexts").hasArg()
                .desc("allocation context count").build());
        options.addOption(Option.builder("r").required(false).longOpt("resource-container").hasArg()
                .desc("resource container count").build());

        options.addOption(
                Option.builder("al").required(false).longOpt("allocation").hasArg().desc("allocate actions").build());
        options.addOption(
                Option.builder("mi").required(false).longOpt("migrate").hasArg().desc("migrate actions").build());
        options.addOption(
                Option.builder("de").required(false).longOpt("deallocate").hasArg().desc("deallocate actions").build());
        options.addOption(Option.builder("cr").required(false).longOpt("change-repo").hasArg()
                .desc("change repo actions").build());

        options.addOption(
                Option.builder("ac").required(false).longOpt("acquire").hasArg().desc("acquire actions").build());
        options.addOption(
                Option.builder("re").required(false).longOpt("replicate").hasArg().desc("replicate actions").build());
        options.addOption(
                Option.builder("te").required(false).longOpt("terminate").hasArg().desc("terminate actions").build());

        /** help */
        options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());
        return options;
    }
}
