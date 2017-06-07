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
 */
public class ModelOptimization extends AbstractTransformation<PlanningData, PlanningData> {

	private final static int EXEC_SUCCESS = 0;
	private final static int EXEC_ERROR = 1;

	@Override
	protected void execute(PlanningData planningData) throws Exception {
		CandidateGeneration.LOG.info("Model Optimizing");

		URI inputModelDir = planningData.getProcessedModelDir();
		AdaptationData adaptationData = planningData.getAdaptationData();
		ExecutionWrapper exec = new ExecutionWrapper(inputModelDir, planningData.getPerOpteryxDir(), planningData.getLqnsDir());

		int result = 2;//exec.startModelGeneration();

		if (result != EXEC_SUCCESS) {
			// String uriString =
			// "C:\\GitRepositorys\\iobserve-analysis\\analysis\\res\\working_dir\\snapshot\\processedModel\\PerOpteryx_results\\costOptimalModel";
			String uriString = "C:\\GitRepositorys\\iobserve-analysis\\analysis\\res\\working_dir\\snapshot\\Test";

			adaptationData.setReDeploymentURI(URI.createFileURI(uriString));
			// throw new RuntimeException("PerOpteryx exited with error code " +
			// result);
		} else {
			adaptationData.setReDeploymentURI(planningData.getProcessedModelDir());
		}

		this.outputPort.send(planningData);
	}

}
