package org.iobserve.planning;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.data.PlanningData;
import org.iobserve.planning.peropteryx.ExecutionWrapper;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class creates potential migration candidates via PerOpteryx. The class
 * is OS independent.
 *
 * @author Philipp Weimann
 * @author Robert Heinrich
 */
public class ModelOptimization extends AbstractTransformation<PlanningData, PlanningData> {

	private final static int EXEC_SUCCESS = 0;

	private final static String[] PerOpteryxOutputSubFolders = new String[] { "PerOpteryx_results", "costOptimalModel" };

	@Override
	protected void execute(PlanningData planningData) throws Exception {
		CandidateGeneration.LOG.info("Model Optimizing");

		URI inputModelDir = planningData.getProcessedModelDir();
		AdaptationData adaptationData = planningData.getAdaptationData();

		ExecutionWrapper exec = new ExecutionWrapper(inputModelDir, planningData.getPerOpteryxDir(), planningData.getLqnsDir(),
				planningData.getPrivacyAnalysisFile());

		CandidateGeneration.LOG.warn("Skipping actual generation for evaluation purposes!");
		int result = -1;// exec.startModelGeneration();

		if (result != EXEC_SUCCESS) {
			String uriString = "C:\\GitRepositorys\\iobserve-analysis\\analysis\\res\\working_dir\\snapshot\\Test";
			
			CandidateGeneration.LOG.warn(String.format("Setting %s as geneartion output string", uriString));
			
			adaptationData.setReDeploymentURI(URI.createFileURI(uriString));
			// throw new RuntimeException("PerOpteryx exited with error code " + result);
		} else {
			URI redeploymentURI = planningData.getProcessedModelDir().appendSegments(PerOpteryxOutputSubFolders);
			adaptationData.setReDeploymentURI(redeploymentURI);
		}

		this.outputPort.send(planningData);
	}

}
