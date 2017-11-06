package org.iobserve.adaptation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.TerminateAction;

import teetime.stage.basic.AbstractTransformation;

/**
 * This stage orderes the adaptation {@link Action}s into an executable
 * sequence.
 * 
 * @author Philipp Weimann
 *
 */
public class AdaptationPlanning extends AbstractTransformation<AdaptationData, AdaptationData> {

	Collection<ResourceContainerAction> aquires;
	Collection<ResourceContainerAction> terminates;

	Collection<AssemblyContextAction> allocations;
	Collection<AssemblyContextAction> migrations;
	Collection<AssemblyContextAction> changes;
	Collection<AssemblyContextAction> deallocations;

	@Override
	protected void execute(AdaptationData element) throws Exception {
		
		SystemAdaptation.LOG.info("Planning adaptation order");
		
		init(element);

		List<Action> adaptionSteps = new ArrayList<Action>();
		adaptionSteps.addAll(aquires);
		adaptionSteps.addAll(deallocations);
		adaptionSteps.addAll(allocations);
		adaptionSteps.addAll(changes);
		adaptionSteps.addAll(migrations);
		adaptionSteps.addAll(terminates);

		element.setExecutionOrder(adaptionSteps);

		this.printAdaptionSequence(adaptionSteps);
		this.outputPort.send(element);
	}

	private void init(AdaptationData data) {
		this.aquires = data.getRcActions().stream().filter(s -> s instanceof AcquireAction).collect(Collectors.toSet());
		this.terminates = data.getRcActions().stream().filter(s -> s instanceof TerminateAction).collect(Collectors.toSet());

		this.allocations = data.getAcActions().stream().filter(s -> s instanceof AllocateAction).collect(Collectors.toSet());
		this.migrations = data.getAcActions().stream().filter(s -> s instanceof MigrateAction).collect(Collectors.toSet());
		this.changes = data.getAcActions().stream().filter(s -> s instanceof ChangeRepositoryComponentAction).collect(Collectors.toSet());
		this.deallocations = data.getAcActions().stream().filter(s -> s instanceof DeallocateAction).collect(Collectors.toSet());
	}

	private void printAdaptionSequence(List<Action> adaptionSteps) {
		StringBuilder sb = new StringBuilder();
		sb.append("Adaption sequence:\n");

		for (int i = 0; i < adaptionSteps.size(); i++) {
			sb.append(i + "\t" + this.printAction(adaptionSteps.get(i)) + "\n");
		}

		SystemAdaptation.LOG.info(sb.toString());
	}

	private String printAction(Action action) {
		StringBuilder sb = new StringBuilder();

		if (action instanceof AcquireAction) {
			AcquireAction acquire = (AcquireAction) action;
			sb.append("Acquire:\t" + acquire.getSourceResourceContainer().getEntityName());
			sb.append("\tID: " + acquire.getSourceResourceContainer().getId());

		} else if (action instanceof TerminateAction) {
			TerminateAction terminate = (TerminateAction) action;
			sb.append("Terminate:\t" + terminate.getSourceResourceContainer().getEntityName());
			sb.append("\tID: " + terminate.getSourceResourceContainer().getId());

		} else if (action instanceof AllocateAction) {
			AllocateAction allocate = (AllocateAction) action;
			sb.append("Allocate:\t" + allocate.getSourceAssemblyContext().getEntityName());
			sb.append("\tID: " + allocate.getSourceAssemblyContext().getId());
			sb.append("\t" + " ------- ");
			sb.append("\t->\t" + allocate.getNewAllocationContext().getEntityName());

		} else if (action instanceof MigrateAction) {
			MigrateAction migrate = (MigrateAction) action;
			sb.append("Migrate:\t" + migrate.getSourceAssemblyContext().getEntityName());
			sb.append("\tID: " + migrate.getSourceAssemblyContext().getId());
			sb.append("\t" + migrate.getSourceAllocationContext().getResourceContainer_AllocationContext().getEntityName());
			sb.append("\t->\t" + migrate.getNewAllocationContext().getResourceContainer_AllocationContext().getEntityName());

		} else if (action instanceof ChangeRepositoryComponentAction) {
			ChangeRepositoryComponentAction change = (ChangeRepositoryComponentAction) action;
			sb.append("ChangeComp:\t" + change.getSourceAssemblyContext().getEntityName());
			sb.append("\tID: " + change.getSourceAssemblyContext().getId());
			sb.append("\t" + change.getSourceAssemblyContext().getEncapsulatedComponent__AssemblyContext().getEntityName());
			sb.append("\t->\t" + change.getNewRepositoryComponent().getEntityName());

		} else if (action instanceof DeallocateAction) {
			DeallocateAction deAllocate = (DeallocateAction) action;
			sb.append("Deallocate:\t" + deAllocate.getSourceAssemblyContext().getEntityName());
			sb.append("\tID: " + deAllocate.getSourceAssemblyContext().getId());

		} else {
			sb.append("UNKOWN:\t" + " ------------------------------------ ");
			sb.append("\tID: " + " ------------------------------------ ");
		}

		return sb.toString();
	}

}
