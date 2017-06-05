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

		URI inputModelDir = planningData.getAdaptationData().getRuntimeModelURI();
		AdaptationData adaptationData = planningData.getAdaptationData();

		ExecutionWrapper exec = new ExecutionWrapper(inputModelDir, planningData.getPerOpteryxDir(), planningData.getLqnsDir());

		int result = EXEC_SUCCESS;// exec.startModelGeneration();

		if (result == EXEC_ERROR) {
			adaptationData.setReDeploymentURI(URI.createFileURI(
					"C:\\GitRepositorys\\cocome\\cocome-cloud-jee-privacy\\EvalPCMModels\\SystemAdaptation\\AssemblyContextActionModel"));
		} else if (result == EXEC_SUCCESS) {
			adaptationData.setReDeploymentURI(URI.createFileURI(
					"C:\\GitRepositorys\\cocome\\cocome-cloud-jee-privacy\\EvalPCMModels\\SystemAdaptation\\ResourceContainerActionModel"));
		} else {
			throw new RuntimeException("PerOpteryx exited with unkown result/exec code");
		}

		this.outputPort.send(planningData);
	}

}
