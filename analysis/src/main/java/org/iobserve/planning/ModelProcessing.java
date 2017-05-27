package org.iobserve.planning;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.data.PlanningData;

import teetime.stage.basic.AbstractTransformation;

public class ModelProcessing extends AbstractTransformation<URI, PlanningData> {

	private final URI perOpteryxDir;

	public ModelProcessing(final URI perOpteryxDir) {
		this.perOpteryxDir = perOpteryxDir;
	}

	@Override
	protected void execute(URI element) throws Exception {
		AdaptationData adaptationData = new AdaptationData();
		PlanningData planningData = new PlanningData();

		adaptationData.setRuntimeModelURI(element);
		planningData.setAdaptationData(adaptationData);
		planningData.setPerOpteryxDir(this.perOpteryxDir);
		planningData.setOriginalModelDir(element);

		ModelTransformer modelTransformer = new ModelTransformer(planningData);

		modelTransformer.transformModel();

		this.outputPort.send(planningData);
	}

}
