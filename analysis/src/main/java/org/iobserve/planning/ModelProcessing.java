package org.iobserve.planning;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.data.PlanningData;

import teetime.stage.basic.AbstractTransformation;

/**
 * Stage for processing the PCM model before the model is used in PerOpteryx for
 * generating adaptation candidates. This stage performs the grouping of
 * allocation contexts into allocation groups to reduce the available degrees of
 * freedom for the design space exploration.
 *
 * @author Tobias Pöppke
 *
 */
public class ModelProcessing extends AbstractTransformation<AdaptationData, PlanningData> {

	private final URI perOpteryxDir;

	/**
	 * Creates a new model processing stage and fills the planning data with the
	 * given location of the headless PerOpteryx version. The planning data is
	 * passed on through all planning stages.
	 *
	 * @param perOpteryxDir
	 *            the location of the headless PerOpteryx executable
	 */
	public ModelProcessing(final URI perOpteryxDir) {
		this.perOpteryxDir = perOpteryxDir;
	}

	@Override
	protected void execute(AdaptationData element) throws Exception {
		AdaptationData adaptationData = element;
		PlanningData planningData = new PlanningData();

//		adaptationData.setRuntimeModelURI(element);
		planningData.setAdaptationData(adaptationData);
		planningData.setPerOpteryxDir(this.perOpteryxDir);
		planningData.setOriginalModelDir(adaptationData.getRuntimeModelURI());

		ModelTransformer modelTransformer = new ModelTransformer(planningData);

		modelTransformer.transformModel();

		this.outputPort.send(planningData);
	}

}
