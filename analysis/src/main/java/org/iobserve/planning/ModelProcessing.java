package org.iobserve.planning;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.planning.data.PlanningData;

import teetime.stage.basic.AbstractTransformation;

/**
 * Stage for processing the PCM model before the model is used in PerOpteryx for
 * generating adaptation candidates. This stage performs the grouping of
 * allocation contexts into allocation groups to reduce the available degrees of
 * freedom for the design space exploration.
 *
 * @author Tobias Poeppke
 * @author Philipp Weimann
 * @author Robert Heinrich
 *
 */
public class ModelProcessing extends AbstractTransformation<AdaptationData, PlanningData> {
	public static final String PROCESSED_MODEL_FOLDER = "processedModel";

	private final URI perOpteryxDir;
	private final URI lqnsDir;
	private final URI privacyAnalysisFile;

	/**
	 * Creates a new model processing stage and fills the planning data with the
	 * given location of the headless PerOpteryx version. The planning data is
	 * passed on through all planning stages.
	 *
	 * @param perOpteryxDir
	 *            the location of the headless PerOpteryx executable
	 */
	public ModelProcessing(final URI perOpteryxDir, final URI lqnsDir, final URI privacyAnalysisFile) {
		this.perOpteryxDir = perOpteryxDir;
		this.lqnsDir = lqnsDir;
		this.privacyAnalysisFile = privacyAnalysisFile;
	}

	@Override
	protected void execute(AdaptationData element) throws Exception {
		CandidateGeneration.LOG.info("Model Processing");
		
		AdaptationData adaptationData = element;
		PlanningData planningData = new PlanningData();

		// Set required information
		planningData.setAdaptationData(adaptationData);
		planningData.setPerOpteryxDir(this.perOpteryxDir);
		planningData.setOriginalModelDir(adaptationData.getRuntimeModelURI());
		planningData.setLqnsDir(lqnsDir);
		planningData.setPrivacyAnalysisFile(privacyAnalysisFile);

		// Create Snapshot
		InitializeModelProviders models = adaptationData.getRuntimeModelProviders();
		SnapshotBuilder snapshotBuilder = new SnapshotBuilder(PROCESSED_MODEL_FOLDER, models);
		URI snapshotLocation = snapshotBuilder.createSnapshot();
		planningData.setProcessedModelDir(snapshotLocation);

		this.outputPort.send(planningData);
	}

}
