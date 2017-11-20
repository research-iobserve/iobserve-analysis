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
 * @author Tobias Pöppke
 */
public class ModelOptimization extends AbstractTransformation<PlanningData, PlanningData> {

	private final static int EXEC_SUCCESS = 0;

	@Override
	protected void execute(final PlanningData planningData) throws Exception {

		final URI inputModelDir = planningData.getProcessedModelDir();
		final AdaptationData adaptationData = planningData.getAdaptationData();

		final ExecutionWrapper exec = new ExecutionWrapper(inputModelDir, planningData.getPerOpteryxDir(), planningData.getLqnsDir());

		final int result = exec.startModelGeneration();

		if (result != EXEC_SUCCESS) {
			throw new RuntimeException("PerOpteryx exited with error code " + result);
		} else {
			adaptationData.setReDeploymentURI(planningData.getProcessedModelDir());
		}

		this.outputPort.send(planningData);
	}

}
