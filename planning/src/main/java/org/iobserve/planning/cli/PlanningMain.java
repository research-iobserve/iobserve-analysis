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
package org.iobserve.planning.cli;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.model.PCMModelHandler;
import org.iobserve.planning.environment.PalladioEclipseEnvironment;
import org.iobserve.planning.utils.ModelHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for executing the planning phase outside of the pipeline and for creating cloud
 * containers into the resource environment, so they can be used for modelling the deployment.
 *
 * @author Tobias Pöppke
 *
 */
public final class PlanningMain {
    public static final String INPUT_WORKING_DIR_OPTION = "working-dir";
    public static final String INPUT_WORKING_DIR_OPTION_SHORT = "w";

    public static final String INPUT_MODEL_NAME_OPTION = "model-name";
    public static final String INPUT_MODEL_NAME_OPTION_SHORT = "m";

    public static final String CREATE_RESOURCEENVIRONMENT_OPTION = "create-resources";
    public static final String CREATE_RESOURCEENVIRONMENT_OPTION_SHORT = "r";

    public static final String PEROPTERYX_DIR_OPTION = "peropteryx-dir";
    public static final String PEROPTERYX_DIR_OPTION_SHORT = "p";

    public static final String LQNS_DIR_OPTION = "lqns-dir";
    public static final String LQNS_DIR_OPTION_SHORT = "l";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanningMain.class);

    private PlanningMain() {
        // Do nothing.
    }

    public static void main(final String[] args) throws IOException, InitializationException {
        final CommandLineParser parser = new DefaultParser();

        final String workingDir;
        final String perOpteryxDir;

        final CommandLine commandLine;
        try {
            for (final String arg : args) {
                if (PlanningMain.LOGGER.isInfoEnabled()) {
                    PlanningMain.LOGGER.info("arg: " + arg);
                }
            }
            commandLine = parser.parse(PlanningMain.createOptions(), args);
            workingDir = commandLine.getOptionValue(PlanningMain.INPUT_WORKING_DIR_OPTION);
            perOpteryxDir = commandLine.getOptionValue(PlanningMain.PEROPTERYX_DIR_OPTION);

            if (PlanningMain.LOGGER.isInfoEnabled()) {
                PlanningMain.LOGGER.info("Working dir: " + workingDir + ", PerOpteryx dir: " + perOpteryxDir);
            }
        } catch (final ParseException exp) {
            // LOG.error("CLI error: " + exp.getMessage());
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("planning", PlanningMain.createOptions());
            return;
        }

        final URI modelURI = URI.createFileURI(workingDir);
        if (PlanningMain.LOGGER.isInfoEnabled()) {
            PlanningMain.LOGGER.info("modelURI: " + modelURI);
        }

        final URI perOpteryxURI = URI.createFileURI(perOpteryxDir);
        if (PlanningMain.LOGGER.isInfoEnabled()) {
            PlanningMain.LOGGER.info("perOpteryxURI: " + perOpteryxURI);
            PlanningMain.LOGGER.info("lqnsURI: " + perOpteryxURI);
        }
        PalladioEclipseEnvironment.INSTANCE.setup();

        if (!commandLine.hasOption(PlanningMain.CREATE_RESOURCEENVIRONMENT_OPTION)) {
            if (PlanningMain.LOGGER.isInfoEnabled()) {
                PlanningMain.LOGGER.info("Executing optimization...");
            }

            final AdaptationData adaptationData = new AdaptationData();
            adaptationData.setRuntimeModelURI(modelURI);

            // TODO: removed by lbl during refactoring of planning
            // final PlanningData planningData = new PlanningData();
            // planningData.setAdaptationData(adaptationData);
            // planningData.setOriginalModelDir(modelURI);
            // planningData.setPerOpteryxDir(perOpteryxURI);
            //
            // // Process model
            // final ModelTransformer transformer = new ModelTransformer(planningData);
            // transformer.transformModel();

            // Execute PerOpteryx
            final int result = 0;
            // try {
            // ExecutionWrapper execution = new
            // ExecutionWrapper(planningData.getProcessedModelDir(),
            // perOpteryxURI);
            // result = execution.startModelGeneration();
            // } catch (IOException e) {
            // LOG.error("Execution failed with IOException.", e);
            // return;
            // }

            if (result == 0) {
                if (PlanningMain.LOGGER.isInfoEnabled()) {
                    PlanningMain.LOGGER.info("Optimization was successful.");
                }
            } else {
                if (PlanningMain.LOGGER.isInfoEnabled()) {
                    PlanningMain.LOGGER.info("Optimization failed.");
                }
            }
        } else {
            if (PlanningMain.LOGGER.isInfoEnabled()) {
                PlanningMain.LOGGER.info("Creating ResourceEnvironment...");
            }
            final PCMModelHandler modelHandler = new PCMModelHandler(new File(workingDir));
            ModelHelper.fillResourceEnvironmentFromCloudProfile(
                    org.eclipse.emf.common.util.URI.createFileURI(workingDir), modelHandler);
            if (PlanningMain.LOGGER.isInfoEnabled()) {
                PlanningMain.LOGGER.info("ResourceEnvironment successfully created.");
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

        final Option workDirOption = new Option(PlanningMain.INPUT_WORKING_DIR_OPTION_SHORT,
                PlanningMain.INPUT_WORKING_DIR_OPTION, true,
                "Working directory containing the model files. Note that the files may be changed in the process.");
        workDirOption.setRequired(true);

        final Option modelNameOption = new Option(PlanningMain.INPUT_MODEL_NAME_OPTION_SHORT,
                PlanningMain.INPUT_MODEL_NAME_OPTION, true,
                "The name of the model contained in the working directory.");
        modelNameOption.setRequired(true);

        final Option perOpteryxDirOption = new Option(PlanningMain.PEROPTERYX_DIR_OPTION_SHORT,
                PlanningMain.PEROPTERYX_DIR_OPTION, true, "Directory containing the PerOpteryx headless executable.");
        modelNameOption.setRequired(true);

        final Option lqnsDirOption = new Option(PlanningMain.LQNS_DIR_OPTION_SHORT, PlanningMain.LQNS_DIR_OPTION, true,
                "Directory containing the LQN Solver executable.");
        modelNameOption.setRequired(true);

        final Option createResourcesOption = new Option(PlanningMain.CREATE_RESOURCEENVIRONMENT_OPTION_SHORT,
                PlanningMain.CREATE_RESOURCEENVIRONMENT_OPTION, false,
                "Create resource environment from cloudprofile. This is only needed when the cloudprofile changes.");
        modelNameOption.setRequired(false);

        final Option helpOption = new Option("h", "help", false, "Show usage information");

        options.addOption(workDirOption);
        options.addOption(modelNameOption);
        options.addOption(perOpteryxDirOption);
        options.addOption(createResourcesOption);
        options.addOption(lqnsDirOption);

        /** help */
        options.addOption(helpOption);

        return options;
    }

}
