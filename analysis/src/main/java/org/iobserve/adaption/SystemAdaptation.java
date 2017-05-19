package org.iobserve.adaption;

import org.iobserve.adaption.data.AdaptationData;
import org.iobserve.analysis.utils.AbstractLinearComposition;

public class SystemAdaptation extends AbstractLinearComposition<AdaptationData, AdaptationData> {

	public SystemAdaptation(ModelComparision comparer, ModelTransformationPlanning planner, ModelTransformationExecution executer) {
		super(comparer.getInputPort(), executer.getOutputPort());

		this.connectPorts(comparer.getOutputPort(), planner.getInputPort());
		this.connectPorts(planner.getOutputPort(), executer.getInputPort());
	}

}
