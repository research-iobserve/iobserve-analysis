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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.planning.ModelTransformer;
import org.iobserve.planning.data.PlanningData;
import org.iobserve.planning.environment.PalladioEclipseEnvironment;
import org.iobserve.planning.utils.ModelHelper;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.seff.ResourceDemandingSEFF;
import org.palladiosimulator.pcm.seff.SeffFactory;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.pcm.seff.StopAction;

/**
 * Main class for executing the planning phase outside of the pipeline and for
 * creating cloud containers into the resource environment, so they can be used
 * for modelling the deployment.
 *
 * @author Tobias Pöppke
 *
 */
public final class PlanningMain {
	private static final Logger LOG = LogManager.getLogger(PlanningMain.class);

	public static final String INPUT_WORKING_DIR_OPTION = "working-dir";
	public static final String INPUT_WORKING_DIR_OPTION_SHORT = "w";

	public static final String INPUT_MODEL_NAME_OPTION = "model-name";
	public static final String INPUT_MODEL_NAME_OPTION_SHORT = "m";

	public static final String CREATE_RESOURCEENVIRONMENT_OPTION = "create-resources";
	public static final String CREATE_RESOURCEENVIRONMENT_OPTION_SHORT = "r";

	public static final String CREATE_SEFFS_OPTION = "create-seffs";
	public static final String CREATE_SEFFS_OPTION_SHORT = "s";

	public static final String PEROPTERYX_DIR_OPTION = "peropteryx-dir";
	public static final String PEROPTERYX_DIR_OPTION_SHORT = "p";

	private PlanningMain() {
		// Do nothing.
	}

	public static void main(final String[] args) {
		final CommandLineParser parser = new DefaultParser();

		final String workingDir;
		final String perOpteryxDir;

		final CommandLine commandLine;
		try {
			for (String arg : args) {
				LOG.info("arg: " + arg);
			}
			commandLine = parser.parse(PlanningMain.createOptions(), args);
			workingDir = commandLine.getOptionValue(INPUT_WORKING_DIR_OPTION);
			perOpteryxDir = commandLine.getOptionValue(PEROPTERYX_DIR_OPTION);

			LOG.info("Working dir: " + workingDir + ", PerOpteryx dir: " + perOpteryxDir);
		} catch (final ParseException exp) {
			// LOG.error("CLI error: " + exp.getMessage());
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("planning", PlanningMain.createOptions());
			return;
		}

		URI modelURI = URI.createFileURI(workingDir);
		LOG.info("modelURI: " + modelURI);

		URI perOpteryxURI = URI.createFileURI(perOpteryxDir);
		LOG.info("perOpteryxURI: " + perOpteryxURI);

		LOG.info(String.format("URI was %s", modelURI.path()));

		PalladioEclipseEnvironment.INSTANCE.setup();

		if (commandLine.hasOption(CREATE_RESOURCEENVIRONMENT_OPTION)) {
			LOG.info("Creating ResourceEnvironment...");
			InitializeModelProviders modelProviders = new InitializeModelProviders(new File(workingDir));
			ModelHelper.fillResourceEnvironmentFromCloudProfile(modelProviders);
			LOG.info("ResourceEnvironment successfully created.");
		} else if (commandLine.hasOption(CREATE_SEFFS_OPTION)) {
			LOG.info("Creating seffs...");
			InitializeModelProviders modelProviders = new InitializeModelProviders(new File(workingDir));
			RepositoryModelProvider repositoryProvider = modelProviders.getRepositoryModelProvider();

			for (RepositoryComponent component : repositoryProvider.getModel().getComponents__Repository()) {
				if (component instanceof BasicComponent) {
					BasicComponent basicComponent = (BasicComponent) component;
					for (ProvidedRole role : basicComponent.getProvidedRoles_InterfaceProvidingEntity()) {
						if (role instanceof OperationProvidedRole) {
							OperationProvidedRole operationRole = (OperationProvidedRole) role;
							for (OperationSignature signature : operationRole
							        .getProvidedInterface__OperationProvidedRole()
							        .getSignatures__OperationInterface()) {
								ServiceEffectSpecification seffOpt = basicComponent
								        .getServiceEffectSpecifications__BasicComponent().stream().filter(seff -> seff
								                .getDescribedService__SEFF().getId().equals(signature.getId()))
								        .findFirst().orElse(null);
								if (seffOpt == null) {
									ResourceDemandingSEFF seff = SeffFactory.eINSTANCE.createResourceDemandingSEFF();
									seff.setBasicComponent_ServiceEffectSpecification(basicComponent);
									seff.setDescribedService__SEFF(signature);
									seff.setSeffTypeID(EcoreUtil.generateUUID());
									basicComponent.getServiceEffectSpecifications__BasicComponent().add(seff);
								} else if (seffOpt instanceof ResourceDemandingSEFF) {
									ResourceDemandingSEFF seff = (ResourceDemandingSEFF) seffOpt;
									if (seff.getSteps_Behaviour().size() < 2) {
										StartAction start = SeffFactory.eINSTANCE.createStartAction();
										StopAction stop = SeffFactory.eINSTANCE.createStopAction();

										start.setEntityName("aName");
										start.setId(EcoreUtil.generateUUID());
										start.setSuccessor_AbstractAction(stop);

										stop.setEntityName("aName");
										stop.setId(EcoreUtil.generateUUID());
										stop.setPredecessor_AbstractAction(start);

										seff.getSteps_Behaviour().add(start);
										seff.getSteps_Behaviour().add(stop);
									}
								}
							}
						}
					}
				}
			}
			repositoryProvider.save();
			LOG.info("Seffs successfully created.");
		} else {
			LOG.info("Executing optimization...");

			AdaptationData adaptationData = new AdaptationData();
			adaptationData.setRuntimeModelURI(modelURI);

			PlanningData planningData = new PlanningData();
			planningData.setAdaptationData(adaptationData);
			planningData.setOriginalModelDir(modelURI);
			planningData.setPerOpteryxDir(perOpteryxURI);

			// Process model
			ModelTransformer transformer = new ModelTransformer(planningData);
			try {
				transformer.transformModel();
			} catch (IOException | InitializationException e) {
				LOG.error("Exception while executing model transformation: ", e);
			}

			// Execute PerOpteryx
			int result = 0;
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
				LOG.info("Optimization was successful.");
			} else {
				LOG.info("Optimization failed.");
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

		final Option workDirOption = new Option(INPUT_WORKING_DIR_OPTION_SHORT, INPUT_WORKING_DIR_OPTION, true,
		        "Working directory containing the model files. Note that the files may be changed in the process.");
		workDirOption.setRequired(true);

		final Option modelNameOption = new Option(INPUT_MODEL_NAME_OPTION_SHORT, INPUT_MODEL_NAME_OPTION, true,
		        "The name of the model contained in the working directory.");
		modelNameOption.setRequired(true);

		final Option perOpteryxDirOption = new Option(PEROPTERYX_DIR_OPTION_SHORT, PEROPTERYX_DIR_OPTION, true,
		        "Directory containing the PerOpteryx headless executable.");
		modelNameOption.setRequired(true);

		final Option createResourcesOption = new Option(CREATE_RESOURCEENVIRONMENT_OPTION_SHORT,
		        CREATE_RESOURCEENVIRONMENT_OPTION, false,
		        "Create resource environment from cloudprofile. This is only needed when the cloudprofile changes.");
		modelNameOption.setRequired(false);

		final Option createSeffsOption = new Option(CREATE_SEFFS_OPTION_SHORT, CREATE_SEFFS_OPTION, false,
		        "Create empty seffs in repository.");
		modelNameOption.setRequired(false);

		final Option helpOption = new Option("h", "help", false, "Show usage information");

		options.addOption(workDirOption);
		options.addOption(modelNameOption);
		options.addOption(perOpteryxDirOption);
		options.addOption(createResourcesOption);
		options.addOption(createSeffsOption);

		/** help */
		options.addOption(helpOption);

		return options;
	}

}
