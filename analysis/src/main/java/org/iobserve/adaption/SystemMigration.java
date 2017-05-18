package org.iobserve.adaption;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaption.data.AdapdationData;
import org.iobserve.analysis.privacyanalysis.AbstractLinearComposition;

public class SystemMigration extends AbstractLinearComposition<AdapdationData, AdapdationData> {

	public SystemMigration(ModelComparision comparer, ModelTransformationPlanning planner, ModelTransformationExecution executer) {
		super(comparer.getInputPort(), executer.getOutputPort());
		// TODO Auto-generated constructor stub

		this.connectPorts(comparer.getOutputPort(), planner.getInputPort());
		this.connectPorts(planner.getOutputPort(), executer.getInputPort());
	}

}
