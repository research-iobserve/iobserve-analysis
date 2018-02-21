package org.iobserve.peropteryx.rcp;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.analyzer.workflow.ConstantsContainer;
import org.palladiosimulator.edp2.EDP2Plugin;
import org.palladiosimulator.edp2.impl.RepositoryManager;
import org.palladiosimulator.edp2.models.Repository.LocalMemoryRepository;
import org.palladiosimulator.edp2.models.Repository.Repository;
import org.palladiosimulator.edp2.models.Repository.RepositoryFactory;
import org.palladiosimulator.recorderframework.edp2.config.AbstractEDP2RecorderConfigurationFactory;
import org.palladiosimulator.recorderframework.utils.RecorderExtensionHelper;
import org.palladiosimulator.reliability.MarkovEvaluationType;
import org.palladiosimulator.solver.runconfig.MessageStrings;

import de.uka.ipd.sdq.dsexplore.launch.DSEConstantsContainer;
import de.uka.ipd.sdq.dsexplore.launch.DSEConstantsContainer.QualityAttribute;
import de.uka.ipd.sdq.simucomframework.SimuComConfig;
import de.uka.ipd.sdq.simulation.AbstractSimulationConfig;

/**
 * This class programmatically configures PerOpteryx. In the native PerOpteryx plugin, these
 * settings are defined in the RunConfiguration.
 *
 * @author Tobias Pöppke
 * @author Philipp Weimann
 * @author Lars Bluemke (gradle integration)
 *
 */
public final class PerOpteryxLaunchConfigurationBuilder {

    public static final String DSE_LAUNCH_TYPE_ID = "de.uka.ipd.sdq.dsexplore.launchDSE";
    public static final String DEFAULT_LAUNCH_CONFIG_NAME = "iobserve-peropteryx";
    public static final String DEFAULT_PROJECT_WORKING_DIR = "working_dir";
    public static final String DEFAULT_PROJECT_NAME = "iObserve-PerOpteryx";

    private PerOpteryxLaunchConfigurationBuilder(final Map<String, Object> attr) {
        // not to be instantiated
    }

    /**
     * Creates a new Path for the given model inside the models root directory and returns it to the
     * caller.
     *
     * @param modelDir
     *            - The directory relative to the workspace root where the model is stored. May end
     *            with a '/' or without.
     * @param fileExtension
     *            - The file extension of the specific model including a dot, e.g. .allocation for
     *            an allocation model file.
     * @return Model file path specific to eclipse platform
     */
    private static IPath getModelFilePath(final String modelDir, final String fileExtension) {
        // String modelFile;
        //
        // if (modelDir.endsWith("/")) {
        // modelFile = "platform:/resource/" + modelDir.substring(0,
        // modelDir.length() - 1);
        // } else {
        // modelFile = "platform:/resource/" + modelDir;
        // }

        final IPath filePath = ModelFileHelper.getModelFilePath(modelDir, fileExtension);
        return filePath;
    }

    /**
     * Returns a default launch configuration.
     *
     * @param projectModelDir
     *            The Peropteryx project dir in eclipse workspace
     * @param sourceModelDir
     *            The PCM model directory
     * @return The launch configuration
     * @throws CoreException
     */
    public static ILaunchConfiguration getDefaultLaunchConfiguration(final String projectModelDir,
            final String sourceModelDir) throws CoreException {
        final Map<String, Object> attr = new HashMap<>();

        PerOpteryxLaunchConfigurationBuilder.setDefaultConfigFiles(projectModelDir, attr);

        PerOpteryxLaunchConfigurationBuilder.setDefaultGeneralOptions(attr);

        PerOpteryxLaunchConfigurationBuilder.setDefaultAnalysisOptions(attr);

        PerOpteryxLaunchConfigurationBuilder.setDefaultResourceOptions(attr);

        PerOpteryxLaunchConfigurationBuilder.setDefaultReliabilityOptions(attr);

        // setSimuComDefaultOptions(attr);
        PerOpteryxLaunchConfigurationBuilder.setLQNSDefaultOptions(attr, projectModelDir, sourceModelDir);

        PerOpteryxLaunchConfigurationBuilder.setDefaultTacticsOptions(attr);

        final ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        final ILaunchConfigurationType launchConfigType = launchManager
                .getLaunchConfigurationType(PerOpteryxLaunchConfigurationBuilder.DSE_LAUNCH_TYPE_ID);
        final ILaunchConfigurationWorkingCopy workingCopy = launchConfigType.newInstance(null,
                PerOpteryxLaunchConfigurationBuilder.DEFAULT_LAUNCH_CONFIG_NAME);

        workingCopy.setAttributes(attr);

        final ILaunchConfiguration launchConfig = workingCopy.doSave();


        return launchConfig;
    }

    private static void setDefaultTacticsOptions(final Map<String, Object> attr) {
        attr.put(DSEConstantsContainer.USE_REALLOCATION, false);
        attr.put(DSEConstantsContainer.REALLOCATION_UTILISATION_DIFFERENCE, Double.toString(0.5));
        attr.put(DSEConstantsContainer.REALLOCATION_WEIGHT, Double.toString(1.0));

        attr.put(DSEConstantsContainer.USE_PROCESSING_RATE, false);
        attr.put(DSEConstantsContainer.PROCESSING_RATE_DECREASE_FACTOR, Double.toString(0.25));
        attr.put(DSEConstantsContainer.PROCESSING_RATE_INCREASE_FACTOR, Double.toString(0.25));
        attr.put(DSEConstantsContainer.PROCESSING_RATE_THRESHOLD_HIGH_UTILISATION, Double.toString(0.8));
        attr.put(DSEConstantsContainer.PROCESSING_RATE_THRESHOLD_LOW_UTILISATION, Double.toString(0.2));
        attr.put(DSEConstantsContainer.PROCESSING_RATE_WEIGHT, Double.toString(0.1));

        attr.put(DSEConstantsContainer.USE_SERVER_CONSOLIDATION, false);
        attr.put(DSEConstantsContainer.SERVER_CONSOLIDATION_THRESHOLD_LOW_UTILISATION, Double.toString(0.3));
        attr.put(DSEConstantsContainer.SERVER_CONSOLIDATION_WEIGHT, Double.toString(0.5));

        attr.put(DSEConstantsContainer.USE_SERVER_EXPANSION, false);
        attr.put(DSEConstantsContainer.SERVER_EXPANSION_MAX_NUMBER_OF_REPLACEMENTS, Integer.toString(1));
        attr.put(DSEConstantsContainer.SERVER_EXPANSION_THRESHOLD_HIGH_UTILISATION, Double.toString(0.7));
        attr.put(DSEConstantsContainer.SERVER_EXPANSION_WEIGHT, Double.toString(0.7));

        attr.put(DSEConstantsContainer.USE_ANTIPATTERNS, false);
    }

    private static void setSimuComDefaultOptions(final Map<String, Object> attr) {
        attr.put(SimuComConfig.SIMULATE_LINKING_RESOURCES, false);
        attr.put(SimuComConfig.SIMULATE_THROUGHPUT_OF_LINKING_RESOURCES, true);
        attr.put(SimuComConfig.SIMULATE_FAILURES, false);
        attr.put(ConstantsContainer.FEATURE_CONFIG, ConstantsContainer.DEFAULT_FEATURE_CONFIGURATION_FILE);

        attr.put(AbstractSimulationConfig.SIMULATOR_ID, AbstractSimulationConfig.DEFAULT_SIMULATOR_ID);
        attr.put(AbstractSimulationConfig.EXPERIMENT_RUN, AbstractSimulationConfig.DEFAULT_EXPERIMENT_RUN);
        attr.put(AbstractSimulationConfig.VARIATION_ID, AbstractSimulationConfig.DEFAULT_VARIATION_NAME);
        attr.put(AbstractSimulationConfig.SIMULATION_TIME, AbstractSimulationConfig.DEFAULT_SIMULATION_TIME);
        attr.put(AbstractSimulationConfig.MAXIMUM_MEASUREMENT_COUNT,
                AbstractSimulationConfig.DEFAULT_MAXIMUM_MEASUREMENT_COUNT);
        attr.put(AbstractSimulationConfig.PERSISTENCE_RECORDER_NAME,
                AbstractSimulationConfig.DEFAULT_PERSISTENCE_RECORDER_NAME);
        attr.put(AbstractSimulationConfig.USE_FIXED_SEED, false);
        attr.put(SimuComConfig.USE_CONFIDENCE, SimuComConfig.DEFAULT_USE_CONFIDENCE);
        attr.put(SimuComConfig.CONFIDENCE_LEVEL, Integer.toString(SimuComConfig.DEFAULT_CONFIDENCE_LEVEL));
        attr.put(SimuComConfig.CONFIDENCE_HALFWIDTH, Integer.toString(SimuComConfig.DEFAULT_CONFIDENCE_HALFWIDTH));
        attr.put(SimuComConfig.CONFIDENCE_MODELELEMENT_NAME, SimuComConfig.DEFAULT_CONFIDENCE_MODELELEMENT_NAME);
        attr.put(SimuComConfig.CONFIDENCE_MODELELEMENT_URI, SimuComConfig.DEFAULT_CONFIDENCE_MODELELEMENT_URI);
        attr.put(SimuComConfig.CONFIDENCE_USE_AUTOMATIC_BATCHES,
                SimuComConfig.DEFAULT_CONFIDENCE_USE_AUTOMATIC_BATCHES);
        attr.put(SimuComConfig.CONFIDENCE_BATCH_SIZE, Integer.toString(SimuComConfig.DEFAULT_CONFIDENCE_BATCH_SIZE));
        attr.put(SimuComConfig.CONFIDENCE_MIN_NUMBER_OF_BATCHES,
                SimuComConfig.DEFAULT_CONFIDENCE_MIN_NUMBER_OF_BATCHES);
        attr.put(AbstractSimulationConfig.VERBOSE_LOGGING, false);
        attr.put(ConstantsContainer.DELETE_TEMPORARY_DATA_AFTER_ANALYSIS, true);

        PerOpteryxLaunchConfigurationBuilder.setEDP2ResourceRepository(attr);

        PerOpteryxLaunchConfigurationBuilder.setDefaultPersistenceRecorder(attr);
    }

    private static void setLQNSDefaultOptions(final Map<String, Object> attr, final String projectModelDir,
            final String sourceModelDir) {
        (new File(sourceModelDir + "/lqns_out")).mkdirs();
        (new File(sourceModelDir + "/line_out")).mkdirs();
        attr.put(MessageStrings.SOLVER, MessageStrings.LQNS_SOLVER);
        attr.put(MessageStrings.LQNS_OUTPUT, MessageStrings.LQN_OUTPUT_XML);

        attr.put(MessageStrings.SAMPLING_DIST, Double.toString(1.0));
        attr.put(MessageStrings.MAX_DOMAIN, 256);

        attr.put(MessageStrings.CONV_VALUE, Double.toString(0.001));
        attr.put(MessageStrings.IT_LIMIT, Integer.toString(50));
        attr.put(MessageStrings.PRINT_INT, Integer.toString(10));
        attr.put(MessageStrings.UNDER_COEFF, Double.toString(0.5));

        attr.put(MessageStrings.STOP_ON_MESSAGE_LOSS_LQNS, true);
        attr.put(MessageStrings.STOP_ON_MESSAGE_LOSS_LQSIM, true);
        attr.put(MessageStrings.DEBUG_LINE, true);

        attr.put(MessageStrings.INFINITE_TASK_MULTIPLICITY, true);

        attr.put(MessageStrings.RUN_TIME, "");
        attr.put(MessageStrings.BLOCKS, "");
        attr.put(MessageStrings.PS_QUANTUM, Double.toString(0.001));

        attr.put(MessageStrings.PRAGMAS, "");

        attr.put(MessageStrings.LQNS_OUTPUT_DIR, sourceModelDir + "/lqns_out");
        attr.put(MessageStrings.LQSIM_OUTPUT_DIR, sourceModelDir + "/lqsim_out");
        attr.put(MessageStrings.SRE_OUTPUT_FILE, sourceModelDir + "/sre_out");
        attr.put(MessageStrings.LINE_OUT_DIR, sourceModelDir + "/line_out");
        attr.put(MessageStrings.LINE_PROP_FILE, sourceModelDir + "/" + MessageStrings.LINE_PROP_FILENAME);

        attr.put(MessageStrings.SRE_IS_USE_INPUT_MODEL, false);

        attr.put(ConstantsContainer.FEATURE_CONFIG, ConstantsContainer.DEFAULT_FEATURE_CONFIGURATION_FILE);

        attr.put(ConstantsContainer.DELETE_TEMPORARY_DATA_AFTER_ANALYSIS, true);

        // setEDP2ResourceRepository(attr);

        // setDefaultPersistenceRecorder(attr);
    }

    private static void setDefaultPersistenceRecorder(final Map<String, Object> attr) {
        // set default value for persistence framework
        final List<String> recorderNames = RecorderExtensionHelper.getRecorderNames();

        String edp2RecorderName = null;
        for (final String name : recorderNames) {
            if (name.contains("EDP2")) {
                edp2RecorderName = name;
                break;
            }
        }

        if (edp2RecorderName == null) {
            if (recorderNames.size() > 0) {
                attr.put(AbstractSimulationConfig.PERSISTENCE_RECORDER_NAME, recorderNames.get(0));
            } else {
                throw new IllegalArgumentException("No persistence recorder framework could be found!");
            }
        } else {
            attr.put(AbstractSimulationConfig.PERSISTENCE_RECORDER_NAME, edp2RecorderName);
        }
    }

    private static void setEDP2ResourceRepository(final Map<String, Object> attr) {
        final EList<Repository> repositories = EDP2Plugin.INSTANCE.getRepositories().getAvailableRepositories();
        if (repositories.size() > 0) {
            attr.put(AbstractEDP2RecorderConfigurationFactory.REPOSITORY_ID, repositories.get(0).getId());
        } else {
            final LocalMemoryRepository repository = RepositoryFactory.eINSTANCE.createLocalMemoryRepository();
            RepositoryManager.addRepository(EDP2Plugin.INSTANCE.getRepositories(), repository);
            attr.put(AbstractEDP2RecorderConfigurationFactory.REPOSITORY_ID, repository.getId());
        }
    }

    private static void setDefaultReliabilityOptions(final Map<String, Object> attr) {
        attr.put(MessageStrings.MARKOV_STATISTICS, false);
        attr.put(MessageStrings.SINGLE_RESULTS, false);
        attr.put(MessageStrings.NUMBER_OF_EVALUATED_SYSTEM_STATES_ENABLED, false);
        attr.put(MessageStrings.NUMBER_OF_EVALUATED_SYSTEM_STATES, 1);
        attr.put(MessageStrings.NUMBER_OF_EXACT_DECIMAL_PLACES_ENABLED, false);
        attr.put(MessageStrings.NUMBER_OF_EXACT_DECIMAL_PLACES, 1);
        attr.put(MessageStrings.SOLVING_TIME_LIMIT_ENABLED, false);
        attr.put(MessageStrings.SOLVING_TIME_LIMIT, 1);
        attr.put(MessageStrings.LOG_FILE, "");
        attr.put(MessageStrings.MARKOV_MODEL_REDUCTION_ENABLED, true);
        attr.put(MessageStrings.MARKOV_MODEL_TRACES_ENABLED, false);
        attr.put(MessageStrings.ITERATION_OVER_PHYSICAL_SYSTEM_STATES_ENABLED, true);
        attr.put(MessageStrings.MARKOV_MODEL_STORAGE_ENABLED, false);
        attr.put(MessageStrings.MARKOV_MODEL_FILE, "");
        attr.put(MessageStrings.MARKOV_EVALUATION_MODE, MarkovEvaluationType.POINTSOFFAILURE.toString());
    }

    private static void setDefaultResourceOptions(final Map<String, Object> attr) {
        attr.put(DSEConstantsContainer.MIN_NUMBER_RESOURCE_CONTAINERS, Integer.toString(2));
        attr.put(DSEConstantsContainer.MAX_NUMBER_RESOURCE_CONTAINERS, Integer.toString(9));
        attr.put(DSEConstantsContainer.NUMBER_OF_CANDIDATES_PER_ALLOCATION_LEVEL, Integer.toString(10));

        attr.put(DSEConstantsContainer.REALLOCATION_UTILISATION_DIFFERENCE, Double.toString(0.5));
        attr.put(DSEConstantsContainer.REALLOCATION_WEIGHT, Double.toString(1.0));
    }

    private static void setDefaultAnalysisOptions(final Map<String, Object> attr) {
        attr.put(DSEConstantsContainer.getAnalysisMethod(QualityAttribute.PERFORMANCE_QUALITY), "LQN Solver Analysis");
        attr.put(DSEConstantsContainer.getAnalysisMethod(QualityAttribute.COST_QUALITY), "Cost Analysis");
        attr.put(DSEConstantsContainer.getAnalysisMethod(QualityAttribute.NQR_QUALITY), "none");
        attr.put(DSEConstantsContainer.getAnalysisMethod(QualityAttribute.RELIABILITY_QUALITY),
                "Reliability Solver Analysis");
        attr.put(DSEConstantsContainer.getAnalysisMethod(QualityAttribute.SECURITY_QUALITY), "none");
    }

    private static void setDefaultGeneralOptions(final Map<String, Object> attr) {
        attr.put(ConstantsContainer.ANALYSE_ACCURACY, ConstantsContainer.DEFAULT_ANALYSE_ACCURACY);
        attr.put(ConstantsContainer.DO_SENSITIVITY_ANALYSIS, ConstantsContainer.DEFAULT_DO_SENSITIVITY_ANALYSIS);

        attr.put(DSEConstantsContainer.SEARCH_METHOD, DSEConstantsContainer.SEARCH_EVOLUTIONARY);
        attr.put(DSEConstantsContainer.OPTIMISATION_ONLY, true);
        attr.put(DSEConstantsContainer.DESIGN_DECISIONS_ONLY, false);
        attr.put(DSEConstantsContainer.STORE_RESULTS_AS_EMF, true);
        attr.put(DSEConstantsContainer.STORE_RESULTS_AS_CSV, true);
        attr.put(DSEConstantsContainer.TC_INSIGNIFICANT_FRONT_CHANGE_ACTIVATE, true);
        attr.put(DSEConstantsContainer.DSE_ITERATIONS, "1");
        attr.put(DSEConstantsContainer.CROSSOVER_RATE, "0.5");
        attr.put(DSEConstantsContainer.PREDEFINED_INSTANCES, "");
        attr.put(DSEConstantsContainer.CACHE_INSTANCES, "");
        attr.put(DSEConstantsContainer.ALL_CANDIDATES, "");
        attr.put(DSEConstantsContainer.ARCHIVE_CANDIDATES, "");
        attr.put(DSEConstantsContainer.TACTICS_PROBABILITY, "0.6");
        attr.put(DSEConstantsContainer.USE_REALLOCATION, false);
        attr.put(DSEConstantsContainer.USE_SERVER_CONSOLIDATION, false);
        attr.put(DSEConstantsContainer.USE_SERVER_EXPANSION, false);
        attr.put(DSEConstantsContainer.USE_LINK_REALLOCATION, false);
        attr.put(DSEConstantsContainer.USE_ANTIPATTERNS, false);
        attr.put(DSEConstantsContainer.USE_STARTING_POPULATION_HEURISTIC, false);
        attr.put(DSEConstantsContainer.STOP_ON_INITIAL_FAILURE, false);
        attr.put(DSEConstantsContainer.INDIVIDUALS_PER_GENERATION, "3");
    }

    private static void setDefaultConfigFiles(final String modelDir, final Map<String, Object> attr) {
        // Standard PCM settings
        final IPath allocationFile = PerOpteryxLaunchConfigurationBuilder.getModelFilePath(modelDir, "allocation");
        attr.put(ConstantsContainer.ALLOCATION_FILE, allocationFile.toString());

        final IPath outputFolder = new Path("platform:/resource/output");
        attr.put(ConstantsContainer.CLIENTOUT_PATH, outputFolder.toString());

        final IPath repositoryFile = PerOpteryxLaunchConfigurationBuilder.getModelFilePath(modelDir, "repository");
        attr.put(ConstantsContainer.REPOSITORY_FILE, repositoryFile.toString());

        final IPath resourceEnvFile = PerOpteryxLaunchConfigurationBuilder.getModelFilePath(modelDir,
                "resourceenvironment");
        attr.put(ConstantsContainer.RESOURCEENVIRONMENT_FILE, resourceEnvFile.toString());

        final IPath systemFile = PerOpteryxLaunchConfigurationBuilder.getModelFilePath(modelDir, "system");
        attr.put(ConstantsContainer.SYSTEM_FILE, systemFile.toString());

        // Has to be only the name because PerOpteryx will create a project with
        // this name
        final IPath tempFolder = new Path("platform:/resource/temporary");
        attr.put(ConstantsContainer.TEMPORARY_DATA_LOCATION, tempFolder.toString());

        final IPath usageFile = PerOpteryxLaunchConfigurationBuilder.getModelFilePath(modelDir, "usagemodel");
        attr.put(ConstantsContainer.USAGE_FILE, usageFile.toString());

        attr.put(ConstantsContainer.ACCURACY_QUALITY_ANNOTATION_FILE,
                ConstantsContainer.DEFAULT_ACCURACY_QUALITY_ANNOTATION_FILE);
        attr.put(ConstantsContainer.RMI_MIDDLEWARE_REPOSITORY_FILE,
                ConstantsContainer.DEFAULT_RMI_MIDDLEWARE_REPOSITORY_FILE);
        attr.put(ConstantsContainer.EVENT_MIDDLEWARE_REPOSITORY_FILE, ConstantsContainer.DEFAULT_EVENT_MIDDLEWARE_FILE);
        attr.put(ConstantsContainer.FEATURE_CONFIG, ConstantsContainer.DEFAULT_FEATURE_CONFIGURATION_FILE);

        // PerOpteryx specific settings
        final IPath costFile = PerOpteryxLaunchConfigurationBuilder.getModelFilePath(modelDir, "cost");
        attr.put(DSEConstantsContainer.COST_FILE, costFile.toString());

        final IPath decisionFile = PerOpteryxLaunchConfigurationBuilder.getModelFilePath(modelDir, "designdecision");
        attr.put(DSEConstantsContainer.DESIGN_DECISION_FILE, decisionFile.toString());

        final IPath qmlFile = PerOpteryxLaunchConfigurationBuilder.getModelFilePath(modelDir, "qmldeclarations");
        attr.put(DSEConstantsContainer.QML_DEFINITION_FILE, qmlFile.toString());
    }
}
