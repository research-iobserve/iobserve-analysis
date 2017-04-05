package org.iobserve.migration;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.privacyanalysis.AbstractLinearComposition;
import org.iobserve.migration.data.ModelTransformations;

public class SystemMigration extends AbstractLinearComposition<URI, ModelTransformations> {

	public SystemMigration(ModelComparision comparer, ModelTransformationPlanning planner ,ModelTransformationExecution executer) {
		super(comparer.getInputPort(), executer.getOutputPort());
		// TODO Auto-generated constructor stub
		
		this.connectPorts(comparer.getOutputPort(), planner.getInputPort());
		this.connectPorts(planner.getOutputPort(), executer.getInputPort());
	}

}
